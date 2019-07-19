package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Location.class)
public abstract class Location_ {

	public static volatile SingularAttribute<Location, String> apTypeCode;
	public static volatile SingularAttribute<Location, String> locationName;
	public static volatile SingularAttribute<Location, Integer> apVendorNumber;
	public static volatile ListAttribute<Location, SearchCriteria> searchCriteria;
	public static volatile SingularAttribute<Location, ApLocation> apLocation;
	public static volatile SingularAttribute<Location, String> inactiveSW;
	public static volatile SingularAttribute<Location, LocationKey> key;

}

