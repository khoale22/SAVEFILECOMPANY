package com.heb.pm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateMasterDataExtensionAttribute.class)
public abstract class CandidateMasterDataExtensionAttribute_ {

	public static volatile SingularAttribute<CandidateMasterDataExtensionAttribute, Integer> attributeCode;
	public static volatile SingularAttribute<CandidateMasterDataExtensionAttribute, Double> attributeValueNumber;
	public static volatile SingularAttribute<CandidateMasterDataExtensionAttribute, String> createUserId;
	public static volatile SingularAttribute<CandidateMasterDataExtensionAttribute, String> attributeValueText;
	public static volatile SingularAttribute<CandidateMasterDataExtensionAttribute, LocalDateTime> attributeValueTime;
	public static volatile SingularAttribute<CandidateMasterDataExtensionAttribute, String> lastUpdateUserId;
	public static volatile SingularAttribute<CandidateMasterDataExtensionAttribute, LocalDateTime> lastUpdateDate;
	public static volatile SingularAttribute<CandidateMasterDataExtensionAttribute, LocalDate> attributeValueDate;
	public static volatile SingularAttribute<CandidateMasterDataExtensionAttribute, CandidateWorkRequest> candidateWorkRequest;
	public static volatile SingularAttribute<CandidateMasterDataExtensionAttribute, CandidateMasterDataExtensionAttributeKey> key;
	public static volatile SingularAttribute<CandidateMasterDataExtensionAttribute, String> newData;
	public static volatile SingularAttribute<CandidateMasterDataExtensionAttribute, LocalDateTime> createDate;

}

