package love.linyi.service.security.impl;

import love.linyi.service.security.FilePath;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
@Component
public class FilePathImpl implements FilePath {
    @Override
    //把用户提供的路径和基础的路径拼接，并检查是否在基础路径下，如果不在，输出null
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
    @Override
    //把路径转化为数据库中存储的格式
    public String formalFilePathToDB(String path) {
        Path p = Path.of(path);
        String pathStr = p.toString();
        // 判断第一个字符是否为反斜杠，如果不是则添加一个
        if (!pathStr.startsWith("\\")) {
            pathStr = "\\" + pathStr;
        }
        return pathStr.replace("\\", "/");
    }
}