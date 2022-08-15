package com.seeyon.apps.vastdata.listener;

import com.seeyon.apps.collaboration.event.CollaborationFinishEvent;
import com.seeyon.apps.collaboration.event.CollaborationProcessEvent;
import com.seeyon.apps.collaboration.event.CollaborationStartEvent;
import com.seeyon.apps.vastdata.service.VastDataGenericService;
import com.seeyon.apps.vastdata.service.VastDataMappingService;
import com.seeyon.apps.vastdata.vo.FormMappingVo;
import com.seeyon.ctp.common.exceptions.BusinessException;
import com.seeyon.ctp.common.log.CtpLogFactory;
import com.seeyon.ctp.event.EventTriggerMode;
import com.seeyon.ctp.util.annotation.ListenEvent;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

/**
 * 随便写个监听器
 *
 * @author liuwenping
 */
public class VastDataFormTransferListener {
    private static final Log LOG  = CtpLogFactory.getLog(VastDataFormTransferListener.class);
    private VastDataGenericService vastDataGenericService;

    public VastDataGenericService getVastDataGenericService() {
        return vastDataGenericService;
    }

    public void setVastDataGenericService(VastDataGenericService vastDataGenericService) {
        this.vastDataGenericService = vastDataGenericService;
    }



    @ListenEvent(event = CollaborationFinishEvent.class, async = true, mode = EventTriggerMode.afterCommit)
    public void onFinish(CollaborationFinishEvent finishEvent) {

        try {
           String templateCode = finishEvent.getTemplateCode();
           if(!StringUtils.isEmpty(templateCode)){
               VastDataMappingService vdms = VastDataMappingService.getInstance();
               FormMappingVo fmv = vdms.getCfg(templateCode);
               if(fmv!=null){
                   Long affairId = finishEvent.getAffairId();
                   LOG.info("trigger a finish event for [templateCode:"+templateCode+"][affairId:"+finishEvent.getAffairId()+"]");
                   vastDataGenericService.processData(templateCode,affairId);
               }
           }
        } catch (BusinessException e) {
            e.printStackTrace();
        }


    }

    @ListenEvent(event = CollaborationProcessEvent.class, async = true, mode = EventTriggerMode.afterCommit)
    public void onProcess(CollaborationProcessEvent processEvent) {
        try {
            String templateCode = processEvent.getTemplateCode();
        } catch (BusinessException e) {
            e.printStackTrace();
        }

    }

    @ListenEvent(event = CollaborationStartEvent.class, async = true, mode = EventTriggerMode.afterCommit)
    public void onStart(CollaborationStartEvent startEvent) {
        try {
            ///1.0/cGIyMzQ1Njc=
            String templateCode = startEvent.getTemplateCode();
        } catch (BusinessException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {

    }
}
