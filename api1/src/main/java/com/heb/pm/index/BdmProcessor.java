/*
 * BdmProcessor
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.entity.Bdm;
import com.heb.pm.productHierarchy.BdmDocument;
import org.springframework.batch.item.ItemProcessor;

/**
 * Processor for the Bdm index batch job.
 *
 * @author m314029
 * @since 2.0.6
 */
public class BdmProcessor implements ItemProcessor<Bdm, BdmDocument> {

	@Override
	public BdmDocument process(Bdm item) throws Exception {
		return new BdmDocument(item);
	}
}
