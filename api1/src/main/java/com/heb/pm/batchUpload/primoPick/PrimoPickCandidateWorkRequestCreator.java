/*
 * PrimoPickCreator
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.primoPick;


import com.heb.pm.batchUpload.UnexpectedInputException;
import com.heb.pm.batchUpload.parser.CandidateWorkRequestCreator;
import com.heb.pm.batchUpload.parser.ProductAttribute;
import com.heb.pm.batchUpload.parser.WorkRequestCreatorUtils;
import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.entity.SellingUnit;
import com.heb.pm.repository.SellingUnitRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * The class create the candidate request for primo pick file.
 * datas have been validate then insert to candidate work request table.
 *
 * @author vn87351
 * @since 2.12.0
 */
@Service
public class PrimoPickCandidateWorkRequestCreator extends CandidateWorkRequestCreator {
	public  static final int MIN_PRIMO_PICK_STATUS_LENGTH = 1;
	public  static final int DFLT_COL_SIZE = 16;
	private static final Logger LOGGER = LoggerFactory.getLogger(PrimoPickCandidateWorkRequestCreator.class);
	/**
	 * inject converter for primo pick
	 */
	@Autowired
	private PrimoPickSetter primoPickSetter;
	/**
	 * inject validator for primo pick
	 */
	@Autowired
	private PrimoPickValidator primoPickValidator;
	@Autowired
	private SellingUnitRepository sellingUnitRepository;

	@Override
	public CandidateWorkRequest createRequest(List<String> cellValues, List<ProductAttribute> productAttributes, long transactionId, String userId){
		LOGGER.info(cellValues.size() + ":size");
		PrimoPickBatchUpload primoPick = parsePrimoPick(cellValues);
		formatData(primoPick);
		try {
			primoPickValidator.validateRow(primoPick);
		}catch (UnexpectedInputException e){
			//set upc null in case user input invalid value for upc
			primoPick.setUpc(null);
		}
		CandidateWorkRequest candidateWorkRequest =
				WorkRequestCreatorUtils.getEmptyWorkRequest(primoPick.getProductId(),
						NumberUtils.toLong(primoPick.getUpc(), 0),
						userId, transactionId, CandidateWorkRequest.SRC_SYSTEM_ID_DEFAULT, getBatchStatus(primoPick).getName());
		//set product id for ps work request
		if(null!=primoPick.getUpc()){
			SellingUnit sellingUnit = sellingUnitRepository.findOne(NumberUtils.toLong(primoPick.getUpc(), 0L));
			if(null!=sellingUnit){
				candidateWorkRequest.setProductId(sellingUnit.getProdId());
				primoPick.setProductId(String.valueOf(sellingUnit.getProdId()));
			}
		}				
		if(!primoPick.hasErrors()){
			primoPickSetter.setCandidateProductMaster(
					candidateWorkRequest, primoPick);
			primoPickSetter.setCandidateStatus(candidateWorkRequest, getBatchStatus(primoPick),StringUtils.EMPTY);
		}else{
			primoPickSetter.setCandidateStatus(candidateWorkRequest, getBatchStatus(primoPick),getLengthOptimizedError(primoPick.getErrors()));
		}
		return candidateWorkRequest;
	}
	/**
	 * convert the data to excel file info object.
	 *
	 * @param cellValues List<String>
	 *                   * @param mapHeader
	 *                   Map<Integer, ExcelColumnProperty>
	 * @return PrimoPickBatchUpload
	 * @throws NoSuchMethodException     NoSuchMethodException
	 * @throws InvocationTargetException InvocationTargetException
	 * @throws IllegalAccessException    IllegalAccessException
	 */
	public PrimoPickBatchUpload parsePrimoPick(List<String> cellValues) {
		PrimoPickBatchUpload primoPick = new PrimoPickBatchUpload();
		if (cellValues.size() > DFLT_COL_SIZE) {
			primoPick.setUpc(cellValues.get(PrimoPickBatchUpload.COL_POS_UPC));
			primoPick.setProductId(cellValues.get(PrimoPickBatchUpload.COL_POS_PRODUCT_ID));
			primoPick.setPrimoPick(cellValues.get(PrimoPickBatchUpload.COL_POS_PRIMO_PICK));
			primoPick.setPrimoPickLongDescription(cellValues.get(PrimoPickBatchUpload.COL_POS_PRIMO_PICK_LONG_DESCRIPTION));
			primoPick.setPrimoPickShortDescription(cellValues.get(PrimoPickBatchUpload.COL_POS_PRIMO_PICK_SHORT_DESCRIPTION));
			primoPick.setPrimoPickStatus(cellValues.get(PrimoPickBatchUpload.COL_POS_PRIMO_PICK_STATUS));
			primoPick.setPrimoPickStartDate(cellValues.get(PrimoPickBatchUpload.COL_POS_PRIMO_PICK_START_DATE));
			primoPick.setPrimoPickEndDate(cellValues.get(PrimoPickBatchUpload.COL_POS_PRIMO_PICK_END_DATE));
		}
		return primoPick;
	}

	/**
	 * formatData.
	 * @param primoPick
	 *            The PrimoPickBatchUpload
	 * @author vn87351
	 */
	private void formatData(PrimoPickBatchUpload primoPick) {
		if (StringUtils.isNotBlank(primoPick.getPrimoPickStatus())){
			primoPick.setPrimoPickStatus(primoPick.getPrimoPickStatus().substring(0,MIN_PRIMO_PICK_STATUS_LENGTH));
		}
	}

	/**
	 * look up product id for upc on HEB. will return error if upc invalid or product does not exist on HEB
	 *
	 * @param primoVO
	 */
	private void lookupProductId(PrimoPickBatchUpload primoVO) {
		SellingUnit sellingUnit = sellingUnitRepository.findOne(NumberUtils.toLong(primoVO.getUpc(), 0L));
		primoVO.setProductId(String.valueOf(sellingUnit.getProdId()));
	}
}
