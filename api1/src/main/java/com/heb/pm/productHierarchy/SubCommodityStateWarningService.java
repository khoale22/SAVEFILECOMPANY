package com.heb.pm.productHierarchy;

import com.heb.pm.entity.SubCommodityStateWarning;
import com.heb.pm.repository.SubCommodityStateWarningRepository;
import com.heb.pm.ws.ProductHierarchyManagementServiceClient;
import com.heb.util.ws.SoapException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business functions related to sub-commodity state warnings.
 *
 * @author m314029
 * @since 2.12.0
 */
@Service
public class SubCommodityStateWarningService {

	@Autowired
	private ProductHierarchyManagementServiceClient productHierarchyManagementServiceClient;

	@Autowired
	private SubCommodityStateWarningRepository repository;

	/**
	 * This method calls the product hierarchy management service client to update a list of SubCommodityStateWarning.
	 *
	 * @param subCommodityStateWarnings SubCommodityStateWarnings to update.
	 */
	public void update(List<SubCommodityStateWarning> subCommodityStateWarnings) {
		try {
			this.productHierarchyManagementServiceClient.
					updateSubCommodityStateWarnings(subCommodityStateWarnings);
		} catch (Exception e){
			throw  new SoapException(e.getMessage());
		}
	}

	/**
	 * This method calls the SubCommodityStateWarning repository to find all SubCommodityStateWarnings associated with
	 * a particular sub-commodity.
	 *
	 * @param subCommodityStateWarning SubCommodityStateWarning that has the sub-commodity code to search for.
	 * @return List of all SubCommodityStateWarnings attached to a given sub-commodity.
	 */
	public List<SubCommodityStateWarning> findBySubCommodity(SubCommodityStateWarning subCommodityStateWarning) {
		if(subCommodityStateWarning.getKey() != null && subCommodityStateWarning.getKey().getSubCommodityCode() != null){
			return this.repository.findByKeySubCommodityCode(subCommodityStateWarning.getKey().getSubCommodityCode());
		} else {
			return new ArrayList<>();
		}
	}
}
