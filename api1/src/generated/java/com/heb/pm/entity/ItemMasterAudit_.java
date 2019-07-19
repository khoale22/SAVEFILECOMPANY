package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ItemMasterAudit.class)
public abstract class ItemMasterAudit_ {

	public static volatile SingularAttribute<ItemMasterAudit, ItemType> itemType;
	public static volatile SingularAttribute<ItemMasterAudit, String> subDepartmentId2;
	public static volatile SingularAttribute<ItemMasterAudit, String> subDepartmentId1;
	public static volatile SingularAttribute<ItemMasterAudit, String> subDepartmentId4;
	public static volatile SingularAttribute<ItemMasterAudit, String> subDepartmentId3;
	public static volatile SingularAttribute<ItemMasterAudit, String> departmentId2;
	public static volatile SingularAttribute<ItemMasterAudit, String> departmentId1;
	public static volatile SingularAttribute<ItemMasterAudit, String> oneTouchTypeId;
	public static volatile SingularAttribute<ItemMasterAudit, String> departmentId4;
	public static volatile SingularAttribute<ItemMasterAudit, Boolean> alwaysSubWhenOut;
	public static volatile SingularAttribute<ItemMasterAudit, Long> caseUpc;
	public static volatile SingularAttribute<ItemMasterAudit, String> departmentId3;
	public static volatile SingularAttribute<ItemMasterAudit, LocalDateTime> discontinueDate;
	public static volatile SingularAttribute<ItemMasterAudit, Long> orderingUpc;
	public static volatile SingularAttribute<ItemMasterAudit, String> changedBy;
	public static volatile SingularAttribute<ItemMasterAudit, Boolean> displayReadyUnit;
	public static volatile SingularAttribute<ItemMasterAudit, String> action;
	public static volatile SingularAttribute<ItemMasterAudit, String> itemDescription;
	public static volatile SingularAttribute<ItemMasterAudit, String> itemTypeId;
	public static volatile SingularAttribute<ItemMasterAudit, ItemMasterAuditKey> key;
	public static volatile SingularAttribute<ItemMasterAudit, Long> rowsFacing;
	public static volatile SingularAttribute<ItemMasterAudit, Long> orientation;
	public static volatile SingularAttribute<ItemMasterAudit, DiscontinueReason> discontinueReason;
	public static volatile SingularAttribute<ItemMasterAudit, String> pssDepartment3;
	public static volatile SingularAttribute<ItemMasterAudit, String> pssDepartment2;
	public static volatile SingularAttribute<ItemMasterAudit, String> pssDepartment4;
	public static volatile SingularAttribute<ItemMasterAudit, String> departmentMerchandise1;
	public static volatile SingularAttribute<ItemMasterAudit, String> departmentMerchandise2;
	public static volatile SingularAttribute<ItemMasterAudit, String> departmentMerchandise3;
	public static volatile SingularAttribute<ItemMasterAudit, String> departmentMerchandise4;
	public static volatile SingularAttribute<ItemMasterAudit, String> discontinueReasonId;
	public static volatile SingularAttribute<ItemMasterAudit, OneTouchType> oneTouchType;
	public static volatile SingularAttribute<ItemMasterAudit, String> pssDepartment1;
	public static volatile SingularAttribute<ItemMasterAudit, String> typeOfDRU;
	public static volatile SingularAttribute<ItemMasterAudit, Long> rowsDeep;
	public static volatile SingularAttribute<ItemMasterAudit, Long> rowsHigh;
	public static volatile SingularAttribute<ItemMasterAudit, String> discontinueUserId;

}

