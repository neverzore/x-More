package tech.never.more.xmore.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhouzb on 2018/9/3.
 */
@Getter
@Setter
public class XMoreProperties {


    private String mapperLocation;
}
