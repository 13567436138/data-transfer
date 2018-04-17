package com.youben.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.youben.entity.Datasource;

import java.sql.SQLException;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-17
 * Time: 18:56
 */
public class DatasourceUtils {
    public static DruidDataSource getDataSource(Datasource d){
        DruidDataSource datasource = new DruidDataSource();
        String url="jdbc:mysql://"+d.getIp()+":"+d.getPort()+"/"+d.getDatabaseName();
        datasource.setUrl(url);
        datasource.setUsername(d.getUsername());
        datasource.setPassword(d.getPassword());
        String driver="com.mysql.jdbc.Driver";
        datasource.setDriverClassName(driver);

        //configuration
        datasource.setInitialSize(5);
        datasource.setMinIdle(5);
        datasource.setMaxActive(100);
        datasource.setMaxWait(60000);
        datasource.setTimeBetweenEvictionRunsMillis(60000);
        datasource.setMinEvictableIdleTimeMillis(300000);
        datasource.setValidationQuery("SELECT 1 FROM DUAL  ");
        datasource.setTestWhileIdle(true);
        datasource.setTestOnBorrow(true);
        datasource.setTestOnReturn(false);
        datasource.setPoolPreparedStatements(true);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(20  );

        return datasource;
    }
}
