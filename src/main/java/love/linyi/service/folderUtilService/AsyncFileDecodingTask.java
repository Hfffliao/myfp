package love.linyi.service.folderUtilService;

import java.util.concurrent.TimeUnit;

public interface AsyncFileDecodingTask {
    void decodeFileAsync(String base64, String path,long delay, TimeUnit unit);
}
