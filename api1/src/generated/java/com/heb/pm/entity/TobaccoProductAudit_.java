package com.heb.pm.entity;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TobaccoProductAudit.class)
public abstract class TobaccoProductAudit_ {

	public static volatile SingularAttribute<TobaccoProductAudit, TobaccoProductType> tobaccoProductType;
	public static volatile SingularAttribute<TobaccoProductAudit, String> tobaccoProductTypeCode;
	public static volatile SingularAttribute<TobaccoProductAudit, String> changedBy;
	public static volatile SingularAttribute<TobaccoProductAudit, BigDecimal> cigTaxAmt;
	public static volatile SingularAttribute<TobaccoProductAudit, String> action;
	public static volatile SingularAttribute<TobaccoProductAudit, Boolean> unstampedSw;
	public static volatile SingularAttribute<TobaccoProductAudit, GoodsProduct> goodsProduct;
	public static volatile SingularAttribute<TobaccoProductAudit, TobaccoProductAuditKey> key;

}

