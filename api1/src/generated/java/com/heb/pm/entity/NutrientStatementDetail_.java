package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(NutrientStatementDetail.class)
public abstract class NutrientStatementDetail_ {

	public static volatile SingularAttribute<NutrientStatementDetail, Long> nutrientDailyValue;
	public static volatile SingularAttribute<NutrientStatementDetail, Double> nutrientPDDQuantity;
	public static volatile SingularAttribute<NutrientStatementDetail, Nutrient> nutrient;
	public static volatile SingularAttribute<NutrientStatementDetail, Double> nutrientStatementQuantity;
	public static volatile SingularAttribute<NutrientStatementDetail, NutrientStatementHeader> nutrientStatementHeader;
	public static volatile SingularAttribute<NutrientStatementDetail, NutrientStatementDetailsKey> key;

}

