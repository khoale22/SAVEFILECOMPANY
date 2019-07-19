package com.heb.pm.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(NutrientStatementHeader.class)
public abstract class NutrientStatementHeader_ {

	public static volatile SingularAttribute<NutrientStatementHeader, Character> statementMaintenanceSwitch;
	public static volatile ListAttribute<NutrientStatementHeader, NutrientStatementDetail> nutrientStatementDetailList;
	public static volatile SingularAttribute<NutrientStatementHeader, Long> servingsPerContainer;
	public static volatile SingularAttribute<NutrientStatementHeader, Double> measureQuantity;
	public static volatile SingularAttribute<NutrientStatementHeader, Long> uomCommonCode;
	public static volatile SingularAttribute<NutrientStatementHeader, NutrientUom> nutrientMetricUom;
	public static volatile SingularAttribute<NutrientStatementHeader, ScaleUpc> scaleUpc;
	public static volatile SingularAttribute<NutrientStatementHeader, Long> metricQuantity;
	public static volatile SingularAttribute<NutrientStatementHeader, NutrientUom> nutrientCommonUom;
	public static volatile SingularAttribute<NutrientStatementHeader, Long> nutrientStatementNumber;
	public static volatile SingularAttribute<NutrientStatementHeader, Long> uomMetricCode;
	public static volatile SingularAttribute<NutrientStatementHeader, LocalDate> effectiveDate;

}

