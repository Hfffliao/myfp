package love.linyi.service.security.impl;

import love.linyi.service.security.FilePath;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
@Component
public class FilePathImpl implements FilePath {
    @Override
    public Path formalFilePath(Path baseDir, String userInput) {
        try {
           // System.out.println(baseDir);
            // 解析用户输入
            //用户提交的路径前面有斜杠会被认为是绝对路径，这一步把它转化为相对路径
            Path userPath = Paths.get(userInput);
            Path relativePath = Paths.get("");
            for (Path part:userPath){
                relativePath=relativePath.resolve(part);
            }
            //System.out.println(relativePath);
            // 与基础目录结合并再次规范化
            Path resolvedPath = baseDir.resolve(relativePath).normalize();
            //System.out.println(resolvedPath);

            // 关键检查：确保解析后的路径仍在基础目录内
            if (resolvedPath.startsWith(baseDir)) {
                return resolvedPath;
            }
            return null; // 路径穿越 detected
        } catch (Exception e) {
            return null; // 非法路径
        }
    }
}
