package com.seeyon.apps.hlsj.listener;

import com.seeyon.apps.collaboration.event.CollaborationFinishEvent;
import com.seeyon.apps.collaboration.event.CollaborationProcessEvent;
import com.seeyon.apps.collaboration.event.CollaborationStartEvent;
import com.seeyon.ctp.common.exceptions.BusinessException;
import com.seeyon.ctp.event.EventTriggerMode;
import com.seeyon.ctp.util.annotation.ListenEvent;

/**
 * 随便写个监听器
 *
 * @author liuwenping
 */
public class VastDataFormTransferListener {

    @ListenEvent(event = CollaborationFinishEvent.class, async = true, mode = EventTriggerMode.afterCommit)
    public void onFinish(CollaborationFinishEvent finishEvent) {

        try {
           String templateCode = finishEvent.getTemplateCode();
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
