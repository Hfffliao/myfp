package love.linyi.dao;

import love.linyi.domin.UserFolder;
import love.linyi.domin.UserProfit;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserProfitDao {

    @Select("select * from userprofit where user_id=#{id}")
    UserProfit getuserprofit(@Param("id")int id);
    @Insert("insert into userprofit(remainingSize,totalSize,usedSize,user_id,id) values(#{remainingSize},#{totalSize},#{usedSize},#{user_id},#{id})")
    int addUserProfit(UserProfit userProfit);
    @Delete("delete from userprofit where user_id=#{id}")
    int deleteUserProfit(@Param("id")int userid);
    @Update("update userprofit set remainingSize=#{remainingSize},totalSize=#{totalSize},usedSize=#{usedSize} where user_id=#{user_id}")
    int updateUserProfit(UserProfit userProfit);

}
