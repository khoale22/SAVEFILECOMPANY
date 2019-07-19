package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateItemWarehouseVendor.class)
public abstract class CandidateItemWarehouseVendor_ {

	public static volatile SingularAttribute<CandidateItemWarehouseVendor, Boolean> frtExcpSwitch;
	public static volatile SingularAttribute<CandidateItemWarehouseVendor, LocalDateTime> lastUpdateDate;
	public static volatile SingularAttribute<CandidateItemWarehouseVendor, String> lastUpdateUserId;
	public static volatile SingularAttribute<CandidateItemWarehouseVendor, Double> frtAllowAmt;
	public static volatile SingularAttribute<CandidateItemWarehouseVendor, Boolean> primaryVendorSwitch;
	public static volatile SingularAttribute<CandidateItemWarehouseVendor, CandidateWarehouseLocationItem> candidateWarehouseLocationItem;
	public static volatile SingularAttribute<CandidateItemWarehouseVendor, Boolean> termsExcpSwitch;
	public static volatile SingularAttribute<CandidateItemWarehouseVendor, Boolean> ppaddExcpSw;
	public static volatile SingularAttribute<CandidateItemWarehouseVendor, CandidateVendorLocationItem> candidateVendorLocationItem;
	public static volatile SingularAttribute<CandidateItemWarehouseVendor, String> upDnCstCmt;
	public static volatile SingularAttribute<CandidateItemWarehouseVendor, String> upDnPosOrNeg;
	public static volatile SingularAttribute<CandidateItemWarehouseVendor, Double> mfgSrpAmt;
	public static volatile SingularAttribute<CandidateItemWarehouseVendor, Double> sellAmt;
	public static volatile SingularAttribute<CandidateItemWarehouseVendor, Double> frtBilAmt;
	public static volatile SingularAttribute<CandidateItemWarehouseVendor, CandidateItemWarehouseVendorKey> key;
	public static volatile SingularAttribute<CandidateItemWarehouseVendor, Double> itmUpDnCst;
	public static volatile SingularAttribute<CandidateItemWarehouseVendor, Boolean> frtFreeSw;
	public static volatile SingularAttribute<CandidateItemWarehouseVendor, Double> totAllowAmt;
	public static volatile SingularAttribute<CandidateItemWarehouseVendor, Double> bkhlAmt;

}

