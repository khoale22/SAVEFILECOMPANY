package com.heb.pm.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Nutrient.class)
public abstract class Nutrient_ {

	public static volatile SingularAttribute<Nutrient, Long> uomCode;
	public static volatile SingularAttribute<Nutrient, Character> maintenanceSwitch;
	public static volatile ListAttribute<Nutrient, NutrientStatementDetail> nutrientStatementDetails;
	public static volatile SingularAttribute<Nutrient, NutrientUom> nutrientUom;
	public static volatile SingularAttribute<Nutrient, LocalDate> lstModifiedDate;
	public static volatile SingularAttribute<Nutrient, String> nutrientDescription;
	public static volatile SingularAttribute<Nutrient, Long> nutrientCode;
	public static volatile SingularAttribute<Nutrient, Double> fedLblSequence;
	public static volatile SingularAttribute<Nutrient, Boolean> usePercentDailyValue;
	public static volatile SingularAttribute<Nutrient, Boolean> isFederalRequiredPdv;
	public static volatile SingularAttribute<Nutrient, Long> recommendedDailyAmount;
	public static volatile SingularAttribute<Nutrient, Double> defaultBehaviorOverrideSequence;
	public static volatile SingularAttribute<Nutrient, Boolean> isFederalRequired;
	public static volatile SingularAttribute<Nutrient, Boolean> isDefaultBehaviorOverrideRequired;

}

