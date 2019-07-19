package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(GroupAttribute.class)
public abstract class GroupAttribute_ {

	public static volatile SingularAttribute<GroupAttribute, String> createUserId;
	public static volatile SingularAttribute<GroupAttribute, String> latsUpldateId;
	public static volatile SingularAttribute<GroupAttribute, Long> seqence;
	public static volatile SingularAttribute<GroupAttribute, LocalDateTime> createTime;
	public static volatile SingularAttribute<GroupAttribute, RelationshipGroup> relationshipGroup;
	public static volatile SingularAttribute<GroupAttribute, LocalDateTime> latsUpldateTime;
	public static volatile SingularAttribute<GroupAttribute, GroupAttributeKey> key;

}

