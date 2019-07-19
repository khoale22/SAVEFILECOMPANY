package com.heb.pm.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductOnlineAudit.class)
public abstract class ProductOnlineAudit_ {

	public static volatile SingularAttribute<ProductOnlineAudit, Boolean> showOnSite;
	public static volatile SingularAttribute<ProductOnlineAudit, String> changedBy;
	public static volatile SingularAttribute<ProductOnlineAudit, String> action;
	public static volatile SingularAttribute<ProductOnlineAudit, ProductOnlineAuditKey> key;
	public static volatile SingularAttribute<ProductOnlineAudit, LocalDate> effectiveDate;
	public static volatile SingularAttribute<ProductOnlineAudit, LocalDate> expirationDate;

}

