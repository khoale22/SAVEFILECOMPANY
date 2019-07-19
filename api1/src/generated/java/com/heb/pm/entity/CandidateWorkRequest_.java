package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateWorkRequest.class)
public abstract class CandidateWorkRequest_ {

	public static volatile SingularAttribute<CandidateWorkRequest, Long> statusChangeReason;
	public static volatile ListAttribute<CandidateWorkRequest, CandidateProductMaster> candidateProductMaster;
	public static volatile SingularAttribute<CandidateWorkRequest, LocalDateTime> delegatedTime;
	public static volatile SingularAttribute<CandidateWorkRequest, Integer> sourceSystem;
	public static volatile SingularAttribute<CandidateWorkRequest, LocalDateTime> lastUpdateDate;
	public static volatile ListAttribute<CandidateWorkRequest, CandidateClassCommodity> candidateClassCommodities;
	public static volatile ListAttribute<CandidateWorkRequest, EffectiveDatedMaintenance> effectiveDatedMaintenances;
	public static volatile ListAttribute<CandidateWorkRequest, CandidateMasterDataExtensionAttribute> candidateMasterDataExtensionAttributes;
	public static volatile ListAttribute<CandidateWorkRequest, CandidateGenericEntityRelationship> candidateGenericEntityRelationships;
	public static volatile ListAttribute<CandidateWorkRequest, CandidateProductPkVariation> candidateProductPkVariations;
	public static volatile SingularAttribute<CandidateWorkRequest, Boolean> readyToActivate;
	public static volatile SingularAttribute<CandidateWorkRequest, String> delegatedByUserId;
	public static volatile SingularAttribute<CandidateWorkRequest, ProductMaster> productMaster;
	public static volatile SingularAttribute<CandidateWorkRequest, String> itemKeyTypeCode;
	public static volatile ListAttribute<CandidateWorkRequest, CandidateNutrientPanelHeader> candidateNutrientPanelHeaders;
	public static volatile ListAttribute<CandidateWorkRequest, CandidateProductOnline> candidateProductOnlines;
	public static volatile ListAttribute<CandidateWorkRequest, CandidateItemMaster> candidateItemMasters;
	public static volatile SingularAttribute<CandidateWorkRequest, LocalDateTime> createDate;
	public static volatile SingularAttribute<CandidateWorkRequest, Long> trackingId;
	public static volatile SingularAttribute<CandidateWorkRequest, Long> productId;
	public static volatile SingularAttribute<CandidateWorkRequest, String> lastUpdateUserId;
	public static volatile ListAttribute<CandidateWorkRequest, CandidateStatus> candidateStatuses;
	public static volatile SingularAttribute<CandidateWorkRequest, Long> upc;
	public static volatile SingularAttribute<CandidateWorkRequest, Integer> intent;
	public static volatile SingularAttribute<CandidateWorkRequest, String> userId;
	public static volatile SingularAttribute<CandidateWorkRequest, TransactionTracker> transactionTracking;
	public static volatile ListAttribute<CandidateWorkRequest, CandidateProductScanCodeNutrient> candidateProductScanCodeNutrient;
	public static volatile SingularAttribute<CandidateWorkRequest, Long> itemId;
	public static volatile ListAttribute<CandidateWorkRequest, CandidateComments> candidateComments;
	public static volatile ListAttribute<CandidateWorkRequest, CandidateDistributionFilter> candidateDistributionFilters;
	public static volatile ListAttribute<CandidateWorkRequest, CandidateFulfillmentChannel> candidateFulfillmentChannels;
	public static volatile ListAttribute<CandidateWorkRequest, CandidateNutrient> candidateNutrients;
	public static volatile SingularAttribute<CandidateWorkRequest, Long> workRequestId;
	public static volatile SingularAttribute<CandidateWorkRequest, String> status;

}

