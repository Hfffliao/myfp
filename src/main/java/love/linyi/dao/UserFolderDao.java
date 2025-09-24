package love.linyi.dao;

import love.linyi.domin.ShiJian;
import love.linyi.domin.UserFolder;
import love.linyi.domin.UserProfit;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
public interface UserFolderDao {
    @Insert("<script>" +
            "insert into Folder (user_id, name,path,type) values " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.userId}, #{item.name},#{item.path},#{item.type})" +
            "</foreach>" +
            "</script>")
    public void save(@Param("list") List<UserFolder> userFolderList);
    // @Update("update shijian set time=#{otime},distance=#{distance},where id=#{id}")
    //public void update(ShiJian shiJian);
    @Delete("delete from folder where id=#{id}")
     public void delete(Integer id);
    @Delete("DELETE FROM Folder WHERE path LIKE CONCAT('%', #{path}, '%') AND user_id = #{userId}")
    int deleteByPath(@Param("path") String path, @Param("userId") int userId);
    //  @Select("select id, time as otime, distance from shijian where id=#{id}")
    // public ShiJian getById(Integer id);
    @Select("select * from folder where user_id=#{id}")
    List<UserFolder> getAll(@Param("id")int id);
    @Select("select time as otime, distance from shijian where time>#{min} and time<#{max}")
    List<UserFolder> getArae(@Param("max") String max, @Param("min") String min);

}
