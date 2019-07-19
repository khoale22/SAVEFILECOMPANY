package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(IngredientStatementDetail.class)
public abstract class IngredientStatementDetail_ {

	public static volatile SingularAttribute<IngredientStatementDetail, Ingredient> ingredient;
	public static volatile SingularAttribute<IngredientStatementDetail, IngredientStatementHeader> ingredientStatementHeader;
	public static volatile SingularAttribute<IngredientStatementDetail, Double> ingredientPercentage;
	public static volatile SingularAttribute<IngredientStatementDetail, IngredientStatementDetailsKey> key;

}

