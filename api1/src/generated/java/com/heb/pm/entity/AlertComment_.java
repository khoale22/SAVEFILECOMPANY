package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AlertComment.class)
public abstract class AlertComment_ {

	public static volatile SingularAttribute<AlertComment, String> createUserID;
	public static volatile SingularAttribute<AlertComment, LocalDateTime> createTime;
	public static volatile SingularAttribute<AlertComment, String> comment;
	public static volatile SingularAttribute<AlertComment, AlertCommentKey> key;

}

