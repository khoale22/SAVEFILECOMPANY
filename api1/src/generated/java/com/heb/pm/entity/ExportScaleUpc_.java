package com.heb.pm.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ExportScaleUpc.class)
public abstract class ExportScaleUpc_ {

	public static volatile SingularAttribute<ExportScaleUpc, Boolean> stripFlag;
	public static volatile SingularAttribute<ExportScaleUpc, Long> labelFormatOne;
	public static volatile SingularAttribute<ExportScaleUpc, Integer> freezeByDays;
	public static volatile SingularAttribute<ExportScaleUpc, Double> prePackTare;
	public static volatile SingularAttribute<ExportScaleUpc, Long> ingredientStatement;
	public static volatile SingularAttribute<ExportScaleUpc, Boolean> priceOverride;
	public static volatile SingularAttribute<ExportScaleUpc, Long> nutrientStatement;
	public static volatile SingularAttribute<ExportScaleUpc, Long> upc;
	public static volatile SingularAttribute<ExportScaleUpc, Integer> eatByDays;
	public static volatile ListAttribute<ExportScaleUpc, IngredientStatementDetail> ingredientStatementDetails;
	public static volatile SingularAttribute<ExportScaleUpc, Character> maintFunction;
	public static volatile SingularAttribute<ExportScaleUpc, Double> netWeight;
	public static volatile SingularAttribute<ExportScaleUpc, Double> serviceCounterTare;
	public static volatile SingularAttribute<ExportScaleUpc, Integer> grade;
	public static volatile SingularAttribute<ExportScaleUpc, Boolean> forceTare;
	public static volatile SingularAttribute<ExportScaleUpc, Integer> shelfLifeDays;
	public static volatile SingularAttribute<ExportScaleUpc, Long> actionCode;
	public static volatile SingularAttribute<ExportScaleUpc, Long> graphicsCode;
	public static volatile SingularAttribute<ExportScaleUpc, Long> labelFormatTwo;
	public static volatile SingularAttribute<ExportScaleUpc, LocalDate> effectiveDate;

}

