package love.linyi.service.security.impl;

import love.linyi.controller.Code;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class FilePathImplTest {
    @InjectMocks
    FilePathImpl filePath;
    @Test
    void formalFilePath_standard(){
        //准备
        Path basepath=Path.of(Code.root,"3390351358@qq.com");
        String userPath ="/jin/jin.jps";
        //开始测试
        Path result=filePath.formalFilePath(basepath,userPath);
        //检测结果
        assertEquals(Path.of(Code.root,"3390351358@qq.com","jin","jin.jps"),result);
    }
    @Test
    void formalFilePath_Directory_Traversal(){
        //准备
        Path basepath=Path.of(Code.root,"3390351358@qq.com");
        String userPath ="../jin.jps";
        //开始测试
        Path result=filePath.formalFilePath(basepath,userPath);
        //检测结果
        assertEquals(null,result);
    }
}
