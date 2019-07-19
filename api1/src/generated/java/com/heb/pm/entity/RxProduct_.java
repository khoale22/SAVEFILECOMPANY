package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RxProduct.class)
public abstract class RxProduct_ {

	public static volatile SingularAttribute<RxProduct, DrugScheduleType> drugScheduleType;
	public static volatile SingularAttribute<RxProduct, Double> dirRxCst;
	public static volatile SingularAttribute<RxProduct, String> drugNmAbb;
	public static volatile SingularAttribute<RxProduct, Long> id;
	public static volatile SingularAttribute<RxProduct, String> drugScheduleTypeCode;
	public static volatile SingularAttribute<RxProduct, String> ndc;
	public static volatile SingularAttribute<RxProduct, Double> avgWhlslRxCst;

}

