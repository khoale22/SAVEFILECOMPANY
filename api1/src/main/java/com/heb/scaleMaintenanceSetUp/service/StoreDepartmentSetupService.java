package com.heb.scaleMaintenanceSetUp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.heb.pm.entity.Location;
import com.heb.pm.entity.LocationKey;
import com.heb.pm.repository.LocationRepository;
import com.heb.scaleMaintenanceSetUp.JbatJobTransactional;
import com.heb.scaleMaintenanceSetUp.entity.GblParm;
import com.heb.scaleMaintenanceSetUp.entity.StoreDetails;
import com.heb.scaleMaintenanceSetUp.repository.GblParmRepository;
import com.heb.util.jpa.PageableResult;

/**
 * Holds the business logic for scale maintenance loads.
 *
 * @author m314029
 * @since 2.17.8
 */
@Service
public class StoreDepartmentSetupService {

	private static final Logger logger = LoggerFactory.getLogger(StoreDepartmentSetupService.class);

	@Autowired
	@Qualifier("asyncJobLauncher")
	private JobLauncher jobLauncher;

	@Autowired
	private GblParmRepository gblParmRepository;
	
	@Autowired
	private LocationRepository locationRepository;

	/**
	 * This method is used to Fetch all stores
	 * @return
	 */
	@JbatJobTransactional
	public PageableResult<StoreDetails> fetchAllStores(Integer page, Integer pageSize, Boolean includeCount, String detailsNeeded) {
		PageableResult<StoreDetails> storeDetailsReturn =null; 
		
		if(detailsNeeded.equalsIgnoreCase("strDeptAuth")) {
		
		StoreDepartmentSetupService.logger.info("Inside Str Dept Service");
		PageRequest request = new PageRequest(page, pageSize, GblParm.getDefaultSort());
		PageableResult<GblParm> toReturn =  includeCount ?
				this.findAllByCreatedTimeWithCount(request) :
				this.findAllByCreatedTimeWithOutCount(request);
		int cnt = 0;	
		List<StoreDetails> storeDetailsList = new ArrayList<StoreDetails>();
		for (GblParm gblParm : toReturn.getData()) {
			StoreDepartmentSetupService.logger.info("Inside Str Dept Service: "+cnt++);
			/*RetailLocation retailLocation = new RetailLocation();
			RetailLocationKey locationKey = new RetailLocationKey();
			locationKey.setLocationNumber(Integer.parseInt(gblParm.getGblParmValTxt()));
			locationKey.setLocationTypeCode("S");*/
			LocationKey key = new LocationKey(); 
			key.setLocationNumber(Integer.parseInt(gblParm.getGblParmValTxt()));
			key.setLocationType("S");
			Location location = new Location();
//			retailLocation = retailLocationRepository.findOne(locationKey);
			location = locationRepository.findOne(key);
			StoreDetails storeDetails = new StoreDetails();
			storeDetails.setStoreNum(Integer.parseInt(gblParm.getGblParmValTxt()));
//			storeDetails.setStoreDesc(retailLocation.getStoreDesc());
			storeDetails.setStoreDesc(toCamelCase(location.getLocationName().trim()));
			List<String> deptIds =  new ArrayList<String>(Arrays.asList(gblParm.getDeptIds().split(",")));
			
			//Sort the Dept Id's
			Collections.sort(deptIds);
			
			int count = 0;
			for (String string : deptIds) {
				
				if(count==0){
					storeDetails.setDeptId1(string);
				}
				if(count==1){
					storeDetails.setDeptId2(string);
				}
				if(count==2){
					storeDetails.setDeptId3(string);		
								}
				if(count==3){
					storeDetails.setDeptId4(string);
				}
				if(count==4){
					storeDetails.setDeptId5(string);
				}
				if(count==5){
					storeDetails.setDeptId6(string);
				}
				if(count==6){
					storeDetails.setDeptId7(string);
				}
				if(count==7){
					storeDetails.setDeptId8(string);
				}
				if(count==8){
					storeDetails.setDeptId9(string);
				}
				if(count==9){
					storeDetails.setDeptId10(string);
				}
				if(count==10){
					storeDetails.setDeptId11(string);
				}
				if(count==11){
					storeDetails.setDeptId12(string);
				}
				if(count==12){
					storeDetails.setDeptId13(string);
				}
				count++;
			}
			
//			storeDetails.setDeptIds(deptIds);
			storeDetailsList.add(storeDetails);
		}
		int maxCount  = gblParmRepository.findMaxCount();
		StoreDepartmentSetupService.logger.info("MaxCount: "+maxCount);
		if(includeCount) {
			storeDetailsReturn =  new PageableResult<>(request.getPageNumber(), toReturn.getPageCount(),
					toReturn.getRecordCount(), storeDetailsList);
		}else {
			storeDetailsReturn =  new PageableResult<>(request.getPageNumber(),storeDetailsList);
		}
		StoreDepartmentSetupService.logger.info("storeDetails: "+storeDetailsList);
		} else if(detailsNeeded.equalsIgnoreCase("pantryStr")){
			StoreDepartmentSetupService.logger.info("Inside Pantry Store Call");
			storeDetailsReturn = getPantryStoreList(page,pageSize,includeCount);
		}
		return storeDetailsReturn;
	}
	
