package com.heb.pm.productHierarchy;

import com.heb.pm.entity.SellingRestrictionCode;
import com.heb.pm.repository.SellingRestrictionCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds all business logic related to product hierarchy selling restrictions codes.
 *
 * @author m314029
 * @since 2.8.0
 */
@Service
public class SellingRestrictionCodeService {

	private static final String SHIPPING_RESTRICTION_GROUP_CODE = "9";

	@Autowired
	private SellingRestrictionCodeRepository repository;

	/**
	 * Calls the selling restriction code repository to find all records with a restriction group code of '9' which
	 * represent shipping restrictions.
	 *
	 * @return List of all selling restriction codes that have restriction group code of '9'.
	 */
	public List<SellingRestrictionCode> findAllShippingRestrictions(){
		return this.repository.findByRestrictionGroupCodeOrderByRestrictionDescription(
				SellingRestrictionCodeService.SHIPPING_RESTRICTION_GROUP_CODE);
	}
}
