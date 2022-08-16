package com.seeyon.apps.vastdata.dao;

import com.seeyon.apps.vastdata.vo.FieldMappingVo;
import com.seeyon.apps.vastdata.vo.FormMappingVo;
import com.seeyon.apps.vastdata.vo.SlaveFormMappingVo;
import com.seeyon.ctp.common.exceptions.BusinessException;
import com.seeyon.ctp.common.log.CtpLogFactory;
import com.seeyon.ctp.util.JDBCAgent;
import com.seeyon.ctp.util.UUIDGenerator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractDataBaseDelegate implements DataBaseDelegate {

    private static final Log LOG = CtpLogFactory.getLog(AbstractDataBaseDelegate.class);

    protected String tv(Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof String) {
            return "'" + val + "'";
        }
        return String.valueOf(val);
    }

    protected List<Map> queryDataList(DataSource dataSource, String sql) {
        JDBCAgent agent = null;
        try {
            agent = new JDBCAgent(dataSource.getConnection());
            agent.execute(sql);
            return agent.resultSetToList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (agent != null) {
                agent.close();
            }
        }

        return new ArrayList<>();

    }

    protected boolean isInsert(DataSource dataSource, FormMappingVo fmv, Map data) {
        String tName = fmv.getTargetTableName();
        String tPK = fmv.getTargetPkField();
        Object tPkVal = data.get(tPK);
        String sql = "select * from " + tName + " where " + tPK + "=" + tv(tPkVal);
        List<Map> exisitDataList = queryDataList(dataSource, sql);
        boolean isInsert = true;
        if (!CollectionUtils.isEmpty(exisitDataList)) {
            Map edMap = exisitDataList.get(0);
            if (!CollectionUtils.isEmpty(edMap)) {
                isInsert = false;
            } else {
                isInsert = true;
            }

        } else {

            isInsert = true;
        }
        return isInsert;

    }

    protected String buildInsertSQL(FormMappingVo fmv, Map data, int index) {
        String tName = fmv.getTargetTableName();
        StringBuilder insertSQL = new StringBuilder("INSERT INTO " + tName);
        StringBuilder insertKeys = new StringBuilder();
        StringBuilder insertValues = new StringBuilder();
        for (FieldMappingVo field : fmv.getFields()) {
            Object val = data.get(field.getTargetFieldName());
            if (val != null && !"null".equals(val)) {
                if (StringUtils.isEmpty(insertKeys.toString())) {
                    insertKeys.append(field.getTargetFieldName());
                } else {
                    insertKeys.append(",").append(field.getTargetFieldName());
                }
                if (StringUtils.isEmpty(insertValues.toString())) {
                    insertValues.append(tv(val));
                } else {
                    insertValues.append(",").append(tv(val));
                }
            }
        }
        String pkF = fmv.getPkFiled();
        if (pkF != null) {
            String st = fmv.getPkFiledGenStrategy();
            switch (st) {
                case "auto_increase": {
                    Long id = UUID.randomUUID().getMostSignificantBits();
                    insertKeys.append(",").append(pkF);
                    insertValues.append(",").append(id.intValue());
                    break;
                }
                case "main": {
                }
                default: {
                }
            }
        }
        if (fmv instanceof SlaveFormMappingVo) {
            SlaveFormMappingVo sfmv = (SlaveFormMappingVo) fmv;
            List<FieldMappingVo> eFileds = sfmv.getExtendFields();
            if (eFileds != null) {
                for (FieldMappingVo f : eFileds) {
                    String fName = f.getTargetFieldName();
                    String fType = f.getTargetFieldType();
                    if ("self_increase".equals(fType)) {
                        insertKeys.append(",").append(fName);
                        insertValues.append(",").append(index);
                    }
                    if ("main".equals(fType)) {
                        insertKeys.append(",").append(fName);
                        insertValues.append(",").append(data.get(fName));
                    }
                }
            }
        }
        insertSQL.append("(" + insertKeys.toString() + ")");
        insertSQL.append(" values(" + insertValues.toString() + ")");
        LOG.info("insert sql:" + insertSQL.toString());
        return insertSQL.toString();

    }

    protected String buildUpdateSQL(FormMappingVo fmv, Map data) {
        String tName = fmv.getTargetTableName();
        String tPK = fmv.getTargetPkField();
        Object tPkVal = data.get(tPK);
        StringBuilder updateSQL = new StringBuilder("update " + tName + " set ");
        StringBuilder updateKeys = new StringBuilder("");
        for (FieldMappingVo field : fmv.getFields()) {
            String tfn = field.getTargetFieldName();
            Object val = data.get(tfn);
            if (val != null && !"null".equals(val)) {
                if (updateKeys.toString().isEmpty()) {
                    updateKeys.append(tfn + "=" + tv(val));
                } else {
                    updateKeys.append("," + tfn + "=" + tv(val));
                }
            }


        }
        if (updateKeys.toString().isEmpty()) {
            return "";
        }

        updateSQL.append(" where " + tPK + "=" + tv(tPkVal));
        return updateSQL.toString();

    }

    protected boolean insertData(Connection connection, FormMappingVo fmv, Map data) {
        String insertSQL = buildInsertSQL(fmv, data, 1);
        return executeUpdate(connection, insertSQL);

    }

    protected boolean updateData(Connection connection, FormMappingVo fmv, Map data) {
        String updateSQL = buildUpdateSQL(fmv, data);
        if (!updateSQL.isEmpty()) {
            return executeUpdate(connection, updateSQL);
        }

        return false;

    }

    private boolean executeUpdate(Connection conn, String sql) {
        JDBCAgent agent = new JDBCAgent(conn);
        try {
            int ret = agent.execute(sql);
            LOG.info("EXECUTE:" + sql);
            return ret > 0;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("执行失败", e);
        } finally {
            if (agent != null) {
                try {
                    agent.close();
                    if (conn != null) {
                        conn.close();
                    }
                } catch (Exception throwables) {

                }
            }
        }
        return false;
    }

    @Override
    public void delegate(DataSource dataSource, FormMappingVo fmv, Map data) {
        boolean isInsert = isInsert(dataSource, fmv, data);
        if (isInsert) {
            try {
                insertData(dataSource.getConnection(), fmv, data);
            } catch (SQLException e) {
                LOG.error("get connection failed!", e);
                return;
            }
        } else {
            try {
                updateData(dataSource.getConnection(), fmv, data);
            } catch (SQLException e) {
                LOG.error("get connection failed!", e);
                return;
            }
        }
        //处理子表
        List<SlaveFormMappingVo> slaveTbList = fmv.getSlaves();
        if (CollectionUtils.isEmpty(slaveTbList)) {
            return;
        }
        try {

            for (SlaveFormMappingVo sfmv : slaveTbList) {
                //先删除
                String pkf = sfmv.getTargetPkField();
                Object pkVal = data.get(pkf);
                if (pkVal != null) {
                    String deleteSQL = "DELETE FROM " + sfmv.getTargetTableName() + " where " + pkf + "=" + tv(pkVal);
                    executeUpdate(dataSource.getConnection(), deleteSQL);
                } else {
                    LOG.error("找不到主键，无法更新:" + data);
                    continue;
                }
                //在插入
                List<Map> dataList = (List<Map>) data.get(sfmv.getTargetTableName());
                List<String> insertSQLList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(dataList)) {
                    int index = 1;
                    for (Map dm : dataList) {
                        dm.put(pkf, pkVal);
                        insertSQLList.add(buildInsertSQL(sfmv, dm, index));
                        index++;
                    }

                }
                if (!CollectionUtils.isEmpty(insertSQLList)) {
                    Connection conn = dataSource.getConnection();
                    JDBCAgent agent = new JDBCAgent(conn);
                    try {
                        insertSQLList.stream().forEach(obj -> {
                            LOG.info("EXECUTE:" + obj);
                        });
                        agent.executeBatch(insertSQLList);

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (agent != null) {
                            agent.close();
                        }
                        try {
                            conn.close();
                        } finally {

                        }
                    }
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
