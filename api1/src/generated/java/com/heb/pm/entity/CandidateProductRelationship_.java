package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateProductRelationship.class)
public abstract class CandidateProductRelationship_ {

	public static volatile SingularAttribute<CandidateProductRelationship, CandidateProductMaster> candidateProductMaster;
	public static volatile SingularAttribute<CandidateProductRelationship, String> lstUpdtUsrId;
	public static volatile SingularAttribute<CandidateProductRelationship, Double> quantity;
	public static volatile SingularAttribute<CandidateProductRelationship, Boolean> relatedProductCandidateSwitch;
	public static volatile SingularAttribute<CandidateProductRelationship, LocalDateTime> lastUpdateTs;
	public static volatile SingularAttribute<CandidateProductRelationship, CandidateProductMaster> candidateRelatedProductId;
	public static volatile SingularAttribute<CandidateProductRelationship, Long> productScanCode;
	public static volatile SingularAttribute<CandidateProductRelationship, CandidateProductRelationshipKey> key;

}

