package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateDiscountThreshold.class)
public abstract class CandidateDiscountThreshold_ {

	public static volatile SingularAttribute<CandidateDiscountThreshold, CandidateProductMaster> candidateProductMaster;
	public static volatile SingularAttribute<CandidateDiscountThreshold, Double> thresholdDiscountAmount;
	public static volatile SingularAttribute<CandidateDiscountThreshold, String> thresholdDiscountType;
	public static volatile SingularAttribute<CandidateDiscountThreshold, CandidateDiscountThresholdKey> key;

}

