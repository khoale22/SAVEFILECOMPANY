package com.heb.scaleMaintenance.utils;

import com.heb.pm.entity.NutrientStatementHeader;
import com.heb.pm.scaleManagement.ScaleManagementService;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceUpc;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceUpcKey;
import com.heb.scaleMaintenance.model.ScaleMaintenanceProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utilities for Scale Maintenance.
 *
 * @author m314029
 * @since 2.17.8
 */
@Component
public class ScaleMaintenanceUtils {

	private static final Logger logger = LoggerFactory.getLogger(ScaleMaintenanceUtils.class);

	// log messages
	private static final String SCALE_PRODUCT_NOT_FOUND_ERROR = "Scale product not found for upc: %d.";
	private static final String NUTRIENT_STATEMENT_NOT_FOUND_ERROR = "Nutrient statement not found for code: %d.";

	@Autowired
	private ScaleManagementService scaleManagementService;

	/**
	 * Adds the scale maintenance products into a list of scale maintenance upcs.
	 *
	 * @param scaleMaintenanceUpcs Scale maintenance upcs to add scale products to.
	 * @param scaleMaintenanceProducts List of scale maintenance products to add to scale upcs.
	 * @return List of scale maintenance upcs.
	 */
	private List<ScaleMaintenanceUpc> addScaleProductsToScaleMaintenanceUpcs(
			List<ScaleMaintenanceUpc> scaleMaintenanceUpcs, List<ScaleMaintenanceProduct> scaleMaintenanceProducts) {
		ScaleMaintenanceProduct currentProduct;
		for(ScaleMaintenanceUpc scaleMaintenanceUpc : scaleMaintenanceUpcs){
			currentProduct = this.findScaleMaintenanceProductForUpc(
					scaleMaintenanceProducts, scaleMaintenanceUpc.getKey().getUpc());
			scaleMaintenanceUpc
					.setScaleProductAsJson(currentProduct)
					.setJsonVersion(ScaleMaintenanceProduct.CURRENT_VERSION);
			if(currentProduct == null){
				logger.error(String.format(
						SCALE_PRODUCT_NOT_FOUND_ERROR, scaleMaintenanceUpc.getKey().getUpc()));
				scaleMaintenanceUpc
						.setMessage(String.format(
								SCALE_PRODUCT_NOT_FOUND_ERROR, scaleMaintenanceUpc.getKey().getUpc()));
			} else if (!NutrientStatementHeader.NULL_LIST.contains(currentProduct.getNutrientStatementCode()) &&
					currentProduct.getNutrientStatement() == null) {
				scaleMaintenanceUpc
						.setMessage(String.format(
								NUTRIENT_STATEMENT_NOT_FOUND_ERROR, currentProduct.getNutrientStatementCode()));
			}
		}
		return scaleMaintenanceUpcs;
	}

	/**
	 * Finds the scale maintenance product in the given list of scale maintenance products tied to the upc given.
	 *
	 * @param scaleMaintenanceProducts List scale maintenance products to search through.
	 * @param upc Upc to look for.
	 * @return The scale maintenance product tied to the given upc.
	 */
	private ScaleMaintenanceProduct findScaleMaintenanceProductForUpc(List<ScaleMaintenanceProduct> scaleMaintenanceProducts, Long upc) {
		for(ScaleMaintenanceProduct scaleMaintenanceProduct : scaleMaintenanceProducts){
			if(upc.equals(scaleMaintenanceProduct.getUpc())){
				return scaleMaintenanceProduct;
			}
		}
		return null;
	}

	/**
	 * Updates the given list of scale maintenance upcs with product information.
	 *
	 * @param scaleMaintenanceUpcs List of scale maintenance upcs to update.
	 * @return List of scale maintenance upcs.
	 */
	public List<ScaleMaintenanceUpc> updateScaleMaintenanceUpcWithProductData(
			List<ScaleMaintenanceUpc> scaleMaintenanceUpcs) {
		return this.addScaleProductsToScaleMaintenanceUpcs(
				scaleMaintenanceUpcs,
				this.scaleManagementService.convertUpcsToScaleMaintenanceProduct(
						scaleMaintenanceUpcs.stream()
								.map(ScaleMaintenanceUpc::getKey)
								.map(ScaleMaintenanceUpcKey::getUpc)
								.collect(Collectors.toList())));
	}
}
