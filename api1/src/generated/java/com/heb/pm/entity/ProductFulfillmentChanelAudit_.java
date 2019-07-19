package com.heb.pm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductFulfillmentChanelAudit.class)
public abstract class ProductFulfillmentChanelAudit_ {

	public static volatile SingularAttribute<ProductFulfillmentChanelAudit, LocalDate> effectDate;
	public static volatile SingularAttribute<ProductFulfillmentChanelAudit, FulfillmentChannel> fulfillmentChannel;
	public static volatile SingularAttribute<ProductFulfillmentChanelAudit, String> changedBy;
	public static volatile SingularAttribute<ProductFulfillmentChanelAudit, String> action;
	public static volatile SingularAttribute<ProductFulfillmentChanelAudit, ProductFulfillmentChanelAuditKey> key;
	public static volatile SingularAttribute<ProductFulfillmentChanelAudit, LocalDate> expirationDate;
	public static volatile SingularAttribute<ProductFulfillmentChanelAudit, LocalDateTime> lastUpdateTime;

}

