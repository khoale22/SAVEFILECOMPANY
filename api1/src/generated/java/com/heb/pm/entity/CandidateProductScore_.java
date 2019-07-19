package com.heb.pm.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateProductScore.class)
public abstract class CandidateProductScore_ {

	public static volatile SingularAttribute<CandidateProductScore, CandidateProductMaster> candidateProductMaster;
	public static volatile SingularAttribute<CandidateProductScore, String> prodScoreTstText;
	public static volatile SingularAttribute<CandidateProductScore, Boolean> newDataSw;
	public static volatile SingularAttribute<CandidateProductScore, BigDecimal> productScoreMaxNumber;
	public static volatile SingularAttribute<CandidateProductScore, LocalDate> productScoreDate;
	public static volatile SingularAttribute<CandidateProductScore, String> wineVintageText;
	public static volatile SingularAttribute<CandidateProductScore, BigDecimal> productScoreNumber;
	public static volatile SingularAttribute<CandidateProductScore, CandidateProductScoreKey> key;
	public static volatile SingularAttribute<CandidateProductScore, String> productScoreText;

}

