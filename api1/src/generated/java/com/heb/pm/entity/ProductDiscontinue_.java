package com.heb.pm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductDiscontinue.class)
public abstract class ProductDiscontinue_ {

	public static volatile SingularAttribute<ProductDiscontinue, Boolean> vendorInventory;
	public static volatile SingularAttribute<ProductDiscontinue, ItemMaster> itemMaster;
	public static volatile SingularAttribute<ProductDiscontinue, Boolean> salesSet;
	public static volatile SingularAttribute<ProductDiscontinue, Boolean> openPoSet;
	public static volatile SingularAttribute<ProductDiscontinue, LocalDate> lastReceivedDate;
	public static volatile SingularAttribute<ProductDiscontinue, Boolean> warehouseInventorySet;
	public static volatile SingularAttribute<ProductDiscontinue, String> lastUpdateId;
	public static volatile SingularAttribute<ProductDiscontinue, Boolean> autoDiscontinued;
	public static volatile SingularAttribute<ProductDiscontinue, LocalDateTime> createTime;
	public static volatile SingularAttribute<ProductDiscontinue, String> createId;
	public static volatile SingularAttribute<ProductDiscontinue, LocalDate> lastBillDate;
	public static volatile SingularAttribute<ProductDiscontinue, ProductMaster> productMaster;
	public static volatile SingularAttribute<ProductDiscontinue, Boolean> newItemSet;
	public static volatile SingularAttribute<ProductDiscontinue, Boolean> storeReceiptsSet;
	public static volatile SingularAttribute<ProductDiscontinue, ProductDiscontinueKey> key;
	public static volatile SingularAttribute<ProductDiscontinue, Boolean> inventorySet;
	public static volatile SingularAttribute<ProductDiscontinue, SellingUnit> sellingUnit;
	public static volatile SingularAttribute<ProductDiscontinue, LocalDateTime> lastUpdateTime;

}

