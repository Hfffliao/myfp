package love.linyi.service.security.impl;

import love.linyi.service.security.FilePath;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
@Component
public class FilePathImpl implements FilePath {
    @Override
    //把用户提供的路径和基础的路径拼接，并规范化，并检查是否在基础路径下，如果不在，输出null
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
    //规范路径为不穿越的路径，如果失败，输出null
    public Path formalSingleFilePath(String userInput) {
        try {
           // System.out.println(baseDir);
            // 解析用户输入
            //用户提交的路径前面有斜杠会被认为是绝对路径，这一步把它转化为相对路径
            Path userPath = Paths.get(userInput);
           
            //System.out.println(relativePath);
            // 与基础目录结合并再次规范化
            Path resolvedPath = userPath.normalize();
           
            return resolvedPath; // 路径穿越 detected
        } catch (Exception e) {
            return null; // 非法路径
        }
    }
    @Override
    //把路径转化为数据库中存储的格式 ""   "/a/b"
    public String formalFilePathToDB(String path) {
        Path p = Path.of(path);
        // 1. 转换为绝对路径（相对于当前工作目录）
        // 2. 规范化，移除多余的 "." 和 ".."
        Path absolute = p.toAbsolutePath().normalize();
        // 3. 将分隔符统一为 '/'
        String result = absolute.toString().replace('\\', '/');
        
        // 4. 截取第一个 '/' 及其后的部分
        int firstSlash = result.indexOf('/');
        if (firstSlash != -1) {
            result = result.substring(firstSlash);
        }else {
            result = ""; // 如果没有 '/'，则返回空字符串
        }
        // 如果找不到 '/'（理论上不会发生，但为安全保留原字符串）
        return result;
    }
}