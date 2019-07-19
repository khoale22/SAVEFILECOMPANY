package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MapPrice.class)
public abstract class MapPrice_ {

	public static volatile SingularAttribute<MapPrice, LocalDateTime> expirationTime;
	public static volatile SingularAttribute<MapPrice, Double> mapAmount;
	public static volatile SingularAttribute<MapPrice, MapPriceKey> key;

}

