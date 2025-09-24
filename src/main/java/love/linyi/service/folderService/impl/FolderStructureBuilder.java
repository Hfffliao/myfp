package love.linyi.service.folderService.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import love.linyi.domin.UserFolder;
import love.linyi.service.folderService.FolderStructureBuilderService;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class FolderStructureBuilder implements FolderStructureBuilderService {

    // 构建 fileStructure JSON 字符串
    @Override
    public String buildFileStructure(List<UserFolder> userFolders) throws JsonProcessingException {
        //   构建文件夹映射，键为父路径，值为该路径下的文件和文件夹列表
        Map<String, List<UserFolder>> folderMap = buildFolderMap( userFolders);
        // 构建树形结构
        List<Map<String, Object>> fileStructure = buildTree(folderMap, "");
        // 将树形结构转换为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(fileStructure);
    }
  //   构建文件夹映射，键为父路径，值为该路径下的文件和文件夹列表
    public Map<String, List<UserFolder>> buildFolderMap(List<UserFolder> userFolders) {
        Map<String, List<UserFolder>> folderMap = new HashMap<>();
        for (UserFolder userFolder : userFolders) {
            String parentPath = getParentPath(userFolder.getPath());
            folderMap.computeIfAbsent(parentPath, k -> new ArrayList<>()).add(userFolder);
        }
        return folderMap;
    }

  //   递归构建树形结构
    public List<Map<String, Object>> buildTree(Map<String, List<UserFolder>> folderMap, String parentPath) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<UserFolder> children = folderMap.getOrDefault(parentPath, new ArrayList<>());
        for (UserFolder userFolder : children) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", userFolder.getId());
            item.put("name", userFolder.getName());
            item.put("type", userFolder.getType());
            if ("folder".equals(userFolder.getType())) {
                String currentPath = parentPath.isEmpty()?"/"+userFolder.getName() : parentPath + "/" + userFolder.getName();
                item.put("children", buildTree(folderMap, currentPath));
            }
            result.add(item);
        }
        return result;
    }

  //   获取父路径
    public String getParentPath(String path) {
        if (path == null || path.isEmpty()) {
            return "";
        }
        int lastIndex = path.lastIndexOf("/");
        return lastIndex == -1 ? "" : path;
    }
}