package com.demo.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Eddy
 * @version V1.0
 * @Date 2018-07-20 21:00
 */
@SpringBootConfiguration
public class MyBatisConfig {
    @Resource
    private DataSource dataSource;

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage("com.demo.pojo.*");
        return bean.getObject();
    }

    @Configuration
    @AutoConfigureAfter(MyBatisConfig.class)
    public static class MyBatisMapperScannerConfigurer {
        @Bean
        public MapperScannerConfigurer mapperScannerConfigurer() {
            MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
            mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
            mapperScannerConfigurer.setBasePackage("com.demo.mapper.*");
            //配置通用mappers
            Properties properties = new Properties();
            properties.setProperty("mappers", "com.demo.base.BaseMapper");
            properties.setProperty("notEmpty", "false");
            properties.setProperty("IDENTITY", "MYSQL");
            mapperScannerConfigurer.setProperties(properties);

            return mapperScannerConfigurer;
        }

    }
}
