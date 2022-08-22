package com.seeyon.apps.vastdata.dao;

import com.seeyon.apps.vastdata.vo.FieldMappingVo;
import com.seeyon.apps.vastdata.vo.SlaveFormMappingVo;
import com.seeyon.ctp.common.log.CtpLogFactory;
import org.apache.commons.logging.Log;

import java.util.List;
import java.util.Map;

public class Xmfxbgxg2022CDB extends AbstractVastDataComplicatedDataBuilder {
    private static final Log LOG = CtpLogFactory.getLog(Xmfxbgxg2022CDB.class);


    @Override
    public Map<String, String> getMainFieldMapping() {
        return null;
    }

    @Override
    public List<String> builderInsertSQL(Map mainData, List<Map> slaveDataList, SlaveFormMappingVo sfmv, Map oaData) {
        return super.builderInsertSQL(mainData, slaveDataList, sfmv, oaData);
    }
}
