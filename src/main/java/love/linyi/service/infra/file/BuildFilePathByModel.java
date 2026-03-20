package love.linyi.service.infra.file;

import love.linyi.domin.UserFolder;

import java.nio.file.Path;

public interface BuildFilePathByModel {
        Path buildFilePathByuserFolderAndUserName(UserFolder userFolder,String userName);
}
