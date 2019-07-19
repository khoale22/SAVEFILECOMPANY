package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MarketConsumerEventType.class)
public abstract class MarketConsumerEventType_ {

	public static volatile SingularAttribute<MarketConsumerEventType, String> marketConsumerEventName;
	public static volatile SingularAttribute<MarketConsumerEventType, String> marketConsumerEventCode;
	public static volatile ListAttribute<MarketConsumerEventType, ProductCategory> productCategory;

}

