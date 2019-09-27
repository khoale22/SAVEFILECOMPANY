/*
 * WicCategoryIndexRepository
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */

package com.heb.batch.wic.index.repository;
import com.heb.batch.wic.index.WicSubCategoryDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Repository for indexed WicSubCategoryDocument.
 *
 * @author vn55306
 * @since 1.0.0
 */
public interface WicSubCategoryIndexRepository extends ElasticsearchRepository<WicSubCategoryDocument, String> {
}