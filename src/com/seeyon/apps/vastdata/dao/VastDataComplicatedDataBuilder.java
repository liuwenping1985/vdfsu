package com.seeyon.apps.vastdata.dao;

import com.seeyon.apps.vastdata.vo.SlaveFormMappingVo;

import java.util.List;
import java.util.Map;

/**
 * Created by liuwenping on 2022/8/22.
 */
public interface VastDataComplicatedDataBuilder {

    public List<String> builderInsertSQL(Map mainData, List<Map> slaveDataList, SlaveFormMappingVo sfmv,Map oaData);
}
