package com.youben.utils;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.youben.entity.Datasource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static Connection getConnection(Datasource datasource){
        String driver="com.mysql.jdbc.Driver";  //获取mysql数据库的驱动类
        String url="jdbc:mysql://"+datasource.getIp()+":"+datasource.getPort()+"/"+datasource.getDatabaseName(); //连接数据库（kucun是数据库名）
        Connection conn=null;
        try{
            Class.forName(driver);
            conn= DriverManager.getConnection(url,datasource.getUsername(),datasource.getPassword());//获取连接对
            if(conn!=null&&!conn.isClosed()){
                return conn;
            }
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void createDestTables(DruidPooledConnection fromConn,DruidPooledConnection toConn){
        List<String> tableNameList=new ArrayList<String>();
        ResultSet rs0=null;

        try {
            DatabaseMetaData fromMetaData=fromConn.getMetaData();
            rs0 =fromMetaData .getTables(null, null, null, new String[]{"TABLE"});
            while (rs0.next()) {
                tableNameList.add(rs0.getString("TABLE_NAME"));
            }
            for(int i=0;i<tableNameList.size();i++) {
                String createTableSql="create table "+tableNameList.get(i)+"(";
                ResultSet colRet=null;
                ResultSet rs1=null;
                try {
                    rs1 = toConn.getMetaData().getTables(null, null, tableNameList.get(i), null);
                    if (!rs1.next()) {
                        colRet = fromMetaData.getColumns(null, "%", tableNameList.get(i), "%");
                        while (colRet.next()) {
                            String columnName = colRet.getString("COLUMN_NAME");
                            String columnType = colRet.getString("TYPE_NAME");
                            int type = colRet.getInt("DATA_TYPE ");
                            int datasize = colRet.getInt("COLUMN_SIZE");
                            int digits = colRet.getInt("DECIMAL_DIGITS");
                            int nullable = colRet.getInt("NULLABLE");
                            switch (type){
                                case Types.BIGINT:
                                    String isAutoIncrement=colRet.getString("IS_AUTOINCREMENT ");
                                    createTableSql+=columnName+" bigint("+datasize+")";
                                    if(isAutoIncrement.equals("YES")){
                                        createTableSql+=" auto_invrement";
                                    }
                                    if(nullable==1){
                                        createTableSql+=" null,";
                                    }else{
                                        createTableSql+=" not null,";
                                    }
                                    break;
                                case Types.VARCHAR:
                                    createTableSql+=columnName+" varchar("+datasize+")";
                                    if(nullable==1){
                                        createTableSql+=" null,";
                                    }else{
                                        createTableSql+=" not null,";
                                    }
                                    break;
                                case Types.BIT:
                                    createTableSql+=columnName+" varchar("+datasize+")";
                                    if(nullable==1){
                                        createTableSql+=" null,";
                                    }else{
                                        createTableSql+=" not null,";
                                    }
                                    break;
                                case Types.TINYINT:
                                    createTableSql+=columnName+" tinyint("+datasize+")";
                                    if(nullable==1){
                                        createTableSql+=" null,";
                                    }else{
                                        createTableSql+=" not null,";
                                    }
                                    break;
                                case Types.DATALINK:
                                    createTableSql+=columnName+" datetime";
                                    if(nullable==1){
                                        createTableSql+=" null,";
                                    }else{
                                        createTableSql+=" not null,";
                                    }
                                    break;
                                case Types.INTEGER:
                                    createTableSql+=columnName+" int("+datasize+")";
                                    if(nullable==1){
                                        createTableSql+=" null,";
                                    }else{
                                        createTableSql+=" not null,";
                                    }
                                    break;
                                case Types.DOUBLE:
                                    createTableSql+=columnName+" double";
                                    if(nullable==1){
                                        createTableSql+=" null,";
                                    }else{
                                        createTableSql+=" not null,";
                                    }
                                    break;
                                case Types.LONGVARCHAR:
                                    createTableSql+=columnName+" mediumtext";
                                    if(nullable==1){
                                        createTableSql+=" null,";
                                    }else{
                                        createTableSql+=" not null,";
                                    }
                                    break;
                            }

                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    try {
                        rs1.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    try {
                        colRet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                rs0.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
