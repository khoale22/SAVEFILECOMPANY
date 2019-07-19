package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ItemNotDeleted.class)
public abstract class ItemNotDeleted_ {

	public static volatile SingularAttribute<ItemNotDeleted, ItemMaster> itemMaster;
	public static volatile SingularAttribute<ItemNotDeleted, ItemNotDeletedReason> itemNotDeletedReason;
	public static volatile SingularAttribute<ItemNotDeleted, ItemNotDeletedKey> key;
	public static volatile SingularAttribute<ItemNotDeleted, LocalDateTime> lastUpdateTime;

}

