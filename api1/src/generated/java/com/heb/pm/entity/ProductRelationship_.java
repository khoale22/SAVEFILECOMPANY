package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductRelationship.class)
public abstract class ProductRelationship_ {

	public static volatile SingularAttribute<ProductRelationship, Double> productQuantity;
	public static volatile SingularAttribute<ProductRelationship, ProductMaster> relatedProduct;
	public static volatile SingularAttribute<ProductRelationship, ProductRelationshipKey> key;
	public static volatile SingularAttribute<ProductRelationship, Long> elementUpc;
	public static volatile SingularAttribute<ProductRelationship, ProductMaster> parentProduct;

}

