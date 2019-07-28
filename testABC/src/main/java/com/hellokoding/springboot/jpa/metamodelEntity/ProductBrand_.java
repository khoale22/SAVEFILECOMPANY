package com.hellokoding.springboot.jpa.metamodelEntity;

import com.hellokoding.springboot.jpa.book.ProductBrand;
import com.hellokoding.springboot.jpa.book.ProductBrandCostOwner;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductBrand.class)
public abstract class ProductBrand_ {
	public static volatile SingularAttribute<ProductBrand, String> productBrandDescription;
	public static volatile ListAttribute<ProductBrand, ProductBrandCostOwner> productBrandCostOwners;
	public static volatile SingularAttribute<ProductBrand, Long> productBrandId;


}

