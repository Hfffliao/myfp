package love.linyi.service.Impl;

import love.linyi.dao.UserProfitDao;
import love.linyi.domin.UserProfit;
import love.linyi.service.impl.UserProfitServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserProfitServiceImplTest {
   @Mock
   UserProfitDao userProfitDao;
   @InjectMocks
    UserProfitServiceImpl userProfitService;
   @BeforeEach
   void upset(){

   }
   @Test
    void getuserprofit(){
       //
       when(userProfitDao.getuserprofit(61)).thenReturn(new UserProfit(61,50,100,50));
       //act
    UserProfit userProfit=userProfitService.getuserprofit(61);
    //arrest
    assertTrue(userProfit.getId()>0&&userProfit.getRemainingSize()>=0
           &&userProfit.getTotalSize()>=0&&userProfit.getUsedSize()>=0);
   }
}
