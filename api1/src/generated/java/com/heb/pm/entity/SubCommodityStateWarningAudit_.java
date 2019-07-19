package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SubCommodityStateWarningAudit.class)
public abstract class SubCommodityStateWarningAudit_ {

	public static volatile SingularAttribute<SubCommodityStateWarningAudit, String> changedBy;
	public static volatile SingularAttribute<SubCommodityStateWarningAudit, String> action;
	public static volatile SingularAttribute<SubCommodityStateWarningAudit, ProductStateWarning> productStateWarning;
	public static volatile SingularAttribute<SubCommodityStateWarningAudit, SubCommodityStateWarningAuditKey> key;

}

