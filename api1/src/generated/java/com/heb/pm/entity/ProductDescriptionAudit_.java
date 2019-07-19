package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductDescriptionAudit.class)
public abstract class ProductDescriptionAudit_ {

	public static volatile SingularAttribute<ProductDescriptionAudit, String> lastUpdatedBy;
	public static volatile SingularAttribute<ProductDescriptionAudit, DescriptionType> descriptionType;
	public static volatile SingularAttribute<ProductDescriptionAudit, String> description;
	public static volatile SingularAttribute<ProductDescriptionAudit, String> action;
	public static volatile SingularAttribute<ProductDescriptionAudit, ProductDescriptionAuditKey> key;

}

