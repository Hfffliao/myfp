package love.linyi.service.camera.udp;

import love.linyi.controller.Code;
import love.linyi.service.camera.tcp.VideoStreamWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;

@Component
public class UdpService {
    @Autowired
    VideoStreamWebSocketHandler videoStreamWebSocketHandler;
    // 1. 添加 UDP 接收端口（根据 ESP32 发送的目标端口配置，例如 8888）
    private final int receivePort= Code.receivePort;
    private DatagramSocket receiveSocket; // 接收用的 UDP 套接字
    private volatile boolean isRunning = true; // 控制接收循环的标志

//    // 2. 发送 UDP 消息（原代码不变，保留）
//    public void sendUdpMessage(String message, String host, int port) {
//        try (DatagramSocket socket = new DatagramSocket()) {
//            socket.setBroadcast(true);
//            byte[] buffer = message.getBytes();
//            InetAddress address = InetAddress.getByName(host);
//            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
//            socket.send(packet);
//        } catch (Exception e) {
//            e.printStackTrace(); // 建议替换为日志框架
//            // 异常处理（建议补充具体日志，如 e.printStackTrace() 或日志框架记录）
//        }
//    }

    // 3. 初始化：启动 UDP 接收线程（服务启动时执行）
    @PostConstruct
    public void startUdpReceiver() {
        isRunning = true;
        new Thread(this::receiveUdpMessage, "UDP-Receiver-Thread").start();
    }

    // 4. 核心：UDP 接收逻辑（循环监听端口）
    private void receiveUdpMessage() {
        try {
            receiveSocket = new DatagramSocket(receivePort); // 绑定接收端口
            byte[] buffer = new byte[1024*2]; // 缓冲区（根据 ESP32 发送的数据大小调整）

            while (isRunning) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                receiveSocket.receive(packet); // 阻塞等待接收数据


// 提取有效数据
                int dataLength = packet.getLength();
                byte[] payload = Arrays.copyOfRange(buffer, 0, dataLength);
                // 添加异常处理和连接状态检查
                try {
                    // 检查WebSocket处理器是否可用
                    if (videoStreamWebSocketHandler != null) {
                        videoStreamWebSocketHandler.broadcastBinaryMessage(payload);
                    }
                } catch (Exception e) {
                    System.err.println("WebSocket广播失败: " + e.getMessage());
                    // 可以在这里添加重连逻辑或其他错误处理
                }
                InetAddress senderAddress = packet.getAddress(); // 获取发送方 IP（ESP32 的 IP）
                int senderPort = packet.getPort(); // 获取发送方端口

                // 处理数据（例如打印或转发，根据业务需求修改）
                System.out.println("1");
            }
        } catch (Exception e) {
            if (!isRunning) {
                System.out.println("UDP 接收线程已停止");
            } else {
                e.printStackTrace();
                // 添加线程重启逻辑
                if (isRunning) {
                    System.out.println("UDP接收线程异常，尝试重启...");
                    new Thread(this::receiveUdpMessage, "UDP-Receiver-Thread").start();
                }
            }
        }
    }

    // 5. 销毁：停止 UDP 接收（服务关闭时释放资源）
    @PreDestroy
    public void stopUdpReceiver() {
        isRunning = false;
        if (receiveSocket != null && !receiveSocket.isClosed()) {
            receiveSocket.close(); // 关闭套接字，中断 receive() 阻塞
        }
    }



}