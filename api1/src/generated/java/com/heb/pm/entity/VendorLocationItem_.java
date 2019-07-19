package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(VendorLocationItem.class)
public abstract class VendorLocationItem_ {

	public static volatile SingularAttribute<VendorLocationItem, Country> country;
	public static volatile SingularAttribute<VendorLocationItem, VendorLocationItemMaster> vendorLocationItemMaster;
	public static volatile SingularAttribute<VendorLocationItem, Integer> costOwnerId;
	public static volatile SingularAttribute<VendorLocationItem, String> scaCode;
	public static volatile SingularAttribute<VendorLocationItem, VendorLocation> vendorLocation;
	public static volatile SingularAttribute<VendorLocationItem, Sca> sca;
	public static volatile SingularAttribute<VendorLocationItem, Integer> tie;
	public static volatile SingularAttribute<VendorLocationItem, String> lstUpdtUid;
	public static volatile SingularAttribute<VendorLocationItem, Integer> palletQuantity;
	public static volatile SingularAttribute<VendorLocationItem, String> palletSize;
	public static volatile SingularAttribute<VendorLocationItem, Integer> tier;
	public static volatile SingularAttribute<VendorLocationItem, Integer> costLinkId;
	public static volatile SingularAttribute<VendorLocationItem, Double> cube;
	public static volatile SingularAttribute<VendorLocationItem, Integer> countryOfOriginId;
	public static volatile SingularAttribute<VendorLocationItem, VendorLocationItemKey> key;
	public static volatile SingularAttribute<VendorLocationItem, Double> height;
	public static volatile SingularAttribute<VendorLocationItem, Integer> packQuantity;
	public static volatile SingularAttribute<VendorLocationItem, String> orderQuantityRestrictionCode;
	public static volatile SingularAttribute<VendorLocationItem, ItemMaster> itemMaster;
	public static volatile SingularAttribute<VendorLocationItem, Double> listCost;
	public static volatile SingularAttribute<VendorLocationItem, Double> length;
	public static volatile SingularAttribute<VendorLocationItem, Double> weight;
	public static volatile ListAttribute<VendorLocationItem, ItemWarehouseVendor> itemWarehouseVendorList;
	public static volatile SingularAttribute<VendorLocationItem, CostOwner> costOwner;
	public static volatile SingularAttribute<VendorLocationItem, DsdLocation> dsdLocation;
	public static volatile SingularAttribute<VendorLocationItem, Double> width;
	public static volatile SingularAttribute<VendorLocationItem, Location> location;
	public static volatile SingularAttribute<VendorLocationItem, Double> nestCube;
	public static volatile ListAttribute<VendorLocationItem, VendorItemStore> vendorItemStores;
	public static volatile SingularAttribute<VendorLocationItem, Integer> nestMax;
	public static volatile SingularAttribute<VendorLocationItem, String> vendItemId;

}

