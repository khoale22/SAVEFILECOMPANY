package com.heb.pm.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ScaleUpc.class)
public abstract class ScaleUpc_ {

	public static volatile SingularAttribute<ScaleUpc, Long> labelFormatOne;
	public static volatile SingularAttribute<ScaleUpc, String> englishDescriptionFour;
	public static volatile SingularAttribute<ScaleUpc, String> spanishDescriptionThree;
	public static volatile SingularAttribute<ScaleUpc, String> englishDescriptionTwo;
	public static volatile SingularAttribute<ScaleUpc, IngredientStatementHeader> ingredientStatementHeader;
	public static volatile SingularAttribute<ScaleUpc, Double> prePackTare;
	public static volatile ListAttribute<ScaleUpc, NutrientStatementDetail> nutrientStatementDetails;
	public static volatile SingularAttribute<ScaleUpc, Long> ingredientStatement;
	public static volatile SingularAttribute<ScaleUpc, ScaleGraphicsCode> scaleGraphicsCode;
	public static volatile SingularAttribute<ScaleUpc, Integer> eatByDays;
	public static volatile SingularAttribute<ScaleUpc, String> spanishDescriptionTwo;
	public static volatile SingularAttribute<ScaleUpc, ScaleLabelFormat> secondLabelFormat;
	public static volatile SingularAttribute<ScaleUpc, String> spanishDescriptionFour;
	public static volatile SingularAttribute<ScaleUpc, String> spanishDescriptionOne;
	public static volatile SingularAttribute<ScaleUpc, ScaleLabelFormat> firstLabelFormat;
	public static volatile SingularAttribute<ScaleUpc, String> englishDescriptionOne;
	public static volatile SingularAttribute<ScaleUpc, Long> actionCode;
	public static volatile SingularAttribute<ScaleUpc, Long> labelFormatTwo;
	public static volatile SingularAttribute<ScaleUpc, Boolean> stripFlag;
	public static volatile SingularAttribute<ScaleUpc, Integer> freezeByDays;
	public static volatile SingularAttribute<ScaleUpc, Boolean> priceOverride;
	public static volatile SingularAttribute<ScaleUpc, Long> nutrientStatement;
	public static volatile SingularAttribute<ScaleUpc, Long> upc;
	public static volatile SingularAttribute<ScaleUpc, Character> maintFunction;
	public static volatile SingularAttribute<ScaleUpc, Double> netWeight;
	public static volatile SingularAttribute<ScaleUpc, Double> serviceCounterTare;
	public static volatile SingularAttribute<ScaleUpc, String> englishDescriptionThree;
	public static volatile SingularAttribute<ScaleUpc, Integer> grade;
	public static volatile SingularAttribute<ScaleUpc, Boolean> forceTare;
	public static volatile SingularAttribute<ScaleUpc, Integer> shelfLifeDays;
	public static volatile SingularAttribute<ScaleUpc, Long> graphicsCode;
	public static volatile SingularAttribute<ScaleUpc, AssociatedUpc> associateUpc;
	public static volatile SingularAttribute<ScaleUpc, NutrientStatementHeader> nutrientStatementHeader;
	public static volatile SingularAttribute<ScaleUpc, LocalDate> effectiveDate;
	public static volatile SingularAttribute<ScaleUpc, ScaleActionCode> scaleActionCode;

}

