package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(WarehouseLocationItemAudit.class)
public abstract class WarehouseLocationItemAudit_ {

	public static volatile SingularAttribute<WarehouseLocationItemAudit, Integer> onHoldInventory;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Double> shipWeight;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Integer> distributionReserveInventory;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, LocalDateTime> purchaseStatusUpdateTime;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Boolean> catchWeight;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Double> billCost;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Long> whseTier;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, LocalDateTime> createdOn;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Double> unitFactor2;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Boolean> variableWeight;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Double> unitFactor1;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, String> purchasingStatus;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Double> shipLength;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, LocalDateTime> discontinueDate;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, LocalDateTime> supplierStatusUpdateTime;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, String> changedBy;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, String> action;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Integer> offsiteInventory;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Long> shipPack;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, LocalDateTime> lastUpdatedOn;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, WarehouseLocationItemAuditKey> key;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Integer> billableInventory;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, String> supplierStatusCode;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, String> discontinuedByUID;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Double> shipWidth;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Long> whseTie;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Integer> totalOnHandInventory;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, String> createdBy;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Long> whseMaxShipQuantityNumber;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Double> shipCube;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Long> expectedWeeklyMovement;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, String> flowTypeCode;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, String> orderQuantityTypeCode;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, String> comment;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, String> purchaseStatusUserId;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Double> averageWeight;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Double> averageCost;
	public static volatile SingularAttribute<WarehouseLocationItemAudit, Double> shipHeight;

}

