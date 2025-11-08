package love.linyi.service;

import love.linyi.domin.UserFolder;
import love.linyi.domin.UserProfit;

import java.util.List;

public interface UserFolderService {
    int save(List<UserFolder> userFolderList);
    List<UserFolder> getUserFolderList(int id);
}
