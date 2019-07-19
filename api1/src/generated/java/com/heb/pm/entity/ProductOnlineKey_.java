package com.heb.pm.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductOnlineKey.class)
public abstract class ProductOnlineKey_ {

	public static volatile SingularAttribute<ProductOnlineKey, Long> productId;
	public static volatile SingularAttribute<ProductOnlineKey, String> saleChannelCode;
	public static volatile SingularAttribute<ProductOnlineKey, LocalDate> effectiveDate;

}

