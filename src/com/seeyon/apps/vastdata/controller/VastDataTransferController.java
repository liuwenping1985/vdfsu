package com.seeyon.apps.vastdata.controller;

import com.seeyon.apps.vastdata.service.VastDataGenericService;
import com.seeyon.ctp.common.controller.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 数据转换控制器
 *
 * @author liuwenping
 */
public class VastDataTransferController extends BaseController {

    public VastDataGenericService getVastDataGenericService() {
        return vastDataGenericService;
    }

    public void setVastDataGenericService(VastDataGenericService vastDataGenericService) {
        this.vastDataGenericService = vastDataGenericService;
    }

    private VastDataGenericService vastDataGenericService;

    public ModelAndView showTransLogList(HttpServletRequest request, HttpServletResponse response) {

        return null;
    }

    public ModelAndView listConfigItemList(HttpServletRequest request, HttpServletResponse response) {


        return null;

    }

    public ModelAndView triggerPush(HttpServletRequest request, HttpServletResponse response) {

        return null;
    }

    public ModelAndView oft(HttpServletRequest request, HttpServletResponse response) {
        String page = request.getParameter("page");
        if (page == null) {
            page = "oft";
        }
        ModelAndView oft = new ModelAndView("apps/com.seeyon.apps.vastdata/" + page);
        return oft;
    }

    public ModelAndView checkEnv(HttpServletRequest request, HttpServletResponse response) {
        //对方数据库链接
        //

        return null;
    }

}
