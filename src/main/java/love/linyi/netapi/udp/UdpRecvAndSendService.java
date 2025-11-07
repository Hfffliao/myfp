package love.linyi.netapi.udp;

import love.linyi.controller.Code;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;

@Component
//用来和esp32建立udp连接，用来接收视频流
public class UdpRecvAndSendService {

//这个类用来和esp32，node.js客户端建立udp连接，用来收发视频流，它会把接收的流量都发送到刚连接的客户端
    // 1. 添加 UDP 接收端口（根据 ESP32 发送的目标端口配置，例如 8888）
    private final int receivePort= Code.udpreceiveAndThenSendport;
    private DatagramSocket receiveSocket; // 接收用的 UDP 套接字
    private volatile boolean isRunning = true; // 控制接收循环的标志
    int Initcount = 0;
    InetAddress senderAddress;
    int senderPort;
    // 3. 初始化：启动 UDP 接收线程（服务启动时执行）
    @PostConstruct
    public void startUdpReceiver() {
        isRunning = true;
        // 初始化UDP接收线程
        if(Initcount==0){
            new Thread(this::receiveUdpMessage, "UDP-Receiver-Thread").start();
            Initcount++;
        }
    }
    public void broadcastBinaryMessage(byte[] payload){
        if (receiveSocket != null && !receiveSocket.isClosed()&&senderAddress!=null&&senderPort!=0) {
            try {
                DatagramPacket packet = new DatagramPacket(payload, payload.length, senderAddress, senderPort);
                receiveSocket.send(packet); // 发送数据报到 ESP32
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("UdpRecerve startUdpReceiver receiveSocketd is null");
        }
    }
    // 4. 核心：UDP 接收逻辑（循环监听端口）
    private void receiveUdpMessage() {
        try {
            receiveSocket = new DatagramSocket(receivePort); // 绑定接收端口
            byte[] buffer = new byte[1024*20]; // 缓冲区（根据 ESP32 发送的数据大小调整）
            while (isRunning) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);


                receiveSocket.receive(packet); // 阻塞等待接收数据
                long timestamp = System.currentTimeMillis();
                byte[] timestampBytes = new byte[4];

                // 大端序（高位在前，网络标准），只取低32位
                timestampBytes[0] = (byte) (timestamp >> 24);
                timestampBytes[1] = (byte) (timestamp >> 16);
                timestampBytes[2] = (byte) (timestamp >> 8);
                timestampBytes[3] = (byte) timestamp;

// 提取有效数据
                int dataLength = packet.getLength();
                byte[] receivedData = Arrays.copyOfRange(buffer, 0, dataLength);

                // 3. 创建新的payload数组（时间戳在前 + 接收数据在后）
                byte[] payload = new byte[4 + dataLength];

                // 4. 将时间戳复制到payload前4字节
                System.arraycopy(timestampBytes, 0, payload, 0, 4);

                // 5. 将接收数据复制到payload后部
                System.arraycopy(receivedData, 0, payload, 4, dataLength);
                // 添加异常处理和连接状态检查
                try {
                    broadcastBinaryMessage(payload);
                } catch (Exception e) {
                    System.err.println("WebSocket广播失败: " + e.getMessage());
                    // 可以在这里添加重连逻辑或其他错误处理
                }
                if(senderAddress==null){
                    senderAddress = packet.getAddress(); // 获取发送方 IP（ESP32 的 IP）
                    senderPort = packet.getPort(); // 获取发送方端口
                }
                // 处理数据（例如打印或转发，根据业务需求修改）
                System.out.println("1");

            }
        } catch (Exception e) {
            System.err.println("UDP error: " + e.getMessage());
            // 可以在这里添加重启逻辑或其他错误处理
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