package love.linyi.dao;

import love.linyi.domin.ShiJian;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ShiJianDao {
    @Insert("insert into shijian (time,distance) values (#{otime},#{distance})")
    public void save(ShiJian shiJian);

    @Select("select time as otime, distance from shijian")
    List<ShiJian> getAll();

    @Select("select time as otime, distance from shijian where time>#{min} and time<#{max}")
    List<ShiJian> getArae(@Param("max") String max, @Param("min") String min);
}
