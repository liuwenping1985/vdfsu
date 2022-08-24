package com.seeyon.apps.vastdata.dao;

import com.seeyon.apps.vastdata.vo.FieldMappingVo;
import com.seeyon.apps.vastdata.vo.SimpleFormFieldVo;
import com.seeyon.apps.vastdata.vo.SlaveFormMappingVo;
import com.seeyon.ctp.common.log.CtpLogFactory;
import org.apache.commons.logging.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Xmfxbg2022CDB extends AbstractVastDataComplicatedDataBuilder {
    private static final Log LOG = CtpLogFactory.getLog(Xmfxbg2022CDB.class);

    private boolean isInit = false;

    private static Map<String, SimpleFormFieldVo> dataMap = new HashMap<>();

    static {

        dataMap.put("field0052",new SimpleFormFieldVo("field0052","预计成本-投标费用",1));
        dataMap.put("field0053",new SimpleFormFieldVo("field0053","预计成本-市场费用",2));
        dataMap.put("field0054",new SimpleFormFieldVo("field0054","预计成本-运输保险",3));
        dataMap.put("field0055",new SimpleFormFieldVo("field0055","预计成本-安装集成",4));
        dataMap.put("field0056",new SimpleFormFieldVo("field0056","预计成本-销售差旅",5));
        dataMap.put("field0057",new SimpleFormFieldVo("field0057","预计成本-其他费用",6));
        dataMap.put("field0058",new SimpleFormFieldVo("field0058","预计资金占用",7));
        dataMap.put("field0059",new SimpleFormFieldVo("field0059","预计成本-礼品费用",8));
        dataMap.put("field0065",new SimpleFormFieldVo("field0065","净毛利润",9));
        dataMap.put("field0010",new SimpleFormFieldVo("field0010","收入",10));
        dataMap.put("field0012",new SimpleFormFieldVo("field0012","预计税金",11));

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
