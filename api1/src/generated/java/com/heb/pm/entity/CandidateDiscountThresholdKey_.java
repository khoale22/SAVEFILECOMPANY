package com.heb.pm.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateDiscountThresholdKey.class)
public abstract class CandidateDiscountThresholdKey_ {

	public static volatile SingularAttribute<CandidateDiscountThresholdKey, Long> minDiscountThresholdQuantity;
	public static volatile SingularAttribute<CandidateDiscountThresholdKey, Long> candidateProductId;
	public static volatile SingularAttribute<CandidateDiscountThresholdKey, LocalDate> effectiveDate;

}

