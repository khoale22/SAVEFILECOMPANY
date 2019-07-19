package com.heb.pm.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CandidateFulfillmentChannel.class)
public abstract class CandidateFulfillmentChannel_ {

	public static volatile SingularAttribute<CandidateFulfillmentChannel, CandidateProductMaster> candidateProductMaster;
	public static volatile SingularAttribute<CandidateFulfillmentChannel, CandidateWorkRequest> candidateWorkRequest;
	public static volatile SingularAttribute<CandidateFulfillmentChannel, CandidateFulfillmentChannelKey> key;
	public static volatile SingularAttribute<CandidateFulfillmentChannel, LocalDate> effectiveDate;
	public static volatile SingularAttribute<CandidateFulfillmentChannel, String> newData;
	public static volatile SingularAttribute<CandidateFulfillmentChannel, LocalDate> expirationDate;

}

