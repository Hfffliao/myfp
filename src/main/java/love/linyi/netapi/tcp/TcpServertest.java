//package love.linyi.service.camera.tcp;
//
//import love.linyi.controller.Code;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.Arrays;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//@Component
//public class TcpServertest {
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
//                    System.out.println(buffer[0]);
//                    //图片一
//                    byte[] testString0={(byte) 243,126,98,(byte) 211,0,0,0,0,0,16,119,120,121,122,123,124,125,126,0,0,0,8,5,7,2,4};
//                    byte[] testString1={(byte) 243,126,98,(byte) 211,0,1,0,0,0,4,2,7,2,4};
//                    //图片二
//                    byte[] testString2={(byte) 243,126,98,(byte) 211,0,2,0,0,0,16,119,120,121,122,123,124,125,126,0,0,0,8,5,7,2,4};
//                    byte[] testString3={(byte) 243,126,98,(byte) 211,0,3,0,0,0,4,2,7,2,4};
//
//                    //图片三
//                    byte[] testString4={(byte) 243,126,98,(byte) 211,0,4,0,0,0,16,119,120,121,122,123,124,125,126,0,0,0,8,5,7,2,4};
//                    //缺一个
//                    byte[] testString6={(byte) 243,126,98,(byte) 211,0,6,0,0,0,4,2,7,2,4};
//                    //图片四
//                    byte[] testString7={(byte) 243,126,98,(byte) 211,0,7,0,0,0,16,119,120,121,122,123,124,125,126,0,0,0,8,5,7,2,4};
//                    byte[] testString8={(byte) 243,126,98,(byte) 211,0,8,0,0,0,4,2,7,2,4};
//
//                    //webSocketHandler.broadcastBinaryMessage(Arrays.copyOfRange(buffer, 0, bytesRead));
//
//                    if(buffer[0]==(byte)0){
//                        webSocketHandler.broadcastBinaryMessage(testString0);
//                    }
//                    //if(buffer[0]==(byte)1){
//                        webSocketHandler.broadcastBinaryMessage(testString1);
//                    //}
//                    //if(buffer[0]==(byte)2){
//                        webSocketHandler.broadcastBinaryMessage(testString2);
//                    //}
//                    //if(buffer[0]==(byte)3){
//                        webSocketHandler.broadcastBinaryMessage(testString3);
//                    //}
//                    //if(buffer[0]==(byte)4){
//                        webSocketHandler.broadcastBinaryMessage(testString4);
//                    //}
//                    //if(buffer[0]==(byte)6){
//                        webSocketHandler.broadcastBinaryMessage(testString6);
//                    //}
//                    //if(buffer[0]==(byte)7){
//                        webSocketHandler.broadcastBinaryMessage(testString7);
//                    //}
//                    //if(buffer[0]==(byte)8){
//                        webSocketHandler.broadcastBinaryMessage(testString8);
//                    //}
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
