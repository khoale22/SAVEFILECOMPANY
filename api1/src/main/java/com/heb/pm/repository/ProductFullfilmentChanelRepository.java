/*
 * ProductGroupTypeRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductFullfilmentChanel;
import com.heb.pm.entity.ProductFullfilmentChanelKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * ProductFullfilmentChanel repository
 *
 * @author vn87351
 * @since 2.12.0
 */
public interface ProductFullfilmentChanelRepository  extends JpaRepository<ProductFullfilmentChanel, ProductFullfilmentChanelKey> {
	/**
	 * Count by product id.
	 *
	 * @param productId the id of product.
	 * @return the number of items.
	 */
	long countByKeyProductId(long productId);

	/**
	 * Find the list of product fulfillment channel by product id, sale channel with expiration date greater than
	 * equal current date.
	 * @param productId - The product id
	 * @param salesChanelCode -  The sale channel code
	 * @param expirationDate - The expiration date.
	 * @return List<ProductFullfilmentChanel>
	 */
	List<ProductFullfilmentChanel> findByKeyProductIdAndKeySalesChanelCodeAndExpirationDateGreaterThanEqual(Long productId, String
			salesChanelCode, LocalDate expirationDate);

	/**
	 * Find the list of product fulfillment channel by product id
	 * @param productId - The product id.
	 * @return List<ProductFullfilmentChanel>
	 */
	List<ProductFullfilmentChanel> findByKeyProductId(Long productId);

	/**
	 * Find the list of product fulfillment channel by product id, sale channel equal current date.
	 * @param productId - The product id
	 * @param salesChanelCode -  The sale channel code
	 * @return List<ProductFullfilmentChanel>
	 */
	List<ProductFullfilmentChanel> findByKeyProductIdAndKeySalesChanelCode(Long productId, String salesChanelCode);
}
