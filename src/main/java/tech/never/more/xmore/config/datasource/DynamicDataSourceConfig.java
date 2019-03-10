package tech.never.more.xmore.config.datasource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import tech.never.more.xmore.core.datasource.DynamicDataSourceRegister;

/**
 * Created by zhouzb on 2018/9/7.
 */
@Configuration
@ConditionalOnProperty(prefix = "x-more.datasource.dynamic", name = "enable", havingValue = "true")
@Import({DynamicDataSourceRegister.class})
public class DynamicDataSourceConfig {

}
