package com.youben.service.impl;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.youben.base.GenericMapper;
import com.youben.base.GenericServiceImpl;
import com.youben.entity.DatabaseInfo;
import com.youben.entity.Datasource;
import com.youben.mapper.DatabaseInfoMapper;
import com.youben.mapper.DatasourceMapper;
import com.youben.service.DatabaseInfoService;
import com.youben.utils.JdbcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-19
 * Time: 14:02
 */
@Service
public class DatabaseInfoServiceImpl extends GenericServiceImpl<DatabaseInfo> implements DatabaseInfoService {
    private DatabaseInfoMapper databaseInfoMapper;
    @Autowired
    private DatasourceMapper datasourceMapper;

    @Autowired(required = true)
    public DatabaseInfoServiceImpl(DatabaseInfoMapper databaseInfoMapper) {
        super(databaseInfoMapper);
        this.databaseInfoMapper=databaseInfoMapper;
    }

    @Override
    public void updateBySourceId(int sourceId) {
        Datasource datasource=datasourceMapper.selectByPrimaryKey(String.valueOf(sourceId));
        Connection conn=null;
        DatabaseMetaData dbmd=null;
        ResultSet rs0=null;
        try{
            conn= JdbcUtil.getConnection(datasource);
            dbmd = conn.getMetaData();
            rs0 = dbmd.getTables(null, null, null, new String[]{"TABLE"});
            while (rs0.next()) {
                String tableName=rs0.getString("TABLE_NAME");
                DatabaseInfo databaseInfo=databaseInfoMapper.getByTableName(sourceId,tableName);
                if(databaseInfo==null){
                    databaseInfo=new DatabaseInfo();
                    databaseInfo.setTableName(tableName);
                    databaseInfo.setSourceId(sourceId);
                }
                ResultSet rs1=null;
                PreparedStatement ps1=null;
                ResultSet rs2=null;
                PreparedStatement ps2=null;
                ResultSet rs3=null;
                PreparedStatement ps3=null;
                try {
                    ps1= conn.prepareStatement("select count(*) from " + tableName);
                    rs1=ps1.executeQuery();
                    if(rs1.next()){
                        int count=rs1.getInt(1);
                        databaseInfo.setRecordCount(count);
                        if(tableName.equals("sequences")) {
                            ps2 = conn.prepareStatement("select last_modified from " + tableName + "  ORDER  by last_modified limit 1");
                        }else{
                            ps2 = conn.prepareStatement("select gmt_modified from " + tableName + "  ORDER  by gmt_modified limit 1");
                        }
                        rs2=ps2.executeQuery();
                        if(rs2.next()){
                            databaseInfo.setRecordEalyDate(rs2.getTimestamp(1));
                            if(tableName.equals("sequences")) {
                                ps3 = conn.prepareStatement("select last_modified from " + tableName + "  ORDER  by last_modified desc limit 1");
                            }else{
                                ps3 = conn.prepareStatement("select gmt_modified from " + tableName + "  ORDER  by gmt_modified desc limit 1");
                            }
                            rs3=ps3.executeQuery();
                            if(rs3.next()){
                                databaseInfo.setRecordLateDate(rs3.getTimestamp(1));
                            }
                        }
                    }
                    if(databaseInfo.getId()>0) {
                        databaseInfoMapper.updateByPrimaryKey(databaseInfo);
                    }else{
                        databaseInfoMapper.insert(databaseInfo);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(rs1!=null){
                        rs1.close();;
                    }
                    if(ps1!=null){
                        ps1.close();
                    }
                    if(rs2!=null){
                        rs2.close();
                    }
                    if(ps2!=null){
                        ps2.close();
                    }
                    if(rs3!=null){
                        rs3.close();
                    }
                    if(ps3!=null){
                        ps3.close();
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(rs0!=null){
                try {
                    rs0.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
