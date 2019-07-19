package com.heb.scaleMaintenance.repository;


import com.heb.scaleMaintenance.entity.ScaleMaintenanceTransmit;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceTransmitKey;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for scale maintenance transmit.
 *
 * @author m314029
 * @since 2.17.8
 */
public interface ScaleMaintenanceTransmitRepository extends JpaRepository<ScaleMaintenanceTransmit, ScaleMaintenanceTransmitKey> {

	/**
	 * Finds all scale maintenance transmits by transaction id.
	 *
	 * @param transactionId Transaction id to look for.
	 * @return List of scale maintenance transmits matching the transaction id.
	 */
	List<ScaleMaintenanceTransmit> findByKeyTransactionId(Long transactionId);

	/**
	 * Finds all scale maintenance transmits by transaction id for a given page, but only returns the data and no
	 * page information.
	 *
	 * @param request Request for information (i.e. page, page size, etc.)
	 * @return List of scale maintenance transmits matching the request.
	 */
	List<ScaleMaintenanceTransmit> findByKeyTransactionId(Long transactionId, Pageable request);
}
