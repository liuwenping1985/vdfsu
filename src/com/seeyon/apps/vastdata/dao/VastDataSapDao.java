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

    public VastDataSapDao() {
        init();
    }

    private void init() {
        LOG.info("初始化SAP连接池");
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        try {
            comboPooledDataSource.setDriverClass(SystemProperties.getInstance().getProperty("com.seeyon.apps.vastdata.sap.driver"));
            comboPooledDataSource.setJdbcUrl(SystemProperties.getInstance().getProperty("com.seeyon.apps.vastdata.sap.jdbcurl"));
            comboPooledDataSource.setUser(SystemProperties.getInstance().getProperty("com.seeyon.apps.vastdata.sap.user"));
            comboPooledDataSource.setPassword(SystemProperties.getInstance().getProperty("com.seeyon.apps.vastdata.sap.pwd"));
            comboPooledDataSource.setInitialPoolSize(1);
            comboPooledDataSource.setMaxPoolSize(2);
            dataSource = comboPooledDataSource;
        } catch (Exception e) {
            LOG.info("初始化SAP连接池失败！西内！");
            LOG.error(e.getLocalizedMessage(), e);
        }
        LOG.info("初始化SAP2连接池");
        ComboPooledDataSource comboPooledDataSource2 = new ComboPooledDataSource();
        try {
            comboPooledDataSource2.setDriverClass(SystemProperties.getInstance().getProperty("com.seeyon.apps.vastdata.sap2.driver"));
            comboPooledDataSource2.setJdbcUrl(SystemProperties.getInstance().getProperty("com.seeyon.apps.vastdata.sap2.jdbcurl"));
            comboPooledDataSource2.setUser(SystemProperties.getInstance().getProperty("com.seeyon.apps.vastdata.sap2.user"));
            comboPooledDataSource2.setPassword(SystemProperties.getInstance().getProperty("com.seeyon.apps.vastdata.sap2.pwd"));
            comboPooledDataSource2.setInitialPoolSize(1);
            comboPooledDataSource2.setMaxPoolSize(2);
            dataSource2 = comboPooledDataSource2;
        } catch (Exception e) {
            LOG.info("初始化SAP2连接池失败！西内！");
            LOG.error(e.getLocalizedMessage(), e);
        }
    }


    public void insertOrUpdate(FormMappingVo fmv, Map data) {
        try {

            String delegateClass = AppContext.getSystemProperty("vastdata." + fmv.getCode());
            LOG.info("获取代理类:"+delegateClass);
            if (!StringUtils.isEmpty(delegateClass)) {
                Class cls = Class.forName(delegateClass);
                Object obj = cls.newInstance();
                if (obj instanceof DataBaseDelegate) {
                    ((DataBaseDelegate) obj).delegate(getDataSource(data), fmv, data);
                }
            } else {
                LOG.error("找不到数据库处理代理！[code:" + fmv.getCode() + "]");
            }


        } catch (Exception|Error e) {
            LOG.error("执行SAP传输失败", e);
        }
    }

    public DataSource getDataSource(Map data) {

        Object val = data.remove("IS_OLD_SAP");
        if("否".equals(val)){
            return dataSource2;
        }
        return dataSource;
    }

    public boolean testConn(int dataSourceIndex) {
        DataSource targetDataSource;
        if (dataSourceIndex == 1) {
            targetDataSource = dataSource;
        } else {
            targetDataSource = dataSource2;
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

}
