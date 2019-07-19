package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(NLEA16Nutrient.class)
public abstract class NLEA16Nutrient_ {

	public static volatile SingularAttribute<NLEA16Nutrient, Long> nutrientId;
	public static volatile SingularAttribute<NLEA16Nutrient, UnitOfMeasure> unitOfMeasure;
	public static volatile SingularAttribute<NLEA16Nutrient, String> pdvOrWhole;
	public static volatile SingularAttribute<NLEA16Nutrient, String> sourceSystemReferenceId;
	public static volatile SingularAttribute<NLEA16Nutrient, Double> recommendedDailyAmount;
	public static volatile SingularAttribute<NLEA16Nutrient, String> nutrientDescription;
	public static volatile SingularAttribute<NLEA16Nutrient, Integer> nutrientDisplaySequenceNumber;

}

