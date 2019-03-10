package tech.never.more.xmore.core.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class XMoreDynamicDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> routeKey = new ThreadLocal<>();

    @Override
    protected Object determineCurrentLookupKey() {
        return routeKey.get();
    }

    public static void setRouteKey(String key) {
        routeKey.set(key);
    }

    public static void clearRouteKey() {
        routeKey.remove();
    }
}
