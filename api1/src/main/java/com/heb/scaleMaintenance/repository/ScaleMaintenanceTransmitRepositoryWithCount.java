package com.heb.scaleMaintenance.repository;


import com.heb.scaleMaintenance.entity.ScaleMaintenanceTransmit;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceTransmitKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for scale maintenance transmit.
 *
 * @author m314029
 * @since 2.17.8
 */
public interface ScaleMaintenanceTransmitRepositoryWithCount extends JpaRepository<ScaleMaintenanceTransmit, ScaleMaintenanceTransmitKey> {

	/**
	 * Finds all scale maintenance transmits by transaction id, but only returns the data and no page information.
	 *
	 * @param request Request for information (i.e. page, page size, etc.)
	 * @return List of scale maintenance transmits matching the request.
	 */
	Page<ScaleMaintenanceTransmit> findByKeyTransactionId(Long transactionId, Pageable request);
}
