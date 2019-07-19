package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(NutrientMaster.class)
public abstract class NutrientMaster_ {

	public static volatile SingularAttribute<NutrientMaster, Integer> masterId;
	public static volatile SingularAttribute<NutrientMaster, Integer> sequence;
	public static volatile ListAttribute<NutrientMaster, ProductNutrient> productNutrients;
	public static volatile SingularAttribute<NutrientMaster, Integer> sourceSystem;
	public static volatile SingularAttribute<NutrientMaster, String> nutrientName;

}