	/**
	 * This method is used to return the store desc while adding the store
	 * @param storeNum
	 * @return
	 */
	@JbatJobTransactional
	public StoreDetails getStoreDesc(String storeNum,String detailsNeeded) {
		String message = null;
		GblParm parm = null;
		StoreDetails details = new StoreDetails();
		LocationKey key = new LocationKey(); 
		int strNum = Integer.valueOf(storeNum);
		key.setLocationNumber(strNum);
		key.setLocationType("S");
		Location location = new Location();
		location = locationRepository.findOne(key);
		if(location != null) {
			location = locationRepository.getActiveStores(key.getLocationNumber(),key.getLocationType());
			if(location != null) {
			    if(detailsNeeded.equalsIgnoreCase("strDeptAuth")){
			      parm = gblParmRepository.findStore(String.format("%5s", strNum).replace(' ', '0'));  
			    }else  if(detailsNeeded.equalsIgnoreCase("pantryStr")){
			      parm = gblParmRepository.findPantryStore(String.format("%5s", strNum).replace(' ', '0'));  
			    }       
				
				if(parm == null) {
					details.setStoreDesc(toCamelCase(location.getLocationName()));
				}else {
					message = "Store: "+storeNum + " already Exists"; 
				}
			}
		}
		if(location == null) {
			message = "Store: "+storeNum + " is Not Present " + "or is Inactive";
		}
		details.setResponseMessage(message);
		return details;
	}
	
