package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductRelationshipAudit.class)
public abstract class ProductRelationshipAudit_ {

	public static volatile SingularAttribute<ProductRelationshipAudit, Double> productQuantity;
	public static volatile SingularAttribute<ProductRelationshipAudit, ProductMaster> relatedProduct;
	public static volatile SingularAttribute<ProductRelationshipAudit, String> changedBy;
	public static volatile SingularAttribute<ProductRelationshipAudit, LocalDateTime> lastUpdateTs;
	public static volatile SingularAttribute<ProductRelationshipAudit, String> action;
	public static volatile SingularAttribute<ProductRelationshipAudit, ProductRelationshipAuditKey> key;
	public static volatile SingularAttribute<ProductRelationshipAudit, Long> elementUpc;
	public static volatile SingularAttribute<ProductRelationshipAudit, ProductMaster> parentProduct;

}

