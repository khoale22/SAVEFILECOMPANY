package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CustomerProductGroupMembershipAuditKey.class)
public abstract class CustomerProductGroupMembershipAuditKey_ {

	public static volatile SingularAttribute<CustomerProductGroupMembershipAuditKey, Long> custProductGroupId;
	public static volatile SingularAttribute<CustomerProductGroupMembershipAuditKey, Long> prodId;
	public static volatile SingularAttribute<CustomerProductGroupMembershipAuditKey, LocalDateTime> changedOn;

}

