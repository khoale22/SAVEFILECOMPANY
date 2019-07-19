package com.heb.pm.productDetails.sellingUnit;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ProductScanCodeExtent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Represents Gladson data.
 *
 * @author m594201
 * @since 2.7.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + GladsonDataController.GLADSON_URL)
@AuthorizedResource(ResourceConstants.UPC_INFO)
public class GladsonDataController {

	/**
	 * The constant GLADSON_URL
	 */
	protected static final String GLADSON_URL = "/gladson";

	/**
	 * The constant GET_GLADSON_DATA.
	 */
	protected static final String GET_GLADSON_DATA = "/getGladsonRetailDimensionalData";

	@Autowired
	private GladsonDataService service;


	/**
	 * Gets gladson data that is representative of the measurements tied to the pictures of the products.
	 *
	 * @param upc     the scan_cd_id to search the prod_scn_codes table for the gladson data.
	 * @param request The HTTP request that initiated this call.
	 * @return the gladson data that includes measurements
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GladsonDataController.GET_GLADSON_DATA)
	public List<ProductScanCodeExtent> getGladsonRetailDimensionalData(@RequestParam(value="upc") Long upc, HttpServletRequest request) {
		return this.service.getGladsonRetailDimensionalData(upc);

	}
}
