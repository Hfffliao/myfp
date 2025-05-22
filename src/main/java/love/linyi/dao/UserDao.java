package love.linyi.dao;

import love.linyi.domin.ShiJian;
import love.linyi.domin.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserDao {
    @Insert("insert into user (username,password) values (#{userName},#{password})")
    public void save(User user);
    // @Update("update shijian set time=#{otime},distance=#{distance},where id=#{id}")
    //public void update(ShiJian shiJian);
    //  @Delete("delete from shijian where id=#{id}")
    // public void delete(Integer id);
      @Select("select   username as userName, password from user where username=#{username}")
     public User getByusername(@Param("username") String username );
    @Select("select username as userName, password from user")
    List<User> getAll();
//    @Select("select time as otime, distance from shijian where time>#{min} and time<#{max}")
//    List<ShiJian> getArae(@Param("max") String max, @Param("min") String min);

}
