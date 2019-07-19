package com.heb.gdsn;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(VendorSubscription.class)
public abstract class VendorSubscription_ {

	public static volatile SingularAttribute<VendorSubscription, Integer> sequenceNumber;
	public static volatile SingularAttribute<VendorSubscription, String> createUserId;
	public static volatile SingularAttribute<VendorSubscription, String> trimmedVendorGln;
	public static volatile SingularAttribute<VendorSubscription, String> vendorGln;
	public static volatile SingularAttribute<VendorSubscription, Boolean> sendReviewCic;
	public static volatile SingularAttribute<VendorSubscription, Boolean> sendReceivedCic;
	public static volatile SingularAttribute<VendorSubscription, String> messageId;
	public static volatile SingularAttribute<VendorSubscription, String> vendorName;
	public static volatile SingularAttribute<VendorSubscription, Boolean> sendSynchronizedCic;
	public static volatile SingularAttribute<VendorSubscription, Message> message;
	public static volatile SingularAttribute<VendorSubscription, LocalDateTime> processTime;
	public static volatile SingularAttribute<VendorSubscription, String> messageStatus;
	public static volatile SingularAttribute<VendorSubscription, LocalDateTime> createTime;
	public static volatile SingularAttribute<VendorSubscription, String> subscriptionStatus;
	public static volatile SingularAttribute<VendorSubscription, String> documentId;

}

