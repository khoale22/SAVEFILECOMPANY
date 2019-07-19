package com.heb.scaleMaintenance.service;

import com.heb.pm.entity.ItemClass;
import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.ScaleUpc;
import com.heb.pm.entity.VendorItemStore;
import com.heb.pm.repository.ItemClassRepository;
import com.heb.pm.repository.ItemMasterRepository;
import com.heb.pm.repository.VendorItemStoreRepository;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetail;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetailKey;
import com.heb.scaleMaintenance.utils.EPlumApiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Holds all business logic related to authorizationAndRetail.
 *
 * @author m314029
 * @since 2.17.8
 */
@Service
public class ScaleAuthorizationService {

	private static final Logger logger = LoggerFactory.getLogger(ScaleAuthorizationService.class);

	//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	@Autowired
	private ItemMasterRepository itemMasterRepository;
	@Autowired
	private VendorItemStoreRepository vendorItemStoreRepository;

	@Autowired
	private ItemClassRepository itemClassRepository;
	private ThreadLocal<Boolean> storeDeptAuthorized = new ThreadLocal<Boolean>();
	private ThreadLocal<Boolean> isDSDItem = new ThreadLocal<Boolean>();
	private ThreadLocal<Boolean> isWareHouseItem = new ThreadLocal<Boolean>();
	private String DSDItem = "DSD  ";
	private String StoreLocTypeCd = "S ";
	//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

	/**
	 * Gets authorized information related to the given store and upcs.
	 *
	 * @param transactionId Transaction id to set for scale maintenance authorization retail.
	 * @param store Store to set for scale maintenance authorization retail.
	 * @param upcs List of upcs to makeup the scale maintenance authorization retail.
	 * @return
	 */
	public List<ScaleMaintenanceAuthorizeRetail> getAuthorizedForUpcsByStore(
			Long transactionId, Integer store,
			List<Long> upcs) {
		List<ScaleMaintenanceAuthorizeRetail> toReturn = new ArrayList<>();
		ScaleMaintenanceAuthorizeRetail currentAuthorizationRetail;
		ScaleMaintenanceAuthorizeRetailKey key;
		Map<Long, Boolean> upcAuthorization = this.getAuthorizationsByStoreAndUpcs(store, upcs);
		for (Long upc : upcs) {
			key = new ScaleMaintenanceAuthorizeRetailKey()
					.setStore(store)
					.setTransactionId(transactionId)
					.setUpc(upc);
			currentAuthorizationRetail = new ScaleMaintenanceAuthorizeRetail()
					.setKey(key)
					.setAuthorized(upcAuthorization.get(upc))
					.setCreateTime(LocalDateTime.now());
			toReturn.add(currentAuthorizationRetail);
		}
		return toReturn;
	}

	/**
	 * This method finds out if a given list of upcs is authorized for the given store
	 *
	 * @param store Store to look up authorizationAndRetail for.
	 * @param upcs List of upcs to check for authorizationAndRetail.
	 * @return Map containing upc and whether or not the upc is authorized at the given store.
	 */
	private Map<Long, Boolean> getAuthorizationsByStoreAndUpcs(Integer store, List<Long> upcs) {
		Map<Long, Boolean> toReturn = new HashMap<>();
		if(EPlumApiUtils.LAB_STORES.contains(store)) {
			for(Long upc : upcs){
				toReturn.put(upc, true);
			}
		} else {

			// for now, the user can only select lab stores; eventually when the user is able to select real stores,
			// authorization data will have to be looked up
			for(Long upc : upcs){
				toReturn.put(upc, false);
			}
		}
		return toReturn;
	}

	/**
	 * This method is used to get the ScaleMaintenanceAuthorizeRetail for given upc and store no
	 *
	 * @param transactionId Transaction id related to this information.
	 * @param scaleUpc Scale UPC containing product information.
	 * @param sourceStoreNo Store number used as source for transmitting data to EPlum.
	 * @param departmentNbr Department this transaction is sending information for.
	 * @return ScaleMaintenanceAuthorizeRetail
	 */
	public ScaleMaintenanceAuthorizeRetail getAuthorizationDetailByDepartment(long transactionId, ScaleUpc scaleUpc,
																			  int sourceStoreNo, String departmentNbr){
		ScaleMaintenanceAuthorizeRetailKey key = null;
		ScaleMaintenanceAuthorizeRetail currentAuthorizationRetail = null;
		storeDeptAuthorized.set(false);
		isDSDItem.set(false);
		isWareHouseItem.set(false);
		logger.debug("Processing the UPC:"+scaleUpc.getUpc());
		List<ItemMaster> itemMasterList = itemMasterRepository.findItemMasterByOrderingUpc(scaleUpc.getAssociateUpc().getPdUpcNo());
		if (itemMasterList != null && itemMasterList.size() > 0){
			itemMasterList.forEach(itemMaster ->{
				if (itemMaster.getKey().isDsd()){
					isDSDItem.set(true);
				} else if (itemMaster.getKey().isWarehouse()){
					isWareHouseItem.set(true);
				}
			});
			ItemMaster itemMaster = itemMasterList.get(0);
			if(itemMasterList.size() > 1 && isDSDItem.get() &&  isWareHouseItem.get() ){
				itemMaster.getKey().setItemType("DSD");
			}

			if(itemMaster.getKey().isDsd()){
				logger.debug("ItemCode :"+itemMaster.getKey().getItemCode());
				List<VendorItemStore> vendorItemStoreList = vendorItemStoreRepository.findVendItemStoreByStrDeptNbr(itemMaster.getKey().getItemCode(), DSDItem, sourceStoreNo, StoreLocTypeCd, departmentNbr);
				if (vendorItemStoreList != null && vendorItemStoreList.size() > 0) {
					storeDeptAuthorized.set(true);
				} else if (isDSDItem.get() &&  isWareHouseItem.get()){
					ItemClass itemClass =  itemClassRepository.findOne(Integer.valueOf(itemMaster.getClassCode().toString()));
					if (Integer.parseInt(departmentNbr.trim()) == itemClass.getDepartmentId().intValue()){
						storeDeptAuthorized.set(true);
					}
				}

			} else if (itemMaster.getKey().isWarehouse()){
				ItemClass itemClass =  itemClassRepository.findOne(Integer.valueOf(itemMaster.getClassCode().toString()));
				if (Integer.parseInt(departmentNbr.trim()) == itemClass.getDepartmentId().intValue()){
					storeDeptAuthorized.set(true);
				}
			}

		}

		logger.debug("UPC:"+scaleUpc.getUpc() +" is authorized:"+storeDeptAuthorized.get());
		if (storeDeptAuthorized.get()){
			key = new ScaleMaintenanceAuthorizeRetailKey()
					.setStore(Long.valueOf(sourceStoreNo).intValue())
					.setTransactionId(transactionId)
					.setUpc(scaleUpc.getUpc());
			currentAuthorizationRetail = new ScaleMaintenanceAuthorizeRetail()
					.setKey(key)
					.setAuthorized(true)
					.setCreateTime(LocalDateTime.now());
		}
		return currentAuthorizationRetail;
	}
}
