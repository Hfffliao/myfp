package love.linyi.service.impl;

import love.linyi.controller.loginandout.InHandler;
import love.linyi.dao.UserDao;
import love.linyi.domin.ShiJian;
import love.linyi.domin.User;
import love.linyi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
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
        logger.info("getByusername:{}",username);

        return  userDao.getByusername(username);
    }

}
