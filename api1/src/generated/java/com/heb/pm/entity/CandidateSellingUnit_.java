package com.heb.pm.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateSellingUnit.class)
public abstract class CandidateSellingUnit_ {

	public static volatile SingularAttribute<CandidateSellingUnit, CandidateProductMaster> candidateProductMaster;
	public static volatile SingularAttribute<CandidateSellingUnit, String> lastUpdatedBy;
	public static volatile SingularAttribute<CandidateSellingUnit, RetailUnitOfMeasure> retailUnitOfMeasure;
	public static volatile SingularAttribute<CandidateSellingUnit, Double> quantity;
	public static volatile SingularAttribute<CandidateSellingUnit, Double> quantity2;
	public static volatile SingularAttribute<CandidateSellingUnit, Double> retailWeight;
	public static volatile SingularAttribute<CandidateSellingUnit, Boolean> bonusScanCode;
	public static volatile SingularAttribute<CandidateSellingUnit, Long> wicApprovedProductListId;
	public static volatile SingularAttribute<CandidateSellingUnit, Long> productSubBrandId;
	public static volatile SingularAttribute<CandidateSellingUnit, Long> upc;
	public static volatile SingularAttribute<CandidateSellingUnit, String> retailUnitOfMeasureCode2;
	public static volatile SingularAttribute<CandidateSellingUnit, Double> retailHeight;
	public static volatile SingularAttribute<CandidateSellingUnit, String> tagSize;
	public static volatile SingularAttribute<CandidateSellingUnit, Double> pseGramWeight;
	public static volatile SingularAttribute<CandidateSellingUnit, Double> retailLength;
	public static volatile SingularAttribute<CandidateSellingUnit, String> retailUnitOfMeasureCode;
	public static volatile SingularAttribute<CandidateSellingUnit, String> scanTypeCode;
	public static volatile SingularAttribute<CandidateSellingUnit, Double> retailWidth;
	public static volatile SingularAttribute<CandidateSellingUnit, Date> lastUpdatedOn;
	public static volatile SingularAttribute<CandidateSellingUnit, Boolean> newDataSwitch;
	public static volatile SingularAttribute<CandidateSellingUnit, Boolean> sampleProvdSwitch;
	public static volatile SingularAttribute<CandidateSellingUnit, CandidateSellingUnitKey> key;
	public static volatile SingularAttribute<CandidateSellingUnit, Boolean> testScanned;

}

