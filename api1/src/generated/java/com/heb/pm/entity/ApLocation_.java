package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ApLocation.class)
public abstract class ApLocation_ {

	public static volatile SingularAttribute<ApLocation, String> dsdBnkCode;
	public static volatile SingularAttribute<ApLocation, Integer> dsdDbvLocationCode;
	public static volatile SingularAttribute<ApLocation, Integer> dsdDbvNumber;
	public static volatile SingularAttribute<ApLocation, String> remitAdr1;
	public static volatile SingularAttribute<ApLocation, String> remitCoName;
	public static volatile SingularAttribute<ApLocation, Boolean> dsdSbbSw;
	public static volatile SingularAttribute<ApLocation, Integer> remitZip5Cd;
	public static volatile SingularAttribute<ApLocation, String> remitPhnCmt;
	public static volatile SingularAttribute<ApLocation, Integer> remitZip4Cd;
	public static volatile SingularAttribute<ApLocation, String> remitAdr2;
	public static volatile SingularAttribute<ApLocation, Integer> remitPhnNumber;
	public static volatile SingularAttribute<ApLocation, String> remitState;
	public static volatile SingularAttribute<ApLocation, Integer> remitAreaCd;
	public static volatile SingularAttribute<ApLocation, String> corrContcName;
	public static volatile SingularAttribute<ApLocation, String> dsdProcMode;
	public static volatile SingularAttribute<ApLocation, LocalDateTime> dsdSbbEffDt;
	public static volatile SingularAttribute<ApLocation, ApLocationKey> key;

}

