package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateStatus.class)
public abstract class CandidateStatus_ {

	public static volatile SingularAttribute<CandidateStatus, Long> statusChangeReason;
	public static volatile SingularAttribute<CandidateStatus, String> updateUserId;
	public static volatile SingularAttribute<CandidateStatus, CandidateWorkRequest> candidateWorkRequest;
	public static volatile SingularAttribute<CandidateStatus, String> commentText;
	public static volatile SingularAttribute<CandidateStatus, CandidateStatusKey> key;

}

