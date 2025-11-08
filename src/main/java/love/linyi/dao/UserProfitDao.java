package love.linyi.dao;

import love.linyi.domin.UserFolder;
import love.linyi.domin.UserProfit;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserProfitDao {

    @Select("select * from userprofit where user_id=#{id}")
    UserProfit getuserprofit(@Param("id")int id);

}
