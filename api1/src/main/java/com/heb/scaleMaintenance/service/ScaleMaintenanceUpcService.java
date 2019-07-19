package com.heb.scaleMaintenance.service;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceUpc;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceUpcKey;
import com.heb.scaleMaintenance.model.ScaleMaintenanceProduct;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceUpcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds the business logic for scale maintenance upc.
 *
 * @author m314029
 * @since 2.17.8
 */
@Service
public class ScaleMaintenanceUpcService {

	@Autowired
	private ScaleMaintenanceUpcRepository repository;

	/**
	 * retrieves all of scale maintenance UPCs for the given transaction
	 * @param transactionId
	 * @return List of scale UPCs correlating to the provided transaction.
	 */

	public List<ScaleMaintenanceUpc> getByTransactionId(Long transactionId) {
		return this.repository.findByKeyTransactionId(transactionId);
	}

	/**
	 * returns the scale maintenance product in the given transaction and with the given UPC
	 * @param upc
	 * @param transactionId
	 * @return the appropriate scale maintenance product.
	 */

	public ScaleMaintenanceProduct findAllRetailInformationByStore(long upc, long transactionId){
		ScaleMaintenanceUpcKey scaleMaintenanceUpcKey = new ScaleMaintenanceUpcKey().setUpc(
				upc).setTransactionId( transactionId);
		return this.repository.findOne(scaleMaintenanceUpcKey).getScaleProductAsJson();
	}


}
