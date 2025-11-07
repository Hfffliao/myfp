//可直接使用，但端口可能和其他软件冲突
package love.linyi.netapi.udp;

import love.linyi.controller.Code;
//import love.linyi.netapi.websocket.VideoStreamWebSocketHandler;
import love.linyi.service.camera.http3.StreamManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.concurrent.*;

@Component
//用来和esp32建立udp连接，用来接收视频流
public class UdpRecvAndH3SendService {

    @Autowired
    private StreamManager streamManager;
    //VideoStreamController videoStreamController;
    // 1. 添加 UDP 接收端口（根据 ESP32 发送的目标端口配置，例如 8888）
    private final int jettyUdpPort= Code.UdpRecvAndH3SendPort;
    private DatagramSocket receiveSocket; // 接收用的 UDP 套接字
    public volatile boolean isRunning = false; // 控制接收循环的标志
    int corePoolSize = 10;
    int maxPoolSize = corePoolSize * 5;
    long keepAliveTime = 60L;
    BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(2);

    ExecutorService executor = new ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            keepAliveTime,
            TimeUnit.SECONDS,
            queue,
            new ThreadPoolExecutor.DiscardOldestPolicy()
    );
    // 3. 初始化：启动 UDP 接收线程（服务启动时执行）
    @PostConstruct
    public void startUdpReceiver() {
        isRunning = false;
        new Thread(this::receiveUdpMessage, "UDP-Receiver-Thread").start();
    }

    // 4. 核心：UDP 接收逻辑（循环监听端口）
    private void receiveUdpMessage() {
        try {
            receiveSocket = new DatagramSocket(jettyUdpPort); // 绑定接收端口
            byte[] buffer = new byte[1024*2]; // 缓冲区（根据 ESP32 发送的数据大小调整）
            int streamId=1;
            while (true) {
                if(isRunning){
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    receiveSocket.receive(packet); // 阻塞等待接收数据
                    int dataLength = packet.getLength();
                    byte[] payload = Arrays.copyOfRange(buffer, 0, dataLength);
                    // 添加异常处理和连接状态检查
                    try {
                       for (int i=0;i<5;i++){
                           // 检查WebSocket处理器是否可用
                           if (streamManager.isStreamActive(""+streamId)) {
                               // 使用线程池

                               int finalStreamId = streamId;
                               long timestamp1 = System.currentTimeMillis();

                               executor.execute(() -> streamManager.broadcastToStream(""+finalStreamId, payload));
                               long timestamp2 = System.currentTimeMillis();
                               if(timestamp2-timestamp1>2){
                                   System.out.println(streamId+" :"+(timestamp2-timestamp1)+"ms");
                               }

                               streamId++;
                               break;
                           }else {
                               System.out.println("stream"+streamId+"未注册");
                               streamId++;
                               if(streamId>128){
                                   streamId=1;
                               }
                           }
                       }

                    } catch (Exception e) {
                        System.err.println("http"+streamId+"广播失败: " + e.getMessage());
                        isRunning=false;
                        // 可以在这里添加重连逻辑或其他错误处理
                    }



                }

                if(!isRunning){
                    Thread.sleep(1000);
                    System.out.println("controller 1000ms未获取连接");
                }
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
