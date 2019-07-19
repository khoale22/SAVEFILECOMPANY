package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductRelationshipAuditKey.class)
public abstract class ProductRelationshipAuditKey_ {

	public static volatile SingularAttribute<ProductRelationshipAuditKey, Long> productId;
	public static volatile SingularAttribute<ProductRelationshipAuditKey, Long> relatedProductId;
	public static volatile SingularAttribute<ProductRelationshipAuditKey, String> productRelationshipCode;
	public static volatile SingularAttribute<ProductRelationshipAuditKey, LocalDateTime> changedOn;

}

