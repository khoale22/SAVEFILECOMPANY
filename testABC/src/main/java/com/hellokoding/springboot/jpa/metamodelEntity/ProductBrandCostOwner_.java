package com.hellokoding.springboot.jpa.metamodelEntity;

import com.hellokoding.springboot.jpa.book.CostOwner;
import com.hellokoding.springboot.jpa.book.ProductBrand;
import com.hellokoding.springboot.jpa.book.ProductBrandCostOwner;
import com.hellokoding.springboot.jpa.book.ProductBrandCostOwnerKey;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductBrandCostOwner.class)
public abstract class ProductBrandCostOwner_ {

	public static volatile SingularAttribute<ProductBrandCostOwner, ProductBrand> productBrand;
	public static volatile SingularAttribute<ProductBrandCostOwner, ProductBrandCostOwnerKey> key;
	public static volatile SingularAttribute<ProductBrandCostOwner, CostOwner> costOwner;

}

