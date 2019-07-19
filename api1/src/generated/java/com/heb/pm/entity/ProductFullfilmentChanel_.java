package com.heb.pm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductFullfilmentChanel.class)
public abstract class ProductFullfilmentChanel_ {

	public static volatile SingularAttribute<ProductFullfilmentChanel, LocalDate> effectDate;
	public static volatile SingularAttribute<ProductFullfilmentChanel, FulfillmentChannel> fulfillmentChannel;
	public static volatile SingularAttribute<ProductFullfilmentChanel, String> lastUpdateUserId;
	public static volatile SingularAttribute<ProductFullfilmentChanel, ProductFullfilmentChanelKey> key;
	public static volatile SingularAttribute<ProductFullfilmentChanel, LocalDate> expirationDate;
	public static volatile SingularAttribute<ProductFullfilmentChanel, LocalDateTime> lastUpdateTime;

}

