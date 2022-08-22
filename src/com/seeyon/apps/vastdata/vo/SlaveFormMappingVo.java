package com.seeyon.apps.vastdata.vo;

import java.util.List;

/**
 * SlaveFormMappingVo
 *
 * @author liuwenping
 */
public class SlaveFormMappingVo extends FormMappingVo {
    //映射前的名字，一般来说用表名就可以了比如formson_1347之类的，子表的code和originName基本没用，写出来是为了扩展
    private String originName;

    private List<FieldMappingVo> extendFields;

    private String dataBuilder;

    public List<FieldMappingVo> getExtendFields() {
        return extendFields;
    }

    public void setExtendFields(List<FieldMappingVo> extendFields) {
        this.extendFields = extendFields;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getDataBuilder() {
        return dataBuilder;
    }

    public void setDataBuilder(String dataBuilder) {
        this.dataBuilder = dataBuilder;
    }
}
