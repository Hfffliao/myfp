package love.linyi.service.folderUtilService.impl;

import love.linyi.service.folderUtilService.AsyncFileDecodingTask;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@Service
public class AsyncFileDecodingTaskImpl implements AsyncFileDecodingTask {
    public static class FileDecodingTask {
        private final String base64;
        private final String path;
        private final Date unSealTime;

        public FileDecodingTask(String base64, String path, Date unSealTime) {
            this.base64 = base64;
            this.path = path;
            this.unSealTime = unSealTime;
        }
    }
    private final ConcurrentHashMap<String, FileDecodingTask> tasks = new ConcurrentHashMap<>();


    @Async
    @Override
    public void decodeFileAsync(String base64, String path, long delay, TimeUnit unit) {
        Date unSealTime = new Date(System.currentTimeMillis() + unit.toMillis(delay));
        FileDecodingTask task = new FileDecodingTask(base64, path, unSealTime);
        tasks.put(path, task);


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


