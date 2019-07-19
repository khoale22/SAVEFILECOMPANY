package com.heb.pm.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateVendorItemFactory.class)
public abstract class CandidateVendorItemFactory_ {

	public static volatile SingularAttribute<CandidateVendorItemFactory, Factory> factory;
	public static volatile SingularAttribute<CandidateVendorItemFactory, CandidateVendorLocationItem> candidateVendorLocationItem;
	public static volatile SingularAttribute<CandidateVendorItemFactory, String> lastUpdateUserId;
	public static volatile SingularAttribute<CandidateVendorItemFactory, LocalDate> lastUpdateTs;
	public static volatile SingularAttribute<CandidateVendorItemFactory, CandidateVendorItemFactoryKey> key;

}

