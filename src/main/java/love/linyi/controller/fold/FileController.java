package love.linyi.controller.fold;
import love.linyi.common.context.UserContext;
import love.linyi.config.Config;
import love.linyi.domin.UserFolder;
import love.linyi.exception.BusinessException;
import love.linyi.service.domain.user.UserFolderService;
import love.linyi.service.infra.file.Deletefile;
import love.linyi.service.infra.file.ReNameFileOrFolderOnSystem;
import love.linyi.service.infra.security.FilePath;
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
        File file = new File(Config.root+"/"+name+"/"+filepn);
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
        Path path=filePathImpl.formalFilePath(Path.of(Config.root,name),filepn);
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
//newname 新名称 cannot contain / or \
    //id 文件/文件夹ID can be int or string(can transform to int)
    @PatchMapping
    public ResponseEntity<Map<String,Object>> reName(@RequestBody Map<String, Object> params) {
        Map<String, Object> response = new HashMap<>();
        //check id and newname
        Object idObj = params.get("id");
        int id = 0;
        if (idObj == null) {
            // 明确处理 null：抛出异常或返回错误，避免后续使用未初始化的 id
            throw new IllegalArgumentException("ID不能为空");
        }

        try {
            if (idObj instanceof Integer) {
                id = (Integer) idObj;                 // 自动拆箱为 int
            } else if (idObj instanceof String) {
                id = Integer.parseInt((String) idObj);
            } else {
                // 理论上不会发生，但保留防御性检查
                throw new IllegalArgumentException("ID类型错误，应为整数或字符串");
            }
        } catch (NumberFormatException e) {
            // 字符串解析失败
            throw new IllegalArgumentException("ID格式错误", e);
        }



        String newName = (String) params.get("newName");
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("新名称不能为空");
        }

        if (newName.contains("/") || newName.contains("\\")) {
           throw new BusinessException(Config.BAD_REQUEST,"新名称不能含路径分隔符");
        }

        try {
            //check userinfo
            int userId = UserContext.getUserId().orElse(0);
            if (userId == 0) {
               throw new BusinessException(Config.BAD_REQUEST,"用户登录,dan_shi_mei_xing_xi");
            }
            String username = UserContext.getUsername().orElse("");
            if (username.isEmpty()) {
                throw new RuntimeException("无法获取用户名");
            }

            // 获取当前文件夹信息
            UserFolder folder = userFolderService.getUserFolderById(id);
            if (folder == null) {
                throw new RuntimeException("文件夹不存在");
            }
            String oldName = folder.getName();
            String parentPath = folder.getPath();

            // 构建系统文件路径
            Path baseDir = Paths.get(Config.root, username);
            Path parentDirPath = filePathImpl.formalFilePath(baseDir, parentPath);
            if (parentDirPath == null) {
                throw new RuntimeException("非法路径");
            }

            // 先重命名系统文件，失败直接抛异常，数据库不会执行
            reNameFileOrFolderOnSystem.reName(parentDirPath.toString(), oldName, newName);

            // 系统文件操作成功，再更新数据库
            userFolderService.reNameFileOrFolder(id, newName, userId);

            Map<String, Object> data = new HashMap<>();
            data.put("id", id);
            data.put("oldName", oldName);
            data.put("newName", newName);

            response.put("code", 200);
            response.put("message", "重命名成功");
            response.put("data", data);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new BusinessException(Config.UPDATE_ERR,"重命名失败: " + e.getMessage());
        }
    }

}