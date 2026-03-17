package love.linyi.controller.fold;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import love.linyi.common.context.UserContext;
import love.linyi.controller.Code;
import love.linyi.domin.UserFolder;
import love.linyi.service.UserFolderService;
import love.linyi.service.folderUtilService.Deletefile;
import love.linyi.service.folderUtilService.ReNameFileOrFolderOnSystem;
import love.linyi.service.security.FilePath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileControllerTest {
    @Mock
    HttpSession httpSession;
    @Mock
    HttpServletRequest httpServletRequest;
    @Mock
    FilePath filePathImpl;

    @Mock
    Deletefile deletefile;
    
    @Mock
    UserFolderService userFolderService;
    
    @Mock
    ReNameFileOrFolderOnSystem reNameFileOrFolderOnSystem;
    
    @InjectMocks
    FileController fileController;
    
    @BeforeEach
    void setUp() {
        UserContext.setUserInfo("testuser", 1);
    }
    
    @AfterEach
    void tearDown() {
        UserContext.clear();
    }
    
    @Test
    void downloadLocalFile_standard(){
        String filepn ="jin.jps";
        when(httpSession.getAttribute("user")).thenReturn((Object)"3390351358@qq.com");
        when(httpSession.getAttribute("id")).thenReturn((Object)1);
        when(httpServletRequest.getHeader("User-Agent")).thenReturn("Mozilla");
        when(filePathImpl.formalFilePath(Path.of(Code.root,"3390351358@qq.com"),filepn))
                .thenReturn(Path.of(Code.root,"3390351358@qq.com",filepn));

        File dir = new File(Code.root + File.separator + "3390351358@qq.com");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File normalFile = new File(dir, "jin.jps");
        try {
            normalFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ResponseEntity<Resource> response = fileController.downloadLocalFile(filepn,httpSession,httpServletRequest);

        assertEquals(HttpStatus.OK,response.getStatusCode(),"status error");
    }
    
    @Test
    void downloadLocalFile_Directory_Traversal(){
        String filepn ="jin.jps";
        when(httpSession.getAttribute("user")).thenReturn((Object)"3390351358@qq.com");
        when(httpSession.getAttribute("id")).thenReturn((Object)1);
        when(filePathImpl.formalFilePath(Path.of(Code.root,"3390351358@qq.com"),filepn)).thenReturn(null);

        File dir = new File(Code.root);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File normalFile = new File(dir, "jin.jps");
        try {
            normalFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ResponseEntity<Resource> response = fileController.downloadLocalFile(filepn,httpSession,httpServletRequest);

        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode(),"漏洞被利用：目录穿越");
    }
    
    @Test
    void reName_success() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);
        params.put("newName", "newname.txt");
        
        UserFolder updatedFolder = new UserFolder();
        updatedFolder.setId(1);
        updatedFolder.setPath("/docs/newname.txt");
        updatedFolder.setName("newname.txt");
        
        when(userFolderService.reNameFileOrFolder(1L, "newname.txt", 1)).thenReturn("oldname.txt");
        when(userFolderService.getUserFolderById(1)).thenReturn(updatedFolder);
        when(filePathImpl.formalFilePath(any(Path.class), eq("/docs"))).thenReturn(Path.of(Code.root, "testuser", "docs"));
        
        ResponseEntity<Map<String, Object>> response = fileController.reName(params);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getBody().get("code"));
        assertEquals("重命名成功", response.getBody().get("message"));
        assertEquals("oldname.txt", ((Map<?, ?>) response.getBody().get("data")).get("oldName"));
        assertEquals("newname.txt", ((Map<?, ?>) response.getBody().get("data")).get("newName"));
        
        verify(reNameFileOrFolderOnSystem).reName(anyString(), eq("oldname.txt"), eq("newname.txt"));
    }
    
    @Test
    void reName_idIsNull() {
        Map<String, Object> params = new HashMap<>();
        params.put("newName", "newname.txt");
        
        ResponseEntity<Map<String, Object>> response = fileController.reName(params);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("code"));
        assertEquals("文件/文件夹ID不能为空", response.getBody().get("message"));
    }
    
    @Test
    void reName_newNameIsEmpty() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);
        params.put("newName", "   ");
        
        ResponseEntity<Map<String, Object>> response = fileController.reName(params);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("code"));
        assertEquals("新名称不能为空", response.getBody().get("message"));
    }
    
    @Test
    void reName_newNameContainsPathSeparator() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);
        params.put("newName", "new/name.txt");
        
        ResponseEntity<Map<String, Object>> response = fileController.reName(params);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("code"));
        assertEquals("新名称不能含路径分隔符", response.getBody().get("message"));
    }
    
    @Test
    void reName_newNameContainsBackslash() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);
        params.put("newName", "new\\name.txt");
        
        ResponseEntity<Map<String, Object>> response = fileController.reName(params);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("code"));
        assertEquals("新名称不能含路径分隔符", response.getBody().get("message"));
    }
    
    @Test
    void reName_userNotLoggedIn() {
        UserContext.clear();
        
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);
        params.put("newName", "newname.txt");
        
        ResponseEntity<Map<String, Object>> response = fileController.reName(params);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(401, response.getBody().get("code"));
        assertEquals("用户未登录", response.getBody().get("message"));
    }
    
    @Test
    void reName_duplicateName() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);
        params.put("newName", "newname.txt");
        
        when(userFolderService.reNameFileOrFolder(1L, "newname.txt", 1))
            .thenThrow(new RuntimeException("同名文件或文件夹已存在"));
        
        ResponseEntity<Map<String, Object>> response = fileController.reName(params);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, response.getBody().get("code"));
        assertEquals("重命名失败: 同名文件或文件夹已存在", response.getBody().get("message"));
    }
}
