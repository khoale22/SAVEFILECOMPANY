package com.heb.pm.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SubCommodityAudit.class)
public abstract class SubCommodityAudit_ {

	public static volatile SingularAttribute<SubCommodityAudit, String> changedBy;
	public static volatile SingularAttribute<SubCommodityAudit, String> taxEligible;
	public static volatile SingularAttribute<SubCommodityAudit, String> action;
	public static volatile SingularAttribute<SubCommodityAudit, LocalDate> lstUpdtTs;
	public static volatile SingularAttribute<SubCommodityAudit, String> taxCategoryCode;
	public static volatile SingularAttribute<SubCommodityAudit, String> foodStampEligible;
	public static volatile SingularAttribute<SubCommodityAudit, String> nonTaxCategoryCode;
	public static volatile SingularAttribute<SubCommodityAudit, SubCommodityAuditKey> key;

}

