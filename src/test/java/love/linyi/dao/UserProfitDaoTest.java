// src/test/java/love/linyi/dao/UserProfitDaoTest.java
package love.linyi.dao;

import love.linyi.domin.UserProfit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.junit.jupiter.api.Assertions.*;

public class UserProfitDaoTest extends BaseDaoTest {
    
    @Autowired
    private UserProfitDao userProfitDao;

    
    @BeforeEach
    public void setUp() {
        UserProfit userProfit = new UserProfit(7,60,30,20);

    }
    
    @Test
    public void testGetUserProfit_WithExistingUserId_ShouldReturnUserProfit() {
        // Given - 使用 test-data.sql 中已存在的数据
        int existingUserId = 1;
        
        // When - 执行查询
        UserProfit userProfit = userProfitDao.getuserprofit(existingUserId);
        System.out.println(userProfit.toString());
        // Then - 验证结果
        assertNotNull(userProfit, "应该能查询到存在的用户收益记录");

        assertNotNull(userProfit.getId());
    }
    @Test
    public void testAddUserProfit_WithValidUserId() {
        // 数据


        // When - 执行查询
        UserProfit userProfit = new UserProfit(7,60,30,20,7);
        int result = userProfitDao.addUserProfit(userProfit);
        System.out.println(result);
        // Then - 验证结果
        assertEquals(1, result, "添加用户权益记录应该返回1");


    }
    @Test
    public void testDeleteUserProfit_WithValidUserId() {
        // 数据
        int existingUserId = 3;

        // When - 执行查询
        int result = userProfitDao.deleteUserProfit(existingUserId);
        System.out.println(result);
        // Then - 验证结果
        assertEquals(1, result, "添加用户权益记录应该返回1");


    }
    @Test
    public void testUpdateUserProfit_WithValidUserId() {
        // 数据
        UserProfit userProfit = new UserProfit(7,60,30,3);


        // When - 执行查询
        int result = userProfitDao.updateUserProfit(userProfit);
        System.out.println(result);
        // Then - 验证结果
        assertEquals(1, result, "添加用户权益记录应该返回1");
    }
    

}