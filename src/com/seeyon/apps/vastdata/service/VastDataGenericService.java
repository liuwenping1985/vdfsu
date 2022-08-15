package com.seeyon.apps.vastdata.service;

import com.seeyon.apps.vastdata.dao.VastDataSapDao;
import com.seeyon.apps.vastdata.util.WebUtil;
import com.seeyon.apps.vastdata.vo.FieldMappingVo;
import com.seeyon.apps.vastdata.vo.FormMappingVo;
import com.seeyon.apps.vastdata.vo.SlaveFormMappingVo;
import com.seeyon.ctp.common.log.CtpLogFactory;
import com.seeyon.ctp.organization.bo.V3xOrgMember;
import com.seeyon.ctp.organization.dao.OrgHelper;
import com.seeyon.ctp.util.JDBCAgent;
import org.apache.commons.logging.Log;
import org.apache.geode.distributed.internal.ConfigAttributeChecker;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 通用服务log,data
 * @author liuwenping
 */
public class VastDataGenericService {
    private static final Log LOG  = CtpLogFactory.getLog(VastDataGenericService.class);

    private VastDataSapDao vastDataSapDao;

    public VastDataSapDao getVastDataSapDao() {
        return vastDataSapDao;
    }

    public void setVastDataSapDao(VastDataSapDao vastDataSapDao) {
        this.vastDataSapDao = vastDataSapDao;
    }

    public void processData(String code , Long affairId ){
        //获取oa数据
        Map oaData = fetchOAData(code, affairId);
        //获取映射配置
        FormMappingVo cfg = VastDataMappingService.getInstance().getCfg(code);
        //这就是最后的结果 mainData
        Map mainData = mappingValue(oaData, cfg);
        //处理子表
        if (!CollectionUtils.isEmpty(cfg.getSlaves())) {
            List<SlaveFormMappingVo> sfmList = cfg.getSlaves();
            Map slaveDatas = (Map)oaData.get("slaves");
            for (SlaveFormMappingVo sfmv : sfmList) {
                List<Map> sData = (List<Map>)slaveDatas.get(sfmv.getTableName());
                List<Map> targetSlaveData = new ArrayList<>();
                for(Map ssData:sData){
                    Map tsd = mappingValue(ssData, sfmv);
                    targetSlaveData.add(tsd);
                }
                String tarName = sfmv.getTargetName();
                mainData.put(tarName,targetSlaveData);
            }

        }
        vastDataSapDao.insertOrUpdate(cfg,mainData);

    }

    /**
     * 通过模板编号和事务id获取表单数据（含子表）
     *
     * @param oaTemplateNo
     */
    public static Map fetchOAData(String oaTemplateNo, Long affairId) {
        //第一步查出来数据
        JDBCAgent agent = new JDBCAgent(false, true);
        try {
            String sql = "select form_recordid from ctp_affair where id=" + affairId;
            agent.execute(sql);
            Map idMap = agent.resultSetToMap();
            if (!CollectionUtils.isEmpty(idMap)) {
                //主表id
                Long id = Long.valueOf(String.valueOf(idMap.get("form_recordid")));
                if (id == null) {
                    throw new RuntimeException("主表id为空，严重错误");
                }
                //如果一次推送多个主表数据改这个
                FormMappingVo formMappingVo = VastDataMappingService.getInstance().getCfg(oaTemplateNo);
                if (formMappingVo != null) {
                    String sql2 = "select * from " + formMappingVo.getTableName() + " where id=" + id;
                    agent.execute(sql2);
                    //主表数据
                    Map data = agent.resultSetToMap();
                    //子表结构
                    List<SlaveFormMappingVo> slaves = formMappingVo.getSlaves();
                    //不为空表示有子表
                    if (!CollectionUtils.isEmpty(slaves)) {
                        //子表数据一个一个找
                        //数据结构为：子表code：List<Map>
                        Map slaveData = new HashMap();
                        for (FormMappingVo slave : slaves) {
                            String sql3 = "select * from " + slave.getTableName() + " where formmain_id=" + id;
                            agent.execute(sql3);
                            List<Map> slaveDataList = agent.resultSetToList();
                            //关键点：子表用的表名作为key存储的
                            slaveData.put(slave.getTableName(), slaveDataList);
                        }
                        //把子表数据注入到主表里,这也是关键点，子表怎么存储在主表里的
                        data.put("slaves", slaveData);

                    }
                    return data;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (agent != null) {
                agent.close();
            }
        }
        return new HashMap();
    }

    //不递归 只做一层,子表单独循环处理，也可以改成递归方式一次处理完，先写个low版的
    public static Map mappingValue(Map data, FormMappingVo cfg) {

        //form_recordid
        List<FieldMappingVo> fields = cfg.getFields();
        Map retMap = new HashMap<>();
        for (FieldMappingVo fmv : fields) {
            Object val = data.get(fmv.getSourceFieldName());
            String sType = fmv.getSourceFieldType();
            //TODO:OA的数据处理，比如枚举或者其他一些处理（所有的特殊处理这里均可以胜任）
            String fName = fmv.getTargetFieldName();
            switch (sType) {
                case "enum": {
                    String enumVal = String.valueOf(WebUtil.getEnumShowValue(val));
                    retMap.put(fName, enumVal);
                    break;
                }
                case "member":{
                    try{
                        V3xOrgMember member = OrgHelper.getMember(WebUtil.getLong(val));
                        if(member!=null){
                            retMap.put(fName, member.getName());
                        }else{
                            retMap.put(fName, val);
                        }

                    }catch (Exception e){
                        retMap.put(fName, val);
                    }

                    break;
                }
                case "float":{
                    Float f = WebUtil.getFloat(val);
                    if(f!=null){
                        retMap.put(fName, f);
                    }else{
                        retMap.put(fName, val);
                    }
                    break;
                }
                default:{
                    retMap.put(fName, val);
                }
            }


        }

        return retMap;
    }
}