package love.linyi.service.infra.file;

import java.util.concurrent.TimeUnit;

public interface AsyncFileDecodingTask {
    void decodeFileAsync(String base64, String path,long delay, TimeUnit unit);
}
