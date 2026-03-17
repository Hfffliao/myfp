package love.linyi.controller.fold;
import love.linyi.common.context.UserContext;
import love.linyi.controller.Code;
import love.linyi.domin.UserFolder;
import love.linyi.service.UserFolderService;
import love.linyi.service.folderUtilService.Deletefile;
import love.linyi.service.folderUtilService.ReNameFileOrFolderOnSystem;
import love.linyi.service.security.FilePath;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/file")
public class FileController {
    @Autowired
    Deletefile deletefile;
    @Autowired
    FilePath filePathImpl;
    @Autowired
    UserFolderService userFolderService;
    @Autowired
    ReNameFileOrFolderOnSystem reNameFileOrFolderOnSystem;
    /**
     * 返回本地文件
     *
     * @return 文件响应实体
     */
    @DeleteMapping
    public ResponseEntity<String> deleteLocalFile(@RequestParam("filepn") String filepn,
                                                  @RequestParam("type") String type,
                                                  @RequestParam("fileid") String idfile,
                                                  HttpSession session) {
        //获取用户名并检查是否为空
        String name = (String) (session.getAttribute("user")==null? "":session.getAttribute("user"));
        //获取用户id并检查是否为空
        int id = (int) (session.getAttribute("id")==null? 0:session.getAttribute("id"));
        File file = new File(Code.root+"/"+name+"/"+filepn);
//        System.out.println(idfile);
//        System.out.println(filepn);
//        System.out.println(type);
        int idf = Integer.parseInt(idfile);
        deletefile.deletefile(filepn,idf,type,id);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        try {

            if (deleteFileOrDirectory(file)) {
                return ResponseEntity.ok("文件或文件夹删除成功");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件或文件夹删除失败");
            }
        } catch (Exception e) {
            System.err.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除过程中出现错误: " + e.getMessage());
        }
    }

    /**
     * 递归删除文件或文件夹
     * @param file 要删除的文件或文件夹
     * @return 删除成功返回 true，失败返回 false
     */
    private boolean deleteFileOrDirectory(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    if (!deleteFileOrDirectory(child)) {
                        return false;
                    }
                }
            }
        }
        return file.delete();
    }

    @GetMapping("")
    public ResponseEntity<Resource> downloadLocalFile(@Param("filepn")String filepn, HttpSession session, HttpServletRequest request) {
        //获取用户名并检查是否为空
        String name = (String) (session.getAttribute("user")==null? "":session.getAttribute("user"));
        //获取用户id并检查是否为空
        int id = (int) (session.getAttribute("id")==null? 0:session.getAttribute("id"));
        //System.out.println(id+";"+name);
        // 替换为实际的本地文件路径
        Path path=filePathImpl.formalFilePath(Path.of(Code.root,name),filepn);
        if(path==null){
            return ResponseEntity.badRequest().build();
        }
        //System.out.println(path);
        File file = new File(path.toString());
        Resource resource = new FileSystemResource(file);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        String userAgent = request.getHeader("User-Agent");
        String fileName = file.getName();
        String encodedFileName;

        try {
            if (userAgent != null && userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                // IE 浏览器
                encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            } else if (userAgent != null && userAgent.contains("Mozilla")) {
                // Firefox、Chrome 等现代浏览器
                encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            } else {
                // 其他浏览器
                encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            }

            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"");
        } catch (Exception e) {
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        }

        return ResponseEntity.ok()
                .headers(headers)
                //响应头，文件字节长度
                .contentLength(file.length())
                //响应流是二进制流
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PatchMapping
    public ResponseEntity<Map<String,Object>> reName(@RequestBody Map<String, Object> params) {
        Map<String, Object> response = new HashMap<>();

        Long id = (Long) params.get("id");
        String newName = (String) params.get("newName");

        if (id == null) {
            response.put("code", 400);
            response.put("message", "文件/文件夹ID不能为空");
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
        }

        if (newName == null || newName.trim().isEmpty()) {
            response.put("code", 400);
            response.put("message", "新名称不能为空");
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
        }

        if (newName.contains("/") || newName.contains("\\")) {
            response.put("code", 400);
            response.put("message", "新名称不能含路径分隔符");
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
        }

        try {
            Integer userId = UserContext.getUserId().orElse(0);
            if (userId == 0) {
                response.put("code", 401);
                response.put("message", "用户未登录");
                response.put("data", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String oldName = userFolderService.reNameFileOrFolder(id, newName, userId);

            UserFolder updatedFolder = userFolderService.getUserFolderById(id.intValue());
            if (updatedFolder == null) {
                throw new RuntimeException("无法获取更新后的文件夹信息");
            }

            String dbPath = updatedFolder.getPath();
            String parentPath = getParentPath(dbPath);

            String username = UserContext.getUsername().orElse("");
            if (username.isEmpty()) {
                throw new RuntimeException("无法获取用户名");
            }

            Path baseDir = Paths.get(Code.root, username);
            Path parentDirPath = filePathImpl.formalFilePath(baseDir, parentPath);
            if (parentDirPath == null) {
                throw new RuntimeException("非法路径");
            }

            reNameFileOrFolderOnSystem.reName(parentDirPath.toString(), oldName, newName);

            Map<String, Object> data = new HashMap<>();
            data.put("id", id);
            data.put("oldName", oldName);
            data.put("newName", newName);

            response.put("code", 200);
            response.put("message", "重命名成功");
            response.put("data", data);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "重命名失败: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private String getParentPath(String dbPath) {
        if (dbPath == null || dbPath.equals("/")) {
            return "/";
        }
        int lastSlashIndex = dbPath.lastIndexOf('/');
        if (lastSlashIndex <= 0) {
            return "/";
        }
        String parentPath = dbPath.substring(0, lastSlashIndex);
        return parentPath.isEmpty() ? "/" : parentPath;
    }

}