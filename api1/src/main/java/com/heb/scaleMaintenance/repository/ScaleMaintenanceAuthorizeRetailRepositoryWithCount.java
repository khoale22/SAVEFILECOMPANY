package com.heb.scaleMaintenance.repository;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetail;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetailKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ScaleMaintenanceAuthorizeRetailRepositoryWithCount extends JpaRepository<ScaleMaintenanceAuthorizeRetail, ScaleMaintenanceAuthorizeRetailKey> {
	/**
	 * Finds all scale maintenance retail by store number, but only returns the data and no page information.
	 *
	 * @param transactionId Transaction id to search for.
	 * @param store Store number to search for.
	 * @param request Request for information (i.e. page, page size, etc.)
	 * @return List of scale maintenance authorize retails matching the request.
	 */
	Page<ScaleMaintenanceAuthorizeRetail> findByKeyTransactionIdAndKeyStore(Long transactionId, Integer store, Pageable request);
}
