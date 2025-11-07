package love.linyi.controller.camera.http3;
import jakarta.servlet.http.HttpServletRequest;
import love.linyi.netapi.jetty.JettyInitializer;
import love.linyi.netapi.udp.UdpRecvAndH3SendService;
import love.linyi.service.camera.http3.StreamManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.IOException;
import java.io.OutputStream;

@RestController
public class VideoStreamController {
    @Autowired
    UdpRecvAndH3SendService udpService;
    @Autowired
    private StreamManager streamManager;

    @Autowired
    JettyInitializer jettyInitializer;
    int streamCount = 0;
    @GetMapping(value = "/video-stream/{streamId}")//, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public DeferredResult<Void> streamVideo(
            @PathVariable String streamId, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        streamCount++;
        if(streamId.equals("0")){

            if(streamCount > 1){
                jettyInitializer.removeHttp2Connector();
                //System.out.println("removeHttp2Connector");
            }
            return null;
        }

        udpService.isRunning = true;
        //response.setContentType("application/octet-stream");
        //response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        response.setHeader("Transfer-Encoding", "chunked");
        OutputStream outputStream = response.getOutputStream();
        if(streamId.equals("128")){
            jettyInitializer.addHttp2Connector();
            System.out.println("addHttp2Connector");
            streamCount=0;
        }
        // 创建永不完成的 DeferredResult
        DeferredResult<Void> deferredResult = new DeferredResult<>(30 * 60 * 1000L);
        streamManager.registerStream(streamId, outputStream, deferredResult, response);
        return deferredResult;
    }
}