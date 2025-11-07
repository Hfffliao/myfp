package love.linyi.config;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
//这个配置类注册SqlSessionFactoryBean和MapperScannerConfigurer
//用来启动mybatis
public class MybatisConfig {
    @Bean
    //这里的SqlSessionFactoryBean是mybatis提供的，不是spring提供的
    //DataSource参数是spring扫描到存在的bean，会自动注入
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource){
        SqlSessionFactoryBean factoryBean =new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setTypeAliasesPackage("love.linyi.domin");//为这个包的类创建别名，通常是类名首字母小写
        return factoryBean;
    }
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer ms =new MapperScannerConfigurer();
        ms.setBasePackage("love.linyi.dao");
        return ms;
    }

}
