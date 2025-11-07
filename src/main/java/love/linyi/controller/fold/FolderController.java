package love.linyi.controller.fold;
import com.fasterxml.jackson.core.JsonProcessingException;
import love.linyi.domin.UserFolder;
import love.linyi.service.UserFolderService;
import love.linyi.service.folderService.FolderStructureBuilderService;
import love.linyi.service.folderService.HandleMultipartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
public class FolderController {
    @Autowired
    public FolderStructureBuilderService FolderStructureBuilder;
    @Autowired
   public HandleMultipartService handleMultipartService;
    @Autowired
    public UserFolderService userFolderService;
    @GetMapping("/folder/if")
    public ResponseEntity<Map<String, Object>> ifUpload(  @RequestParam("tatolsize") long totalSize ){
        Map<String, Object> response = new HashMap<>();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/folder")
    public ResponseEntity<Map<String, Object>> saveFloder(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("filestructure") String fileStructureJson, // 接收文件夹结构的 JSON 字符串
            @RequestParam("relativePath") String relativePath,
             HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        System.out.println("FloderController in");
        //获取用户名并检查是否为空
        String name = (String) (session.getAttribute("user")==null? "":session.getAttribute("user"));
        //获取用户id并检查是否为空
        int id = (int) (session.getAttribute("id")==null? 0:session.getAttribute("id"));
        try {
            handleMultipartService.handleMultipart(files, id,fileStructureJson,name,relativePath);
            System.out.println("FloderController out");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/folder")
    public ResponseEntity<String> getfolder(HttpSession session) throws JsonProcessingException {
        Map<String, Object> response = new HashMap<>();
        //获取用户id并检查是否为空
        int id = (int) (session.getAttribute("id")==null? 0:session.getAttribute("id"));
        //根据id获取用户的文件list
        List<UserFolder> userfolders=userFolderService.getUserFolderList(id);
        //将list转化为json
        String json = FolderStructureBuilder.buildFileStructure(userfolders);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(json);
    }
    @GetMapping("/folder/delete")
    public String deletefolder(@RequestParam("path") String path) throws JsonProcessingException {
        System.out.println("deletefolder");
        return "deletefolder";
    }
}
