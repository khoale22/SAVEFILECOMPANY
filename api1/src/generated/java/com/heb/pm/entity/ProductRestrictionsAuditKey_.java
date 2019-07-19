package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductRestrictionsAuditKey.class)
public abstract class ProductRestrictionsAuditKey_ {

	public static volatile SingularAttribute<ProductRestrictionsAuditKey, String> restrictionCode;
	public static volatile SingularAttribute<ProductRestrictionsAuditKey, String> changedBy;
	public static volatile SingularAttribute<ProductRestrictionsAuditKey, String> action;
	public static volatile SingularAttribute<ProductRestrictionsAuditKey, Long> prodId;
	public static volatile SingularAttribute<ProductRestrictionsAuditKey, LocalDateTime> changedOn;

}

