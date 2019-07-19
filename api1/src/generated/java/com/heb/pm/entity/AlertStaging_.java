package com.heb.pm.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AlertStaging.class)
public abstract class AlertStaging_ {

	public static volatile SingularAttribute<AlertStaging, String> createUserId;
	public static volatile SingularAttribute<AlertStaging, String> trimmedAlertKey;
	public static volatile SingularAttribute<AlertStaging, Double> alertCriticalPercent;
	public static volatile SingularAttribute<AlertStaging, String> alertStatusUserId;
	public static volatile SingularAttribute<AlertStaging, String> alertTypeCD;
	public static volatile ListAttribute<AlertStaging, ProductOnline> productOnline;
	public static volatile SingularAttribute<AlertStaging, String> alertKey;
	public static volatile SingularAttribute<AlertStaging, Character> alertHideSw;
	public static volatile SingularAttribute<AlertStaging, String> alertDataTxt;
	public static volatile SingularAttribute<AlertStaging, Date> responseByDate;
	public static volatile SingularAttribute<AlertStaging, String> alertStatusCD;
	public static volatile ListAttribute<AlertStaging, AlertRecipient> alertRecipients;
	public static volatile SingularAttribute<AlertStaging, String> delegatedByUserID;
	public static volatile SingularAttribute<AlertStaging, Integer> alertID;
	public static volatile SingularAttribute<AlertStaging, String> assignedUserID;

}

