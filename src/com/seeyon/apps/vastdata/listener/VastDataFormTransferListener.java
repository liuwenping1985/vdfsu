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



    @ListenEvent(event = CollaborationFinishEvent.class)
    public void onFinish(CollaborationFinishEvent finishEvent) {

        try {
           String templateCode = finishEvent.getTemplateCode();
            LOG.info("vastdata:"+templateCode+",affairId:"+finishEvent.getAffairId());
           if(!StringUtils.isEmpty(templateCode)){
               VastDataMappingService vdms = VastDataMappingService.getInstance();
               FormMappingVo fmv = vdms.getCfg(templateCode);
               if(fmv!=null){
                   Long affairId = finishEvent.getAffairId();
                   LOG.info("trigger a finish event for [templateCode:"+templateCode+"][affairId:"+finishEvent.getAffairId()+"]");
                   vastDataGenericService.processData(templateCode,affairId);
               }else{
                   LOG.info("vastdata cfg not found:"+templateCode+",affairId:"+finishEvent.getAffairId());
               }
           }
        } catch (Exception|Error e) {
            LOG.error("FBI WARNING:"+e.getMessage(),e);
        }


    }

//    @ListenEvent(event = CollaborationStartEvent.class)
//    public void onStart(CollaborationStartEvent startEvent) {
//        try {
//            String templateCode = startEvent.getTemplateCode();
//           Long affairId =  startEvent.getAffair().getId();
//            LOG.info("vastdata:"+templateCode+",affairId:"+affairId);
//            if(!StringUtils.isEmpty(templateCode)){
//                VastDataMappingService vdms = VastDataMappingService.getInstance();
//                FormMappingVo fmv = vdms.getCfg(templateCode);
//                if(fmv!=null){
//
//                    LOG.info("trigger a finish event for [templateCode:"+templateCode+"][affairId:"+affairId+"]");
//                    vastDataGenericService.processData(templateCode,affairId);
//                }else{
//                    LOG.info("vastdata cfg not found:"+templateCode+",affairId:"+affairId);
//                }
//            }
//        } catch (Exception|Error e) {
//            LOG.error("FBI WARNING:"+e.getMessage(),e);
//        }
//
//
//    }

    public static void main(String[] args) {

    }
}
