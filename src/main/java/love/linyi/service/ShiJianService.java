package love.linyi.service;

import love.linyi.domin.ShiJian;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ShiJianService {
    /**
     * 保存
     * @param shiJian
     * @return
     */
    public boolean save(ShiJian shiJian);
    /**
    /* *更新
     * /*@param shiJian
    / * @return*/
     /**/
   /* public boolean update(ShiJian shiJian);
    /**
     *删除
     * @param id
     * @return
     */
   /* public boolean delete(Integer id);
    /**
     *通过id查找
     * @param id
     * @return
     */
  /*  public ShiJian getById(Integer id);
    /**
     *查找全部
     * @param
     * @return
     */
    public List<ShiJian> getAll();
    public List<ShiJian> getArae(String max,String min);
}