	/**
	 * This method is used to Add the store in GBL Parm Table
	 * @param storeDetails
	 * @return
	 */
	@JbatJobTransactional
	public StoreDetails addStores(StoreDetails storeDetails) {
		StoreDepartmentSetupService.logger.info("storeDetails: "+storeDetails.getUpdate());
		StoreDepartmentSetupService.logger.info("storeDetails: "+storeDetails.getTypCd());
		StoreDetails details = new StoreDetails();
		if(storeDetails.getTypCd().equalsIgnoreCase("strDeptAuth")) {
		if(storeDetails.getUpdate().equalsIgnoreCase("true")) {
			StoreDepartmentSetupService.logger.info("Update Call For Store: "+storeDetails.getStoreNum());
			details = updateStore(storeDetails);
		}else {
			    String message = null;
			    GblParm parm = gblParmRepository.findStore(String.format("%5s", storeDetails.getStoreNum()).replace(' ', '0'));
			    long storeNum = storeDetails.getStoreNum();
				if(parm == null) {
					StoreDepartmentSetupService.logger.info("Insert Call For Store: "+storeDetails.getStoreNum());
					List<String> deptIds = new ArrayList<>();
					
					if(storeDetails.getDeptId1().trim().length()>0) {
						deptIds.add(storeDetails.getDeptId1());
					}
					if(storeDetails.getDeptId2().trim().length()>0) {
						deptIds.add(storeDetails.getDeptId2());
					}
					if(storeDetails.getDeptId3().trim().length()>0) {
						deptIds.add(storeDetails.getDeptId3());
					}
					if(storeDetails.getDeptId4().trim().length()>0) {
						deptIds.add(storeDetails.getDeptId4());
					}
					if(storeDetails.getDeptId5().trim().length()>0) {
						deptIds.add(storeDetails.getDeptId5());
					}
					if(storeDetails.getDeptId6().trim().length()>0) {
						deptIds.add(storeDetails.getDeptId6());
					}
					if(storeDetails.getDeptId7().trim().length()>0) {
						deptIds.add(storeDetails.getDeptId7());
					}
					if(storeDetails.getDeptId8().trim().length()>0) {
						deptIds.add(storeDetails.getDeptId8());
					}
					if(storeDetails.getDeptId9().trim().length()>0) {
						deptIds.add(storeDetails.getDeptId9());
					}
					if(storeDetails.getDeptId10().trim().length()>0) {
						deptIds.add(storeDetails.getDeptId10());
					}
					if(storeDetails.getDeptId11().trim().length()>0) {
						deptIds.add(storeDetails.getDeptId11());
					}
					if(storeDetails.getDeptId12().trim().length()>0) {
						deptIds.add(storeDetails.getDeptId12());
					}
					if(storeDetails.getDeptId13().trim().length()>0) {
						deptIds.add(storeDetails.getDeptId13());
					}
					
					StringBuffer deptId = new StringBuffer();
					for (String dept : deptIds) {
						
						deptId.append(dept);
						deptId.append(",");
					}
					String departments = deptId.toString().substring(0, deptId.length()-1);
					
					GblParm gblParm = new GblParm();
					/*RetailLocation retailLocation = new RetailLocation();
					RetailLocationKey locationKey = new RetailLocationKey();
					locationKey.setLocationNumber(Integer.parseInt(gblParm.getGblParmValTxt()));
					locationKey.setLocationTypeCode("S");
					//Check whether the Location Number(Store) is avaialble in Retail_Loc Table
					retailLocation = this.retailLocationRepository.findOne(locationKey);*/
					
					LocationKey key = new LocationKey(); 
					int strNum = (int) storeNum;
					key.setLocationNumber(strNum);
					key.setLocationType("S");
					Location location = new Location();
			//		retailLocation = retailLocationRepository.findOne(locationKey);
			//		location = locationRepository.findOne(key);
					location = locationRepository.getActiveStores(key.getLocationNumber(),key.getLocationType());
					//Validate whether the store is available in Retail Loc Table
						if(location != null) {
							int maxCount  = gblParmRepository.findMaxCount();
							long maxCnt = (long) maxCount;
							gblParm.setGblParmId(maxCnt+1);
							gblParm.setGblParmName("STR_DEPT_AUTH");
							gblParm.setGblParmValTxt(String.format("%5s", storeNum).replace(' ', '0'));
							gblParm.setDeptIds(departments);
							gblParm = this.gblParmRepository.save(gblParm);
							
							if(gblParm.getGblParmId()!=0){
								message = "Store: "+storeNum + " Added Successfully";
							} else {
								message = "Failed "+ " To Add Store: "+storeNum;
							}
							
						} else {
							message = "Store: "+storeNum + " is Not Present " + "or is Inactive";
						}
					} else {
				message = "Store: "+storeNum + " already Exists ";
			}
			 details.setResponseMessage(message);
			}
		} else if(storeDetails.getTypCd().equalsIgnoreCase("pantryStr")) {
			details = addPantryStores(storeDetails);
		}
		return details;
	}
	
	/**
	 * @param storeNum
	 * @return
	 * This method is used to delete the stores
	 */
	@JbatJobTransactional
	public StoreDetails deleteStore(String storeNum,String detailsNeeded) {
		StoreDetails details= new StoreDetails();
		if(detailsNeeded.equalsIgnoreCase("strDeptAuth")){
		String message = null;
		this.gblParmRepository.deleteStore(String.format("%5s", storeNum).replace(' ', '0'));
		message = "Store: "+storeNum+ " is Successfully deleted";
	    details.setResponseMessage(message);
		} else if(detailsNeeded.equalsIgnoreCase("pantryStr")){
			String message = null;
			this.gblParmRepository.deletePantryStore(String.format("%5s", storeNum).replace(' ', '0'));
			message = "Store: "+storeNum+ " is Successfully deleted";
			details.setResponseMessage(message);
		}
		return details;
	}
	
