package com.heb.pm.repository;

import com.heb.pm.entity.CandidateVendorLocationItem;
import com.heb.pm.entity.CandidateVendorLocationItemKey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author vn55306
 */
public interface CandidateVendorLocationItemRepository extends JpaRepository<CandidateVendorLocationItem, CandidateVendorLocationItemKey>{

	/**
	 * Returns a list of candidate vendor location item based on the candidate vendor location item key.
	 *
	 * @param key The candidate vendor location item key.
	 * @return a list of candidate vendor location item.
	 */
	List<CandidateVendorLocationItem> findByKey(CandidateVendorLocationItemKey key);
}
