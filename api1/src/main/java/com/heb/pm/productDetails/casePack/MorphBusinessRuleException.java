package com.heb.pm.productDetails.casePack;

import org.springframework.dao.DataIntegrityViolationException;

/**
 * Created by thangdang on 9/25/2017.
 */
public class MorphBusinessRuleException extends DataIntegrityViolationException {

    /**
     * Constructs a new JobExecutionException.
     *
     * @param message The error message.
     */
    public MorphBusinessRuleException(String message) {
        super(message);
    }
}
