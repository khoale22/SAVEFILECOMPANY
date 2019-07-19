package com.heb.pm.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TierPricingKey.class)
public abstract class TierPricingKey_ {

	public static volatile SingularAttribute<TierPricingKey, LocalDate> effectiveTimeStamp;
	public static volatile SingularAttribute<TierPricingKey, Long> discountQuantity;
	public static volatile SingularAttribute<TierPricingKey, Long> prodId;

}

