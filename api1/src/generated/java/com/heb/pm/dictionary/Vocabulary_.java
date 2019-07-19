package com.heb.pm.dictionary;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Vocabulary.class)
public abstract class Vocabulary_ {

	public static volatile SingularAttribute<Vocabulary, CaseCode> caseCode;
	public static volatile SingularAttribute<Vocabulary, String> lstUpdtUid;
	public static volatile SingularAttribute<Vocabulary, Boolean> active;
	public static volatile SingularAttribute<Vocabulary, LocalDate> lstUpdtTs;
	public static volatile SingularAttribute<Vocabulary, String> wordCodeAttribute;
	public static volatile SingularAttribute<Vocabulary, VocabularyKey> key;
	public static volatile SingularAttribute<Vocabulary, WordCode> wordCode;

}

