package love.linyi.service.impl;

import love.linyi.dao.UserFolderDao;
import love.linyi.domin.UserFolder;
import love.linyi.service.UserFolderService;
import love.linyi.common.context.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserFolderServiceImpl implements UserFolderService {
@Autowired
private UserFolderDao userFolderDao;

    @Override
    public List<UserFolder> getUserFolderList(int id) {
        return  userFolderDao.getAll(id);
    }

    @Override
    public int save(List<UserFolder> userFolderList) {
        userFolderDao.save(userFolderList);
        System.out.println("UserFolderServiceImpl out");

        return 0;
    }

    @Override
    public String reNameFileOrFolder(long id, String newName, int userId) {
        UserFolder folder = userFolderDao.getFolderByIdAndUserId((int) id, userId);
        if (folder == null) {
            throw new RuntimeException("文件夹不存在或无权限访问");
        }

        String oldName = folder.getName();
        String oldPath = folder.getPath();
        String type = folder.getType();

        if (oldName.equals(newName)) {
            return oldName;
        }

        String parentPath;
        if (oldPath.equals("/")) {
            parentPath = "/";
        } else {
            int lastSlashIndex = oldPath.lastIndexOf('/');
            if (lastSlashIndex <= 0) {
                parentPath = "/";
            } else {
                parentPath = oldPath.substring(0, lastSlashIndex);
            }
        }

        String newPath;
        if (parentPath.equals("/")) {
            newPath = "/" + newName;
        } else {
            newPath = parentPath + "/" + newName;
        }

        int count = userFolderDao.countByPathAndUserId(newPath, userId);
        if (count > 0) {
            throw new RuntimeException("同名文件或文件夹已存在");
        }

        int updateResult = userFolderDao.updateFolderNameAndPath((int) id, newName, newPath, userId);
        if (updateResult == 0) {
            throw new RuntimeException("更新文件夹失败");
        }

        if ("folder".equals(type)) {
            userFolderDao.updateChildrenPaths(oldPath, newPath, userId);
        }

        return oldName;
    }

    @Override
    public UserFolder getUserFolderById(int id) {
        return userFolderDao.getFolderById(id);
    }

}
