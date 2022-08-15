package com.seeyon.apps.vastdata.service;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 通用服务
 * @author liuwenping
 */
public class VastDataConfigService {


    public static void main(String[] args) {
        try {
            Class cls = Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=T283", "sa", "123");
            if(cls!=null){
                System.out.println("11111");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }




}
