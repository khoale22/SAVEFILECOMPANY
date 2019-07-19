package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CustomerProductGroupAudit.class)
public abstract class CustomerProductGroupAudit_ {

	public static volatile SingularAttribute<CustomerProductGroupAudit, String> custProductGroupName;
	public static volatile SingularAttribute<CustomerProductGroupAudit, String> custProductGroupDescription;
	public static volatile SingularAttribute<CustomerProductGroupAudit, String> changedBy;
	public static volatile SingularAttribute<CustomerProductGroupAudit, String> action;
	public static volatile SingularAttribute<CustomerProductGroupAudit, String> custProductGroupDescriptionLong;
	public static volatile SingularAttribute<CustomerProductGroupAudit, CustomerProductGroupAuditKey> key;

}

