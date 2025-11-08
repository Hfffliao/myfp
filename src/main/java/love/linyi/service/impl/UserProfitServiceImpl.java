package love.linyi.service.impl;

import love.linyi.dao.UserFolderDao;
import love.linyi.dao.UserProfitDao;
import love.linyi.domin.UserProfit;
import love.linyi.service.UserProfitService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserProfitServiceImpl implements UserProfitService {
    @Autowired
    UserProfitDao userProfitDao;
    @Override
    public void delete(int id) {

    }

    @Override
    public void add(int id) {

    }

    @Override
    public void save(UserProfit userProfit) {

    }

    @Override
    public UserProfit getuserprofit(int id) {

        return  userProfitDao.getuserprofit(id);
    }
}
