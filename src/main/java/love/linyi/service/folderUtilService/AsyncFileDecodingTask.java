package love.linyi.service.folderUtilService;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public interface AsyncFileDecodingTask {
    void decodeFileAsync(String base64, String path,long delay, TimeUnit unit);
}
