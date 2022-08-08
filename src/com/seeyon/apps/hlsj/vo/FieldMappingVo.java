package com.seeyon.apps.hlsj.vo;

/**
 *
 * 字段FieldMappingVo
 *
 * @author liuwenping
 */
public class FieldMappingVo {

    private String tableName;

    private String sourceFieldName;

    private String sourceFieldType;

    private String targetFieldName;
    /**
     * 目标类型
     */
    private String targetFieldType;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSourceFieldName() {
        return sourceFieldName;
    }

    public void setSourceFieldName(String sourceFieldName) {
        this.sourceFieldName = sourceFieldName;
    }

    public String getSourceFieldType() {
        return sourceFieldType;
    }

    public void setSourceFieldType(String sourceFieldType) {
        this.sourceFieldType = sourceFieldType;
    }

    public String getTargetFieldName() {
        return targetFieldName;
    }

    public void setTargetFieldName(String targetFieldName) {
        this.targetFieldName = targetFieldName;
    }

    public String getTargetFieldType() {
        return targetFieldType;
    }

    public void setTargetFieldType(String targetFieldType) {
        this.targetFieldType = targetFieldType;
    }
}
