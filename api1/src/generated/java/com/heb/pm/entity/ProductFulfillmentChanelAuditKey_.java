package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductFulfillmentChanelAuditKey.class)
public abstract class ProductFulfillmentChanelAuditKey_ {

	public static volatile SingularAttribute<ProductFulfillmentChanelAuditKey, String> fullfillmentChanelCode;
	public static volatile SingularAttribute<ProductFulfillmentChanelAuditKey, Long> productId;
	public static volatile SingularAttribute<ProductFulfillmentChanelAuditKey, String> salesChanelCode;
	public static volatile SingularAttribute<ProductFulfillmentChanelAuditKey, LocalDateTime> changedOn;

}

