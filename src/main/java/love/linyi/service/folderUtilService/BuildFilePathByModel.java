package love.linyi.service.folderUtilService;

import love.linyi.domin.UserFolder;

import java.nio.file.Path;

public interface BuildFilePathByModel {
        Path buildFilePathByuserFolderAndUserName(UserFolder userFolder,String userName);
}
