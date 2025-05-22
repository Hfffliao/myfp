package love.linyi.service.impl;

import love.linyi.service.AcquisitionTime;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Service
public class AcquisitionTimeImpl implements AcquisitionTime {
    public LocalDateTime getdata() {
        //获取现在的时间
        LocalDateTime now = LocalDateTime.now();
        // 定义格式
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        // TODO Auto-generated method stub
        System.out.println(now);
        return now;
    }
}
