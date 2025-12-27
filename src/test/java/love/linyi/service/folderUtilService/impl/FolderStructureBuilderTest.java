
package love.linyi.service.folderUtilService.impl;

import love.linyi.domin.UserFolder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
// 使用 classes 属性指定 Spring 配置类，将 YourSpringConfigClass 替换为实际的配置类名
class FolderStructureBuilderTest {
    @Test
    void buildFolderMap_MultipleFoldersDifferentParents_ReturnsCorrectMapping() {
        // Arrange

        FolderStructureBuilder builder = new FolderStructureBuilder();
        List<UserFolder> folders = new ArrayList<>();

        UserFolder folder1 = new UserFolder();
        folder1.setPath("");
        folder1.setName("child");
        folder1.setType("folder");
        UserFolder folder2 = new UserFolder();
        folder2.setPath("/child");
        folder2.setName("child");
        folder2.setType("folder");
        UserFolder folder3 = new UserFolder();
        folder3.setPath("");
        folder3.setName("child2");
        folder3.setType("folder");
        folders.add(folder1);
        folders.add(folder2);
        folders.add(folder3);
         //Act
        Map<String, List<UserFolder>> result = builder.buildFolderMap(folders);
         List<Map<String,Object>> structure =builder.buildTree(result,"");
        assertEquals(2,result.size());
        assertEquals(2,result.get("").size());
        assertEquals(1,result.get("/child").size());

        assertEquals(2,structure.size());
        assertEquals("child",structure.get(0).get("name"));
        List<Map<String,Object>> child_child =(List<Map<String,Object>>) structure.get(0).get("children");
        assertEquals("child",child_child.get(0).get("name"));

    }





}