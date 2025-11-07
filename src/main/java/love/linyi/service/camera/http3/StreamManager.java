package love.linyi.service.camera.http3;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.*;

@Component
public class StreamManager {
    private final ConcurrentMap<String, StreamContext> activeStreams = new ConcurrentHashMap<>(300);
    // 添加一个内部类来封装流和连接信息
    private static class StreamContext {
        private final OutputStream outputStream;
        private final DeferredResult<Void> deferredResult;
        private final HttpServletResponse response;

        public StreamContext(OutputStream outputStream,
                             DeferredResult<Void> deferredResult,
                             HttpServletResponse response) {
            this.outputStream = outputStream;
            this.deferredResult = deferredResult;
            this.response = response;
        }
    }
    public void registerStream(String streamId, OutputStream outputStream,
                               DeferredResult<Void> deferredResult,
                               HttpServletResponse response) {
        if (activeStreams.containsKey(streamId)) {
            System.out.println("activeStreams.containsKey"+streamId);
            // 关闭旧连接
            closeExistingStream(streamId);
        }
        System.out.println("registerStream");
        // 注册新连接
        StreamContext context = new StreamContext(outputStream, deferredResult, response);
        // 设置完成回调
        activeStreams.put(streamId, context);
        deferredResult.onCompletion(() -> closeExistingStream(streamId));
        deferredResult.onTimeout(() -> closeExistingStream(streamId));



    }

    private void closeExistingStream(String streamId) {
        if(!activeStreams.containsKey(streamId)){
            return;
        }
        StreamContext context = activeStreams.remove(streamId);
        if (context != null) {
            try {
//                // 关闭输出流
//                context.outputStream.close();

                // 完成 DeferredResult 以结束请求
                if (!context.deferredResult.isSetOrExpired()) {
                    context.deferredResult.setResult(null);
                }

                // 发送响应结束信号
                if (!context.response.isCommitted()) {
                    context.response.flushBuffer();
                }
            } catch (IOException e) {
                System.err.println("close stream error");
                // 日志记录错误
            }
        }
    }

    public void broadcastToStream(String streamId, byte[] data) {
        StreamContext context = activeStreams.get(streamId);
        if (context != null) {
            try {
                context.outputStream.write(data);
                context.outputStream.flush();
            } catch (IOException e) {
                // 发生错误时移除流
                System.err.println("broadcast error");
                closeExistingStream(streamId);
            }
        }
    }
    public boolean isStreamActive(String streamId) {
        return activeStreams.containsKey(streamId);
    }

}