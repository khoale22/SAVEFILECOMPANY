/*
 *  BaseTexasStateFieldSetMapper
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of H-E-B.
 */
package com.heb.batch.wic.reader;

import com.heb.batch.wic.service.impl.TexasFieldValidatorImpl;
import org.springframework.batch.item.file.transform.FieldSet;
/**
 *  This is the base class for handle mapping field of record.
 *
 * @author vn70529
 * @since 1.0.1
 */
public abstract class BaseTexasStateFieldSetMapper<T> {
    private String recordType;
    protected T getMapField(FieldSet fieldSet){
        if(TexasFieldValidatorImpl.FieldFormats.D6_RECORD.getFieldDescription().equals(recordType)){
            return getD6MapField(fieldSet);
        }
        return getD4MapField(fieldSet);
    }
    protected abstract T getD4MapField(FieldSet fieldSet);
    protected abstract T getD6MapField(FieldSet fieldSet);
    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }
}
