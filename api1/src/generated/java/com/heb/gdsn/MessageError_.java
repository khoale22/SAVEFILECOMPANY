package com.heb.gdsn;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MessageError.class)
public abstract class MessageError_ {

	public static volatile SingularAttribute<MessageError, String> errorMessage;
	public static volatile SingularAttribute<MessageError, String> errorCode;
	public static volatile SingularAttribute<MessageError, Message> message;
	public static volatile SingularAttribute<MessageError, MessageErrorKey> key;

}

