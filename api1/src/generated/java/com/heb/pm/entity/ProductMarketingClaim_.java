package com.heb.pm.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductMarketingClaim.class)
public abstract class ProductMarketingClaim_ {

	public static volatile SingularAttribute<ProductMarketingClaim, String> statusChangeReason;
	public static volatile SingularAttribute<ProductMarketingClaim, String> marketingClaimStatusCode;
	public static volatile SingularAttribute<ProductMarketingClaim, String> lastUpdateId;
	public static volatile SingularAttribute<ProductMarketingClaim, MarketingClaim> marketingClaim;
	public static volatile SingularAttribute<ProductMarketingClaim, LocalDate> promoPickExpirationDate;
	public static volatile SingularAttribute<ProductMarketingClaim, LocalDate> promoPickEffectiveDate;
	public static volatile SingularAttribute<ProductMarketingClaim, ProductMarketingClaimKey> key;

}

