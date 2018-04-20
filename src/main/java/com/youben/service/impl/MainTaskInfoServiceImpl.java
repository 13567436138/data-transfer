package com.youben.service.impl;

import com.youben.base.GenericMapper;
import com.youben.base.GenericServiceImpl;
import com.youben.entity.DatabaseInfo;
import com.youben.entity.Datasource;
import com.youben.entity.MainTask;
import com.youben.entity.MainTaskInfo;
import com.youben.mapper.DatasourceMapper;
import com.youben.mapper.MainTaskInfoMapper;
import com.youben.mapper.MainTaskMapper;
import com.youben.service.MainTaskInfoService;
import com.youben.utils.JdbcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.sql.*;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-19
 * Time: 14:04
 */
@Service
public class MainTaskInfoServiceImpl extends GenericServiceImpl<MainTaskInfo> implements MainTaskInfoService {
    private MainTaskInfoMapper mainTaskInfoMapper;
    @Autowired
    private MainTaskMapper mainTaskMapper;
    @Autowired
    private DatasourceMapper datasourceMapper;
    @Autowired(required = true)
    public MainTaskInfoServiceImpl(MainTaskInfoMapper mainTaskInfoMapper) {
        super(mainTaskInfoMapper);
        this.mainTaskInfoMapper=mainTaskInfoMapper;
    }

    @Override
    public void updateBymainTaskId(int mainTaskId) {
        MainTask mainTask=mainTaskMapper.selectByPrimaryKey(String.valueOf(mainTaskId));
        Datasource fromSource=datasourceMapper.selectByPrimaryKey(String.valueOf(mainTask.getFromSource()));
        Datasource toSource=datasourceMapper.selectByPrimaryKey(String.valueOf(mainTask.getToSource()));
        Connection fromConn=null;
        Connection toConn=null;
        try{
            DatabaseMetaData dbmdFrom=null;
            ResultSet rsFrom0=null;
            try {
                toConn=JdbcUtil.getConnection(toSource);
                fromConn = JdbcUtil.getConnection(fromSource);
                dbmdFrom = fromConn.getMetaData();
                rsFrom0 = dbmdFrom.getTables(null, null, null, new String[]{"TABLE"});
                while (rsFrom0.next()) {
                    String tableName = rsFrom0.getString("TABLE_NAME");
                    MainTaskInfo mainTaskInfo = mainTaskInfoMapper.getByTableName(mainTaskId, tableName);
                    if (mainTaskInfo == null) {
                        mainTaskInfo = new MainTaskInfo();
                        mainTaskInfo.setTableName(tableName);
                        mainTaskInfo.setMainTaskId(mainTaskId);
                    }
                    ResultSet rsFrom1 = null;
                    PreparedStatement psFrom1 = null;
                    ResultSet rsFrom2 = null;
                    PreparedStatement psFrom2 = null;
                    ResultSet rsFrom3 = null;
                    PreparedStatement psFrom3 = null;
                    try {
                        psFrom1 = fromConn.prepareStatement("select count(*) from " + tableName);
                        rsFrom2 = psFrom1.executeQuery();
                        if (rsFrom2.next()) {
                            int count = rsFrom2.getInt(1);
                            mainTaskInfo.setFromRecordCount(count);
                            if (tableName.equals("sequences")) {
                                psFrom2 = fromConn.prepareStatement("select last_modified from " + tableName + "  ORDER  by last_modified limit 1");
                            } else {
                                psFrom2 = fromConn.prepareStatement("select gmt_modified from " + tableName + "  ORDER  by gmt_modified limit 1");
                            }
                            rsFrom2 = psFrom2.executeQuery();
                            if (rsFrom2.next()) {
                                mainTaskInfo.setFromRecordEalyDate(rsFrom2.getTimestamp(1));
                                if (tableName.equals("sequences")) {
                                    psFrom3 = fromConn.prepareStatement("select last_modified from " + tableName + "  ORDER  by last_modified desc limit 1");
                                } else {
                                    psFrom3 = fromConn.prepareStatement("select gmt_modified from " + tableName + "  ORDER  by gmt_modified desc limit 1");
                                }
                                rsFrom3 = psFrom3.executeQuery();
                                if (rsFrom3.next()) {
                                    mainTaskInfo.setFromRecordLateDate(rsFrom3.getTimestamp(1));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (rsFrom1 != null) {
                            rsFrom1.close();
                            ;
                        }
                        if (psFrom1 != null) {
                            psFrom1.close();
                        }
                        if (rsFrom2 != null) {
                            rsFrom2.close();
                        }
                        if (psFrom2 != null) {
                            psFrom2.close();
                        }
                        if (rsFrom3 != null) {
                            rsFrom3.close();
                        }
                        if (psFrom3 != null) {
                            psFrom3.close();
                        }
                    }
                    ResultSet rsTo1 = null;
                    PreparedStatement psTo1 = null;
                    ResultSet rsTo2 = null;
                    PreparedStatement psTo2 = null;
                    ResultSet rsTo3 = null;
                    PreparedStatement psTo3 = null;
                    try {
                        psTo1 = toConn.prepareStatement("select count(*) from " + tableName);
                        rsTo1 = psTo1.executeQuery();
                        if (rsTo1.next()) {
                            int count = rsTo1.getInt(1);
                            mainTaskInfo.setToRecordCount(count);
                            if (tableName.equals("sequences")) {
                                psTo2 = toConn.prepareStatement("select last_modified from " + tableName + "  ORDER  by last_modified limit 1");
                            } else {
                                psTo2 = toConn.prepareStatement("select gmt_modified from " + tableName + "  ORDER  by gmt_modified limit 1");
                            }
                            rsTo2 = psTo2.executeQuery();
                            if (rsTo2.next()) {
                                mainTaskInfo.setToRecordEalyDate(rsTo2.getTimestamp(1));
                                if (tableName.equals("sequences")) {
                                    psTo3 = toConn.prepareStatement("select last_modified from " + tableName + "  ORDER  by last_modified desc limit 1");
                                } else {
                                    psTo3 = toConn.prepareStatement("select gmt_modified from " + tableName + "  ORDER  by gmt_modified desc limit 1");
                                }
                                rsTo3 = psTo3.executeQuery();
                                if (rsTo3.next()) {
                                    mainTaskInfo.setToRecordLateDate(rsTo3.getTimestamp(1));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (rsTo1 != null) {
                            rsTo1.close();
                            ;
                        }
                        if (psTo1 != null) {
                            psTo1.close();
                        }
                        if (rsTo2 != null) {
                            rsTo2.close();
                        }
                        if (psTo2 != null) {
                            psTo2.close();
                        }
                        if (rsTo3 != null) {
                            rsTo3.close();
                        }
                        if (psTo3 != null) {
                            psTo3.close();
                        }
                    }
                    if (mainTaskInfo.getId() > 0) {
                        mainTaskInfoMapper.updateByPrimaryKey(mainTaskInfo);
                    } else {
                        mainTaskInfoMapper.insert(mainTaskInfo);
                    }
                }

            }finally {
                rsFrom0.close();
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fromConn!=null){
                try {
                    fromConn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(toConn!=null){
                try {
                    toConn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
