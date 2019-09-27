/*
 * BaseJavaDao
 *
 * Copyright (c) 2018 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */
package com.heb.batch.wic.webservice.util;
import com.heb.batch.wic.webservice.vo.BaseVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BaseJavaDao.
 * @author vn55306
 */
public abstract class BaseJavaDao implements BaseJavaDaoInterface{
    private static final Logger LOGGER = LogManager.getLogger(BaseJavaDao.class);
    /**
     * Format request sent to java dao api
     *
     * @param entity String
     * @param objects List<BaseVO>
     * @return HttpEntity<String>
     * @throws IOException
     * @author vn55306.
     */
    public HttpEntity<String> createRequest(String entity, List<BaseVO> objects, String apiKey) throws IOException{
        ObjectMapper mapperObj = new ObjectMapper();
        Map<String,List> fetchMap = new HashMap<>();
        fetchMap.put(entity,objects);
        String requestJson = mapperObj.writeValueAsString(fetchMap);
        LOGGER.info("createRequest "+requestJson);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(apiKey!=null && !StringUtils.isEmpty(apiKey)){
            headers.set("apikey",apiKey);
        }
        return new HttpEntity<>(requestJson,headers);
    }
}
