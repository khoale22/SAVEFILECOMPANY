/*
 * WicCategoryServiceClient
 *
 * Copyright (c) 2018 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */
package com.heb.batch.wic.webservice;
import com.heb.batch.wic.exception.WicException;
import com.heb.batch.wic.webservice.util.BaseJavaDao;
import com.heb.batch.wic.webservice.vo.BaseVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

/**
 * This is WicCategoryServiceClient class.
 *
 * @author vn55306
 * @since 1.0.0
 */
@Service
public class WicCategoryServiceClient extends BaseJavaDao {
    private static final Logger LOGGER = LogManager.getLogger(WicCategoryServiceClient.class);
    private static String entity = "WicCat";
    @Override
    public String submitRequest(List<BaseVO> objects, String url, String apiKey) throws WicException {
        LOGGER.info("WicCategoryServiceClient= "+url+"--"+apiKey);
        RestTemplate restTemplateWicCat = new RestTemplate();
        String responseExtractor = null;
        try {
            HttpEntity<String> request = this.createRequest(entity, objects, apiKey);
            responseExtractor = restTemplateWicCat.postForObject(url, request, String.class);
        }catch (IOException|RestClientException e){
            LOGGER.info(e.getMessage(),e);
            throw new WicException(e.getMessage());
        }
        return responseExtractor;
    }
}
