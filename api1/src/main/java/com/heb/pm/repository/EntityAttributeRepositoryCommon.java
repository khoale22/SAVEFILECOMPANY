package com.heb.pm.repository;

/**
 * This interfaces holds all of the strings for custom queries on the EntityAttribute entity
 * @author s753601
 * @version 2.15.0
 */
public interface EntityAttributeRepositoryCommon {
	String TAGS_AND_SPECS_QUERY = "select en " +
			"from  EntityAttribute en " +
			"  inner join en.attribute at  "+
			"where en.key.entityId = 16 and at.dynamicAttributeSwitch = 'Y'" +
			"order by en.entityAttributeFieldName";
}
