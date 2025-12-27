package love.linyi.service.folderUtilService.impl;

import love.linyi.controller.Code;
import love.linyi.domin.UserFolder;
import love.linyi.service.folderUtilService.BuildFilePathByModel;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)

public class BuildFilePathByModelImplTest {
   @Mock
   private UserFolder userFolder;
   @InjectMocks
   private BuildFilePathByModelImpl buildFilePathByModelImpl;
   String userName="liao";
   //测试buildFilePathByuserFolderAndUserName方法
    //输入是userFolder和userName
    //输出是Path，是电脑文件系统中给用户文件夹下的文件或文件夹的路径
   @ParameterizedTest
   @MethodSource("buildFilePathByModelImplTestProvider")
   void buildFilePathByModelImplTest(UserFolder userFolder, String userName, Path expectedPath) {
      // Arrange
        // Act
        Path actualPath = buildFilePathByModelImpl.buildFilePathByuserFolderAndUserName(userFolder, userName);
        // Assert
        assertEquals(expectedPath, actualPath);
   }
   static Stream<Arguments> buildFilePathByModelImplTestProvider() {
        return Stream.of(
                //测试文件
                Arguments.of(new UserFolder(1,"1.txt","/dd","file"), "liao", Path.of(Code.root,"liao","dd","1.txt")),
                //测试文件夹
                Arguments.of(new UserFolder(1,"dd","/dd","folder"), "liao", Path.of(Code.root,"liao","dd","dd"))
                );
   }
}
