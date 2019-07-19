package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Varietal.class)
public abstract class Varietal_ {

	public static volatile SingularAttribute<Varietal, Integer> varietalId;
	public static volatile SingularAttribute<Varietal, String> varietalName;
	public static volatile SingularAttribute<Varietal, String> varietalTypeCode;
	public static volatile SingularAttribute<Varietal, VarietalType> varietalType;

}

