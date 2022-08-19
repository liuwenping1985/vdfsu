package com.seeyon.apps.vastdata.dao;

import com.seeyon.apps.vastdata.vo.FormMappingVo;
import com.seeyon.ctp.common.log.CtpLogFactory;
import org.apache.commons.logging.Log;

import javax.sql.DataSource;
import java.util.Map;

public class Xmfxbg2022DbDelegate extends AbstractDataBaseDelegate {
    private static final Log LOG = CtpLogFactory.getLog(Xmfxbg2022DbDelegate.class);

    @Override
    public void delegate(DataSource dataSource, FormMappingVo fmv, Map data) {
        LOG.info("调用Xmfxbg2022DbDelegate开始");
        super.delegate(dataSource,fmv,data);
        LOG.info("调用Xmfxbg2022DbDelegate结束");
    }
}
