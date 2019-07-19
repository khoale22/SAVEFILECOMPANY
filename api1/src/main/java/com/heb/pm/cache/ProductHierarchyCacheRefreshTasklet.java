/*
 * ProductHierarchyCacheRefreshTasklet
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.cache;

import com.heb.pm.entity.Department;
import com.heb.pm.productHierarchy.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;

import java.util.List;

/**
 * Tasklet that runs to refresh the product hierarchy cache.
 *
 * @author m314029
 * @since 2.5.0
 */
public class ProductHierarchyCacheRefreshTasklet implements Tasklet {

	public static final Logger logger = LoggerFactory.getLogger(ProductHierarchyCacheRefreshTasklet.class);
	private static final String CLEAR_CACHE_CONTENTS_MESSAGE = "Clearing contents of product hierarchy cache to refresh data.";
	private static final String PRODUCT_HIERARCHY_TASK_MESSAGE = "Retrieving data for product hierarchy cache.";
	private static final String PRODUCT_HIERARCHY_TASK_COMPLETE_MESSAGE = "Retrieved %d departments for product " +
			"hierarchy cache.";

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private EhCacheCacheManager cacheManager;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		Cache productHierarchyCache = cacheManager.getCache("productHierarchyCache");
		if(productHierarchyCache != null) {
			logger.info(CLEAR_CACHE_CONTENTS_MESSAGE);
			productHierarchyCache.clear();
		}
		logger.info(PRODUCT_HIERARCHY_TASK_MESSAGE);
		List<Department> departmentList = this.departmentService.findAll();
		logger.info(String.format(PRODUCT_HIERARCHY_TASK_COMPLETE_MESSAGE, departmentList.size()));
		return RepeatStatus.FINISHED;
	}
}
