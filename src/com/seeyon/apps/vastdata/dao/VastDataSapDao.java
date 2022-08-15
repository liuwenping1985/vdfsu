package com.seeyon.apps.vastdata.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.seeyon.apps.vastdata.vo.FormMappingVo;
import com.seeyon.ctp.common.constants.SystemProperties;
import com.seeyon.ctp.common.log.CtpLogFactory;
import org.apache.commons.logging.Log;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class VastDataSapDao {
    private static final Log LOG = CtpLogFactory.getLog(VastDataSapDao.class);
    private static DataSource dataSource = null;

    public VastDataSapDao() {
        init();
    }

    private void init() {
        LOG.info("初始化SAP连接池");
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        String url = SystemProperties.getInstance().getProperty("vastdata.sap.jdbcurl");
        try {
            comboPooledDataSource.setDriverClass(SystemProperties.getInstance().getProperty("vastdata.sap.driver"));
            comboPooledDataSource.setJdbcUrl(SystemProperties.getInstance().getProperty("vastdata.sap.jdbcurl"));
            comboPooledDataSource.setUser(SystemProperties.getInstance().getProperty("vastdata.sap.user"));
            comboPooledDataSource.setPassword(SystemProperties.getInstance().getProperty("vastdata.sap.pwd"));
            comboPooledDataSource.setInitialPoolSize(1);
            comboPooledDataSource.setMaxPoolSize(2);
            dataSource = comboPooledDataSource;
        } catch (Exception e) {
            LOG.info("初始化SAP连接池失败！西内！");
            LOG.error(e.getLocalizedMessage(), e);
        }
    }

    public void insertOrUpdate(String tableName, String whereStatement, Map data){
        try {
            Connection conn = dataSource.getConnection();

        } catch (Exception e) {
            LOG.error("执行SQL失败",e);
        }
    }

    public void insertOrUpdate(FormMappingVo fmv, Map data){
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            System.out.println(conn);
            LOG.error(fmv);
            LOG.error(data);

        } catch (Exception e) {
            LOG.error("执行SQL失败",e);
        }finally {
            try {
                conn.close();
            } catch (Exception throwables) {

            }
        }
    }



}
