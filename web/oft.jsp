<%@ page import="com.seeyon.ctp.util.JDBCAgent" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.alibaba.fastjson.JSON" %>
<%@ page import="com.seeyon.apps.vastdata.util.WebUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="com.seeyon.ctp.common.AppContext" %>
<%@ page import="com.seeyon.apps.vastdata.dao.VastDataSapDao" %>
<%@ page import="com.seeyon.ctp.common.constants.SystemProperties" %>
<%@ page import="com.mchange.v2.c3p0.ComboPooledDataSource" %><%--
  Created by IntelliJ IDEA.
  User: liuwenping
  Date: 2022/8/5
  Time: 08:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ceshi</title>
</head>
<body>
<%

//    JDBCAgent agent = new JDBCAgent();
//
//    agent.execute("select form_recordid from ctp_affair where id=-1324981772125846133");
//
//    List<Map> idMap = agent.resultSetToList();

    //WebUtil.responseJSON(idMap,response);

   // agent.close();
    ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
    comboPooledDataSource.setDriverClass(SystemProperties.getInstance().getProperty("vastdata.sap.driver"));
    comboPooledDataSource.setJdbcUrl(SystemProperties.getInstance().getProperty("vastdata.sap.jdbcurl"));
    comboPooledDataSource.setUser(SystemProperties.getInstance().getProperty("vastdata.sap.user"));
    comboPooledDataSource.setPassword(SystemProperties.getInstance().getProperty("vastdata.sap.pwd"));
    comboPooledDataSource.setInitialPoolSize(1);
    comboPooledDataSource.setMaxPoolSize(2);

    //VastDataSapDao dao = (VastDataSapDao) AppContext.getBean("vastDataSapDao");

    JDBCAgent agent = new JDBCAgent(comboPooledDataSource.getConnection());
    agent.execute("select * from ORDR_oa2SAP where Docentry=7900");
    WebUtil.responseJSON(agent.resultSetToList(),response);
    agent.close();

%>
</body>
</html>
