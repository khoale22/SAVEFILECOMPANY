package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SubDepartment.class)
public abstract class SubDepartment_ {

	public static volatile SingularAttribute<SubDepartment, Double> shrinkLow;
	public static volatile SingularAttribute<SubDepartment, Double> shrinkHigh;
	public static volatile SingularAttribute<SubDepartment, Double> grossProfitLow;
	public static volatile SingularAttribute<SubDepartment, Department> departmentMaster;
	public static volatile SingularAttribute<SubDepartment, String> name;
	public static volatile SingularAttribute<SubDepartment, Double> grossProfitHigh;
	public static volatile SingularAttribute<SubDepartment, Long> reportGroupCode;
	public static volatile SingularAttribute<SubDepartment, SubDepartmentKey> key;

}

