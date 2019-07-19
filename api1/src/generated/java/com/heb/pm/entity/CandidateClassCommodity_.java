package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateClassCommodity.class)
public abstract class CandidateClassCommodity_ {

	public static volatile SingularAttribute<CandidateClassCommodity, String> createUserId;
	public static volatile SingularAttribute<CandidateClassCommodity, String> ebmId;
	public static volatile SingularAttribute<CandidateClassCommodity, LocalDateTime> lastUpdateDate;
	public static volatile SingularAttribute<CandidateClassCommodity, String> lastUpdateUserId;
	public static volatile SingularAttribute<CandidateClassCommodity, String> bdaId;
	public static volatile SingularAttribute<CandidateClassCommodity, CandidateWorkRequest> candidateWorkRequest;
	public static volatile SingularAttribute<CandidateClassCommodity, CandidateClassCommodityKey> key;
	public static volatile SingularAttribute<CandidateClassCommodity, LocalDateTime> createDate;

}

