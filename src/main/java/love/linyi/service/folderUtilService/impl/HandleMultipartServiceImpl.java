package love.linyi.service.folderUtilService.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import love.linyi.controller.Code;
import love.linyi.domin.UserFolder;
import love.linyi.exception.BusinessException;
import love.linyi.service.UserFolderService;
import love.linyi.service.folderUtilService.HandleMultipartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//调用UserFolderService类存文件信息到数据库
//在本类存储文件到本地，除了存文件到本地的函数，其它地方用的路径分割符都是"/"（windows）
@Service
public class HandleMultipartServiceImpl implements HandleMultipartService {
    @Autowired
    public UserFolderService userFolderService;
    public ObjectMapper objectMapper = new ObjectMapper();

    // 定义存储文件的根目录，可根据实际情况修改
    String rootDir ;
    @Override
    public void handleMultipart(MultipartFile[] files,int id,String fileStructureJson,String username,String relativePath) {
        if (relativePath==null){
            relativePath = "";
        }
        if (!relativePath.isEmpty()){
            relativePath="/"+relativePath;
        }

        rootDir = Code.root+File.separator + username;
        //建立一个UserFolder的list，用于存储文件信息
        List<UserFolder> userFolderList =  new ArrayList<>();
        // 检查参数是否为空
        if (shouldReturnEarly(files, id, fileStructureJson)) {
            System.err.println("参数为空，跳过处理");
            return;
        }

        //创建两个hashmap，用于存储并快速查找文件信息
        Map<String, String> filePathMap = new HashMap<>();
        // 将 JSON 字符串转换为 List<Map<String, Object>>
        List<Map<String, Object>> fileStructure;
        try {
            fileStructure = objectMapper.readValue(fileStructureJson, List.class);
        } catch (JsonProcessingException e) {
            System.err.println("处理文件结构时发生异常: " + e.getMessage());
            // 发生异常时直接返回
            return;
        }
        // 递归处理文件结构,提取文件信息
        try {
            printFileStructure(fileStructure,  filePathMap,relativePath,id, userFolderList);

            // 可以在这里使用 fileStructure 做进一步处理
            System.out.println("FolderController:File structure: " + fileStructure);
        }catch (Exception e) {
            System.err.println("处理文件结构时发生异常: " + e.getMessage());
            // 发生异常时直接返回
            return;
        }
        try {
            for (MultipartFile multipartFile : files) {
//                if (multipartFile.isEmpty()) {
//                    continue; // 使用 continue 跳过当前空文件，继续处理下一个文件
//                }
                if (multipartFile != null) {
                    String fileName = multipartFile.getOriginalFilename();
                    String filePath = filePathMap.get(fileName);
                    //保存文件到文件系统
                    saveFileToFileSystem(multipartFile, filePath, fileName,username);

                }

            }
            if (userFolderList.isEmpty()) {
                System.err.println("skip empty file");
                return;
            }
            // 保存文件信息到数据库
            userFolderService.save(userFolderList);
        } catch (Exception e) {
            throw new BusinessException(5000, e.getMessage(),e);
        }
    }

    private void printFileStructure(List<Map<String, Object>> fileStructure,
                                    Map<String, String> filePathMap,
                                    String parentPath
                                      ,int id    ,List<UserFolder> userFolderList  ) {
        for (Map<String, Object> item : fileStructure) {
            String name = (String) item.get("name");
            String fileType = (String) item.get("type");
            if (fileType.equals("file")) {
                filePathMap.put(name, parentPath);
            }
            saveFileToUserFolderList(name,parentPath,fileType,id,userFolderList);
            // 若存在子元素，则递归处理
            if (item.containsKey("children")) {
                List<Map<String, Object>> children = (List<Map<String, Object>>) item.get("children");
                printFileStructure(children, filePathMap,  parentPath + "/" + name,id, userFolderList);
            }
        }
    }
    private boolean shouldReturnEarly(MultipartFile[] files, int id, String fileStructureJson) {
        return fileStructureJson == null || fileStructureJson.isEmpty()
                || id == 0
                || files == null || files.length == 0;
    }
    private  void saveFileToUserFolderList(String fileName,String filePath,String fileType,int id,List<UserFolder> userFolderList){
      //查空值
      if (true) {
          if ( fileName == null || fileName.isEmpty()) {
              System.err.println("skip empty file name");
              return;
          }
          if (filePath == null || filePath.isEmpty()) {
              filePath = "";
          }
          if (fileType == null || fileType.isEmpty()) {
              return;
          }
      }

      // 每次循环创建一个新的 UserFolder 实例
      UserFolder userFolder = new UserFolder();
      // 处理文件名和路径
      userFolder.setName(fileName);
      userFolder.setPath(filePath);
      userFolder.setType(fileType);
      userFolder.setUserId(id);
      userFolderList.add(userFolder);
      System.out.println("文件名: " + fileName);
      System.out.println("文件路径: " + filePath);
      System.out.println("文件类型: " +fileType);
      System.out.println("文件id: " + id);
  }
    private void saveFileToFileSystem(MultipartFile file, String filePath, String fileName,String username) throws Exception {
        // 构建完整的文件存储路径
        // 处理路径中的斜杠在不同系统兼容性问题
        if(filePath == null || filePath.isEmpty()){
            filePath = "";
        }
        String[] pathSegments = filePath.split("/");
        filePath= String.join(File.separator, pathSegments);
        String fullPath = rootDir + filePath + File.separator + fileName;
        File destFile = new File(fullPath);

        try {
            // 创建父目录
            if (!destFile.getParentFile().exists()) {
                if (!destFile.getParentFile().mkdirs()) {
                    System.err.println("无法创建目录: " + destFile.getParent());
                    return;
                }
            }
            Path targetPath=destFile.toPath();

            //file.transferTo(destFile);
            // 底层文件复制//等效于transferTo
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println("文件已保存到: " + fullPath);
        } catch (Exception e) {
            System.err.println("保存文件 " + fullPath + " 时出错: " + e.getMessage());
        }
    }
}
