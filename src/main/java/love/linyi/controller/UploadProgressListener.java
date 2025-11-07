//package love.linyi.controller;
//
//import org.apache.commons.fileupload.ProgressListener;
//import jakarta.servlet.AsyncEvent;
//import jakarta.servlet.AsyncListener;
//import jakarta.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//public class UploadProgressListener implements AsyncListener, ProgressListener {
//    private long bytesRead = 0;
//    private long contentLength = 0;
//    private int progress = 0;
//    private HttpServletRequest request;
//    public UploadProgressListener(HttpServletRequest request) {
//        this.request = request;
//    }
//
//    @Override
//    public void onComplete(AsyncEvent event) throws IOException {
//        // 上传完成
//        request.getSession().setAttribute("uploadProgress", 100);
//    }
//    @Override
//    public void update(long bytesRead, long contentLength, int items) {
//        this.bytesRead = bytesRead;
//        this.contentLength = contentLength;
//        if (contentLength > 0) {
//            this.progress = (int) ((bytesRead * 100) / contentLength);
//        }
////        System.out.println("Upload progress: " + this.progress + "%");
//        request.getSession().setAttribute("uploadProgress", this.progress);
//    }
//    @Override
//    public void onTimeout(AsyncEvent event) throws IOException {
//        // 上传超时
//        request.getSession().setAttribute("uploadProgress", -1);
//    }
//
//    @Override
//    public void onError(AsyncEvent event) throws IOException {
//        // 上传出错
//        request.getSession().setAttribute("uploadProgress", -2);
//    }
//
//    @Override
//    public void onStartAsync(AsyncEvent event) throws IOException {
//        // 开始异步处理
//        request.getSession().setAttribute("uploadProgress", 0);
//    }
//    public int getProgress() {
//        return progress;
//    }
//}