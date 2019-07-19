package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateNutrientPanelHeader.class)
public abstract class CandidateNutrientPanelHeader_ {

	public static volatile SingularAttribute<CandidateNutrientPanelHeader, Boolean> approved;
	public static volatile SingularAttribute<CandidateNutrientPanelHeader, String> xtrnlId;
	public static volatile SingularAttribute<CandidateNutrientPanelHeader, String> servingsPerContainerText;
	public static volatile SingularAttribute<CandidateNutrientPanelHeader, String> approvedBy;
	public static volatile SingularAttribute<CandidateNutrientPanelHeader, LocalDateTime> approvedTime;
	public static volatile SingularAttribute<CandidateNutrientPanelHeader, LocalDateTime> sourceTime;
	public static volatile SingularAttribute<CandidateNutrientPanelHeader, String> servingSizeText;
	public static volatile SingularAttribute<CandidateNutrientPanelHeader, CandidateWorkRequest> candidateWorkRequest;
	public static volatile SingularAttribute<CandidateNutrientPanelHeader, CandidateNutrientPanelHeaderKey> key;

}

