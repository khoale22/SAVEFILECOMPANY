/*
 * ImageInfoReader
 *
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.index;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.ProductScanImageURI;
import com.heb.pm.productDetails.sellingUnit.ImageInfoService;
import com.heb.util.jpa.PageableResult;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

/**
 * Reads ProductScanImageURI from the database.
 *
 * @author vn70529
 * @since 2.39.0
 */
public class ImageInfoReader  implements ItemReader<ProductScanImageURI>, StepExecutionListener {

    @Autowired
    private ImageInfoService imageInfoService;

    private Iterator<ProductScanImageURI> data;
    private int pageSize = 100;
    private int currentPage = 0;
    private boolean isLatestPage = false;

    /**
     * Sets up the data to be returned.
     *
     * @param stepExecution The environment this step is going to run in.
     */
    @Override
    public void beforeStep(StepExecution stepExecution) { }

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
     * Called by Spring Batch to return the next ProductScanImageURI in the list.
     *
     * @return The next itemClass in the list. Null when there is no more data.
     */
    @Override
    @CoreTransactional
    public ProductScanImageURI read() throws Exception {

        // If there is still data, return it.
        if (this.data != null && this.data.hasNext()) {
            return this.data.next();
        }
        if (isLatestPage) return null;
        // If not, see if you can fetch another set.
        PageableResult<ProductScanImageURI> page  = imageInfoService.handleFetchApprovedImage(this.currentPage, this.pageSize, true);
        if (page != null && page.getData().iterator().hasNext()){
            if(page.getPageCount() == 1) isLatestPage = true;

            this.data = page.getData().iterator();
            return data.next();
        }
        // If not, we're at the end of the data.
        return null;
    }
}
