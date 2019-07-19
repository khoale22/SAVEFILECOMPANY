package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateStatusKey.class)
public abstract class CandidateStatusKey_ {

	public static volatile SingularAttribute<CandidateStatusKey, LocalDateTime> lastUpdateDate;
	public static volatile SingularAttribute<CandidateStatusKey, Long> workRequestId;
	public static volatile SingularAttribute<CandidateStatusKey, String> status;

}

