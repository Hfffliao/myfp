package love.linyi.service;

import love.linyi.domin.UserFolder;

import java.util.List;

public interface UserFolderService {
    int save(List<UserFolder> userFolderList);
    List<UserFolder> getUserFolderList(int id);
    String reNameFileOrFolder(int id, String newName, int userId);
    UserFolder getUserFolderById(int id);

}
