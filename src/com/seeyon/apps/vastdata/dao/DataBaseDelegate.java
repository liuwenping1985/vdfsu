package com.seeyon.apps.vastdata.dao;

import com.seeyon.apps.vastdata.vo.FormMappingVo;

import javax.sql.DataSource;
import java.util.Map;

public interface DataBaseDelegate {

     void delegate(DataSource dataSource, FormMappingVo fmv, Map data);
}
