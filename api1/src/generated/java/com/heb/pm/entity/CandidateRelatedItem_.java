package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateRelatedItem.class)
public abstract class CandidateRelatedItem_ {

	public static volatile SingularAttribute<CandidateRelatedItem, Integer> sequenceNumber;
	public static volatile SingularAttribute<CandidateRelatedItem, Integer> quantity;
	public static volatile SingularAttribute<CandidateRelatedItem, String> itemRelationshipType;
	public static volatile SingularAttribute<CandidateRelatedItem, CandidateItemMaster> candidateItemMasterByPsRelatedItmId;
	public static volatile SingularAttribute<CandidateRelatedItem, CandidateRelatedItemKey> key;
	public static volatile SingularAttribute<CandidateRelatedItem, CandidateItemMaster> candidateItemMasterByPsItmId;

}

