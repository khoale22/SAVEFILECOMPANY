package com.heb.pm.repository;

/**
 * Common constants for the count and non-count prod_master JPA repository implementations.
 * Created by s753601 on 3/28/2017.
 * @since 2.4.0
 */
public interface ProductInfoRepositoryCommon {
    /**
     * String used to generate the search by description query for basic product search.
     */
    String PRODUCT_DESCRIPTION_QUERY = "select pm from ProductMaster pm left outer join " +
            "pm.productDescriptions pd1 on  pd1.key.languageType = " +
            "'ENG' and pd1.key.descriptionType = 'TAG1' and upper(pd1.description) like upper(:description) " +
            "left outer join pm.productDescriptions pd2 on pd2.key.languageType = " +
            "'ENG' and pd2.key.descriptionType = 'TAG2' and upper(pd2.description) like upper(:description) " +
            "where upper(pm.description) like upper(:description) or " +
			"upper(pd1.description) like upper(:description) or upper(pd2.description) like upper(:description)";

	/**
	 * String used to generate the query for products by a specific vendor.
	 */
	String PRODUCT_QUERY_FOR_VENDOR = "select p from ProductMaster p where exists " +
			"(select 1 from ProdItem pi join pi.itemMaster im join im.vendorLocationItems vli join vli.location l " +
			"where l.apVendorNumber=:apVendorNumber and (l.apTypeCode in (:apTypeCodes)) and p.prodId = pi.key.productId)";
}
