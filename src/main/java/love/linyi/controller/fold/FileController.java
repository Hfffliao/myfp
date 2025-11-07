package love.linyi.controller.fold;
import love.linyi.controller.Code;
import love.linyi.service.folderService.Deletefile;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/file")
public class FileController {
    @Autowired
    Deletefile deletefile;
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
        System.out.println(id+";"+name);
        // 替换为实际的本地文件路径
        File file = new File(Code.root+"/"+name+"/"+filepn);
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

}