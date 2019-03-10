package tech.never.more.xmore.core.datasource.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.sql.DataSource;

/**
 * Created by zhouzb on 2018/9/7.
 */
@Getter
@Setter
public class DynamicSource {
    private String type;
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
