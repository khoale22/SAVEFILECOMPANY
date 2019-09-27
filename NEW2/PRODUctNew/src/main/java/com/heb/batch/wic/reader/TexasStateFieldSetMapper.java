/*
 * TexasStateFieldSetMapper
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */
package com.heb.batch.wic.reader;

import com.heb.batch.wic.index.TexasStateDocument;
import com.heb.batch.wic.utils.WicConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;
/**
 * Mapper class.
 *
 * @author vn55306
 * @since 1.0.0
 */
public class TexasStateFieldSetMapper extends BaseTexasStateFieldSetMapper<TexasStateDocument> implements FieldSetMapper<TexasStateDocument> {
    private static final Logger LOGGER = LogManager.getLogger(TexasStateFieldSetMapper.class);

    @Override
    public TexasStateDocument mapFieldSet(FieldSet fieldSet) throws BindException {
        return getMapField(fieldSet);
    }

    @Override
    protected TexasStateDocument getD4MapField(FieldSet fieldSet) {
        StringBuilder dataRaw = new StringBuilder();
        TexasStateDocument texasStateDocument = new TexasStateDocument();
        texasStateDocument.setIdCode(fieldSet.readString(WicConstants.ID_CODE));
        texasStateDocument.setSequenceNumber(fieldSet.readString(WicConstants.SEQUENCE_NUMBER));
        texasStateDocument.setMessageId(fieldSet.readString(WicConstants.MESSAGE_ID));
        texasStateDocument.setAplPreFix(fieldSet.readString(WicConstants.APL_PREFIX));
        texasStateDocument.setWicAplId(fieldSet.readRawString(WicConstants.APL_PREFIX).concat(fieldSet.readRawString(WicConstants.UPC_KEY))
                .concat(fieldSet.readRawString(WicConstants.CHECK_DIGIT)));
        texasStateDocument.setScnCdId(fieldSet.readString(WicConstants.UPC_KEY));
        texasStateDocument.setUpcCheckDigit(fieldSet.readString(WicConstants.CHECK_DIGIT));
        texasStateDocument.setWicProdDes(fieldSet.readString(WicConstants.DESCRIPTION));
        texasStateDocument.setWicCatId(fieldSet.readString(WicConstants.CATEGORY_CODE));
        texasStateDocument.setWicCategoryDesc(fieldSet.readString(WicConstants.CATEGORY_DESCRIPTION));
        texasStateDocument.setWicSubCatId(fieldSet.readString(WicConstants.SUBCATEGORY_CODE));
        texasStateDocument.setWicSubCategoryDesc(fieldSet.readString(WicConstants.SUBCATEGORY_DESCRIPTION));
        texasStateDocument.setWicUntTxt(fieldSet.readString(WicConstants.UNIT_OF_MEASURE));
        texasStateDocument.setWicPkgSzQty(fieldSet.readString(WicConstants.PACKAGE_SIZE));
        texasStateDocument.setWicBnFtQty(fieldSet.readString(WicConstants.BENEFIT_QUANTITY));
        texasStateDocument.setWicBnftUntTxt(fieldSet.readString(WicConstants.BENEFIT_UNIT));
        texasStateDocument.setWicPrcAmt(fieldSet.readString(WicConstants.ITEM_PRICE));
        texasStateDocument.setWicPrcCd(fieldSet.readString(WicConstants.PRICE_TYPE));
        texasStateDocument.setWicCrdAcptId(fieldSet.readString(WicConstants.CARD_ID));
        texasStateDocument.setEffDt(fieldSet.readString(WicConstants.EFFECTIVE_DATE));
        texasStateDocument.setEndDt(fieldSet.readString(WicConstants.END_DATE));
        texasStateDocument.setUpcPluLength(fieldSet.readString(WicConstants.UPC_PLU_LENGTH));
        texasStateDocument.setPurchaseIndicator(fieldSet.readString(WicConstants.PURCHASE_INDICATOR));
        texasStateDocument.setManualVoucherIndicator(fieldSet.readString(WicConstants.MANUAL_VOUCHER_INDICATOR));
        dataRaw = dataRaw.append(fieldSet.readRawString(WicConstants.ID_CODE)).append(fieldSet.readRawString(WicConstants.SEQUENCE_NUMBER))
                .append(fieldSet.readRawString(WicConstants.MESSAGE_ID)).append(fieldSet.readRawString(WicConstants.APL_PREFIX))
                .append(fieldSet.readRawString(WicConstants.UPC_KEY)).append(fieldSet.readRawString(WicConstants.CHECK_DIGIT))
                .append(fieldSet.readRawString(WicConstants.DESCRIPTION)).append(fieldSet.readRawString(WicConstants.CATEGORY_CODE))
                .append(fieldSet.readRawString(WicConstants.CATEGORY_DESCRIPTION)).append(fieldSet.readRawString(WicConstants.SUBCATEGORY_CODE))
                .append(fieldSet.readRawString(WicConstants.SUBCATEGORY_DESCRIPTION)).append(fieldSet.readRawString(WicConstants.UNIT_OF_MEASURE))
                .append(fieldSet.readRawString(WicConstants.PACKAGE_SIZE)).append(fieldSet.readRawString(WicConstants.BENEFIT_QUANTITY))
                .append(fieldSet.readRawString(WicConstants.BENEFIT_UNIT)).append(fieldSet.readRawString(WicConstants.ITEM_PRICE))
                .append(fieldSet.readRawString(WicConstants.PRICE_TYPE)).append(fieldSet.readRawString(WicConstants.CARD_ID))
                .append(fieldSet.readRawString(WicConstants.EFFECTIVE_DATE)).append(fieldSet.readRawString(WicConstants.END_DATE));
        texasStateDocument.setDataRaw(dataRaw.toString());
        LOGGER.debug(texasStateDocument.toString());
        return texasStateDocument;
    }

    @Override
    protected TexasStateDocument getD6MapField(FieldSet fieldSet) {
        TexasStateDocument texasStateDocument = new TexasStateDocument();
        texasStateDocument.setIdCode(fieldSet.readString(WicConstants.ID_CODE));
        texasStateDocument.setSequenceNumber(fieldSet.readString(WicConstants.SEQUENCE_NUMBER));
        texasStateDocument.setMessageId(fieldSet.readString(WicConstants.MESSAGE_ID));
        texasStateDocument.setWicCatId(fieldSet.readString(WicConstants.CATEGORY_CODE));
        texasStateDocument.setWicCategoryDesc(fieldSet.readString(WicConstants.CATEGORY_DESCRIPTION));
        texasStateDocument.setWicSubCatId(fieldSet.readString(WicConstants.SUBCATEGORY_CODE));
        texasStateDocument.setWicSubCategoryDesc(fieldSet.readString(WicConstants.SUBCATEGORY_DESCRIPTION));
        texasStateDocument.setWicUntTxt(fieldSet.readString(WicConstants.BENEFIT_UNIT_DESCRIPTION));
        LOGGER.debug(texasStateDocument.toString());
        return texasStateDocument;
    }
}
