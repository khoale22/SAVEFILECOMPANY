package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SubCommodityStateWarningAuditKey.class)
public abstract class SubCommodityStateWarningAuditKey_ {

	public static volatile SingularAttribute<SubCommodityStateWarningAuditKey, String> stateCode;
	public static volatile SingularAttribute<SubCommodityStateWarningAuditKey, String> stateProductWarningCode;
	public static volatile SingularAttribute<SubCommodityStateWarningAuditKey, Integer> subCommodityCode;
	public static volatile SingularAttribute<SubCommodityStateWarningAuditKey, LocalDateTime> changedOn;

}

