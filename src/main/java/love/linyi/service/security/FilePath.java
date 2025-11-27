package love.linyi.service.security;

import java.nio.file.Path;

public interface FilePath {
    Path formalFilePath(Path baseDir, String userInput);
}
