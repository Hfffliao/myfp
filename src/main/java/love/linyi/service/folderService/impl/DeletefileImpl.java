package love.linyi.service.folderService.impl;

import love.linyi.dao.UserFolderDao;
import love.linyi.service.folderService.Deletefile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeletefileImpl implements Deletefile {
    @Autowired
    UserFolderDao userFolderDao;
    @Override
    public void deletefile(String path, int id, String type, int userId) {
        if (type.equals("file")){
            //删除文件
            userFolderDao.delete(id);
        }else if (type.equals("folder")){
            //删除文件夹
            userFolderDao.deleteByPath(path,userId);
            userFolderDao.delete(id);
        }else {
            throw new RuntimeException("不存在的type");
            //删除文件夹
        }
    }
}
