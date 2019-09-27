/*
 * BaseJavaDaoInterface
 *
 * Copyright (c) 2018 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */
package com.heb.batch.wic.webservice.util;

import com.heb.batch.wic.exception.WicException;
import com.heb.batch.wic.webservice.vo.BaseVO;
import java.util.List;

/**
 * BaseJavaDaoInterface.
 * @author vn55306
 */
public interface BaseJavaDaoInterface {
    String submitRequest(List<BaseVO> objects, String url, String apiKey) throws WicException;
}
