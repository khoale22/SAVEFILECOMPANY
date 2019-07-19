package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductLineBrand.class)
public abstract class ProductLineBrand_ {

	public static volatile SingularAttribute<ProductLineBrand, LocalDateTime> timeStamp;
	public static volatile SingularAttribute<ProductLineBrand, ProductLine> productLine;
	public static volatile SingularAttribute<ProductLineBrand, ProductBrand> productBrand;
	public static volatile SingularAttribute<ProductLineBrand, String> userId;
	public static volatile SingularAttribute<ProductLineBrand, ProductLineBrandKey> key;

}

