package com.heb.pm.productDetails.casePack;

import com.heb.pm.entity.ItemMasterKey;
import com.heb.pm.entity.SupplierItemSubstitutions;
import com.heb.pm.repository.SupplierItemSubstitutionsRepository;
import com.heb.util.jpa.PageableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds all business logic related to Supplier Item Substitutions
 *
 * @author m594201
 * @since 2.8.0
 */
@Service
public class ItemSubstitutionService {

	@Autowired
	private SupplierItemSubstitutionsRepository repository;

	/**
	 * Gets item sub info. To better understand the products that are ok for substitution.
	 *
	 * @param itemCode        the item code number that is tied to the the container for multiple upc's
	 * @return the item sub info
	 */
	public List<SupplierItemSubstitutions> getItemSubInfo(Long itemCode) {
		return this.repository.findByKeyItemIdAndKeyLocationTypeCodeAndKeyItemKeyTypeCode(itemCode,
				SupplierItemSubstitutions.WAREHOUSE_LOCATION, ItemMasterKey.WAREHOUSE );
	}
}
