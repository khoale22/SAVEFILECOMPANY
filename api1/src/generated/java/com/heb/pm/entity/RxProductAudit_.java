package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RxProductAudit.class)
public abstract class RxProductAudit_ {

	public static volatile SingularAttribute<RxProductAudit, String> ndcId;
	public static volatile SingularAttribute<RxProductAudit, String> changedBy;
	public static volatile SingularAttribute<RxProductAudit, String> action;
	public static volatile SingularAttribute<RxProductAudit, String> drugScheduleTypeCode;
	public static volatile SingularAttribute<RxProductAudit, RxProductAuditKey> key;

}

