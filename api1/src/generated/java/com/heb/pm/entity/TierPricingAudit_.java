package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TierPricingAudit.class)
public abstract class TierPricingAudit_ {

	public static volatile SingularAttribute<TierPricingAudit, String> changedBy;
	public static volatile SingularAttribute<TierPricingAudit, String> action;
	public static volatile SingularAttribute<TierPricingAudit, Double> discountValue;
	public static volatile SingularAttribute<TierPricingAudit, TierPricingAuditKey> key;
	public static volatile SingularAttribute<TierPricingAudit, String> discountTypeCode;
	public static volatile SingularAttribute<TierPricingAudit, LocalDateTime> createDate;

}

