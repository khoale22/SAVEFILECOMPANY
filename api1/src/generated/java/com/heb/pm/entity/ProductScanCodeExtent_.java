package com.heb.pm.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductScanCodeExtent.class)
public abstract class ProductScanCodeExtent_ {

	public static volatile SingularAttribute<ProductScanCodeExtent, String> createUserId;
	public static volatile SingularAttribute<ProductScanCodeExtent, Integer> sourceSystem;
	public static volatile SingularAttribute<ProductScanCodeExtent, String> lastUpdateUserId;
	public static volatile SingularAttribute<ProductScanCodeExtent, LocalDate> lstUpdtTs;
	public static volatile SingularAttribute<ProductScanCodeExtent, String> prodDescriptionText;
	public static volatile SingularAttribute<ProductScanCodeExtent, ProductScanCodeExtentKey> key;

}

