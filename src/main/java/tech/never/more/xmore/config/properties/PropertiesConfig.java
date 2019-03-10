package tech.never.more.xmore.config.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.never.more.xmore.core.properties.XMoreProperties;

@Configuration
public class PropertiesConfig {
    @Bean
    public XMoreProperties loadXMoreProperties() {
        return new XMoreProperties();
    }
}
