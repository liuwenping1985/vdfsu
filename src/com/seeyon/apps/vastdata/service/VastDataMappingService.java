package com.seeyon.apps.vastdata.service;

import com.alibaba.fastjson.JSON;
import com.seeyon.apps.vastdata.vo.FormMappingVo;
import com.seeyon.ctp.common.log.CtpLogFactory;
import com.seeyon.ctp.util.IOUtility;
import org.apache.commons.logging.Log;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuwenping on 2022/8/5.
 */
public class VastDataMappingService {
    private static final Log LOG = CtpLogFactory.getLog(VastDataMappingService.class);

    public VastDataMappingService() {
        init();
    }

    private void init() {
        //TODO:读所有的配置文件然后放到container里
        String rootPath = VastDataMappingService.class.getResource("").getPath();
        File dir = new File(rootPath);
        File[] mappingFiles = dir.listFiles(f -> {

            boolean isJson = f.getName().endsWith("json");
            if (isJson) {
                LOG.info(f.getName()+" is a mapping file!");
            }
            return isJson;

        });
        Arrays.stream(mappingFiles).forEach(file -> {
            try {
                String content = IOUtility.toString(new FileReader(file));
                FormMappingVo vo = JSON.parseObject(content, FormMappingVo.class);
                LOG.info("put " + vo.getCode() + " to mapping service container!");
                cfgContainer.put(vo.getCode(), vo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private Map<String, FormMappingVo> cfgContainer = new HashMap<>();

    public FormMappingVo getCfg(String code) {
        return cfgContainer.get(code);
    }

    public static VastDataMappingService getInstance() {
        return Hoder.instance;
    }

    private static class Hoder {
        static VastDataMappingService instance = new VastDataMappingService();
    }
}
