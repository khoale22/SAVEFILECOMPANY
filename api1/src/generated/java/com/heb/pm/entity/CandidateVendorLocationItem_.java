package com.heb.pm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateVendorLocationItem.class)
public abstract class CandidateVendorLocationItem_ {

	public static volatile SingularAttribute<CandidateVendorLocationItem, Country> country;
	public static volatile SingularAttribute<CandidateVendorLocationItem, String> minTypeText;
	public static volatile SingularAttribute<CandidateVendorLocationItem, String> dutyInfoText;
	public static volatile SingularAttribute<CandidateVendorLocationItem, VendorLocation> vendorLocation;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Long> hts3Number;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Sca> sca;
	public static volatile SingularAttribute<CandidateVendorLocationItem, String> containerSizeCode;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Integer> tie;
	public static volatile SingularAttribute<CandidateVendorLocationItem, String> deptNbr;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Integer> palletQuantity;
	public static volatile SingularAttribute<CandidateVendorLocationItem, String> sampProvideSwitch;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Boolean> authdSwitch;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Double> dutyPercent;
	public static volatile SingularAttribute<CandidateVendorLocationItem, String> season;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Integer> countryOfOriginId;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Boolean> newData;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Long> seasonalityYear;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Boolean> guarntSaleSwitch;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Double> listCost;
	public static volatile SingularAttribute<CandidateVendorLocationItem, LocalDate> inStoreDate;
	public static volatile SingularAttribute<CandidateVendorLocationItem, String> cartonMarking;
	public static volatile SingularAttribute<CandidateVendorLocationItem, String> dutyConfirmationText;
	public static volatile SingularAttribute<CandidateVendorLocationItem, DsdLocation> dsdLocation;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Long> top2topId;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Integer> sellByYear;
	public static volatile SingularAttribute<CandidateVendorLocationItem, String> subDeptId;
	public static volatile SingularAttribute<CandidateVendorLocationItem, LocalDate> prorationDate;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Long> minOrderQuantity;
	public static volatile SingularAttribute<CandidateVendorLocationItem, String> vendItemId;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Boolean> importSwitch;
	public static volatile SingularAttribute<CandidateVendorLocationItem, String> pickupPoint;
	public static volatile SingularAttribute<CandidateVendorLocationItem, String> color;
	public static volatile SingularAttribute<CandidateVendorLocationItem, String> freightConfirmationText;
	public static volatile SingularAttribute<CandidateVendorLocationItem, LocalDate> warehouseFlushDate;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Integer> costOwnerId;
	public static volatile SingularAttribute<CandidateVendorLocationItem, String> scaCode;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Long> sesnlyId;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Double> agentCommissionPercent;
	public static volatile SingularAttribute<CandidateVendorLocationItem, String> palletSize;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Integer> tier;
	public static volatile ListAttribute<CandidateVendorLocationItem, CandidateVendorItemStore> candidateVendorItemStores;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Integer> costLinkId;
	public static volatile SingularAttribute<CandidateVendorLocationItem, CandidateVendorLocationItemKey> key;
	public static volatile SingularAttribute<CandidateVendorLocationItem, CandidateItemMaster> candidateItemMaster;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Long> hts2Number;
	public static volatile SingularAttribute<CandidateVendorLocationItem, String> orderQuantityRestrictionCode;
	public static volatile SingularAttribute<CandidateVendorLocationItem, String> lastUpdateUserId;
	public static volatile ListAttribute<CandidateVendorLocationItem, CandidateVendorItemFactory> candidateVendorItemFactorys;
	public static volatile SingularAttribute<CandidateVendorLocationItem, CostOwner> costOwner;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Long> htsNumber;
	public static volatile ListAttribute<CandidateVendorLocationItem, CandidateItemWarehouseVendor> candidateItemWarehouseVendors;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Long> expctWklyMvt;
	public static volatile SingularAttribute<CandidateVendorLocationItem, LocalDateTime> lastUpdatedate;
	public static volatile SingularAttribute<CandidateVendorLocationItem, Location> location;
	public static volatile SingularAttribute<CandidateVendorLocationItem, String> incoTermCode;

}

