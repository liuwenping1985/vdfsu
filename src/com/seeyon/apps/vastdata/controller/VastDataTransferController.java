package com.seeyon.apps.vastdata.controller;

import com.seeyon.apps.vastdata.dao.VastDataSapDao;
import com.seeyon.apps.vastdata.service.VastDataGenericService;
import com.seeyon.apps.vastdata.service.VastDataMappingService;
import com.seeyon.apps.vastdata.util.WebUtil;
import com.seeyon.ctp.common.AppContext;
import com.seeyon.ctp.common.controller.BaseController;
import org.apache.commons.lang.StringUtils;
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

    private VastDataMappingService mappingService;

    private ModelAndView reload(HttpServletRequest request, HttpServletResponse response) {
        if (mappingService == null) {
            mappingService = (VastDataMappingService) AppContext.getBean("vastDataMappingService");
        }
        mappingService.reload();
        WebUtil.responseJSON("ok data is reloaded!", response);
        return null;
    }

    public ModelAndView showTransLogList(HttpServletRequest request, HttpServletResponse response) {

        return null;
    }

    public ModelAndView listConfigItemList(HttpServletRequest request, HttpServletResponse response) {


        return null;

    }

    public ModelAndView triggerPush(HttpServletRequest request, HttpServletResponse response) {
        String tno = request.getParameter("tno");
        String aid = request.getParameter("aid");
        if (StringUtils.isEmpty(tno) || StringUtils.isEmpty(aid)) {
            WebUtil.responseJSON("Argument is not presented!", response);
            return null;
        }
        vastDataGenericService.processData(tno, Long.valueOf(aid));
        WebUtil.responseJSON("Data is pushed!", response);
        return null;
    }

    public ModelAndView oft(HttpServletRequest request, HttpServletResponse response) {
        String page = request.getParameter("page");
        if (page == null) {
            page = "oft";
        }
        ModelAndView oft = new ModelAndView("apps/vastdata/" + page);
        return oft;
    }

    public ModelAndView checkEnv(HttpServletRequest request, HttpServletResponse response) {
        //对方数据库链接
        //
        VastDataSapDao dao = (VastDataSapDao) AppContext.getBean("vastDataSapDao");
        String index = request.getParameter("index");
        if (index == null) {
            index = "1";
        }
        Boolean isOk = dao.testConn(Integer.parseInt(index));
        WebUtil.responseJSON("dataSource is ok? result is "+isOk+" !",response);
        return null;
    }

}
