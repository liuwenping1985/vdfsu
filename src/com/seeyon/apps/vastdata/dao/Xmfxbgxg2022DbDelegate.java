package com.seeyon.apps.vastdata.dao;

import com.seeyon.apps.vastdata.vo.FormMappingVo;
import com.seeyon.ctp.common.log.CtpLogFactory;
import org.apache.commons.logging.Log;

import javax.sql.DataSource;
import java.util.Map;

public class Xmfxbgxg2022DbDelegate extends AbstractDataBaseDelegate {
    private static final Log LOG = CtpLogFactory.getLog(Xmfxbgxg2022DbDelegate.class);

    @Override
    public void delegate(DataSource dataSource, FormMappingVo fmv, Map data, Map oaData) {
        LOG.info("调用Xmfxbgxg2022DbDelegate开始");
        super.delegate(dataSource, fmv, data, oaData);
        LOG.info("调用Xmfxbgxg2022DbDelegate结束");
    }
}
