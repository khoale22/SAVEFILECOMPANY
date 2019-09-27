/*
 *  TexasStateIndexRepository
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of H-E-B.
 */
package com.heb.batch.wic.index.repository;

import com.heb.batch.wic.index.TexasStateDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * This is TexasStateIndexRepository class.
 *
 * @author vn55306
 * @since 1.0.0
 */
public interface TexasStateIndexRepository extends ElasticsearchRepository<TexasStateDocument, String> {
	List<TexasStateDocument> findByScnCdIdAndWicAplIdAndWicCatIdAndWicSubCatId(String scnCdId, String wicAplId, String wicCatId, String wicSubCatId);
}
