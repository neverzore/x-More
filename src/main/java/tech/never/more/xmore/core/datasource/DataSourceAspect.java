package tech.never.more.xmore.core.datasource;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by zhouzb on 2018/9/3.
 */
@Aspect
@Component
@Order(value = 1)
public class DataSourceAspect {
//    // TYPE1
//    @Pointcut(value = "@annotation(tech.never.more.xmore.core.datasource.DataSource) && @annotation(dataSource)")
//    public void execute(DataSource dataSource) {
//
//    }
//
//    @Before("execute(dataSource)")
//    public void before(JoinPoint joinPoint, DataSource dataSource) {
//        System.out.println("i'm before");
//    }
//
//    @AfterReturning(value = "execute(dataSource)", returning = "result")
//    public void afterReturning(JoinPoint joinPoint, Object result, DataSource dataSource) {
//
//    }
//
//    @After("execute(dataSource)")
//    public void after(JoinPoint joinPoint, DataSource dataSource) {
//
//    }

    // TYPE2
    @Pointcut(value = "@annotation(tech.never.more.xmore.core.datasource.DataSource)")
    public void execute() {

    }

    @Before("execute() && @annotation(dataSource)")
    public void before(JoinPoint joinPoint, DataSource dataSource) {
        String dsName = dataSource.value();

        if (StringUtils.isBlank(dsName)) {
            return;
        }

        XMoreDynamicDataSource.setRouteKey(dsName);
    }

//    @AfterReturning(value = "execute()", returning = "result")
//    public void afterReturning(JoinPoint joinPoint, Object result) {
//
//    }

    @After("execute() && @annotation(dataSource)")
    public void after(JoinPoint joinPoint, DataSource dataSource) {
        String dsName = dataSource.value();
        XMoreDynamicDataSource.setRouteKey(dsName);
    }

}
