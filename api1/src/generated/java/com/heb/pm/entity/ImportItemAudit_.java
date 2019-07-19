package com.heb.pm.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ImportItemAudit.class)
public abstract class ImportItemAudit_ {

	public static volatile SingularAttribute<ImportItemAudit, String> pickupPoint;
	public static volatile SingularAttribute<ImportItemAudit, String> color;
	public static volatile SingularAttribute<ImportItemAudit, String> minOrderDescription;
	public static volatile SingularAttribute<ImportItemAudit, LocalDate> warehouseFlushDate;
	public static volatile SingularAttribute<ImportItemAudit, LocalDate> inStoreDate;
	public static volatile SingularAttribute<ImportItemAudit, String> cartonMarking;
	public static volatile SingularAttribute<ImportItemAudit, String> containerSizeCode;
	public static volatile SingularAttribute<ImportItemAudit, Double> agentCommissionPercent;
	public static volatile SingularAttribute<ImportItemAudit, Integer> sellByYear;
	public static volatile SingularAttribute<ImportItemAudit, String> changedBy;
	public static volatile SingularAttribute<ImportItemAudit, LocalDate> prorationDate;
	public static volatile SingularAttribute<ImportItemAudit, Double> dutyPercent;
	public static volatile SingularAttribute<ImportItemAudit, String> action;
	public static volatile SingularAttribute<ImportItemAudit, String> season;
	public static volatile SingularAttribute<ImportItemAudit, Long> minOrderQuantity;
	public static volatile SingularAttribute<ImportItemAudit, Long> hts2;
	public static volatile SingularAttribute<ImportItemAudit, Long> hts1;
	public static volatile SingularAttribute<ImportItemAudit, String> countryOfOrigin;
	public static volatile SingularAttribute<ImportItemAudit, String> dutyConfirmationDate;
	public static volatile SingularAttribute<ImportItemAudit, Long> hts3;
	public static volatile SingularAttribute<ImportItemAudit, String> freightConfirmationDate;
	public static volatile SingularAttribute<ImportItemAudit, ImportItemAuditKey> key;
	public static volatile SingularAttribute<ImportItemAudit, String> dutyInformation;
	public static volatile SingularAttribute<ImportItemAudit, String> incoTermCode;

}

