package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(WarehouseLocationItem.class)
public abstract class WarehouseLocationItem_ {

	public static volatile SingularAttribute<WarehouseLocationItem, Integer> onHoldInventory;
	public static volatile SingularAttribute<WarehouseLocationItem, Double> shipWeight;
	public static volatile SingularAttribute<WarehouseLocationItem, Integer> distributionReserveInventory;
	public static volatile SingularAttribute<WarehouseLocationItem, OrderQuantityType> orderQuantityType;
	public static volatile SingularAttribute<WarehouseLocationItem, LocalDateTime> purchaseStatusUpdateTime;
	public static volatile SingularAttribute<WarehouseLocationItem, Boolean> catchWeight;
	public static volatile SingularAttribute<WarehouseLocationItem, Double> billCost;
	public static volatile SingularAttribute<WarehouseLocationItem, String> splrItemStatusCode;
	public static volatile SingularAttribute<WarehouseLocationItem, Long> whseTier;
	public static volatile SingularAttribute<WarehouseLocationItem, LocalDateTime> createdOn;
	public static volatile SingularAttribute<WarehouseLocationItem, Double> unitFactor2;
	public static volatile SingularAttribute<WarehouseLocationItem, Boolean> variableWeight;
	public static volatile SingularAttribute<WarehouseLocationItem, Double> unitFactor1;
	public static volatile SingularAttribute<WarehouseLocationItem, String> purchasingStatus;
	public static volatile SingularAttribute<WarehouseLocationItem, Double> shipLength;
	public static volatile SingularAttribute<WarehouseLocationItem, LocalDateTime> discontinueDate;
	public static volatile SingularAttribute<WarehouseLocationItem, LocalDateTime> supplierStatusUpdateTime;
	public static volatile SingularAttribute<WarehouseLocationItem, Integer> offsiteInventory;
	public static volatile SingularAttribute<WarehouseLocationItem, Long> shipPack;
	public static volatile SingularAttribute<WarehouseLocationItem, LocalDateTime> lastUpdatedOn;
	public static volatile SingularAttribute<WarehouseLocationItem, PurchasingStatusCode> purchasingStatusCode;
	public static volatile SingularAttribute<WarehouseLocationItem, String> currentSlotNumber;
	public static volatile SingularAttribute<WarehouseLocationItem, String> lastUpdatedId;
	public static volatile SingularAttribute<WarehouseLocationItem, WarehouseLocationItemKey> key;
	public static volatile ListAttribute<WarehouseLocationItem, ItemWarehouseComments> itemWarehouseCommentsList;
	public static volatile SingularAttribute<WarehouseLocationItem, Integer> billableInventory;
	public static volatile SingularAttribute<WarehouseLocationItem, String> supplierStatusCode;
	public static volatile SingularAttribute<WarehouseLocationItem, ItemMaster> itemMaster;
	public static volatile SingularAttribute<WarehouseLocationItem, String> discontinuedByUID;
	public static volatile SingularAttribute<WarehouseLocationItem, Double> shipWidth;
	public static volatile SingularAttribute<WarehouseLocationItem, WarehouseLocationItemExtendedAttributes> warehouseLocationItemExtendedAttributes;
	public static volatile SingularAttribute<WarehouseLocationItem, Long> whseTie;
	public static volatile ListAttribute<WarehouseLocationItem, ItemWarehouseVendor> itemWarehouseVendorList;
	public static volatile SingularAttribute<WarehouseLocationItem, Integer> totalOnHandInventory;
	public static volatile SingularAttribute<WarehouseLocationItem, String> createdBy;
	public static volatile SingularAttribute<WarehouseLocationItem, Long> whseMaxShipQuantityNumber;
	public static volatile SingularAttribute<WarehouseLocationItem, Double> shipCube;
	public static volatile SingularAttribute<WarehouseLocationItem, Long> expectedWeeklyMovement;
	public static volatile SingularAttribute<WarehouseLocationItem, String> flowTypeCode;
	public static volatile SingularAttribute<WarehouseLocationItem, String> orderQuantityTypeCode;
	public static volatile SingularAttribute<WarehouseLocationItem, String> comment;
	public static volatile SingularAttribute<WarehouseLocationItem, Location> location;
	public static volatile SingularAttribute<WarehouseLocationItem, String> purchaseStatusUserId;
	public static volatile SingularAttribute<WarehouseLocationItem, Double> averageWeight;
	public static volatile SingularAttribute<WarehouseLocationItem, Double> averageCost;
	public static volatile SingularAttribute<WarehouseLocationItem, Double> shipHeight;
	public static volatile SingularAttribute<WarehouseLocationItem, FlowType> flowType;

}

