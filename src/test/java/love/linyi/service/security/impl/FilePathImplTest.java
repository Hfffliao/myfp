package love.linyi.service.security.impl;

import love.linyi.controller.Code;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class FilePathImplTest {
    @InjectMocks
    FilePathImpl filePath;
    @Test
        //输入C:/dd和/dd，输出C:/dd/dd
    void formalFilePath_standard(){
        //准备
        Path basepath=Path.of(Code.root,"3390351358@qq.com");
        String userPath ="/jin/jin.jps";
        //开始测试
        Path result=filePath.formalFilePath(basepath,userPath);
        //检测结果
        System.out.println(result);
        assertEquals(Path.of(Code.root,"3390351358@qq.com","jin","jin.jps"),result);
    }
    @Test
        //输入""和/dd/dd,输出dd/dd
    void formalFilePath_1(){
        //准备
        Path basepath=Path.of("");
        String userPath ="/jin/jin.jps";
        //开始测试
        Path result=filePath.formalFilePath(basepath,userPath);
        //检测结果
        System.out.println(result);
        assertEquals(Path.of("jin","jin.jps"),result);
    }
    @Test
        //输入"/dd"和/dd/dd,输出dd/dd
    void formalFilePath_2(){
        //准备
        Path basepath=Path.of("/dd");//输出/dd
        String userPath ="/jin/jin.jps";
        //开始测试
        Path result=filePath.formalFilePath(basepath,userPath);
        //检测结果
        System.out.println(result);
        assertEquals(Path.of("jin","jin.jps"),result);
    }
    @Test
    //目录穿越
    void formalFilePath_Directory_Traversal(){
        //准备
        Path basepath=Path.of(Code.root,"3390351358@qq.com");
        String userPath ="../jin.jps";
        //开始测试
        Path result=filePath.formalFilePath(basepath,userPath);
        //检测结果
        assertEquals(null,result);
    }
    @Test
    //探究Path.of的效果
    void formalFilePath_standard1(){
        //准备
        Path basepath=Path.of("/jin/jin.jps");
        Path basepathf=Path.of("jin/jin.jps");
        //检测结果
        System.out.println(basepath);
        System.out.println(basepathf);
    }

    @ParameterizedTest
    @MethodSource("provide")
        //测试 formalFilePathToDB 方法,提供正反斜杠的文件路径，相对路径和绝对路径，检测是否能正确转换为数据库格式
    void formalFilePath_ToDB(String oldPath,String newPath,String msg){
        //准备
        String userPath =oldPath;
        //开始测试
        String result=filePath.formalFilePathToDB(userPath);
        //检测结果
        assertEquals(newPath,result,msg);
    }
    /**
     * 提供参数化测试数据,提供各种文件路径
     */
    private static Stream<Arguments> provide() {
        return Stream.of(
                Arguments.of("/qwe/qwe", "/qwe/qwe", "用户名和密码不能为空"),
                Arguments.of("qwe/qwe", "/qwe/qwe", "用户名和密码不能为空"),
                Arguments.of("\\qwe\\qwe", "/qwe/qwe", "用户名和密码不能为空"),
                Arguments.of("qwe\\qwe", "/qwe/qwe", "用户名和密码不能为空")
        );
    }
}
