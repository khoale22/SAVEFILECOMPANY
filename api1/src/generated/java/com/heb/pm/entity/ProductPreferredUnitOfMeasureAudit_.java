package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductPreferredUnitOfMeasureAudit.class)
public abstract class ProductPreferredUnitOfMeasureAudit_ {

	public static volatile SingularAttribute<ProductPreferredUnitOfMeasureAudit, Integer> sequenceNumber;
	public static volatile SingularAttribute<ProductPreferredUnitOfMeasureAudit, RetailUnitOfMeasure> retailUnitOfMeasure;
	public static volatile SingularAttribute<ProductPreferredUnitOfMeasureAudit, String> changedBy;
	public static volatile SingularAttribute<ProductPreferredUnitOfMeasureAudit, String> retailUnitOfMeasureCode;
	public static volatile SingularAttribute<ProductPreferredUnitOfMeasureAudit, String> action;
	public static volatile SingularAttribute<ProductPreferredUnitOfMeasureAudit, ProductPreferredUnitOfMeasureAuditKey> key;

}

