package com.seeyon.apps.vastdata.vo;

/**
 * SlaveFormMappingVo
 *
 * @author liuwenping
 */
public class SlaveFormMappingVo extends FormMappingVo {
    //映射前的名字，一般来说用表名就可以了比如formson_1347之类的，子表的code和originName基本没用，写出来是为了扩展
    private String originName;


    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }


}
