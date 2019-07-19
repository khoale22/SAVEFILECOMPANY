package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductCategoryRole.class)
public abstract class ProductCategoryRole_ {

	public static volatile SingularAttribute<ProductCategoryRole, String> productCategoryRoleCode;
	public static volatile SingularAttribute<ProductCategoryRole, String> productCategoryRoleDescription;
	public static volatile ListAttribute<ProductCategoryRole, ProductCategory> productCategory;

}

