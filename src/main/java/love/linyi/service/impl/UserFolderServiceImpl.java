package love.linyi.service.impl;

import love.linyi.dao.UserFolderDao;
import love.linyi.domin.UserFolder;
import love.linyi.service.UserFolderService;
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

}
