package com.seeyon.apps.vastdata.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.seeyon.apps.vastdata.vo.FormMappingVo;
import com.seeyon.ctp.common.log.CtpLogFactory;
import com.seeyon.ctp.util.IOUtility;
import org.apache.commons.logging.Log;

import java.io.File;
import java.io.FileReader;
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
                ObjectMapper mapper = new ObjectMapper();
                //JSON.parseObject()
                FormMappingVo vo = mapper.readValue(content,FormMappingVo.class);
                //FormMappingVo vo = JSON.parseObject(content, FormMappingVo.class);
                LOG.info("put " + vo.getCode() + " to mapping service container!");
                cfgContainer.put(vo.getCode(), vo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private Map<String, FormMappingVo> cfgContainer = new HashMap<>();

    public FormMappingVo getCfg(String code) {
        return cfgContainer.get(code);
    }

    public void reload(){
        init();
    }

    public static VastDataMappingService getInstance() {
        return Hoder.instance;
    }

    private static class Hoder {
        static VastDataMappingService instance = new VastDataMappingService();
    }
}
