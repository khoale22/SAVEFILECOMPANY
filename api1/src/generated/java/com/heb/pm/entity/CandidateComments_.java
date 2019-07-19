package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateComments.class)
public abstract class CandidateComments_ {

	public static volatile SingularAttribute<CandidateComments, String> comments;
	public static volatile SingularAttribute<CandidateComments, Boolean> vendorVisibility;
	public static volatile SingularAttribute<CandidateComments, LocalDateTime> time;
	public static volatile SingularAttribute<CandidateComments, String> userId;
	public static volatile SingularAttribute<CandidateComments, CandidateWorkRequest> candidateWorkRequest;
	public static volatile SingularAttribute<CandidateComments, CandidateCommentsKey> key;

}

