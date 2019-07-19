package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(StoreDepartmentAudit.class)
public abstract class StoreDepartmentAudit_ {

	public static volatile SingularAttribute<StoreDepartmentAudit, Double> shrinkLow;
	public static volatile SingularAttribute<StoreDepartmentAudit, Double> shrinkHigh;
	public static volatile SingularAttribute<StoreDepartmentAudit, Double> grossProfitLow;
	public static volatile SingularAttribute<StoreDepartmentAudit, Department> departmentMaster;
	public static volatile SingularAttribute<StoreDepartmentAudit, String> name;
	public static volatile SingularAttribute<StoreDepartmentAudit, Double> grossProfitHigh;
	public static volatile SingularAttribute<StoreDepartmentAudit, Long> reportGroupCode;
	public static volatile SingularAttribute<StoreDepartmentAudit, SubDepartmentKey> key;

}

