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

    @Select("SELECT id, path, name, type FROM folder WHERE id = #{id} AND user_id = #{userId}")
    UserFolder getFolderByIdAndUserId(@Param("id") Long id, @Param("userId") int userId);

    @Select("SELECT id, path, name, type FROM folder WHERE id = #{id}")
    UserFolder getFolderById(@Param("id") Integer id);

    @Select("SELECT COUNT(*) FROM folder WHERE path = #{path} AND user_id = #{userId}")
    int countByPathAndUserId(@Param("path") String path, @Param("userId") int userId);

    @Update("UPDATE folder SET name = #{newName}, path = #{newPath} WHERE id = #{id} AND user_id = #{userId}")
    int updateFolderNameAndPath(@Param("id") Long id, @Param("newName") String newName, @Param("newPath") String newPath, @Param("userId") int userId);

    @Update("UPDATE folder SET path = REPLACE(path, #{oldPath}, #{newPath}) WHERE user_id = #{userId} AND path LIKE CONCAT(#{oldPath}, '/%')")
    int updateChildrenPaths(@Param("oldPath") String oldPath, @Param("newPath") String newPath, @Param("userId") int userId);

}
