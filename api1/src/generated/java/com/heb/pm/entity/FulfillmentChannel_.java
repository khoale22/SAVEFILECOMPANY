package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FulfillmentChannel.class)
public abstract class FulfillmentChannel_ {

	public static volatile SingularAttribute<FulfillmentChannel, String> description;
	public static volatile SingularAttribute<FulfillmentChannel, String> abbreviation;
	public static volatile SingularAttribute<FulfillmentChannel, SalesChannel> salesChannel;
	public static volatile SingularAttribute<FulfillmentChannel, FulfillmentChannelKey> key;

}

