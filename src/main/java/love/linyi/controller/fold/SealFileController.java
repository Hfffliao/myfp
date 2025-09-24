package love.linyi.controller.fold;

import love.linyi.controller.Code;
import love.linyi.domin.SealFileRequset;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
//用FileInputStream的 byte[] bytes = new byte[fis.available()];说明只能处理小文件，
//用duration说明封印时间不能超过一个月
@Controller
@ResponseBody
@RequestMapping("/sealFile")
public class SealFileController {
    private final ConcurrentHashMap<String, FileDecodingTask> tasks = new ConcurrentHashMap<>();

    @PostMapping
    public ResponseEntity<String> sealFile(@RequestBody SealFileRequset sealFileRequset,
                                           HttpSession session) {
        String username = (String) session.getAttribute("user");
        String path = Code.root + "/" + username + "/" + sealFileRequset.getFilepath() + "/" + sealFileRequset.getFilename();
        File file = new File(path);
        LocalDateTime sealTime = sealFileRequset.getSealTime();
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(sealTime)) {
            return ResponseEntity.badRequest().body(" sealTime is over");
        }
        // 使用 Duration 类计算两个时间点之间的差值
        Duration duration = Duration.between(now, sealTime);
        long seconds = duration.getSeconds();
        System.out.println("封印秒数"+seconds);
        try {
            byte[] bytes = new byte[0];
            try (FileInputStream fis = new FileInputStream(file);
                 // 使用 BufferedInputStream 包装 FileInputStream 以提高读取效率
                 BufferedInputStream bis = new BufferedInputStream(fis)) {
                // 定义一个缓冲区，这里设置为 8KB
                byte[] buffer = new byte[8192];
                int bytesRead;
                // 使用 ByteArrayOutputStream 来存储读取的字节数据
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // 循环读取文件内容到缓冲区
                while ((bytesRead = bis.read(buffer)) != -1) {
                    // 将缓冲区中的数据写入 ByteArrayOutputStream
                    baos.write(buffer, 0, bytesRead);
                }
                // 将 ByteArrayOutputStream 中的数据转换为字节数组
                 bytes = baos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // 删除文件
            if (file.delete()) {
                System.out.println("文件删除成功: " + file.getAbsolutePath());
            } else {
                System.err.println("文件删除失败: " + file.getAbsolutePath());
            }
            String base64 = Base64.getEncoder().encodeToString(bytes);

            FileDecodingTask task = new FileDecodingTask(base64, path);
            tasks.put(path, task);

            // 提交异步任务，120 小时后执行
            task.schedule(seconds, TimeUnit.SECONDS);

        } catch (Exception e) {
           throw new RuntimeException(e);
        }

        return ResponseEntity.ok("sealFile");
    }

    @Async
    static class FileDecodingTask {
        private final String base64;
        private final String path;

        public FileDecodingTask(String base64, String path) {
            this.base64 = base64;
            this.path = path;
        }

        public void schedule(long delay, TimeUnit unit) {
            try {
                // 等待指定时间
                Thread.sleep(unit.toMillis(delay));
                byte[] decodedBytes = Base64.getDecoder().decode(base64);
                File decodedFile = new File(path);
                try (FileOutputStream fos = new FileOutputStream(decodedFile)) {
                    fos.write(decodedBytes);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
