/*
 * ProductOnlineRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ProductOnline;
import com.heb.pm.entity.ProductOnlineKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for Product Online.
 *
 * @author vn70516
 * @since 2.14.0
 */
public interface ProductOnlineRepository extends JpaRepository<ProductOnline, ProductOnlineKey> {

    /**
     * Find a product online by product id and sale channel code and must be show on site sw turn on order by effective
     * date.
     * @param productId - The product id
     * @param saleChannel - The sale channel code.
     * @param showOnSite - The show on site switch
     * @return ProductOnline
     */
    ProductOnline findTop1ByKeyProductIdAndKeySaleChannelCodeAndShowOnSiteOrderByKeyEffectiveDateDesc(Long productId, String saleChannel, boolean showOnSite);

	/**
	 * find a product online entity by product id and sale chanel code
	 * @param productId - The product id
	 * @param saleChannel - The sale channel code.
	 * @return ProductOnline
	 */
	ProductOnline findTop1ByKeyProductIdAndKeySaleChannelCodeOrderByKeyEffectiveDateDesc(Long productId, String saleChannel);
}
