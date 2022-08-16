package com.seeyon.apps.vastdata.vo;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * FormMappingVo
 *
 * @author liuwenping
 */
public class FormMappingVo {

    private String code;

    private String tableName;
    private String targetTableName;
    private String targetPkField;
    private String pkFiled;
    private String pkFiledGenStrategy;
    private List<FieldMappingVo> fields;

    private List<SlaveFormMappingVo> slaves;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<FieldMappingVo> getFields() {
        return fields;
    }

    public String getPkFiledGenStrategy() {
        return pkFiledGenStrategy;
    }

    public void setPkFiledGenStrategy(String pkFiledGenStrategy) {
        this.pkFiledGenStrategy = pkFiledGenStrategy;
    }

    public void setFields(List<FieldMappingVo> fields) {
        this.fields = fields;
    }

    public List<SlaveFormMappingVo> getSlaves() {
        return slaves;
    }

    public void setSlaves(List<SlaveFormMappingVo> slaves) {
        this.slaves = slaves;
    }

    public String getTargetTableName() {
        return targetTableName;
    }

    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName;
    }

    public String getTargetPkField() {
        return targetPkField;
    }

    public void setTargetPkField(String targetPkField) {
        this.targetPkField = targetPkField;
    }

    public String getPkFiled() {
        return pkFiled;
    }

    public void setPkFiled(String pkFiled) {
        this.pkFiled = pkFiled;
    }

    public static void main(String[] args) {
        FormMappingVo vo = new FormMappingVo();
        vo.setCode("OA-XXXX");
        vo.setTableName("formain_1347");
        List<FieldMappingVo> mainFieldList = new ArrayList<>();
        FieldMappingVo fvo = new FieldMappingVo();
        fvo.setSourceFieldName("field0001");
        fvo.setSourceFieldType("string");
        fvo.setTargetFieldName("userName");
        fvo.setTargetFieldType("string");
        fvo.setTableName(vo.getTableName());
        mainFieldList.add(fvo);
        vo.setFields(mainFieldList);
        SlaveFormMappingVo vo2 = new SlaveFormMappingVo();
        vo2.setTableName("formson_1234");
        List<FieldMappingVo> mainFieldList2 = new ArrayList<>();
        FieldMappingVo fvo2 = new FieldMappingVo();
        fvo2.setSourceFieldName("field0003");
        fvo2.setSourceFieldType("string");
        fvo2.setTargetFieldName("userName1");
        fvo2.setTargetFieldType("string");
        fvo2.setTableName(vo.getTableName());
        mainFieldList2.add(fvo2);
        vo2.setFields(mainFieldList2);
        List<SlaveFormMappingVo> sList = new ArrayList<>();
        sList.add(vo2);
        vo.setSlaves(sList);
        System.out.println(JSON.toJSONString(vo));

    }
}
