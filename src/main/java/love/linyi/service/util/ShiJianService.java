package love.linyi.service.util;

import love.linyi.domin.ShiJian;

import java.util.List;

public interface ShiJianService {
    public boolean save(ShiJian shiJian);

    public List<ShiJian> getAll();

    public List<ShiJian> getArae(String max, String min);
}
