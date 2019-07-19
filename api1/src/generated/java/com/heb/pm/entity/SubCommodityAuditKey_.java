package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SubCommodityAuditKey.class)
public abstract class SubCommodityAuditKey_ {

	public static volatile SingularAttribute<SubCommodityAuditKey, Integer> classCode;
	public static volatile SingularAttribute<SubCommodityAuditKey, Integer> commodityCode;
	public static volatile SingularAttribute<SubCommodityAuditKey, Integer> subCommodityCode;
	public static volatile SingularAttribute<SubCommodityAuditKey, LocalDateTime> changedOn;

}

