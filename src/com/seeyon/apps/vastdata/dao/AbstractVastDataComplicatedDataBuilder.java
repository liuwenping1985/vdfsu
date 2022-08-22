package com.seeyon.apps.vastdata.dao;

import com.seeyon.apps.vastdata.vo.FieldMappingVo;
import com.seeyon.apps.vastdata.vo.SlaveFormMappingVo;

import java.util.List;
import java.util.Map;

/**
 * Created by liuwenping on 2022/8/22.
 */
public abstract class AbstractVastDataComplicatedDataBuilder implements VastDataComplicatedDataBuilder {

    public abstract Map<String, String> getMainFieldMapping();

    @Override
    public List<String> builderInsertSQL(Map mainData, List<Map> slaveData, SlaveFormMappingVo sfmv, Map oaData) {


        return null;
    }

}
