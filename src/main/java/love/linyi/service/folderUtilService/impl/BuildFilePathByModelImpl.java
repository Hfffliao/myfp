package love.linyi.service.folderUtilService.impl;

import love.linyi.controller.Code;
import love.linyi.domin.UserFolder;
import love.linyi.service.folderUtilService.BuildFilePathByModel;

import java.nio.file.Path;

public class BuildFilePathByModelImpl implements BuildFilePathByModel {
    @Override
    //通过文件模型和用户名构建文件在操作系统的路径
    //因为用户名是通过UserContext获取的，所以不需要校验用户名是否为空，以及是否被篡改
    public Path buildFilePathByuserFolderAndUserName(UserFolder userFolder, String userName) {
        String path = userFolder.getPath();
        String fileName = userFolder.getName();
        Path filePath = Path.of(Code.root,userName, path, fileName);
        return filePath;
    }
}
