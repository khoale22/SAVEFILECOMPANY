package com.heb.pm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateGenericEntityRelationship.class)
public abstract class CandidateGenericEntityRelationship_ {

	public static volatile SingularAttribute<CandidateGenericEntityRelationship, String> activeSwitch;
	public static volatile SingularAttribute<CandidateGenericEntityRelationship, LocalDateTime> lastUpdatedTimestamp;
	public static volatile SingularAttribute<CandidateGenericEntityRelationship, String> createUserId;
	public static volatile SingularAttribute<CandidateGenericEntityRelationship, Boolean> display;
	public static volatile SingularAttribute<CandidateGenericEntityRelationship, Boolean> defaultParent;
	public static volatile SingularAttribute<CandidateGenericEntityRelationship, Long> sequence;
	public static volatile SingularAttribute<CandidateGenericEntityRelationship, String> lastUpdatedUserId;
	public static volatile SingularAttribute<CandidateGenericEntityRelationship, String> newDataSwitch;
	public static volatile SingularAttribute<CandidateGenericEntityRelationship, CandidateWorkRequest> candidateWorkRequest;
	public static volatile SingularAttribute<CandidateGenericEntityRelationship, CandidateGenericEntityRelationshipKey> key;
	public static volatile SingularAttribute<CandidateGenericEntityRelationship, LocalDate> effectiveDate;
	public static volatile SingularAttribute<CandidateGenericEntityRelationship, LocalDateTime> createDate;
	public static volatile SingularAttribute<CandidateGenericEntityRelationship, LocalDate> expirationDate;

}

