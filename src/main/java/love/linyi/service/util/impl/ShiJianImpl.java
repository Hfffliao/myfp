package love.linyi.service.util.impl;

import love.linyi.dao.ShiJianDao;
import love.linyi.domin.ShiJian;
import love.linyi.service.util.AcquisitionTime;
import love.linyi.service.util.ShiJianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShiJianImpl implements ShiJianService {
    @Autowired
    private ShiJianDao shiJianDao;
    @Autowired
    AcquisitionTime acquisitionTime;

    @Override
    public boolean save(ShiJian shiJian) {
        shiJian.setOtime(acquisitionTime.getdata());
        shiJianDao.save(shiJian);
        return true;
    }

    @Override
    public List<ShiJian> getAll() {
        List<ShiJian> shiJianList = shiJianDao.getAll();
        return shiJianList;
    }

    @Override
    public List<ShiJian> getArae(String max, String min) {
        List<ShiJian> shiJianList = shiJianDao.getArae(max, min);
        return shiJianList;
    }
}
