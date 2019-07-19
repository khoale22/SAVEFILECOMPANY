package com.heb.pm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductOnlineAuditKey.class)
public abstract class ProductOnlineAuditKey_ {

	public static volatile SingularAttribute<ProductOnlineAuditKey, Long> productId;
	public static volatile SingularAttribute<ProductOnlineAuditKey, String> salesChannelCode;
	public static volatile SingularAttribute<ProductOnlineAuditKey, LocalDate> effectiveDate;
	public static volatile SingularAttribute<ProductOnlineAuditKey, LocalDateTime> changedOn;

}

