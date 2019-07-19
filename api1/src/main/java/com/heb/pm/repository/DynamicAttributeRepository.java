/*
 * DynamicAttributeRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.DynamicAttribute;
import com.heb.pm.entity.DynamicAttributeKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for dynamic attributes.
 *
 * @author d116773
 * @since 2.0.7
 */
public interface DynamicAttributeRepository extends JpaRepository<DynamicAttribute, DynamicAttributeKey> {

	String SEARCH_TEXT_FIELD ="select da from DynamicAttribute da where da.key.attributeId = :attributeId " +
			"and UPPER(attr_val_txt) like %:text% and da.key.sourceSystem = :sourceSystem" ;

	/**
	 * Searches the dynamic attribute table for a particular attribute ID for a text string. It is not case
	 * sensitive.
	 *
	 * @param attributeId The ID of the attribute to search for.
	 * @param text The text to search for.

	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from the dynamic attribute table. Will include total available
	 * record counts and number of available pages.
	 */
	@Query(DynamicAttributeRepository.SEARCH_TEXT_FIELD)
	Page<DynamicAttribute> findByTextAttributeRegularExpressionWithCounts(@Param("attributeId") int attributeId,
																		  @Param("text") String text,
																		  @Param("sourceSystem") int sourceSystem,
																		  Pageable pageRequest);


	/**
	 * Searches the dynamic attribute table for a particular attribute ID for a text string. It is not case
	 * sensitive.
	 *
	 * @param attributeId The ID of the attribute to search for.
	 * @param text The text to search for.
	 * @param sourceSystem The ID of the source system to limit results to.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from the dynamic attribute table. Will not include total available
	 * record counts and number of available pages.
	 */
	@Query(DynamicAttributeRepository.SEARCH_TEXT_FIELD)
	List<DynamicAttribute> findByTextAttributeRegularExpression(@Param("attributeId") int attributeId,
																@Param("text") String text,
																@Param("sourceSystem") int sourceSystem,
																Pageable pageRequest);

	/**
	 * This method returns all dynamic attributes that are part of the tags and specs attributes for a product
	 * @param key the product id
	 * @param keyType the type of product for the attribute
	 * @param sourceSystem the source of the attribute
	 * @return
	 */
	List<DynamicAttribute> findByKeyKeyAndKeyKeyTypeAndKeySourceSystemOrderByKeyAttributeId(long key, String keyType, int sourceSystem);

	/**
	 * This method returns all dynamic attributes that are of the given key id and key type.
	 * @param key The key id to look for.
	 * @param keyType The type of product for the attribute.
	 * @return List matching the query.
	 */
	List<DynamicAttribute> findByKeyKeyAndKeyKeyType(long key, String keyType);

	/**
	 * This method returns all dynamic attributes that in a list of given key id and of a specific key type.
	 * @param keys The key ids to look for.
	 * @param keyType The type of product for the attribute.
	 * @return List matching the query.
	 */
	List<DynamicAttribute> findAllByKeyKeyInAndKeyKeyType(List<Long> keys, String keyType);

	/**
	 * This method returns all dynamic attributes that are of the given key ids and key type.
	 * @param key The key id to look for.
	 * @param keyType The type of product for the attribute.
	 * @return List matching the query.
	 */
	List<DynamicAttribute> findByKeyKeyInAndKeyKeyType(List<Long> key, String keyType);

	/**
	 * Returns the max sequence number
	 *
	 * @param attributeId Attribute Id to look for.
	 * @param key Key ids to look for.
	 * @param keyType Key type to look for.
	 * @return The max sequence number.
	 */
	@Query("select max (da.key.sequenceNumber) FROM DynamicAttribute da where da.key.attributeId = :attributeId and da.key.key = :keyId and da.key.keyType = :keyType and da.key.sourceSystem = :sourceSystem")
	Integer findMaxSequenceNumberForAttributeAndKey(@Param("attributeId") Integer attributeId,
													@Param("keyId") Long key,
													@Param("keyType") String keyType,
													@Param("sourceSystem") Integer sourceSystem);
}
