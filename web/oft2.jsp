<%@ page import="com.alibaba.fastjson.JSON" %>
<%@ page import="com.mchange.v2.c3p0.ComboPooledDataSource" %>
<%@ page import="com.seeyon.apps.vastdata.dao.DataBaseDelegate" %>
<%@ page import="com.seeyon.apps.vastdata.service.VastDataGenericService" %>
<%@ page import="com.seeyon.apps.vastdata.service.VastDataMappingService" %>
<%@ page import="com.seeyon.apps.vastdata.util.WebUtil" %>
<%@ page import="com.seeyon.apps.vastdata.vo.FieldMappingVo" %>
<%@ page import="com.seeyon.apps.vastdata.vo.FormMappingVo" %>
<%@ page import="com.seeyon.apps.vastdata.vo.SlaveFormMappingVo" %>
<%@ page import="com.seeyon.ctp.common.AppContext" %>
<%@ page import="com.seeyon.ctp.common.constants.SystemProperties" %>
<%@ page import="com.seeyon.ctp.common.log.CtpLogFactory" %>
<%@ page import="com.seeyon.ctp.organization.bo.V3xOrgMember" %>
<%@ page import="com.seeyon.ctp.organization.dao.OrgHelper" %>
<%@ page import="com.seeyon.ctp.util.JDBCAgent" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.springframework.util.CollectionUtils" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ceshi</title>
</head>
<body>
<%!
    static final Log LOG = CtpLogFactory.getLog(VastDataGenericService.class);
    static DataSource dataSource = null;
    static DataSource dataSource2 = null;
    static boolean isInit = false;

    public static Map fetchOADataJSP(String oaTemplateNo, Long affairId) {
        //第一步查出来数据
        JDBCAgent agent = new JDBCAgent();
        try {

            String sql = "select form_recordid from ctp_affair where id=" + affairId;
            LOG.info("formmain id query sql:" + sql);
            agent.execute(sql);
            Map idMap = agent.resultSetToMap();
            if (!CollectionUtils.isEmpty(idMap)) {
                //主表idform_recordid
                Long id = Long.valueOf(String.valueOf(idMap.get("form_recordid")));
                if (id == null) {
                    throw new RuntimeException("主表id为空，严重错误");
                }
                //如果一次推送多个主表数据改这个
                FormMappingVo formMappingVo = VastDataMappingService.getInstance().getCfg(oaTemplateNo);
                if (formMappingVo != null) {
                    String sql2 = "select * from " + formMappingVo.getTableName() + " where id=" + id;
                    LOG.info("query form data sql:" + sql2);
                    agent.execute(sql2);
                    //主表数据
                    Map data = agent.resultSetToMap();
                    LOG.info("query form data:" + data);
                    //子表结构
                    List<SlaveFormMappingVo> slaves = formMappingVo.getSlaves();
                    //不为空表示有子表
                    if (!CollectionUtils.isEmpty(slaves)) {
                        //子表数据一个一个找
                        //数据结构为：子表code：List<Map>
                        Map slaveData = new HashMap();
                        for (FormMappingVo slave : slaves) {

                            String sql3 = "select * from " + slave.getTableName() + " where formmain_id=" + id;
                            LOG.info("query slave form data:" + sql3);
                            agent.execute(sql3);
                            List<Map> slaveDataList = agent.resultSetToList();
                            LOG.info("slave form data:" + JSON.toJSONString(slaveDataList));
                            //关键点：子表用的表名作为key存储的
                            slaveData.put(slave.getTableName(), slaveDataList);
                        }
                        //把子表数据注入到主表里,这也是关键点，子表怎么存储在主表里的
                        data.put("slaves", slaveData);

                    }
                    LOG.info("data is " + data);
                    return data;
                }

            } else {
                LOG.info(" no formmain id is found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (agent != null) {
                agent.close();
            }
        }
        return new HashMap();
    }

    //不递归 只做一层,子表单独循环处理，也可以改成递归方式一次处理完，先写个low版的
    public static Map mappingValueJSP(Map data, FormMappingVo cfg) {

        //form_recordid
        List<FieldMappingVo> fields = cfg.getFields();
        Map retMap = new HashMap<>();
        for (FieldMappingVo fmv : fields) {
            Object val = data.get(fmv.getSourceFieldName());
            String sType = fmv.getSourceFieldType();
            //TODO:OA的数据处理，比如枚举或者其他一些处理（所有的特殊处理这里均可以胜任）
            String fName = fmv.getTargetFieldName();
            switch (sType) {
                case "enum": {
                    String enumVal = String.valueOf(WebUtil.getEnumShowValue(val));
                    retMap.put(fName, enumVal);
                    break;
                }
                case "member": {
                    try {
                        V3xOrgMember member = OrgHelper.getMember(WebUtil.getLong(val));
                        if (member != null) {
                            retMap.put(fName, member.getName());
                        } else {
                            retMap.put(fName, val);
                        }

                    } catch (Exception e) {
                        retMap.put(fName, val);
                    }

                    break;
                }
                case "float": {
                    Float f = WebUtil.getFloat(val);
                    if (f != null) {
                        retMap.put(fName, f);
                    } else {
                        retMap.put(fName, val);
                    }
                    break;
                }
                case "int": {
                    Integer fint = WebUtil.getInteger(val);
                    if (fint != null) {
                        retMap.put(fName, fint);
                    } else {
                        retMap.put(fName, val);
                    }
                    break;
                }
                default: {
                    retMap.put(fName, val);
                }
            }


        }

        return retMap;
    }

    public static void processDataJSP(String code, Long affairId) {

        //获取oa数据
        LOG.info("开始查询OA数据");
        Map oaData = fetchOADataJSP(code, affairId);
        LOG.info("OA数据查询完毕:" + oaData);
        //获取映射配置
        FormMappingVo cfg = VastDataMappingService.getInstance().getCfg(code);
        //这就是最后的结果 mainData
        LOG.info("开始处理OA主表数据");
        Map mainData = mappingValueJSP(oaData, cfg);
        LOG.info("处理OA主表数据完毕:" + mainData);
        //处理子表
        if (!CollectionUtils.isEmpty(cfg.getSlaves())) {
            LOG.info("开始处理OA子表数据");
            List<SlaveFormMappingVo> sfmList = cfg.getSlaves();
            Map slaveDatas = (Map) oaData.get("slaves");
            for (SlaveFormMappingVo sfmv : sfmList) {
                List<Map> sData = (List<Map>) slaveDatas.get(sfmv.getTableName());
                List<Map> targetSlaveData = new ArrayList<>();
                for (Map ssData : sData) {
                    Map tsd = mappingValueJSP(ssData, sfmv);
                    targetSlaveData.add(tsd);
                }
                String tarName = sfmv.getTargetTableName();
                mainData.put(tarName, targetSlaveData);
            }
            LOG.info("处理OA子表数据结束:" + mainData);

        }
        LOG.info("向SAP表插入");
        try {
            insertOrUpdateJSP(cfg, mainData);
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
        LOG.info("向SAP表插入结束");

    }

    /*dao  implement*/
    public static void insertOrUpdateJSP(FormMappingVo fmv, Map data) {
        try {

            String delegateClass = AppContext.getSystemProperty("vastdata." + fmv.getCode());
            LOG.info("获取代理类:" + delegateClass);
            if (!StringUtils.isEmpty(delegateClass)) {
                Class cls = Class.forName(delegateClass);
                Object obj = cls.newInstance();
                if (obj instanceof DataBaseDelegate) {
                    LOG.info("调用代理类处理方法");
                    //((DataBaseDelegate) obj).delegate(getDataSourceJSP(data), fmv, data);
                    delegateJSP(getDataSourceJSP(data), fmv, data);
                } else {
                    LOG.error("严重错误，初始化调用类失败。delegateJSP");
                }
            } else {
                LOG.error("找不到数据库处理代理！[code:" + fmv.getCode() + "]");
            }


        } catch (Exception | Error e) {
            LOG.error("执行SAP传输失败", e);
        }
    }

    public static DataSource getDataSourceJSP(Map data) {
        if (!isInit) {
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
        }
        isInit = true;
        Object val = data.remove("IS_OLD_SAP");
        if ("否".equals(val)) {
            return dataSource2;
        }
        return dataSource;
    }
    /* DAO IMPLEMENTED */

    public static String tv(Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof String) {
            return "'" + val + "'";
        }
        return String.valueOf(val);
    }

    public static List<Map> queryDataList(DataSource dataSource, String sql) {
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

    public static boolean isInsert(DataSource dataSource, FormMappingVo fmv, Map data) {
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

    public static String buildInsertSQL(FormMappingVo fmv, Map data, int index) {
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
//                    Long id = UUID.randomUUID().getMostSignificantBits();
//                    insertKeys.append(",").append(pkF);
//                    insertValues.append(",").append(id.intValue());
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

    public static String buildUpdateSQL(FormMappingVo fmv, Map data) {
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
        updateSQL.append(updateKeys.toString());

        updateSQL.append(" where " + tPK + "=" + tv(tPkVal));
        return updateSQL.toString();

    }

    public static boolean insertData(Connection connection, FormMappingVo fmv, Map data) {
        String insertSQL = buildInsertSQL(fmv, data, 1);
        return executeUpdate(connection, insertSQL);

    }

    public static boolean updateData(Connection connection, FormMappingVo fmv, Map data) {
        String updateSQL = buildUpdateSQL(fmv, data);
        if (!updateSQL.isEmpty()) {
            return executeUpdate(connection, updateSQL);
        }

        return false;

    }

    public static boolean executeUpdate(Connection conn, String sql) {
        JDBCAgent agent = new JDBCAgent(conn);
        try {
            LOG.info("BEFORE EXECUTE:" + sql);
            int ret = agent.execute(sql);
            LOG.info("after EXECUTE:" + ret);
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


    public static void delegateJSP(DataSource dataSource, FormMappingVo fmv, Map data) {
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
                        for (String insertSQL : insertSQLList) {
                            try {
                                LOG.info("EXECUTE:" + insertSQL);
                                int ret = agent.execute(insertSQL);
                                LOG.info("EXECUTE finish:" + ret);
                            } catch (Exception e) {
                                e.printStackTrace();
                                LOG.error("EXECUTE ERROR:" + e.getMessage(), e);
                            }

                        }

                        // agent.executeBatch(insertSQLList);

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

%>
<%

    out.write(SystemProperties.getInstance().getProperty("vastdata.sap.driver"));


%>
</body>
</html>
