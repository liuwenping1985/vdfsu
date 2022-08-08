package com.seeyon.apps.hlsj.listener;

import com.seeyon.apps.collaboration.event.CollaborationFinishEvent;
import com.seeyon.apps.collaboration.event.CollaborationProcessEvent;
import com.seeyon.apps.collaboration.event.CollaborationStartEvent;
import com.seeyon.ctp.event.EventTriggerMode;
import com.seeyon.ctp.util.annotation.ListenEvent;

/**
 * 随便写个监听器
 *
 * @author liuwenping
 */
public class VastDataFormTransferListener {

    @ListenEvent(event = CollaborationFinishEvent.class, async = true, mode = EventTriggerMode.afterCommit)
    public void onFinish() {



    }

    @ListenEvent(event = CollaborationProcessEvent.class, async = true, mode = EventTriggerMode.afterCommit)
    public void onProcess() {


    }

    @ListenEvent(event = CollaborationStartEvent.class, async = true, mode = EventTriggerMode.afterCommit)
    public void onStart() {



    }
}
