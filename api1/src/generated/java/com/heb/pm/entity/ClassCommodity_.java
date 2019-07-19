package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ClassCommodity.class)
public abstract class ClassCommodity_ {

	public static volatile SingularAttribute<ClassCommodity, String> trimmedBdmCode;
	public static volatile SingularAttribute<ClassCommodity, Integer> classCode;
	public static volatile SingularAttribute<ClassCommodity, Integer> maxCustomerOrderQuantity;
	public static volatile SingularAttribute<ClassCommodity, String> eBMid;
	public static volatile ListAttribute<ClassCommodity, SearchCriteria> searchCriteria;
	public static volatile SingularAttribute<ClassCommodity, String> pdpTemplateCode;
	public static volatile SingularAttribute<ClassCommodity, Character> classCommodityActive;
	public static volatile SingularAttribute<ClassCommodity, Bdm> bdm;
	public static volatile ListAttribute<ClassCommodity, SubCommodity> subCommodityList;
	public static volatile SingularAttribute<ClassCommodity, PDPTemplate> pdpTemplate;
	public static volatile SingularAttribute<ClassCommodity, Integer> omiDirector;
	public static volatile SingularAttribute<ClassCommodity, String> name;
	public static volatile SingularAttribute<ClassCommodity, Integer> imsCommunicationCode;
	public static volatile SingularAttribute<ClassCommodity, Integer> commodityCode;
	public static volatile SingularAttribute<ClassCommodity, ItemClass> itemClassMaster;
	public static volatile SingularAttribute<ClassCommodity, ClassCommodityKey> key;
	public static volatile SingularAttribute<ClassCommodity, Integer> pssDepartment;
	public static volatile SingularAttribute<ClassCommodity, String> bDAid;
	public static volatile SingularAttribute<ClassCommodity, String> bdmCode;

}