	/**
	 * @param storeNum
	 * @param deptIds
	 * This method is used to update an existing store
	 * @return
	 */
	private StoreDetails updateStore(StoreDetails storeDetails) {
		
		StoreDetails details = new StoreDetails();
		long storeNum = storeDetails.getStoreNum();
        List<String> deptIds = new ArrayList<>();
		
		if(storeDetails.getDeptId1().trim().length()>0) {
		deptIds.add(storeDetails.getDeptId1());
		}
		if(storeDetails.getDeptId2().trim().length()>0) {
		deptIds.add(storeDetails.getDeptId2());
		}
		if(storeDetails.getDeptId3().trim().length()>0) {
		deptIds.add(storeDetails.getDeptId3());
		}
		if(storeDetails.getDeptId4().trim().length()>0) {
		deptIds.add(storeDetails.getDeptId4());
		}
		if(storeDetails.getDeptId5().trim().length()>0) {
		deptIds.add(storeDetails.getDeptId5());
		}
		if(storeDetails.getDeptId6().trim().length()>0) {
		deptIds.add(storeDetails.getDeptId6());
		}
		if(storeDetails.getDeptId7().trim().length()>0) {
		deptIds.add(storeDetails.getDeptId7());
		}
		if(storeDetails.getDeptId8().trim().length()>0) {
		deptIds.add(storeDetails.getDeptId8());
		}
		if(storeDetails.getDeptId9().trim().length()>0) {
		deptIds.add(storeDetails.getDeptId9());
		}
		if(storeDetails.getDeptId10().trim().length()>0) {
		deptIds.add(storeDetails.getDeptId10());
		}
		if(storeDetails.getDeptId11().trim().length()>0) {
		deptIds.add(storeDetails.getDeptId11());
		}
		if(storeDetails.getDeptId12().trim().length()>0) {
		deptIds.add(storeDetails.getDeptId12());
		}
		if(storeDetails.getDeptId13().trim().length()>0) {
		deptIds.add(storeDetails.getDeptId13());
		}
		String message = null;
		StringBuffer deptId = new StringBuffer();
		for (String dept : deptIds) {
			
			deptId.append(dept);
			deptId.append(",");
		}
		String departments = deptId.toString().substring(0, deptId.length()-1);
		
		GblParm gblParm = new GblParm();
		
		gblParm.setGblParmName("STR_DEPT_AUTH");
		gblParm.setGblParmValTxt(String.format("%5s", storeNum).replace(' ', '0'));
		gblParm.setDeptIds(departments);
		int count = this.gblParmRepository.updateStore(gblParm.getGblParmValTxt(), gblParm.getDeptIds());
		
		if(count > 0){
			message = "Store: "+storeNum + " updated Successfully";
		} else {
			message = "Failed "+ "To Add Store: "+storeNum;
		}
		
		details.setResponseMessage(message);
		return details;
		
	}
	
