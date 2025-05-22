package love.linyi.service.impl;

import love.linyi.dao.UserDao;
import love.linyi.domin.ShiJian;
import love.linyi.domin.User;
import love.linyi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public List<User> getAll() {

        return userDao.getAll();
    }

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public User getByusername(String username) {
        System.out.println("impl" +username);
        return  userDao.getByusername(username);
    }
}
