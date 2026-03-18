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
    public String reNameFileOrFolder(int id, String newName, int userId) {
        UserFolder folder = userFolderDao.getFolderByIdAndUserId(id, userId);
        if (folder == null) {
            throw new RuntimeException("文件夹不存在或无权限访问");
        }

        String oldName = folder.getName();
        String oldPath = folder.getPath();// "" or "/folder1/folder2"
        String type = folder.getType();

        if (oldName.equals(newName)) {
            throw new RuntimeException("新名称与旧名称相同");
        }
        //extract standrad parent path
        String parentPath;
        if (oldPath.equals("")) parentPath = "/";
        else {
          parentPath = oldPath;
        }
        //construct standard old and new path
        String standardNewPath ="";
        String standardOldPath ="";
        if (parentPath.equals("/")){
            standardOldPath = parentPath + oldName;
            standardNewPath = parentPath + newName;
        }else {
            standardOldPath = parentPath + "/" + oldName;
            standardNewPath = parentPath + "/" + newName;
        }

//      checked,this is sql action,so use "/"
        int count = userFolderDao.countByPathAndUserId(oldPath, userId,type,newName);
        if (count > 0) {
            throw new RuntimeException("同名文件或文件夹已存在");
        }
        //update folder name but path
        int updateResult = userFolderDao.updateFolderNameAndPath(id, newName, oldPath, userId);
        if (updateResult == 0) {
            throw new RuntimeException("更新文件夹失败:exec sql but not effect");
        }

        if (type.equals("folder")){
            userFolderDao.updateChildrenPaths(standardOldPath, standardNewPath, userId);
        }
        return oldName;
    }

    @Override
    public UserFolder getUserFolderById(int id) {
        return userFolderDao.getFolderById(id);
    }

}
