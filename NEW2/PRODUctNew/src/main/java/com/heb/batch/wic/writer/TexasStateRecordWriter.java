/*
 *  TexasStateRecordWriter
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of H-E-B.
 */
package com.heb.batch.wic.writer;

import java.util.List;
/**
 *  This is the adapter class for writer body record.
 *
 * @author vn70529
 * @since 1.0.1
 */
public interface TexasStateRecordWriter<T> {
    void write(List<? extends T> items) throws Exception;
}
