package com.seeyon.apps.vastdata.po;

import com.alibaba.fastjson.annotation.JSONField;


public class RDR7OA2SAP {


    private Integer docentry;

    private Integer lineNum;

    private String fylx;


    private Float amount;

    public Integer getDocentry() {
        return docentry;
    }

    public void setDocentry(Integer docentry) {
        this.docentry = docentry;
    }

    public String getFylx() {
        return fylx;
    }

    public void setFylx(String fylx) {
        this.fylx = fylx;
    }

    public Integer getLineNum() {
        return lineNum;
    }

    public void setLineNum(Integer lineNum) {
        this.lineNum = lineNum;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
