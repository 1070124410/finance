package com.finance.anubis.report.common;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusLanguageDriverAutoConfiguration;
import com.github.pagehelper.PageInterceptor;
import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author baocheng
 * @date 2022/12/26 16:54
 */
@Configuration
//@ComponentScan(basePackages = {"com.finance.anubis.anubis.repository"})
//@MapperScan(basePackages = {"com.finance.anubis.anubis.repository.mapper"})
@Import({CommonConfiguration.TestPersistentConfiguration.class})
//@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
//@ConditionalOnSingleCandidate(DataSource.class)
//@AutoConfigureAfter({DataSourceAutoConfiguration.class})
public class CommonConfiguration {


    @Bean
    public Executor executor() {
        return Runnable::run;
    }

    /**
     * 持久层框架配置
     */
    @Configuration
    @Import({MybatisPlusLanguageDriverAutoConfiguration.class,
            MybatisPlusAutoConfiguration.class})
    public static class TestPersistentConfiguration {

    }


    @Bean
    public DataSource dataSource() {
        YamlPropertiesFactoryBean yamlMapFactoryBean = new YamlPropertiesFactoryBean(); //可以加载多个yml文件
        yamlMapFactoryBean.setResources(new ClassPathResource("datasource.yml"));
        Properties properties1 = yamlMapFactoryBean.getObject();
        Properties properties = new Properties();
        properties.setProperty("driverClassName", properties1.getProperty("spring.datasource.driverClassName"));
        properties.setProperty("url", properties1.getProperty("spring.datasource.url"));
        properties.setProperty("username", properties1.getProperty("spring.datasource.username"));
        properties.setProperty("password", properties1.getProperty("spring.datasource.password"));
        try {
            return DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取SqlSessionFactoryBean
     *
     * @return
     */

//    @Bean
//    public PlatformTransactionManager transactionManager() throws Exception {
//        return new DataSourceTransactionManager(dataSource());
//    }


//    @Bean
//    public DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection() {
//        DataSource dataSource = dataSource();
//        final DatabaseConfigBean config = new DatabaseConfigBean();
//        config.setAllowEmptyFields(true);
//        final DatabaseDataSourceConnectionFactoryBean dbConnectionFactory =
//                new DatabaseDataSourceConnectionFactoryBean(dataSource);
//        config.setDatatypeFactory(new H2DataTypeFactory());
//        dbConnectionFactory.setDatabaseConfig(config);
//        return dbConnectionFactory;
//    }

    @Bean
    public PageInterceptor pageInterceptor() {
        return new PageInterceptor();
    }


}
