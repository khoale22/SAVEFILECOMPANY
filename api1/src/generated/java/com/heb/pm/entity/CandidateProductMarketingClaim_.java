package com.heb.pm.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateProductMarketingClaim.class)
public abstract class CandidateProductMarketingClaim_ {

	public static volatile SingularAttribute<CandidateProductMarketingClaim, CandidateProductMaster> candidateProductMaster;
	public static volatile SingularAttribute<CandidateProductMarketingClaim, String> changeReason;
	public static volatile SingularAttribute<CandidateProductMarketingClaim, CandidateProductMarketingClaimKey> key;
	public static volatile SingularAttribute<CandidateProductMarketingClaim, String> newData;
	public static volatile SingularAttribute<CandidateProductMarketingClaim, LocalDate> effectiveDate;
	public static volatile SingularAttribute<CandidateProductMarketingClaim, String> status;
	public static volatile SingularAttribute<CandidateProductMarketingClaim, LocalDate> expirationDate;

}

