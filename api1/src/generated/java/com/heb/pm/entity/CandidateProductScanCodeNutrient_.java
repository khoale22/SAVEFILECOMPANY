package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateProductScanCodeNutrient.class)
public abstract class CandidateProductScanCodeNutrient_ {

	public static volatile SingularAttribute<CandidateProductScanCodeNutrient, String> createUserId;
	public static volatile SingularAttribute<CandidateProductScanCodeNutrient, String> actionCode;
	public static volatile SingularAttribute<CandidateProductScanCodeNutrient, CandidateWorkRequest> candidateWorkRequest;
	public static volatile SingularAttribute<CandidateProductScanCodeNutrient, CandidateProductScanCodeNutrientKey> key;
	public static volatile SingularAttribute<CandidateProductScanCodeNutrient, LocalDateTime> createDate;

}

