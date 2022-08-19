package com.seeyon.apps.vastdata.service;

import com.seeyon.apps.vastdata.vo.FormMappingVo;

/**
 * 通用服务
 * @author liuwenping
 */
public class VastDataConfigService {


    public static void main(String[] args) {
        try {

                VastDataMappingService vdms = VastDataMappingService.getInstance();
                FormMappingVo fmv = vdms.getCfg("xmfxbg2022");
           // System.out.println(TextEncoder.decode("/2.4/QvElyCwkwgBwGnNbGm3Z+w=="));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
