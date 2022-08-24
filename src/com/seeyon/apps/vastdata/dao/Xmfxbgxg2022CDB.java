package com.seeyon.apps.vastdata.dao;

import com.seeyon.apps.vastdata.vo.SimpleFormFieldVo;
import com.seeyon.apps.vastdata.vo.SlaveFormMappingVo;
import com.seeyon.ctp.common.log.CtpLogFactory;
import org.apache.commons.logging.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Xmfxbgxg2022CDB extends AbstractVastDataComplicatedDataBuilder {
    private static final Log LOG = CtpLogFactory.getLog(Xmfxbgxg2022CDB.class);


    private static Map<String,SimpleFormFieldVo> dataMap = new HashMap<>();

    static{
        dataMap.put("field0044",new SimpleFormFieldVo("field0044","预计成本-投标费用",1));
        dataMap.put("field0048",new SimpleFormFieldVo("field0048","预计成本-市场费用",2));
        dataMap.put("field0052",new SimpleFormFieldVo("field0052","预计成本-运输保险",3));
        dataMap.put("field0056",new SimpleFormFieldVo("field0056","预计成本-安装集成",4));
        dataMap.put("field0060",new SimpleFormFieldVo("field0060","预计成本-销售差旅",5));
        dataMap.put("field0064",new SimpleFormFieldVo("field0064","预计成本-其他费用",6));
        dataMap.put("field0068",new SimpleFormFieldVo("field0068","预计资金占用",7));
        dataMap.put("field0072",new SimpleFormFieldVo("field0072","预计成本-礼品费用",8));
        dataMap.put("field0079",new SimpleFormFieldVo("field0079","净毛利润",9));
        dataMap.put("field0016",new SimpleFormFieldVo("field0016","收入",10));
        dataMap.put("field0024",new SimpleFormFieldVo("field0024","预计税金",11));

    }
    @Override
    public Map<String, SimpleFormFieldVo> getMainFieldMapping() {

        return dataMap;
    }

    @Override
    public List<String> builderInsertSQL(Map mainData, List<Map> slaveDataList, SlaveFormMappingVo sfmv, Map oaData) {
        return super.builderInsertSQL(mainData, slaveDataList, sfmv, oaData);
    }
}
