package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductNutrient.class)
public abstract class ProductNutrient_ {

	public static volatile SingularAttribute<ProductNutrient, NutrientMaster> nutrientMaster;
	public static volatile SingularAttribute<ProductNutrient, String> servingSizeUomCode;
	public static volatile SingularAttribute<ProductNutrient, LocalDateTime> lastUpdateDate;
	public static volatile SingularAttribute<ProductNutrient, SourceSystem> sourceSystem;
	public static volatile SingularAttribute<ProductNutrient, ServingSizeUOM> servingSizeUOM;
	public static volatile SingularAttribute<ProductNutrient, String> dclrOnLblSw;
	public static volatile SingularAttribute<ProductNutrient, Double> nutrientQuantity;
	public static volatile SingularAttribute<ProductNutrient, Double> dalyValSrvngPct;
	public static volatile SingularAttribute<ProductNutrient, ProductNutrientKey> key;
	public static volatile SingularAttribute<ProductNutrient, LocalDateTime> createDate;

}

