package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateNutrient.class)
public abstract class CandidateNutrient_ {

	public static volatile SingularAttribute<CandidateNutrient, Integer> masterId;
	public static volatile SingularAttribute<CandidateNutrient, String> uomCode;
	public static volatile SingularAttribute<CandidateNutrient, NutrientMaster> nutrientMaster;
	public static volatile SingularAttribute<CandidateNutrient, Integer> sourceSystem;
	public static volatile SingularAttribute<CandidateNutrient, ServingSizeUOM> servingSizeUOM;
	public static volatile SingularAttribute<CandidateNutrient, String> dclrOnLblSw;
	public static volatile SingularAttribute<CandidateNutrient, String> ntrntMeasrTxt;
	public static volatile SingularAttribute<CandidateNutrient, Integer> valPreprdTypCd;
	public static volatile SingularAttribute<CandidateNutrient, Double> nutrientQuantity;
	public static volatile SingularAttribute<CandidateNutrient, Double> dalyValSrvngPct;
	public static volatile SingularAttribute<CandidateNutrient, CandidateWorkRequest> candidateWorkRequest;
	public static volatile SingularAttribute<CandidateNutrient, CandidateNutrientKey> key;

}

