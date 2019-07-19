package com.heb.pm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TierPricingAuditKey.class)
public abstract class TierPricingAuditKey_ {

	public static volatile SingularAttribute<TierPricingAuditKey, LocalDate> effectiveTimeStamp;
	public static volatile SingularAttribute<TierPricingAuditKey, Long> discountQuantity;
	public static volatile SingularAttribute<TierPricingAuditKey, Long> prodId;
	public static volatile SingularAttribute<TierPricingAuditKey, LocalDateTime> changedOn;

}

