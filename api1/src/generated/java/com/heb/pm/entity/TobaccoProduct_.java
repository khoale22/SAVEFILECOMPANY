package com.heb.pm.entity;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TobaccoProduct.class)
public abstract class TobaccoProduct_ {

	public static volatile SingularAttribute<TobaccoProduct, TobaccoProductType> tobaccoProductType;
	public static volatile SingularAttribute<TobaccoProduct, String> tobaccoProductTypeCode;
	public static volatile SingularAttribute<TobaccoProduct, BigDecimal> cigTaxAmt;
	public static volatile SingularAttribute<TobaccoProduct, Long> prodId;
	public static volatile SingularAttribute<TobaccoProduct, Boolean> unstampedSw;
	public static volatile SingularAttribute<TobaccoProduct, GoodsProduct> goodsProduct;

}

