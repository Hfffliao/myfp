////用来和esp32建立tcp连接，用来接收视频流
//package love.linyi.service.camera.tcp;
//
//import love.linyi.controller.Code;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.Arrays;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
////用来和esp32建立tcp连接，用来接收视频流
//@Component
//public class TcpServer  {
//
//    private final int port= Code.tcpserverPort;
//    private ServerSocket serverSocket;
//    private ExecutorService executorService;
//    private volatile boolean running;
//
//    @Autowired
//    private VideoStreamWebSocketHandler webSocketHandler;
//
//
//    @PostConstruct
//    public void start() {
//        System.out.println("@PostConstruct");
//        executorService = Executors.newCachedThreadPool();
//        running = true;
//        executorService.execute(this::runServer);
//    }
//
//    private void runServer() {
//        try {
//            serverSocket = new ServerSocket(port);
//            System.out.println("TCP服务器启动，监听端口: " + port);
//
//            while (running) {
//                Socket clientSocket = serverSocket.accept();
//                System.out.println("ESP32连接: " + clientSocket.getInetAddress());
//                executorService.execute(new ClientHandler(clientSocket, webSocketHandler));
//            }
//        } catch (IOException e) {
//            if (running) {
//                System.err.println("TCP服务器错误: " + e.getMessage());
//            }
//        }
//    }
//
//    @PreDestroy
//    public void stop() {
//        running = false;
//        if (executorService != null) {
//            executorService.shutdown();
//        }
//        if (serverSocket != null) {
//            try {
//                serverSocket.close();
//            } catch (IOException e) {
//                System.err.println("关闭服务器套接字错误: " + e.getMessage());
//            }
//        }
//        System.out.println("TCP服务器已停止");
//    }
//
//    private static class ClientHandler implements Runnable {
//        private final Socket socket;
//        private final VideoStreamWebSocketHandler webSocketHandler;
//        public ClientHandler(Socket socket, VideoStreamWebSocketHandler webSocketHandler) {
//            this.socket = socket;
//            this.webSocketHandler = webSocketHandler;
//        }
//
//        @Override
//        public void run() {
//            try {
//                byte[] buffer = new byte[1024*2];
//                int bytesRead;
//                int whileCount=0;
//                while ((bytesRead = socket.getInputStream().read(buffer)) != -1) {
//
//                    System.out.println("d");
//                    webSocketHandler.broadcastBinaryMessage(Arrays.copyOfRange(buffer, 0, bytesRead));
//
//
//                }
//            } catch (IOException e) {
//                System.out.println("ESP32断开连接: " + socket.getInetAddress());
//            } finally {
//                try {
//                    socket.close();
//                } catch (IOException e) {
//                    System.err.println("关闭客户端套接字错误: " + e.getMessage());
//                }
//            }
//        }
//
//    }
//}
