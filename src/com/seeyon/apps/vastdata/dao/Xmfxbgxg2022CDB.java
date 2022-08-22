package com.seeyon.apps.vastdata.dao;

import com.seeyon.apps.vastdata.vo.SlaveFormMappingVo;
import com.seeyon.ctp.common.log.CtpLogFactory;
import org.apache.commons.logging.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Xmfxbgxg2022CDB extends AbstractVastDataComplicatedDataBuilder {
    private static final Log LOG = CtpLogFactory.getLog(Xmfxbgxg2022CDB.class);
    private boolean isInit = false;

    private Map<String,String> dataMap = new HashMap<>();

    private void init(){
        isInit = true;
        dataMap.put("field0044","预计成本-投标费用");
        dataMap.put("field0048","预计成本-市场费用");
        dataMap.put("field0052","预计成本-运输保险");
        dataMap.put("field0056","预计成本-安装集成");
        dataMap.put("field0060","预计成本-销售差旅");
        dataMap.put("field0064","预计成本-其他费用");
        dataMap.put("field0068","预计资金占用");
        dataMap.put("field0072","预计成本-礼品费用");
        dataMap.put("field0079","净毛利润");
        dataMap.put("field0016","收入");
        dataMap.put("field0024","预计税金");

    }
    @Override
    public Map<String, String> getMainFieldMapping() {
        if(isInit){
            init();
        }
        return dataMap;
    }

    @Override
    public List<String> builderInsertSQL(Map mainData, List<Map> slaveDataList, SlaveFormMappingVo sfmv, Map oaData) {
        return super.builderInsertSQL(mainData, slaveDataList, sfmv, oaData);
    }
}
