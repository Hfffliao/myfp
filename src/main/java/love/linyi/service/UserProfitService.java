package love.linyi.service;

import love.linyi.domin.UserProfit;

//负责通过mapper层增删改查用户权益
public interface UserProfitService {
    void delete(int id);
    void add(int id);
    void save(UserProfit userProfit);
    UserProfit getuserprofit(int id);
}
