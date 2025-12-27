package love.linyi.dao;

import love.linyi.domin.ShiJian;
import love.linyi.domin.UserFolder;
import love.linyi.domin.UserProfit;
import org.apache.ibatis.annotations.*;

import java.nio.file.Path;
import java.util.List;
public interface UserFolderDao {
    @Insert("<script>" +
            "insert into folder (user_id, name,path,type) values " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.userId}, #{item.name},#{item.path},#{item.type})" +
            "</foreach>" +
            "</script>")
    public void save(@Param("list") List<UserFolder> userFolderList);
    // @Update("update shijian set time=#{otime},distance=#{distance},where id=#{id}")
    //public void update(ShiJian shiJian);
    @Delete("delete from folder where id=#{id}")
     public void delete(Integer id);
    @Delete("DELETE FROM folder WHERE path LIKE CONCAT('%', #{path}, '%') AND user_id = #{userId}")
    int deleteByPath(@Param("path") String path, @Param("userId") int userId);
    //  @Select("select id, time as otime, distance from shijian where id=#{id}")
    // public ShiJian getById(Integer id);
    @Select("select * from folder where user_id=#{id}")
    List<UserFolder> getAll(@Param("id")int id);
    @Update("update folder set path=#{newPath} where path=#{Path} and user_id=#{id}")
    void updateFileName(@Param("Path") String Path, @Param("id") int id);

}
