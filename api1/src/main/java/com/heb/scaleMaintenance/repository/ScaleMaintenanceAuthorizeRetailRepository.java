package com.heb.scaleMaintenance.repository;


import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetail;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetailKey;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Repository for scale maintenance authorize retail.
 *
 * @author m314029
 * @since 2.17.8
 */
public interface ScaleMaintenanceAuthorizeRetailRepository extends JpaRepository<ScaleMaintenanceAuthorizeRetail, ScaleMaintenanceAuthorizeRetailKey> {

	/**
	 * Get unique stores used linked to a given transaction.
	 *
	 * @param transactionId Transaction id to search for.
	 * @return Set of stores.
	 */
	@Query(value = "select s.key.store from ScaleMaintenanceAuthorizeRetail s where s.key.transactionId = :transactionId and s.authorized = 'Y'")
	Set<Integer> getUniqueStoresByTransactionIdAndAuthorized(@Param("transactionId") Long transactionId);

	/**
	 * Get by transaction id, store, and authorized == 'true'.
	 *
	 * @param transactionId Transaction id to search for.
	 * @param store Store to look for.
	 * @return List of scale maintenance authorize retails.
	 */
	List<ScaleMaintenanceAuthorizeRetail> getUniqueByKeyTransactionIdAndKeyStoreAndAuthorizedAndMessageIsNull(Long transactionId, Integer store, Boolean authorized);

	/**
	 * Get by transaction id, and store.
	 *
	 * @param transactionId Transaction id to search for.
	 * @param store Store to look for.
	 * @return List of scale maintenance authorize retails.
	 */
	List<ScaleMaintenanceAuthorizeRetail> findByKeyTransactionIdAndKeyStore(Long transactionId, Integer store);

	/**
	 * Finds all scale maintenance retail by store number, but only returns the data and no page information.
	 *
	 * @param transactionId Transaction id to search for.
	 * @param store Store number to search for.
	 * @param request Request for information (i.e. page, page size, etc.)
	 * @return List of scale maintenance authorize retails matching the request.
	 */
	List<ScaleMaintenanceAuthorizeRetail> findByKeyTransactionIdAndKeyStore(Long transactionId, Integer store, Pageable request);
}
