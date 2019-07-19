package com.heb.pm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateProductOnline.class)
public abstract class CandidateProductOnline_ {

	public static volatile SingularAttribute<CandidateProductOnline, CandidateProductMaster> candidateProductMaster;
	public static volatile SingularAttribute<CandidateProductOnline, String> createUserId;
	public static volatile SingularAttribute<CandidateProductOnline, String> showOnSite;
	public static volatile SingularAttribute<CandidateProductOnline, String> lastUpdateUserId;
	public static volatile SingularAttribute<CandidateProductOnline, LocalDateTime> lastUpdateDate;
	public static volatile SingularAttribute<CandidateProductOnline, CandidateWorkRequest> candidateWorkRequest;
	public static volatile SingularAttribute<CandidateProductOnline, CandidateProductOnlineKey> key;
	public static volatile SingularAttribute<CandidateProductOnline, Date> effectiveDate;
	public static volatile SingularAttribute<CandidateProductOnline, LocalDate> expirationDate;
	public static volatile SingularAttribute<CandidateProductOnline, LocalDateTime> createDate;

}

