package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductBrand.class)
public abstract class ProductBrand_ {

	public static volatile SingularAttribute<ProductBrand, Boolean> showOnWeb;
	public static volatile SingularAttribute<ProductBrand, String> productBrandDescription;
	public static volatile SingularAttribute<ProductBrand, String> productBrandAbbreviation;
	public static volatile SingularAttribute<ProductBrand, ProductBrandTier> productBrandTier;
	public static volatile ListAttribute<ProductBrand, ProductBrandCostOwner> productBrandCostOwners;
	public static volatile SingularAttribute<ProductBrand, Long> productBrandId;
	public static volatile SingularAttribute<ProductBrand, Long> productBrandTierId;

}

