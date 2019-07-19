package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateDescription.class)
public abstract class CandidateDescription_ {

	public static volatile SingularAttribute<CandidateDescription, CandidateProductMaster> candidateProductMaster;
	public static volatile SingularAttribute<CandidateDescription, DescriptionType> descriptionType;
	public static volatile SingularAttribute<CandidateDescription, String> lastUpdateUserId;
	public static volatile SingularAttribute<CandidateDescription, LocalDateTime> lastUpdateDate;
	public static volatile SingularAttribute<CandidateDescription, String> description;
	public static volatile SingularAttribute<CandidateDescription, CandidateDescriptionKey> key;

}

