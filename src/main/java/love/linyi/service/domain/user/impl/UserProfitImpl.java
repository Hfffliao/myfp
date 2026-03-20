package love.linyi.service.domain.user.impl;

import love.linyi.dao.UserProfitDao;
import love.linyi.domin.UserProfit;
import love.linyi.service.domain.user.UserProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfitImpl implements UserProfitService {
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
