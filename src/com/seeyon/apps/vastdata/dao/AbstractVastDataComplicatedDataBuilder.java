package com.seeyon.apps.vastdata.dao;

import com.seeyon.apps.vastdata.po.RDR7OA2SAP;
import com.seeyon.apps.vastdata.util.WebUtil;
import com.seeyon.apps.vastdata.vo.SimpleFormFieldVo;
import com.seeyon.apps.vastdata.vo.SlaveFormMappingVo;
import com.seeyon.innercompiler.tools.sjavac.Log;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liuwenping on 2022/8/22.
 */
public abstract class AbstractVastDataComplicatedDataBuilder implements VastDataComplicatedDataBuilder {

    public abstract Map<String, SimpleFormFieldVo> getMainFieldMapping();

    /**
     * 销售额-ApolloCS-6%
     * 销售额-ApolloDB-6%
     * 销售额-Vastbase E100-13%
     * 销售额-Vastbase G100-13%
     * 销售额-Vastdata E3000-13%
     * 预计成本-ApolloCS-6%
     * 预计成本-ApolloDB-6%
     * 预计成本-Vastbase E100-13%
     * 预计成本-Vastbase G100-13%
     * 预计成本-Vastdata E3000-13%
     *
     * @param mainData
     * @param slaveData
     * @param sfmv
     * @param oaData
     * @return
     */

    private static String TAX_RATE = "TAX_RATE";
    private static String FYLX_MAIN = "FYLX_MAIN";
    private static String XSE = "XSE";
    private static String YJCB = "YJCB";
    private static String LINE_SORT = "LineNum";
    private static String DOCENTRY = "Docentry";

    @Override
    public List<String> builderInsertSQL(Map mainData, List<Map> slaveData, SlaveFormMappingVo sfmv, Map oaData) {
        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isEmpty(slaveData)) {
            return sqlList;
        }
        String tableName = sfmv.getTargetTableName();

        //从主表里那拿数据
        Map<String, SimpleFormFieldVo> keyMaps = getMainFieldMapping();
        List<RDR7OA2SAP> rdr7OA2SAPList = new ArrayList<>();
        Integer docentry = WebUtil.getInteger(mainData.get(DOCENTRY));
        int index = 1;
        for (Map sMap : slaveData) {
            Object taxRate = sMap.get(TAX_RATE);
            Object fylx = sMap.get(FYLX_MAIN);
            Object xse = sMap.get(XSE);
            Object yjcb = sMap.get(YJCB);
            Integer sort = WebUtil.getInteger(sMap.get(LINE_SORT));
            if (sort == null) {
                sort = index;
            }
            if (sort * 2 > index) {
                index = sort * 2;
            }

            RDR7OA2SAP item = new RDR7OA2SAP();
            item.setAmount(WebUtil.getFloat(xse));
            item.setDocentry(docentry);
            if (String.valueOf(fylx).contains(" ")) {
                fylx = String.valueOf(fylx).replaceAll(" ", " ");
            }
            item.setFylx("销售额-" + fylx + "-" + taxRate);
            item.setLineNum(2 * sort - 1);
            rdr7OA2SAPList.add(item);
            RDR7OA2SAP item2 = new RDR7OA2SAP();
            item2.setAmount(WebUtil.getFloat(yjcb));
            item2.setDocentry(docentry);
            item2.setFylx("预计成本-" + fylx + "-" + taxRate);
            item2.setLineNum(2 * sort);
            rdr7OA2SAPList.add(item2);

        }
        for (String key : keyMaps.keySet()) {
            SimpleFormFieldVo field = keyMaps.get(key);
            RDR7OA2SAP item = new RDR7OA2SAP();
            Object amount = oaData.get(key);
            item.setAmount(WebUtil.getFloat(amount));
            item.setDocentry(WebUtil.getInteger(docentry));
            item.setFylx(field.getFieldName());
            item.setLineNum(field.getSort(index));
            rdr7OA2SAPList.add(item);
        }
        for (RDR7OA2SAP item : rdr7OA2SAPList) {
            StringBuilder stb = new StringBuilder("INSERT INTO " + tableName);
            stb.append(" (Docentry,LineNum,FYLX,Amount) values");
            stb.append(" (" + item.getDocentry() + "," + item.getLineNum() + ",'" + item.getFylx() + "'," + item.getAmount() + ")");
            sqlList.add(stb.toString());
        }
        for (String sql : sqlList) {
            Log.info("YJFY:" + sql);
        }
        return sqlList;
    }


}
