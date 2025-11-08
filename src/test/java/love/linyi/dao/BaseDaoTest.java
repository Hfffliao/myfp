// src/test/java/love/linyi/dao/BaseDaoTest.java
package love.linyi.dao;


import love.linyi.config.JdbcConfig;
import love.linyi.config.MybatisConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MybatisConfig.class,JdbcConfig.class})//MybatisConfig.class,
@Transactional // 启用事务管理
@Rollback // 默认测试完成后回滚事务
public abstract class BaseDaoTest {
//@Autowired
//    protected JdbcTemplate jdbcTemplate;
//    @Autowired
//    protected DataSource dataSource;
    @AfterEach
    public void tearDown() {
        // 清空数据库中的数据


    }
}