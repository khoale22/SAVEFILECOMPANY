package com.heb.pm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateWarehouseLocationItem.class)
public abstract class CandidateWarehouseLocationItem_ {

	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Integer> onHoldInventory;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, String> mfgId;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Boolean> dalyHistSwitch;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Boolean> itemRemarkSwitch;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, OrderQuantityType> orderQuantityType;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, LocalDateTime> purchaseStatusUpdateTime;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Boolean> catchWeight;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Double> billCost;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Boolean> dsconTrxSwitch;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Long> whseTier;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, LocalDateTime> createdOn;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Double> unitFactor2;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Boolean> variableWeight;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Double> unitFactor1;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, String> purchasingStatus;
	public static volatile ListAttribute<CandidateWarehouseLocationItem, CandidateItemWarehouseComments> candidateItemWarehouseComments;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, LocalDate> discontinueDate;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, LocalDateTime> supplierStatusUpdateTime;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Integer> offsiteInventory;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, LocalDateTime> lastUpdatedOn;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, PurchasingStatusCode> purchasingStatusCode;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, String> currentSlotNumber;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, String> lastUpdatedId;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, CandidateWarehouseLocationItemKey> key;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Boolean> newData;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, CandidateItemMaster> candidateItemMaster;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Integer> billableInventory;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Boolean> mandFlwThrgSwitch;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, String> discontinuedByUID;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, LocalDate> lastUpdateDt;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Boolean> trnsfrAvailSwitch;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Long> whseTie;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Boolean> mandFlwThruSwitch;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Integer> totalOnHandInventory;
	public static volatile ListAttribute<CandidateWarehouseLocationItem, CandidateItemWarehouseVendor> candidateItemWarehouseVendors;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, String> createdBy;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Boolean> poRcmdSwitch;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, String> flowTypeCode;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, String> orderQuantityTypeCode;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, String> comment;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Location> location;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, String> purchaseStatusUserId;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Double> averageWeight;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, Double> averageCost;
	public static volatile SingularAttribute<CandidateWarehouseLocationItem, FlowType> flowType;

}