	/**
	 * Finds all scale maintenance tracking sorted by created time without including count query.
	 *
	 * @param request Request containing search criteria (page, pageSize, sort).
	 * @return Page of data.
	 */
	private PageableResult<GblParm> findAllByCreatedTimeWithOutCount(PageRequest request) {
		List<GblParm> data = this.gblParmRepository
				.findAllByPage(request);
		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Finds all scale maintenance tracking sorted by created time including count query.
	 *
	 * @param request Request containing search criteria (page, pageSize, sort).
	 * @return Page of data.
	 */
	private PageableResult<GblParm> findAllByCreatedTimeWithCount(PageRequest request) {
		Page<GblParm> data = this.gblParmRepository.findAllByPages(request);
		return new PageableResult<>(request.getPageNumber(), data.getTotalPages(),
				data.getTotalElements(), data.getContent());
	}
	
	/**
	 * Thos Method is used to get the List of pantry stores
	 * @param page
	 * @param pageSize
	 * @param includeCount
	 * @return
	 */
	public PageableResult<StoreDetails> getPantryStoreList(Integer page, Integer pageSize, Boolean includeCount) {

		PageableResult<StoreDetails> storeDetailsReturn =null; 
		StoreDepartmentSetupService.logger.info("Inside Pantry Store Service");
		PageRequest request = new PageRequest(page, pageSize, GblParm.getDefaultSort());
		PageableResult<GblParm> toReturn =  includeCount ?
				this.findAllForPantryStoresWithCount(request) :
				this.findAllForPantryStoresWithOutCount(request);
		int cnt = 0;	
		List<StoreDetails> storeDetailsList = new ArrayList<StoreDetails>();
		for (GblParm gblParm : toReturn.getData()) {
			StoreDepartmentSetupService.logger.info("Inside Pantry Store Service: "+cnt++);
			/*RetailLocation retailLocation = new RetailLocation();
			RetailLocationKey locationKey = new RetailLocationKey();
			locationKey.setLocationNumber(Integer.parseInt(gblParm.getGblParmValTxt()));
			locationKey.setLocationTypeCode("S");*/
			LocationKey key = new LocationKey(); 
			key.setLocationNumber(Integer.parseInt(gblParm.getGblParmValTxt()));
			key.setLocationType("S");
			Location location = new Location();
//			retailLocation = retailLocationRepository.findOne(locationKey);
			location = locationRepository.findOne(key);
			StoreDetails storeDetails = new StoreDetails();
			storeDetails.setStoreNum(Integer.parseInt(gblParm.getGblParmValTxt()));
//			storeDetails.setStoreDesc(retailLocation.getStoreDesc());
			storeDetails.setStoreDesc(toCamelCase(location.getLocationName().trim()));
//			storeDetails.setDeptIds(deptIds);
			storeDetailsList.add(storeDetails);
		}
		int maxCount  = gblParmRepository.findMaxCount();
		StoreDepartmentSetupService.logger.info("MaxCount: "+maxCount);
		if(includeCount) {
			storeDetailsReturn =  new PageableResult<>(request.getPageNumber(), toReturn.getPageCount(),
					toReturn.getRecordCount(), storeDetailsList);
		}else {
			storeDetailsReturn =  new PageableResult<>(request.getPageNumber(),storeDetailsList);
		}
		StoreDepartmentSetupService.logger.info("storeDetails: "+storeDetailsList);
		
		return storeDetailsReturn;
	}
	
	public StoreDetails addPantryStores(StoreDetails storeDetails) {
		
		StoreDepartmentSetupService.logger.info("Pantry Store Method Invoked: "+storeDetails.getStoreNum());

		StoreDetails details = new StoreDetails();
		 String message = null;
		    GblParm parm = gblParmRepository.findPantryStore(String.format("%5s", storeDetails.getStoreNum()).replace(' ', '0'));
		    long storeNum = storeDetails.getStoreNum();
			if(parm == null) {
				StoreDepartmentSetupService.logger.info("Insert Call For Pantry Store: "+storeDetails.getStoreNum());
				
				GblParm gblParm = new GblParm();
				/*RetailLocation retailLocation = new RetailLocation();
				RetailLocationKey locationKey = new RetailLocationKey();
				locationKey.setLocationNumber(Integer.parseInt(gblParm.getGblParmValTxt()));
				locationKey.setLocationTypeCode("S");
				//Check whether the Location Number(Store) is avaialble in Retail_Loc Table
				retailLocation = this.retailLocationRepository.findOne(locationKey);*/
				
				LocationKey key = new LocationKey(); 
				int strNum = (int) storeNum;
				key.setLocationNumber(strNum);
				key.setLocationType("S");
				Location location = new Location();
		//		retailLocation = retailLocationRepository.findOne(locationKey);
		//		location = locationRepository.findOne(key);
				location = locationRepository.getActiveStores(key.getLocationNumber(),key.getLocationType());
				//Validate whether the store is available in Retail Loc Table
					if(location != null) {
						int maxCount  = gblParmRepository.findMaxCount();
						long maxCnt = (long) maxCount;
						gblParm.setGblParmId(maxCnt+1);
						gblParm.setGblParmName("PANTRY_STORES");
						gblParm.setGblParmValTxt(String.format("%5s", storeNum).replace(' ', '0'));
						gblParm = this.gblParmRepository.save(gblParm);
						
						if(gblParm.getGblParmId()!=0){
							message = "Store: "+storeNum + " Added Successfully";
						} else {
							message = "Failed "+ " To Add Store: "+storeNum;
						}
						
					} else {
						message = "Store: "+storeNum + " is Not Present " + "or is Inactive";
					}
				} else {
			message = "Store: "+storeNum + " already Exists ";
		}
		 details.setResponseMessage(message);
		 return details;
		}
	
	/**
	 * This method is used to convert a string to CamelCase
	 * @param init
	 * @return
	 */
	public static String toCamelCase(final String init) {
	    if (init == null)
	        return null;

	    final StringBuilder ret = new StringBuilder(init.length());

	    for (final String word : init.split(" ")) {
	        if (!word.isEmpty()) {
	            ret.append(Character.toUpperCase(word.charAt(0)));
	            ret.append(word.substring(1).toLowerCase());
	        }
	        if (!(ret.length() == init.length()))
	            ret.append(" ");
	    }

	    return ret.toString();
	}
	
	/**
	 * Finds all scale maintenance tracking sorted by created time without including count query.
	 *
	 * @param request Request containing search criteria (page, pageSize, sort).
	 * @return Page of data.
	 */
	private PageableResult<GblParm> findAllForPantryStoresWithOutCount(PageRequest request) {
		List<GblParm> data = this.gblParmRepository
				.findAllPantryStoresByPage(request);
		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Finds all scale maintenance tracking sorted by created time including count query.
	 *
	 * @param request Request containing search criteria (page, pageSize, sort).
	 * @return Page of data.
	 */
	private PageableResult<GblParm> findAllForPantryStoresWithCount(PageRequest request) {
		Page<GblParm> data = this.gblParmRepository.findAllPantryStoresByPages(request);
		return new PageableResult<>(request.getPageNumber(), data.getTotalPages(),
				data.getTotalElements(), data.getContent());
	}

}
