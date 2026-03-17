package love.linyi.service.folderUtilService.impl;

import love.linyi.service.folderUtilService.ReNameFileOrFolderOnSystem;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ReNameFileOrFolderOnSystemImpl implements ReNameFileOrFolderOnSystem {
    @Override
    public void reName(String parentDirPath, String oldName, String newName) {
        File oldFile = new File(parentDirPath, oldName);
        File newFile = new File(parentDirPath, newName);

        if (!oldFile.exists()) {
            throw new RuntimeException("要重命名的文件或文件夹不存在: " + oldFile.getAbsolutePath());
        }

        if (newFile.exists()) {
            throw new RuntimeException("目标文件或文件夹已存在: " + newFile.getAbsolutePath());
        }

        if (!oldFile.renameTo(newFile)) {
            throw new RuntimeException("文件系统重命名失败");
        }
    }
}
