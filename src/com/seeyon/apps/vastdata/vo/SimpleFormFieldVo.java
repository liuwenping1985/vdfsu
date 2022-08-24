package com.seeyon.apps.vastdata.vo;

/**
 * Created by liuwenping on 2022/8/24.
 */
public class SimpleFormFieldVo {
    private String fieldCode;
    private String fieldName;
    private Integer sort;

    public SimpleFormFieldVo() {
    }

    public SimpleFormFieldVo(String code, String name, Integer sort) {
        this.fieldCode = code;
        this.fieldName = name;
        this.sort = sort;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Integer getSort() {
        return sort;
    }

    public Integer getSort(Integer index) {
        return sort + index;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
