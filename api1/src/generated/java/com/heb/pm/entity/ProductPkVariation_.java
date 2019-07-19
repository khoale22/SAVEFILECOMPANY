package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductPkVariation.class)
public abstract class ProductPkVariation_ {

	public static volatile SingularAttribute<ProductPkVariation, String> productValueDescription;
	public static volatile SingularAttribute<ProductPkVariation, String> houseHoldMeasurement;
	public static volatile SingularAttribute<ProductPkVariation, String> servingSizeUomCode;
	public static volatile SingularAttribute<ProductPkVariation, LocalDateTime> lastUpdateDate;
	public static volatile SingularAttribute<ProductPkVariation, SourceSystem> sourceSystem;
	public static volatile SingularAttribute<ProductPkVariation, String> servingsPerContainerText;
	public static volatile SingularAttribute<ProductPkVariation, String> panelTypeCode;
	public static volatile SingularAttribute<ProductPkVariation, Double> servingSizeQuantity;
	public static volatile SingularAttribute<ProductPkVariation, ProductPkVariationKey> key;
	public static volatile SingularAttribute<ProductPkVariation, LocalDateTime> createDate;

}

