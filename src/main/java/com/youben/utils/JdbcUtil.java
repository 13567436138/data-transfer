package com.youben.utils;

import com.youben.entity.Datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-16
 * Time: 14:41
 */
public class JdbcUtil {
    public static boolean testOk(Datasource datasource){
        String driver="com.mysql.jdbc.Driver";  //获取mysql数据库的驱动类
        String url="jdbc:mysql://"+datasource.getIp()+":"+datasource.getPort()+"/"+datasource.getDatabaseName(); //连接数据库（kucun是数据库名）
        Connection conn=null;
        try{
            Class.forName(driver);
            conn= DriverManager.getConnection(url,datasource.getUsername(),datasource.getPassword());//获取连接对
            if(conn!=null&&!conn.isClosed()){
                return true;
            }
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;

    }
}
