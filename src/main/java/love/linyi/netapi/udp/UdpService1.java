//无法直接使用
////这个udp接收器用来配合VideoStreamWebSocketHandler，用websocket发送视频给浏览器，
//// 但是VideoStreamWebSocketHandler早就改成接收浏览器的控制指令的接收端了，这个也暂时弃用了
//
//package love.linyi.netapi.udp;
//
//import love.linyi.controller.Code;
//import love.linyi.controller.camera.http3.VideoStreamController;
//import love.linyi.netapi.websocket.VideoStreamWebSocketHandler;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import java.net.DatagramSocket;
//import java.net.DatagramPacket;
//import java.net.InetAddress;
//import java.util.Arrays;
//
//@Component
////用来和esp32建立udp连接，用来接收视频流
//public class UdpService1 {
//
//    @Autowired
//    VideoStreamWebSocketHandler videoStreamWebSocketHandler;
//    //VideoStreamController videoStreamController;
//    // 1. 添加 UDP 接收端口（根据 ESP32 发送的目标端口配置，例如 8888）
//    private final int receivePort= Code.receivePort;
//    private DatagramSocket receiveSocket; // 接收用的 UDP 套接字
//    private volatile boolean isRunning = true; // 控制接收循环的标志
//    // 3. 初始化：启动 UDP 接收线程（服务启动时执行）
//    @PostConstruct
//    public void startUdpReceiver() {
//        isRunning = false;
//        new Thread(this::receiveUdpMessage, "UDP-Receiver-Thread").start();
//    }
//
//    // 4. 核心：UDP 接收逻辑（循环监听端口）
//    private void receiveUdpMessage() {
//        try {
//            receiveSocket = new DatagramSocket(receivePort); // 绑定接收端口
//            byte[] buffer = new byte[1024*2]; // 缓冲区（根据 ESP32 发送的数据大小调整）
//
//            while (isRunning) {
//                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//                receiveSocket.receive(packet); // 阻塞等待接收数据
//
//
//// 提取有效数据
//                int dataLength = packet.getLength();
//                byte[] payload = Arrays.copyOfRange(buffer, 0, dataLength);
//                // 添加异常处理和连接状态检查
//                try {
//                    // 检查WebSocket处理器是否可用
//                    if (videoStreamWebSocketHandler != null) {
//                        videoStreamWebSocketHandler.broadcastBinaryMessage(payload);
//                    }
//                } catch (Exception e) {
//                    System.err.println("WebSocket广播失败: " + e.getMessage());
//                    // 可以在这里添加重连逻辑或其他错误处理
//                }
//                InetAddress senderAddress = packet.getAddress(); // 获取发送方 IP（ESP32 的 IP）
//                int senderPort = packet.getPort(); // 获取发送方端口
//
//                // 处理数据（例如打印或转发，根据业务需求修改）
//                System.out.println("1");
//                if(!isRunning){
//                    Thread.sleep(200);
//                }
//            }
//        } catch (Exception e) {
//            if (!isRunning) {
//                System.out.println("UDP 接收线程已停止");
//            } else {
//                e.printStackTrace();
//                // 添加线程重启逻辑
//                if (isRunning) {
//                    System.out.println("UDP接收线程异常，尝试重启...");
//                    new Thread(this::receiveUdpMessage, "UDP-Receiver-Thread").start();
//                }
//            }
//        }
//    }
//
//    // 5. 销毁：停止 UDP 接收（服务关闭时释放资源）
//    @PreDestroy
//    public void stopUdpReceiver() {
//        isRunning = false;
//        if (receiveSocket != null && !receiveSocket.isClosed()) {
//            receiveSocket.close(); // 关闭套接字，中断 receive() 阻塞
//        }
//    }
//
//
//
//}