package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductDescriptionAuditKey.class)
public abstract class ProductDescriptionAuditKey_ {

	public static volatile SingularAttribute<ProductDescriptionAuditKey, String> languageType;
	public static volatile SingularAttribute<ProductDescriptionAuditKey, Long> productId;
	public static volatile SingularAttribute<ProductDescriptionAuditKey, String> descriptionType;
	public static volatile SingularAttribute<ProductDescriptionAuditKey, LocalDateTime> changedOn;

}

