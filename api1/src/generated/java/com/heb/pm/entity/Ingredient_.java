package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Ingredient.class)
public abstract class Ingredient_ {

	public static volatile SingularAttribute<Ingredient, String> maintFunction;
	public static volatile SingularAttribute<Ingredient, IngredientCategory> ingredientCategory;
	public static volatile SingularAttribute<Ingredient, Boolean> soiFlag;
	public static volatile SingularAttribute<Ingredient, Long> categoryCode;
	public static volatile SingularAttribute<Ingredient, String> ingredientCode;
	public static volatile SingularAttribute<Ingredient, String> ingredientCatDescription;
	public static volatile SingularAttribute<Ingredient, Integer> ingredientCodeAsNumber;
	public static volatile ListAttribute<Ingredient, IngredientSub> ingredientSubs;
	public static volatile SingularAttribute<Ingredient, String> ingredientDescription;
	public static volatile ListAttribute<Ingredient, IngredientStatementDetail> statementDetails;

}

