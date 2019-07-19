package com.heb.pm.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductOnline.class)
public abstract class ProductOnline_ {

	public static volatile SingularAttribute<ProductOnline, Boolean> showOnSite;
	public static volatile SingularAttribute<ProductOnline, ProductOnlineKey> key;
	public static volatile SingularAttribute<ProductOnline, LocalDate> expirationDate;

}

