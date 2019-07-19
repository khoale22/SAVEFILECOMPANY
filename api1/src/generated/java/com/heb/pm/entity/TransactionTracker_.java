package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TransactionTracker.class)
public abstract class TransactionTracker_ {

	public static volatile SingularAttribute<TransactionTracker, Long> icCntlNbr;
	public static volatile SingularAttribute<TransactionTracker, Long> grpCntlNbr;
	public static volatile SingularAttribute<TransactionTracker, String> fileNm;
	public static volatile SingularAttribute<TransactionTracker, Long> trxCntlNbr;
	public static volatile SingularAttribute<TransactionTracker, String> trxStatCd;
	public static volatile SingularAttribute<TransactionTracker, String> source;
	public static volatile SingularAttribute<TransactionTracker, String> userRole;
	public static volatile SingularAttribute<TransactionTracker, String> userId;
	public static volatile ListAttribute<TransactionTracker, CandidateWorkRequest> candidateWorkRequest;
	public static volatile SingularAttribute<TransactionTracker, Long> trackingId;
	public static volatile SingularAttribute<TransactionTracker, LocalDateTime> createDate;
	public static volatile SingularAttribute<TransactionTracker, String> fileDes;

}
/*
	select generatedAlias0 from TransactionTracker as generatedAlias0 where
		( generatedAlias0.fileNm not in (:param0, :param1, :param2, :param3, :param4) ) and
		( exists (select generatedAlias1.trackingId from CandidateWorkRequest as generatedAlias1 where
		( generatedAlias1.trackingId=generatedAlias0.trackingId ) and ( generatedAlias1.sourceSystem in (4, 9, 14) )) ) and
		( generatedAlias0.createDate>=:param5 ) and ( generatedAlias0.createDate<:param6 ) order by generatedAlias0.createDate desc
*/
