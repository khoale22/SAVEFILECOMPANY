package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Transaction.class)
public abstract class Transaction_ {

	public static volatile SingularAttribute<Transaction, String> sourceInfoTxt;
	public static volatile SingularAttribute<Transaction, String> createUserId;
	public static volatile SingularAttribute<Transaction, LocalDateTime> createTime;
	public static volatile SingularAttribute<Transaction, Integer> sourceSystem;
	public static volatile SingularAttribute<Transaction, String> transactionId;

}

