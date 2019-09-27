/*
 * ProductScanCodeWicIndexRepository
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */

package com.heb.batch.wic.index.repository;

import com.heb.batch.wic.index.ProductScanCodeWicDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Returns ProductScanCodeDocument in the ProductScanCodesIndexRepository.
 *
 * @author vn55306
 * @since 1.0.0
 */
public interface ProductScanCodeWicIndexRepository extends ElasticsearchRepository<ProductScanCodeWicDocument, String> {
	List<ProductScanCodeWicDocument> findByUpcAndWicSw(Long upc,String wicSwitch);
	List<ProductScanCodeWicDocument> findByUpc(Long upc);
}
