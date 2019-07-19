package com.heb.pm.productDetails.casePack;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ItemMasterKey;
import com.heb.pm.entity.SupplierItemSubstitutions;
import com.heb.util.jpa.LazyObjectResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Represents ItemSubstitutionController information.
 *
 * @author m594201
 * @since 2.8.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ItemSubstitutionController.ITM_SUB_URL)
@AuthorizedResource(ResourceConstants.CASE_PACK_ITEM_SUB)
public class ItemSubstitutionController {

	/**
	 * The constant ITM_SUB_URL.
	 */
	protected static final String ITM_SUB_URL = "/itemSubstitution";

	protected static final String GET_ITEM_SUB_INFO = "/getItemSubInformation";

	private SupplierItemSubstitutionsResolver resolver = new SupplierItemSubstitutionsResolver();

	/**
	 * Resolves the lazy loading of SupplierItemSubstitutions entity.
	 */
	private class SupplierItemSubstitutionsResolver implements LazyObjectResolver<SupplierItemSubstitutions>{

		@Override
		public void fetch(SupplierItemSubstitutions d) {
			d.getKey().getItemId();
			if(d.getSupplierSubTypeCode() != null){
				d.getSupplierSubTypeCode().getSubTypeDescription();
			}
			if(d.getSupItemsWarehouseLocationItem() != null){
				d.getSupItemsWarehouseLocationItem().getSupplierStatusCode();
				d.getSupItemsWarehouseLocationItem().getItemMaster().getPack();
			}
		}
	}

	@Autowired private ItemSubstitutionService service;

	/**
	 * Gets item sub info. To better understand the products that are ok for substitution.
	 *
	 * @param itemCode the item code number that is tied to the the container for multiple upc's
	 * @param request  the request
	 * @return the item sub info
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ItemSubstitutionController.GET_ITEM_SUB_INFO)
	public List<SupplierItemSubstitutions> getItemSubInfo(@RequestParam(value = "itemCode") Long itemCode, HttpServletRequest request){

		List<SupplierItemSubstitutions> supplierItemSubstitutions = this.service.getItemSubInfo(itemCode);

	    supplierItemSubstitutions.forEach(this.resolver::fetch);

		return supplierItemSubstitutions;
	}
}
