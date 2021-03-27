package com.kg.platform.common.mongo;

import com.kg.platform.common.utils.PropertyLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author hesimin 2016/11/15
 */
@Component
public class MongoProperty {
    private static String uri;

    private static String defaultDatabaseName;

    private static Integer connectionsPerHost;

    @Resource(name = "propertyLoader")
    protected PropertyLoader propertyLoader;

    public static String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        MongoProperty.uri = uri;
    }

    public static String getDefaultDatabaseName() {
        return defaultDatabaseName;
    }

    public void setDefaultDatabaseName(String defaultDatabaseName) {
        MongoProperty.defaultDatabaseName = defaultDatabaseName;
    }

    public static Integer getConnectionsPerHost() {
        return connectionsPerHost;
    }

    public void setConnectionsPerHost(Integer connectionsPerHost) {
        MongoProperty.connectionsPerHost = connectionsPerHost;
    }

    @PostConstruct
    private void init() {
        uri = propertyLoader.getProperty("mongo","uri");
        defaultDatabaseName = propertyLoader.getProperty("mongo","defaultDatabaseName");
        connectionsPerHost = Integer.valueOf(propertyLoader.getProperty("mongo","connectionsPerHost"));
    }
}
