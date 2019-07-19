package com.heb.pm.repository;

import com.heb.pm.entity.CandidateVendorItemFactory;
import com.heb.pm.entity.CandidateVendorItemFactoryKey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author vn55306
 */
public interface CandidateVendorItemFactoryXrfRepository extends JpaRepository<CandidateVendorItemFactory,CandidateVendorItemFactoryKey>{
	/**
	 * Returns a list of candidate vendor item factory based on the candidate vendor item factory key.
	 *
	 * @param key The candidate vendor item factory key.
	 * @return a list of candidate vendor item factory.
	 */
	List<CandidateVendorItemFactory> findByKey(CandidateVendorItemFactoryKey key);

	/**
	 * Returns a list of candidate vendor item factory based on the candidate item id and vendor number and vendor type.
	 *
	 * @param candidateItemId The candidate item id.
	 * @param vendorNumber The vendor number.
	 * @param vendorType The vendor type.
	 * @return a list of candidate vendor item factory.
	 */
	List<CandidateVendorItemFactory> findByKeyCandidateItemIdAndKeyVendorNumberAndKeyVendorType(Integer candidateItemId, Integer vendorNumber, String vendorType);
}
