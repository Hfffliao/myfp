package love.linyi.controller.fold;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import love.linyi.controller.Code;
import love.linyi.service.folderUtilService.Deletefile;
import love.linyi.service.security.FilePath;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileControllerTest {
    @Mock
    HttpSession httpSession;
    @Mock
    HttpServletRequest httpServletRequest;
    @Mock
    FilePath filePath;

    @Mock
    Deletefile deletefile;
    @InjectMocks
    FileController fileController;
    @Test
    void downloadLocalFile_standard(){
        //准备阶段
        String filepn ="jin.jps"; //记得测filepn ="/jin.jps";
        when(httpSession.getAttribute("user")).thenReturn((Object)"3390351358@qq.com");
        when(httpSession.getAttribute("id")).thenReturn((Object)1);
        when(httpServletRequest.getHeader("User-Agent")).thenReturn("Mozilla");
        when(filePath.formalFilePath(Path.of(Code.root,"3390351358@qq.com"),filepn))
                .thenReturn(Path.of(Code.root,"3390351358@qq.com",filepn));

        File dir = new File(Code.root + File.separator + "3390351358@qq.com");
        if (!dir.exists()) {
            dir.mkdirs(); // 创建多级目录
        }

        File normalFile = new File(dir, "jin.jps");
        try {
            normalFile.createNewFile(); // 创建空文件
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //执行测试
        ResponseEntity<Resource> response = fileController.downloadLocalFile(filepn,httpSession,httpServletRequest);
        //验证结果

        assertEquals(HttpStatus.OK,response.getStatusCode(),"status error");


    }
    @Test
    void downloadLocalFile_Directory_Traversal(){
        //准备阶段
        String filepn ="jin.jps";
        when(httpSession.getAttribute("user")).thenReturn((Object)"3390351358@qq.com");
        when(httpSession.getAttribute("id")).thenReturn((Object)1);
        when(filePath.formalFilePath(Path.of(Code.root,"3390351358@qq.com"),filepn)).thenReturn(null);
//创建测试要用的文件
        //创建标准目录的上级目录的jin.jps文件，如果能获取到就算漏洞被利用了
        File dir = new File(Code.root);
        if (!dir.exists()) {
            dir.mkdirs(); // 创建多级目录
        }
        File normalFile = new File(dir, "jin.jps");
        try {
            normalFile.createNewFile(); // 创建空文件
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //执行测试
        ResponseEntity<Resource> response = fileController.downloadLocalFile(filepn,httpSession,httpServletRequest);
        //验证结果

        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode(),"漏洞被利用：目录穿越");
    }
}
