/*
 *  CodeTablesService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable;

import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * code table service.
 *
 * @author vn87351
 * @since 2.12.0
 */
@Service
public class CodeTablesService {
	/**
	 * log message
	 */
	private static final Logger logger = LoggerFactory.getLogger(CodeTablesService.class);

	private static final String DELETE_ACTION = "D";
	private static final String ADD_NEW_ACTION = "A";
	private static final String UPDATE_ACTION = "";

	/**
	 * repository to process Varietal table
	 */
	@Autowired
	private VarietalRepository varietalRepository;

	/**
	 * repository to process Varietal Type table
	 */
	@Autowired
	private VarietalTypeRepository varietalTypeRepository;

	/**
	 * repository to process Wine Area table
	 */
	@Autowired
	private WineAreaRepository wineAreaRepository;

	/**
	 * repository to process Wine Maker table
	 */
	@Autowired
	private WineMakerRepository wineMakerRepository;

	/**
	 * repository to process Wine Region table
	 */
	@Autowired
	private WineRegionRepository wineRegionRepository;

	@Autowired
	private ProductGroupTypeRepository productGroupTypeRepository;

	@Autowired
	private CodeTableManagementServiceClient serviceClient;

	//user info service to get user id login
	@Autowired
	private UserInfo userInfo;
	/**
	 * find all wine region with default sort
	 * @return list wine region
	 */
	public List<WineRegion> findAllWineRegion(){
		return wineRegionRepository.findAll(WineRegion.getDefaultSort());
	}
	/**
	 * find all wine maker with default sort by id
	 * @return list wine maker
	 */
	public List<WineMaker> findAllWineMaker(){
		return wineMakerRepository.findAll(WineMaker.getDefaultSort());
	}
	/**
	 * find all wine area with defalt sort.
	 * @return list wine area
	 */
	public List<WineArea> findAllWineArea(){
		return wineAreaRepository.findAll(WineArea.getDefaultSort());
	}

	/**
	 * find all varietal type then return to table on ui
	 *
	 * @throws Exception
	 * @author vn87351
	 */
	public List<VarietalType> findAllVarietalType() throws Exception {
		return varietalTypeRepository.findAll(VarietalType.getDefaultSort());
	}
	/**
	 * find all varietal with default sort asc Varietal id
	 *
	 * @throws Exception
	 * @author vn87351
	 */
	public List<Varietal> findAllVarietal() throws Exception {
		return varietalRepository.findAll(Varietal.getDefaultSort());
	}

	/**
	 * Find all product group types.
	 *
	 * @return list of product group types
	 */
	public List<ProductGroupType> findAllProductGroupTypes(){
		return productGroupTypeRepository.findAll(ProductGroupType.getDefaultSort());
	}

	/**
	 * add new varietal funtion. call webserice to save all new varietal
	 * @param lst list varietal
	 */
	public void addNewVarietal(List<Varietal> lst){
		serviceClient.updateVarietal(lst,CodeTablesService.ADD_NEW_ACTION);
	}

	/**
	 * update list varietal
	 * @param lst list varietal to update
	 */
	public void updateVarietal(List<Varietal> lst){
		serviceClient.updateVarietal(lst,CodeTablesService.UPDATE_ACTION);
	}

	/**
	 * delete list varietal
	 * @param lst list varietal to delete
	 */
	public void deleteVarietal(List<Varietal> lst){
		serviceClient.updateVarietal(lst,CodeTablesService.DELETE_ACTION);
	}
	/**
	 * add new varietal type funtion. call webserice to save all new varietal type
	 * @param lst list varietal type
	 */
	public void addNewVarietalType(List<VarietalType> lst){
		serviceClient.updateVarietalType(lst,CodeTablesService.ADD_NEW_ACTION);
	}

	/**
	 * update list varietal type
	 * @param lst list varietal type to update
	 */
	public void updateVarietalType(List<VarietalType> lst){
		serviceClient.updateVarietalType(lst,CodeTablesService.UPDATE_ACTION);
	}

	/**
	 * delete list varietal type
	 * @param lst list varietal type to delete
	 */
	public void deleteVarietalType(List<VarietalType> lst){
		serviceClient.updateVarietalType(lst,CodeTablesService.DELETE_ACTION);
	}
	/**
	 * add new wine region funtion. call webserice to save all new wine region
	 * @param lst list wine region
	 */
	public void addNewWineRegion(List<WineRegion> lst){
		serviceClient.updateWineRegion(lst,CodeTablesService.ADD_NEW_ACTION);
	}

	/**
	 * update list wine region
	 * @param lst list wine region to update
	 */
	public void updateWineRegion(List<WineRegion> lst){
		serviceClient.updateWineRegion(lst,CodeTablesService.UPDATE_ACTION);
	}

	/**
	 * delete list wine region
	 * @param lst list wine region to delete
	 */
	public void deleteWineRegion(List<WineRegion> lst){
		serviceClient.updateWineRegion(lst,CodeTablesService.DELETE_ACTION);
	}
	/**
	 * add new wine area funtion. call webserice to save all new wine area
	 * @param lst list wine area
	 */
	public void addNewWineArea(List<WineArea> lst){
		serviceClient.updateWineArea(lst,CodeTablesService.ADD_NEW_ACTION);
	}

	/**
	 * update list wine area
	 * @param lst list wine area to update
	 */
	public void updateWineArea(List<WineArea> lst){
		serviceClient.updateWineArea(lst,CodeTablesService.UPDATE_ACTION);
	}

	/**
	 * delete list wine area
	 * @param lst list wine area to delete
	 */
	public void deleteWineArea(List<WineArea> lst){
		serviceClient.updateWineArea(lst,CodeTablesService.DELETE_ACTION);
	}
	/**
	 * add new wine maker funtion. call webserice to save all new wine maker
	 * @param lst list wine maker
	 */
	public void addNewWineMaker(List<WineMaker> lst){
		serviceClient.updateWineMaker(lst,CodeTablesService.ADD_NEW_ACTION);
	}

	/**
	 * update list wine maker
	 * @param lst list wine maker to update
	 */
	public void updateWineMaker(List<WineMaker> lst){
		serviceClient.updateWineMaker(lst,CodeTablesService.UPDATE_ACTION);
	}

	/**
	 * delete list wine maker
	 * @param lst list wine maker to delete
	 */
	public void deleteWineMaker(List<WineMaker> lst){
		serviceClient.updateWineMaker(lst,CodeTablesService.DELETE_ACTION);
	}
}
