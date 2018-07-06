package xyz.sanshan.main.dao.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.sql.SQLException;

@Configuration
public class DbConfig {

    @Value("${druid.driver}")
    private String driver;
    @Value("${druid.url}")
    private String url;
    @Value("${druid.username}")
    private String username;
    @Value("${druid.password}")
    private String passowrd;
    @Value("${druid.filters}")
    private String filters;
    @Value("${druid.maxActive}")
    private int maxActive;
    @Value("${druid.initialSize}")
    private int initialSize;
    @Value("${druid.maxWait}")
    private long maxWait;
    @Value("${druid.minIdle}")
    private int minIdle;
    @Value("${druid.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;
    @Value("${druid.minEvictableIdleTimeMillis}")
    private long minEvictableIdleTimeMillis;
    @Value("${druid.testWhileIdle}")
    private boolean testWhileIdle;
    @Value("${druid.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${druid.testOnReturn}")
    private boolean testOnReturn;
    @Value("${druid.maxOpenPreparedStatements}")
    private int maxOpenPreparedStatements;
    @Value("${druid.removeAbandoned}")
    private boolean removeAbandoned;
    @Value("${druid.removeAbandonedTimeout}")
    private int removeAbandonedTimeout;
    @Value("${druid.logAbandoned}")
    private boolean logAbandoned;



    @Bean(initMethod = "init", destroyMethod = "close")
    public DruidDataSource dataSource() throws SQLException {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(passowrd);
//        最大并发连接数
        ds.setMaxActive(maxActive);
//        初始化连接数量
        ds.setInitialSize(initialSize);
//        配置获取连接等待超时的时间
        ds.setMaxWait(maxWait);
//       最小空闲连接数
        ds.setMinIdle(minIdle);
//        配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        ds.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
//       配置一个连接在池中最小生存的时间，单位是毫秒
        ds.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        ds.setValidationQuery("SELECT 1");
        ds.setTestWhileIdle(testWhileIdle);
        //不开启mysql的自动事务
        ds.setDefaultAutoCommit(false);
        ds.setTestOnBorrow(testOnBorrow);
        ds.setTestOnReturn(testOnReturn);
        ds.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
        ds.setFilters(filters);
//       打开removeAbandoned功能
        ds.setRemoveAbandoned(removeAbandoned);
//       1800秒，也就是30分钟
        ds.setRemoveAbandonedTimeout(removeAbandonedTimeout);
//       关闭abanded连接时输出错误日志
        ds.setLogAbandoned(logAbandoned);
        return ds;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
