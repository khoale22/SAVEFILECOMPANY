package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(VendorLocationItemAudit.class)
public abstract class VendorLocationItemAudit_ {

	public static volatile SingularAttribute<VendorLocationItemAudit, Country> country;
	public static volatile SingularAttribute<VendorLocationItemAudit, String> vendorItemId;
	public static volatile SingularAttribute<VendorLocationItemAudit, LocalDateTime> orderProcessingTime;
	public static volatile SingularAttribute<VendorLocationItemAudit, Integer> costOwnerId;
	public static volatile SingularAttribute<VendorLocationItemAudit, Integer> tie;
	public static volatile SingularAttribute<VendorLocationItemAudit, Integer> palletQuantity;
	public static volatile SingularAttribute<VendorLocationItemAudit, Character> palletSize;
	public static volatile SingularAttribute<VendorLocationItemAudit, Integer> tier;
	public static volatile SingularAttribute<VendorLocationItemAudit, String> changedBy;
	public static volatile SingularAttribute<VendorLocationItemAudit, String> action;
	public static volatile SingularAttribute<VendorLocationItemAudit, Integer> costLinkId;
	public static volatile SingularAttribute<VendorLocationItemAudit, Double> cube;
	public static volatile SingularAttribute<VendorLocationItemAudit, Integer> countryOfOriginId;
	public static volatile SingularAttribute<VendorLocationItemAudit, VendorLocationItemAuditKey> key;
	public static volatile SingularAttribute<VendorLocationItemAudit, Double> height;
	public static volatile SingularAttribute<VendorLocationItemAudit, Integer> packQuantity;
	public static volatile SingularAttribute<VendorLocationItemAudit, String> orderQuantityRestrictionCode;
	public static volatile SingularAttribute<VendorLocationItemAudit, String> supplyChainAnalystId;
	public static volatile SingularAttribute<VendorLocationItemAudit, LocalDateTime> orderLeadTime;
	public static volatile SingularAttribute<VendorLocationItemAudit, Integer> distributionContainerId;
	public static volatile SingularAttribute<VendorLocationItemAudit, Double> listCost;
	public static volatile SingularAttribute<VendorLocationItemAudit, Double> length;
	public static volatile SingularAttribute<VendorLocationItemAudit, Double> weight;
	public static volatile SingularAttribute<VendorLocationItemAudit, Sca> supplyChainAnalyst;
	public static volatile SingularAttribute<VendorLocationItemAudit, CostOwner> costOwner;
	public static volatile SingularAttribute<VendorLocationItemAudit, Double> width;
	public static volatile SingularAttribute<VendorLocationItemAudit, Integer> inventorySequenceNumber;
	public static volatile SingularAttribute<VendorLocationItemAudit, Double> nestCube;
	public static volatile SingularAttribute<VendorLocationItemAudit, Integer> nestMax;

}

