package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Department.class)
public abstract class Department_ {

	public static volatile SingularAttribute<Department, Double> shrinkLow;
	public static volatile SingularAttribute<Department, Double> shrinkHigh;
	public static volatile SingularAttribute<Department, Double> grossProfitLow;
	public static volatile SingularAttribute<Department, String> departmentNumber;
	public static volatile SingularAttribute<Department, String> name;
	public static volatile SingularAttribute<Department, Double> grossProfitHigh;
	public static volatile ListAttribute<Department, SubDepartment> subDepartmentList;
	public static volatile SingularAttribute<Department, Long> reportGroupCode;
	public static volatile SingularAttribute<Department, SubDepartmentKey> key;

}

