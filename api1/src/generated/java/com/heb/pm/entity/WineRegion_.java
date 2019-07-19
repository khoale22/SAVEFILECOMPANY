package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(WineRegion.class)
public abstract class WineRegion_ {

	public static volatile SingularAttribute<WineRegion, String> wineRegionDescription;
	public static volatile SingularAttribute<WineRegion, String> wineRegionName;
	public static volatile SingularAttribute<WineRegion, WineArea> wineArea;
	public static volatile SingularAttribute<WineRegion, Integer> wineRegionId;

}

