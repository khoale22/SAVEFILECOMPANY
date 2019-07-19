package com.heb.pm.productDetails;

import com.heb.pm.entity.RecalledProduct;
import com.heb.pm.ws.ProductRecallServiceClient;
import com.heb.util.ws.SoapException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This holds all of the business logic for Product Details.
 *
 * @author m594201
 * @since 2.13.0
 */
@Service
public class ProductDetailService {

	@Autowired
	private ProductRecallServiceClient productRecallServiceClient;

	/**
	 * Gets product recall data for the productDetail Header banner.  Calls a web service and consolidates the data into one returned object.
	 *
	 * @param prodId the prod id
	 * @return the recall data associated with that product.
	 */
	public RecalledProduct getProductRecallData(Long prodId) {
		try{
			return this.productRecallServiceClient.getProductRecallStatus(prodId);
		} catch (Exception e) {
			if(e.getMessage().equals("No Records Found")) {
				return new RecalledProduct();
			}
			throw new SoapException(e.getMessage());
		}
	}
}
