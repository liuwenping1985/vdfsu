package com.seeyon.apps.vastdata.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.seeyon.apps.vastdata.vo.FormMappingVo;
import com.seeyon.ctp.common.AppContext;
import com.seeyon.ctp.common.constants.SystemProperties;
import com.seeyon.ctp.common.log.CtpLogFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class VastDataSapDao {
    private static final Log LOG = CtpLogFactory.getLog(VastDataSapDao.class);
    private  DataSource dataSource = null;
    private  DataSource dataSource2 = null;
    private  DataSource dataSource3 = null;
    public VastDataSapDao() {
        init();
    }

    private void init() {
        LOG.info("初始化SAP连接池");
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        try {
            comboPooledDataSource.setDriverClass(SystemProperties.getInstance().getProperty("vastdata.sap.driver"));
            comboPooledDataSource.setJdbcUrl(SystemProperties.getInstance().getProperty("vastdata.sap.jdbcurl"));
            comboPooledDataSource.setUser(SystemProperties.getInstance().getProperty("vastdata.sap.user"));
            comboPooledDataSource.setPassword(SystemProperties.getInstance().getProperty("vastdata.sap.pwd"));
            comboPooledDataSource.setInitialPoolSize(2);
            comboPooledDataSource.setMaxPoolSize(3);
            dataSource = comboPooledDataSource;
        } catch (Exception e) {
            LOG.info("初始化SAP连接池失败！西内！");
            LOG.error(e.getLocalizedMessage(), e);
        }
        LOG.info("初始化SAP2连接池");
        ComboPooledDataSource comboPooledDataSource2 = new ComboPooledDataSource();
        try {
            comboPooledDataSource2.setDriverClass(SystemProperties.getInstance().getProperty("vastdata.sap2.driver"));
            comboPooledDataSource2.setJdbcUrl(SystemProperties.getInstance().getProperty("vastdata.sap2.jdbcurl"));
            comboPooledDataSource2.setUser(SystemProperties.getInstance().getProperty("vastdata.sap2.user"));
            comboPooledDataSource2.setPassword(SystemProperties.getInstance().getProperty("vastdata.sap2.pwd"));
            comboPooledDataSource2.setInitialPoolSize(2);
            comboPooledDataSource2.setMaxPoolSize(3);
            dataSource2 = comboPooledDataSource2;
        } catch (Exception e) {
            LOG.info("初始化SAP2连接池失败！西内！");
            LOG.error(e.getLocalizedMessage(), e);
        }
        LOG.info("初始化SAP3连接池");
        ComboPooledDataSource comboPooledDataSource3 = new ComboPooledDataSource();
        try {
            comboPooledDataSource3.setDriverClass(SystemProperties.getInstance().getProperty("vastdata.sap3.driver"));
            comboPooledDataSource3.setJdbcUrl(SystemProperties.getInstance().getProperty("vastdata.sap3.jdbcurl"));
            comboPooledDataSource3.setUser(SystemProperties.getInstance().getProperty("vastdata.sap3.user"));
            comboPooledDataSource3.setPassword(SystemProperties.getInstance().getProperty("vastdata.sap3.pwd"));
            comboPooledDataSource3.setInitialPoolSize(2);
            comboPooledDataSource3.setMaxPoolSize(3);
            dataSource3 = comboPooledDataSource3;
        } catch (Exception e) {
            LOG.info("初始化SAP3连接池失败！西内！");
            LOG.error(e.getLocalizedMessage(), e);
        }
    }


    public void insertOrUpdate(FormMappingVo fmv, Map data,Map oaData) {
        try {

            String delegateClass = AppContext.getSystemProperty("vastdata." + fmv.getCode());
            LOG.info("获取代理类:"+delegateClass);
            if (!StringUtils.isEmpty(delegateClass)) {
                Class cls = Class.forName(delegateClass);
                Object obj = cls.newInstance();
                if (obj instanceof DataBaseDelegate) {
                    LOG.info("调用代理类处理方法");
                    ((DataBaseDelegate) obj).delegate(getDataSource(data), fmv, data,oaData);
                }
            } else {
                LOG.error("找不到数据库处理代理！[code:" + fmv.getCode() + "]");
            }


        } catch (Exception|Error e) {
            LOG.error("执行SAP传输失败", e);
        }
    }

    public DataSource getDataSource(Map data) {

        Object val = data.get("IS_OLD_SAP");
        if("否".equals(val)){
            return dataSource2;
        }
        return dataSource;
    }

    public boolean testConn(int dataSourceIndex) {
        DataSource targetDataSource;
        if (dataSourceIndex == 1) {
            targetDataSource = dataSource;
        } else if(dataSourceIndex == 2) {
            targetDataSource = dataSource2;
        }else{
            targetDataSource = dataSource3;
        }
        if (targetDataSource != null) {
            try {
                Connection conn = targetDataSource.getConnection();
                if (conn != null) {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;

    }

    public DataSource getDataSource1() {
        return dataSource;
    }



    public DataSource getDataSource2() {
        return dataSource2;
    }

    public DataSource getDataSource3() {
        return dataSource3;
    }

}
