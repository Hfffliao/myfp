package love.linyi.service.domain.user.impl;

import love.linyi.dao.UserProfitDao;
import love.linyi.domin.UserProfit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserProfitImplTest {
   @Mock
   UserProfitDao userProfitDao;
   @InjectMocks
     UserProfitImpl userProfitService;
   @BeforeEach
   void upset(){

   }
   @Test
    void getuserprofit(){
       //
       when(userProfitDao.getuserprofit(61)).thenReturn(new UserProfit(10,50,100,50,61));
       //act
    UserProfit userProfit=userProfitService.getuserprofit(61);
    //arrest
    assertTrue(userProfit.getId()>0&&userProfit.getRemainingSize()>=0
           &&userProfit.getTotalSize()>=0&&userProfit.getUsedSize()>=0&&userProfit.getUser_id()==61);
   }
}
