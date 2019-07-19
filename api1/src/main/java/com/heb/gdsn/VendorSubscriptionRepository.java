package com.heb.gdsn;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for the VendorSubscription entity.
 *
 * @author d116773
 * @since 2.3.0
 */
public interface VendorSubscriptionRepository extends JpaRepository<VendorSubscription, Integer> {

	/**
	 * Searches for vendor subscription record by GLN.
	 *
	 * @param vendorGln The GLN to search by.
	 * @param pageRequest The page request.
	 * @return A page of vendor subscriptions.
	 */
	List<VendorSubscription> findByTrimmedVendorGln(String vendorGln, Pageable pageRequest);

	/**
	 * Searches for vendor subscription record by vendor name.
	 *
	 * @param vendorName The vendor name to search by.
	 * @param pageRequest The page request.
	 * @return A page of vendor subscriptions.
	 */
	@Query("select v from VendorSubscription v where upper(v.vendorName )like upper(:vendorName)")
	List<VendorSubscription> findByVendorrName(@Param("vendorName")String vendorName, Pageable pageRequest);

	/**
	 * Returns the max ID for a vendor subscription record.
	 *
	 * @return The max ID for a vendor subscription record.
	 */
	@Query("select max(v.sequenceNumber) from VendorSubscription v")
	int findMaxSubscriptionNumber();
}
