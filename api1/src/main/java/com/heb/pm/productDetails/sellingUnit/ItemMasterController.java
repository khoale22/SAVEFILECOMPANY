package com.heb.pm.productDetails.sellingUnit;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.Shipper;
import com.heb.pm.entity.User;
import com.heb.pm.user.UserService;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST endpoint for Item Master Information.
 *
 * @author m594201
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ItemMasterController.ITEM_MASTER_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_BASIC_INFORMATION)
public class ItemMasterController {

	private static final Logger logger = LoggerFactory.getLogger(ItemMasterController.class);

	private static final String FIND_ITEM_MASTER_LINKED_TO_UPC  = "Find item master linked to UPC: %d have been requested.";


	/**
	 * The constant ITEM_MASTER_LINKED_UPC.
	 */
	protected static final String ITEM_MASTER_LINKED_UPC = "/getAllItemMastersLinkedToUpc";

	/**
	 * The constant ITEM_MASTER_URL.
	 */
	protected  static final String ITEM_MASTER_URL = "/itemMaster";

	@Autowired
	private ItemMasterService itemMasterService;
	@Autowired
	private UserService userService;


	private LazyObjectResolver<List<ItemMaster>> itemMasterDataLazyObjectResolver = new ItemMasterDataResolver();


	/**
	 * Resolves a ItemMaster object. It will load the following properties:
	 * 1 ItemMaster
	 */
	private class ItemMasterDataResolver implements LazyObjectResolver<List<ItemMaster>> {

		@Override
		public void fetch(List<ItemMaster> itemMasters) {
			for(ItemMaster itemMaster: itemMasters) {
				itemMaster.getPrimaryUpc().getUpc();
				for(Shipper shipper : itemMaster.getPrimaryUpc().getShipper()){
					shipper.getKey().getShipperUpc();
				}

				if (userService != null) {
					User user = userService.getUserById(itemMaster.getAddedUsrId());
					if (user != null) {
						itemMaster.setDisplayCreatedName(user.getDisplayName());
					} else {
						itemMaster.setDisplayCreatedName(itemMaster.getAddedUsrId());
					}
				}
			}
		}
	}

	/**
	 * Find all item masters linked to upc list.
	 *
	 * @param upc     the upc
	 * @param request the request
	 * @return the list of Item Master to display in UI.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ItemMasterController.ITEM_MASTER_LINKED_UPC)
	public List<ItemMaster> findAllItemMastersLinkedToUpc(@RequestParam long upc, HttpServletRequest request) {

		List<ItemMaster>  returnItemMaster;

		this.logFindAllItemMaterLinkedToUPC(upc);

		returnItemMaster = this.itemMasterService.findAllItemMastersLinkedToUpc(upc);

		this.itemMasterDataLazyObjectResolver.fetch(returnItemMaster);

		return  returnItemMaster;
	}

	private void logFindAllItemMaterLinkedToUPC(long upc) {
		ItemMasterController.logger.info(
				String.format(ItemMasterController.FIND_ITEM_MASTER_LINKED_TO_UPC, upc)
		);
	}

}
