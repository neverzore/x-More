package tech.never.more.xmore.core.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import tech.never.more.xmore.core.datasource.support.DynamicSource;
import tech.never.more.xmore.core.datasource.support.DruidSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouzb on 2018/9/7.
 */
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    private static final String DYNAMIC_SOURCE_PREFIX = "x-more.datasource.dynamic.source";
    private Binder binder;
    private Environment environment;
    private DruidSource druidSource;
    private String DEFAULT_TYPE;

    private Map<String, Object> dataSourceMap = new HashMap<>();

    private DataSource buildDataSource(DynamicSource dynamicSource) throws ClassNotFoundException {
        String type = dynamicSource.getType();
        if (StringUtils.isBlank(type)) {
            type = DEFAULT_TYPE;
        }

        Class<? extends DataSource> dsType = (Class<? extends DataSource>) Class.forName(type);

        DataSourceBuilder factory = DataSourceBuilder.create().
                driverClassName(dynamicSource.getDriverClassName()).
                url(dynamicSource.getUrl()).
                username(dynamicSource.getUsername()).
                password(dynamicSource.getPassword()).
                type(dsType);

        return factory.build();
    }

    @ConfigurationProperties(prefix = "spring.datasource")
    private DataSource primaryDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        druidSource.config(dataSource);
        return dataSource;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        DataSource defaultDataSource = primaryDataSource();

        dataSourceMap.put("dataSource", defaultDataSource);

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(XMoreDynamicDataSource.class);

        MutablePropertyValues values = beanDefinition.getPropertyValues();
        values.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        values.addPropertyValue("targetDataSources", dataSourceMap);

        beanDefinitionRegistry.registerBeanDefinition("dataSource", beanDefinition);
    }

    private void loadDynamicDataSource(Environment environment) {
        String ddsName = environment.getProperty("x-more.datasource.dynamic.source.names");
        if (StringUtils.isBlank(ddsName)) {
            return;
        }

        DataSource dataSource;
        DynamicSource dynamicSource;

        String[] ddsNames = ddsName.split(",");
        for (String dsName : ddsNames) {
            dynamicSource = this.binder.bind(StringUtils.join(new String[]{DYNAMIC_SOURCE_PREFIX, dsName}, "."),
                    Bindable.of(DynamicSource.class)).get();

            try {
                dataSource = buildDataSource(dynamicSource);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                continue;
            }

            dataSourceMap.put(dsName, dataSource);
        }
    }

    private void loadDruidProperties() {
        druidSource = binder.bind("spring.datasource.druid", Bindable.of(DruidSource.class)).get();
    }

    private void loadConfiguration(Environment environment) {
        DEFAULT_TYPE = environment.getProperty("spring.datasource.default-type");

        loadDynamicDataSource(environment);

        loadDruidProperties();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        this.binder = Binder.get(environment);

        loadConfiguration(environment);
    }
}
