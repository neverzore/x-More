package tech.never.more.xmore.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Created by zhouzb on 2018/9/3.
 */
@Configuration
@ConditionalOnProperty(prefix = "x-more.datasource.dynamic", name = "enable", havingValue = "false")
public class PrimaryDataSourceConfig {
    @Bean("dataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource primaryDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        return dataSource;
    }
}
