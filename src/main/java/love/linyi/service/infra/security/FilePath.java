package love.linyi.service.infra.security;

import java.nio.file.Path;

public interface FilePath {
    Path formalFilePath(Path baseDir, String userInput);
    String formalFilePathToDB(String path);
    Path formalSingleFilePath(String userInput);
}
