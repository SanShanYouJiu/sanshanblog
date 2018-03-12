package xyz.sanshan.web.config.javaconfig;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.sql.SQLException;

@Configuration
//@PropertySource("file:D:/SanShanBlog.properties")
public class DbConfig {

    @Value("${druid.driver}")
    private String driver;
    @Value("${druid.url}")
    private String url;
    @Value("${druid.username}")
    private String username;
    @Value("${druid.password}")
    private String passowrd;
    @Value("${filters}")
    private String filters;
    @Value("${maxActive}")
    private int maxActive;
    @Value("${initialSize}")
    private int initialSize;
    @Value("${maxWait}")
    private long maxWait;
    @Value("${minIdle}")
    private int minIdle;
    @Value("${timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;
    @Value("${minEvictableIdleTimeMillis}")
    private long minEvictableIdleTimeMillis;
    @Value("${testWhileIdle}")
    private boolean testWhileIdle;
    @Value("${testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${testOnReturn}")
    private boolean testOnReturn;
    @Value("${maxOpenPreparedStatements}")
    private int maxOpenPreparedStatements;
    @Value("${removeAbandoned}")
    private boolean removeAbandoned;
    @Value("${removeAbandonedTimeout}")
    private int removeAbandonedTimeout;
    @Value("${logAbandoned}")
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
