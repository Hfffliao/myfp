package love.linyi.service.folderUtilService.impl;

import love.linyi.service.folderUtilService.ReNameFileOrFolderOnSystem;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ReNameFileOrFolderOnSystemImpl implements ReNameFileOrFolderOnSystem {
    @Override
    public void reName(String parentDirPath, String oldName, String newName) throws IOException {
        Path oldPath = Paths.get(parentDirPath, oldName);
        Path newPath = Paths.get(parentDirPath, newName);

        if (!Files.exists(oldPath)) {
            throw new IOException("要重命名的文件或文件夹不存在: " + oldPath);
        }

        if (Files.exists(newPath)) {
            throw new IOException("目标文件或文件夹已存在: " + newPath);
        }

        Files.move(oldPath, newPath, StandardCopyOption.ATOMIC_MOVE);
    }
}
