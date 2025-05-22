package love.linyi.service.impl;

import love.linyi.dao.ShiJianDao;
import love.linyi.domin.ShiJian;
import love.linyi.service.AcquisitionTime;
import love.linyi.service.ShiJianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ShiJianServiceImpl implements ShiJianService {
    @Autowired
    private ShiJianDao shiJianDao;
    @Autowired
    AcquisitionTime acquisitionTime;
   /* @Override
    public boolean delete(Integer id) {
        shiJianDao.delete(id);
        System.out.println(id);
        return true;
    }*/

    @Override
    public boolean save(ShiJian shiJian) {

        shiJian.setOtime(acquisitionTime.getdata());
        shiJianDao.save(shiJian);
        return true;
    }

    /*@Override
    public boolean update(ShiJian shiJian) {
        shiJianDao.update(shiJian);
        return true;
    }

    @Override
    public ShiJian getById(Integer id) {
        ShiJian shiJian = shiJianDao.getById(id);
        return shiJian;
    }*/

    @Override
    public List<ShiJian> getAll() {
        List<ShiJian> shiJianList = shiJianDao.getAll();
//        System.out.println(shiJianList);
//        System.out.println(56);
        return shiJianList;
    }
    @Override
    public List<ShiJian> getArae(String max, String min) {
        List<ShiJian> shiJianList = shiJianDao.getArae(max,min);
        return shiJianList;
    }
}
