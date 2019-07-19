/*
 * CommodityReader
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.ClassCommodity;
import com.heb.pm.productHierarchy.ClassCommodityService;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Iterator;

/**
 * Reads commodities from the database.
 *
 * @author d116773
 * @since 2.0.2
 */
public class CommodityReader implements ItemReader<ClassCommodity>, StepExecutionListener{

	@Autowired
	private ClassCommodityService service;

	private Iterator<ClassCommodity> data;
	private int pageSize = 100;
	private int currentPage = 0;

	/**
	 * Called by Spring Batch to return the next commodity in the list.
	 *
	 * @return The next commodity in the list. Null when there is no more data.
	 * @throws Exception
	 * @throws UnexpectedInputException
	 * @throws ParseException
	 * @throws NonTransientResourceException
	 */
	@Override
	@CoreTransactional
	public ClassCommodity read() throws Exception {

		// If there is still data, return it.
		if (this.data != null && this.data.hasNext()) {
			return this.data.next();
		}

		// If not, see if you can fetch another set.
		Page<ClassCommodity> page =
				this.service.findAllCommoditiesForIndexByPage(new PageRequest(this.currentPage++, this.pageSize));
		// If there was results, return the next one.
		if (page.hasContent()) {
			this.data = page.iterator();
			return data.next();
		}
		// If not, we're at the end of the data.
		return null;
	}

	/**
	 * Sets up the data to be returned.
	 *
	 * @param stepExecution The environment this step is going to run in.
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.currentPage = 0;
	}

	/**
	 * Unimplemented.
	 *
	 * @param stepExecution Ignored.
	 * @return Always reutrns null.
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}

	/**
	 * Sets the ClassCommodityService to read data from. This is used for testing.
	 *
	 * @param service The ClassCommodityService to read data from.
	 */
	public void setService(ClassCommodityService service) {
		this.service = service;
	}

	/**
	 * Sets the size of the page to data in.
	 *
	 * @param pageSize The size of the page to read data in.
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
