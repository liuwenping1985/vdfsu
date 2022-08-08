package com.seeyon.apps.hlsj.vo;

/**
 * SlaveFormMappingVo
 *
 * @author liuwenping
 */
public class SlaveFormMappingVo extends FormMappingVo {
    //映射前的名字，一般来说用表名就可以了比如formson_1347之类的，子表的code和originName基本没用，写出来是为了扩展
    private String originName;
    //映射后的名字（必填）
    private String targetName;

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }
}
