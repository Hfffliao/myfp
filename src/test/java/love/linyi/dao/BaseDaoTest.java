// src/test/java/love/linyi/dao/BaseDaoTest.java
package love.linyi.dao;


import love.linyi.config.JdbcConfig;
import love.linyi.config.MybatisConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MybatisConfig.class,JdbcConfig.class})//MybatisConfig.class,
public abstract class BaseDaoTest {

    @AfterEach
    public void tearDown() {
    }
}