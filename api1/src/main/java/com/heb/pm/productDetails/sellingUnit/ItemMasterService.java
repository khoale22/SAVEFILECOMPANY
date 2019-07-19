package com.heb.pm.productDetails.sellingUnit;

import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.ItemMasterKey;
import com.heb.pm.repository.ItemMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds business logic related to Item Master data retrieval based on upc's.
 *
 * @author m594201
 * @since 2.12.0
 */
@Service
public class ItemMasterService {

	@Autowired
	private ItemMasterRepository itemMasterRepository;

	/**
	 * Find all item masters linked to upc list.
	 *
	 * @param upc the upc
	 * @return the list
	 */
	public List<ItemMaster> findAllItemMastersLinkedToUpc(long upc) {

		return this.itemMasterRepository.findByPrimaryUpcShipperKeyShipperUpcOrPrimaryUpcAssociateUpcsUpc(upc, upc);
	}

	/**
	 * Returns an ItemMaster by item code and item key type code.
	 *
	 * @param itemCode the item code.
	 * @param itemType the item type.
	 * @return the request item master.
	 */
	public ItemMaster findByItemCodeAndItemKeyTypeCode(Long itemCode, String itemType) {
		ItemMasterKey key = new ItemMasterKey();
		key.setItemCode(itemCode);
		key.setItemType(itemType);
		return this.itemMasterRepository.findOne(key);
	}
	/**
	 * Returns ItemMasters by ItemType and OrderingUpc.
	 *
	 * @param itemType the itemType.
	 * @param orderingUpc the orderingUpc.
	 * @return ItemMasters by ItemType and OrderingUpc.
	 */
	public List<ItemMaster> findByKeyItemTypeAndOrderingUpc(String itemType, Long orderingUpc) {
		return this.itemMasterRepository.findByKeyItemTypeAndOrderingUpc(itemType, orderingUpc);
	}
}
