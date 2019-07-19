package com.heb.pm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ImportItem.class)
public abstract class ImportItem_ {

	public static volatile SingularAttribute<ImportItem, String> pickupPoint;
	public static volatile SingularAttribute<ImportItem, String> color;
	public static volatile SingularAttribute<ImportItem, LocalDate> warehouseFlushDate;
	public static volatile SingularAttribute<ImportItem, LocalDateTime> importUpdtTs;
	public static volatile SingularAttribute<ImportItem, String> containerSizeCode;
	public static volatile SingularAttribute<ImportItem, Double> agentCommissionPercent;
	public static volatile SingularAttribute<ImportItem, String> importUpdtUsr;
	public static volatile SingularAttribute<ImportItem, Double> dutyPercent;
	public static volatile SingularAttribute<ImportItem, String> season;
	public static volatile SingularAttribute<ImportItem, Long> hts2;
	public static volatile SingularAttribute<ImportItem, Long> hts1;
	public static volatile SingularAttribute<ImportItem, Long> hts3;
	public static volatile SingularAttribute<ImportItem, ImportItemKey> key;
	public static volatile SingularAttribute<ImportItem, String> dutyInformation;
	public static volatile SingularAttribute<ImportItem, String> minOrderDescription;
	public static volatile SingularAttribute<ImportItem, LocalDate> inStoreDate;
	public static volatile SingularAttribute<ImportItem, String> cartonMarking;
	public static volatile SingularAttribute<ImportItem, Integer> sellByYear;
	public static volatile SingularAttribute<ImportItem, LocalDate> prorationDate;
	public static volatile SingularAttribute<ImportItem, Location> location;
	public static volatile SingularAttribute<ImportItem, Long> minOrderQuantity;
	public static volatile SingularAttribute<ImportItem, String> countryOfOrigin;
	public static volatile SingularAttribute<ImportItem, String> dutyConfirmationDate;
	public static volatile SingularAttribute<ImportItem, String> freightConfirmationDate;
	public static volatile ListAttribute<ImportItem, VendorItemFactory> vendorItemFactory;
	public static volatile SingularAttribute<ImportItem, String> incoTermCode;

}

