/*
 *  CaseUPCGeneratorService
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.utilities.caseUPCGenerator;

import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
/**
 * This service will implement any function on case upc generator screnn.
 *
 * @author vn86116
 * @updated vn86116
 * @since 2.15.0
 */
@Service
public class CaseUPCGeneratorService {

	@Autowired
	private SellingUnitRepository sellingUnitRepository;

	@Autowired
	private ItemMasterRepository itemMasterRepository;

	@Autowired
	private CandidateItemMasterRepository candidateItemMasterRepository;

	@Autowired
	private CandidateItemScanCodeRepository candidateItemScanCodeRepository;

	@Autowired
	private ProductMasterRepository productMasterRepository;

	private static final String STRING_EMPTY = "";

	private static final String START_UPC_GEN_CHARACTER = "00";

	private static final int NUMBER_ONE = 1;

	private static final int NUMBER_NINE = 9;

	private static final int NUMBER_ONE_HUNDRED = 100;

	private static final String MESSAGE_INVALID_NUMBER = "Selling Unit UPC is not valid. Please enter another one.";

	private static final Long START_INDEX = 4122000000L;

	private static final Long END_INDEX = 8868000000L;

	private ItemMasterResolver itemMasterResolver = new ItemMasterResolver();

	/**
	 * Method to get upc generate for case upc.
	 *
	 * @param upcNumber
	 * @param isMrtGen
	 * @return
	 */
	public Map<String, Object> getUpcGenerateForCaseUpc(Long upcNumber, Boolean isMrtGen) {
		Map<String, Object> mapResult = new HashMap<>();
		if (!isMrtGen) {
			SellingUnit sellingUnit = sellingUnitRepository.findOne(upcNumber);
			if (sellingUnit != null) {
				List<Long> upcsGenLst = new ArrayList<>();
				String upcIdStr = String.valueOf(upcNumber);
				String upcGen;
				for (int i = NUMBER_ONE; i < NUMBER_NINE; i++) {
					upcGen = STRING_EMPTY;
					upcGen += i + START_UPC_GEN_CHARACTER + upcIdStr;
					upcsGenLst.add(Long.valueOf(upcGen));
				}
				List<Long> lstExistUpcs = getListExistUpcs(upcsGenLst);
				lstExistUpcs = new ArrayList<>(new LinkedHashSet<>(lstExistUpcs));
				validateAllUpcGenExistInDB(upcsGenLst, lstExistUpcs, mapResult);
				List<ItemMaster> itemMasters = getListItemMasterByProdId(sellingUnit.getProdId());
				List<ItemMaster> itemMasterList = getListItemMasterByUpc(sellingUnit.getUpc());
				if (!itemMasters.isEmpty() && !itemMasterList.isEmpty()) {
					itemMasterResolver.fetch(itemMasters);
					itemMasterResolver.fetch(itemMasterList);
					mapResult.put("lstCasePackByProdId", itemMasters);
					mapResult.put("lstCasePackByUpc", itemMasterList);
				}
			} else {
				mapResult.put("message", MESSAGE_INVALID_NUMBER);
			}
		} else {
			List<Long> upcsGenLst = new ArrayList<>();
			Long startIndex = START_INDEX;
			Long endIndex = END_INDEX;
			while (startIndex < endIndex) {
				for (Long i = startIndex; i < startIndex + NUMBER_ONE_HUNDRED; i++) {
					upcsGenLst.add(i);
				}
				List<Long> lstExistUpcs = getListExistUpcs(upcsGenLst);
				if (validateAllUpcGenExistInDB(upcsGenLst, lstExistUpcs, mapResult)) {
					upcsGenLst = new ArrayList<>();
					startIndex += NUMBER_ONE_HUNDRED;
				} else startIndex = endIndex;
			}
		}
		return mapResult;
	}

	/**
	 * Method to get list of upc is exist in database.
	 *
	 * @param upcsGenLst list of upcs generate.
	 * @return list of upc is exist in database.
	 */
	private List<Long> getListExistUpcs(List<Long> upcsGenLst) {
		List<Long> lstExistUpcs = new ArrayList<>();
		if (!sellingUnitRepository.findCaseUpcsByUpcGenList(upcsGenLst).isEmpty()) {
			lstExistUpcs.addAll(sellingUnitRepository.findCaseUpcsByUpcGenList(upcsGenLst));
		}
		if (!itemMasterRepository.findCaseUpcsByUpcGenList(upcsGenLst).isEmpty()) {
			lstExistUpcs.addAll(itemMasterRepository.findCaseUpcsByUpcGenList(upcsGenLst));
		}
		if (!candidateItemMasterRepository.findCaseUpcsByUpcGenList(upcsGenLst).isEmpty()) {
			lstExistUpcs.addAll(candidateItemMasterRepository.findCaseUpcsByUpcGenList(upcsGenLst));
		}
		if (!candidateItemScanCodeRepository.findCaseUpcsByUpcGenList(upcsGenLst).isEmpty()) {
			lstExistUpcs.addAll(candidateItemScanCodeRepository.findCaseUpcsByUpcGenList(upcsGenLst));
		}
		return lstExistUpcs;
	}

	/**
	 * Check all upc generated are exist in database and set upc id.
	 *
	 * @param lstUPCsGen list of upcs generated.
	 * @param lstUPCsExist list of upcs are exist in database.
	 * @param mapResult map contain result.
	 * @return result of validation.
	 */
	private boolean validateAllUpcGenExistInDB(List<Long> lstUPCsGen, List<Long> lstUPCsExist, Map<String, Object> mapResult) {
		boolean flagExist = true;
		Map<Long, Long> mapExistUpcs = new HashMap<>();
		for (Long upcExist : lstUPCsExist) {
			mapExistUpcs.put(upcExist, upcExist);
		}
		for (Long upc : lstUPCsGen) {
			if (!mapExistUpcs.containsKey(upc)) {
				flagExist = false;
				mapResult.put("id", upc);
				mapResult.put("selected", false);
				break;
			}
		}
		return flagExist;
	}

	/**
	 * Method to get list of item master by product id.
	 *
	 * @param prodId the product id.
	 * @return list of item master.
	 */
	private List<ItemMaster> getListItemMasterByProdId(Long prodId) {
		ProductMaster productMaster = productMasterRepository.findOne(prodId);
		List<ItemMaster> itemMasters = new ArrayList<>();
		if (productMaster!=null){
			List<ProdItem> prodItems = productMaster.getProdItems();
			if (prodItems!=null&&!prodItems.isEmpty()){
				for (ProdItem prodItem : prodItems) {
					if(prodItem.getItemMaster() != null){
						itemMasters.add(prodItem.getItemMaster());
					}
				}
			}
		}
		return itemMasters;
	}

	/**
	 * Method to get list of item master by upc.
	 *
	 * @param upc the upc number.
	 * @return list of item master.
	 */
	private List<ItemMaster> getListItemMasterByUpc(Long upc) {
		List<ItemMaster> itemMasters = new ArrayList<>();
		if (itemMasterRepository.findByOrderingUpc(upc)!=null){
			itemMasters.add(itemMasterRepository.findByOrderingUpc(upc));
		}
		else if (itemMasterRepository.findByItemCode(upc)!=null){
			itemMasters.add(itemMasterRepository.findByItemCode(upc));
		}
		return itemMasters;
	}
}
