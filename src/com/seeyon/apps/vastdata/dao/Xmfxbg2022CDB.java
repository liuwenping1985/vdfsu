package com.seeyon.apps.vastdata.dao;

import com.seeyon.apps.vastdata.vo.FieldMappingVo;
import com.seeyon.apps.vastdata.vo.SlaveFormMappingVo;
import com.seeyon.ctp.common.log.CtpLogFactory;
import org.apache.commons.logging.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Xmfxbg2022CDB extends AbstractVastDataComplicatedDataBuilder {
    private static final Log LOG = CtpLogFactory.getLog(Xmfxbg2022CDB.class);

    private boolean isInit = false;

    private Map<String,String> dataMap = new HashMap<>();

    private void init(){
        isInit = true;
        dataMap.put("field0052","预计成本-投标费用");
        dataMap.put("field0053","预计成本-市场费用");
        dataMap.put("field0054","预计成本-运输保险");
        dataMap.put("field0055","预计成本-安装集成");
        dataMap.put("field0056","预计成本-销售差旅");
        dataMap.put("field0057","预计成本-其他费用");
        dataMap.put("field0058","预计资金占用");
        dataMap.put("field0059","预计成本-礼品费用");
        dataMap.put("field0065","净毛利润");
        dataMap.put("field0012","预计税金");
        dataMap.put("field0010","收入");

    }
    @Override
    public Map<String, String> getMainFieldMapping() {
        if(isInit){
            init();
        }
        return dataMap;
    }

    @Override
    public List<String> builderInsertSQL(Map mainData, List<Map> slaveDataList, SlaveFormMappingVo sfmv,Map oaData) {
        return super.builderInsertSQL(mainData, slaveDataList, sfmv,oaData);
    }
}
