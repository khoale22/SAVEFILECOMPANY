package com.heb.pm.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(IngredientStatementHeader.class)
public abstract class IngredientStatementHeader_ {

	public static volatile SingularAttribute<IngredientStatementHeader, Boolean> maintenanceSwitch;
	public static volatile ListAttribute<IngredientStatementHeader, ScaleUpc> scaleUpcs;
	public static volatile SingularAttribute<IngredientStatementHeader, LocalDate> maintenanceDate;
	public static volatile SingularAttribute<IngredientStatementHeader, Character> maintenanceCode;
	public static volatile ListAttribute<IngredientStatementHeader, IngredientStatementDetail> ingredientStatementDetails;
	public static volatile SingularAttribute<IngredientStatementHeader, Long> statementNumber;

}

