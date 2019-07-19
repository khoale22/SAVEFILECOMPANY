package com.heb.pm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateVendorItemStore.class)
public abstract class CandidateVendorItemStore_ {

	public static volatile SingularAttribute<CandidateVendorItemStore, LocalDate> unAuthorizeDate;
	public static volatile SingularAttribute<CandidateVendorItemStore, CandidateVendorLocationItem> candidateVendorLocationItem;
	public static volatile SingularAttribute<CandidateVendorItemStore, String> lastUpdateUserId;
	public static volatile SingularAttribute<CandidateVendorItemStore, Boolean> authorized;
	public static volatile SingularAttribute<CandidateVendorItemStore, LocalDateTime> lastUpdate;
	public static volatile SingularAttribute<CandidateVendorItemStore, Integer> invSeqNbr;
	public static volatile SingularAttribute<CandidateVendorItemStore, Integer> costGroupId;
	public static volatile SingularAttribute<CandidateVendorItemStore, Boolean> omitStrSWitch;
	public static volatile SingularAttribute<CandidateVendorItemStore, LocalDate> authorizeDate;
	public static volatile SingularAttribute<CandidateVendorItemStore, Double> dirCstAmt;
	public static volatile SingularAttribute<CandidateVendorItemStore, CandidateVendorItemStoreKey> key;

}

