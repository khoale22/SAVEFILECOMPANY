package com.heb.scaleMaintenance.repository;


import com.heb.scaleMaintenance.entity.ScaleMaintenanceUpc;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceUpcKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for scale maintenance upc.
 *
 * @author m314029
 * @since 2.17.8
 */
public interface ScaleMaintenanceUpcRepository extends JpaRepository<ScaleMaintenanceUpc, ScaleMaintenanceUpcKey> {

	/**
	 * Finds all scale maintenance upcs by transaction id;
	 *
	 * @param transactionId Transaction id to look for.
	 * @return List of ScaleMaintenanceUpc matching the request.
	 */
	List<ScaleMaintenanceUpc> findByKeyTransactionId(Long transactionId);
}
