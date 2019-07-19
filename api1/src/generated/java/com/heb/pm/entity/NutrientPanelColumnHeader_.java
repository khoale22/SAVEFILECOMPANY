package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(NutrientPanelColumnHeader.class)
public abstract class NutrientPanelColumnHeader_ {

	public static volatile SingularAttribute<NutrientPanelColumnHeader, Double> caloriesQuantity;
	public static volatile SingularAttribute<NutrientPanelColumnHeader, NutrientStatementPanelHeader> nutrientStatementPanelHeader;
	public static volatile ListAttribute<NutrientPanelColumnHeader, NutrientPanelDetail> nutrientPanelDetails;
	public static volatile SingularAttribute<NutrientPanelColumnHeader, NutrientPanelColumnHeaderKey> key;
	public static volatile SingularAttribute<NutrientPanelColumnHeader, String> servingSizeLabel;

}

