/*
 *  MasterDataExtensionAttributeRepository
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.MasterDataExtensionAttribute;
import com.heb.pm.entity.MasterDataExtensionAttributeKey;
import com.heb.pm.entity.ProductPanelType;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JPA repository for Master Data Extension Attribute.
 *
 * @author vn70516
 * @since 2.14.0
 */
public interface MasterDataExtensionAttributeRepository extends JpaRepository<MasterDataExtensionAttribute, MasterDataExtensionAttributeKey> {

    /**
     * Get MasterDataExtensionAttribute by key of that object, the same with findOne.
     *
     * @param attributeId      the attributeId to find.
     * @param id               the keyId is productId or upc to find.
     * @param itemProdIdCode   the type of productId or upc to find.
     * @param dataSourceSystem the data source system.
     * @return the MasterDataExtensionAttribute object.
     */
    MasterDataExtensionAttribute findByKeyAttributeIdAndKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystem(long attributeId, long id, String itemProdIdCode, long dataSourceSystem);

    /**
     * Get MasterDataExtensionAttribute by key of that object, the same with findOne.
     *
     * @param id the keyId is productId or upc to find.
     * @param dataSourceSystem the data source system.
     * @return the list of MasterDataExtensionAttribute object.
     */
    List<MasterDataExtensionAttribute> findByKeyIdAndKeyDataSourceSystem(long id, long dataSourceSystem);

    /**
     * Get the list of MasterDataExtensionAttribute by key of that object, the same with findOne.
     *
     * @param attributeId      the attributeId to find.
     * @param id               the keyId is productId or upc to find.
     * @param itemProdIdCode   the type of productId or upc to find.
     * @param dataSourceSystem the data source system.
     * @return the MasterDataExtensionAttribute object.
     */
    List<MasterDataExtensionAttribute> findByKeyAttributeIdAndKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystemOrderByAttributeCode(long attributeId, long id, String itemProdIdCode, long dataSourceSystem);

    /**
     * Get master data extention by attribute id, product id and source system id.
     * @param attributesId the attribute id to find
     * @param productId    the product id to find
     * @param dataSourceSystem the source system to find
     * @return MasterDataExtensionAttribute object.
     */
    MasterDataExtensionAttribute findByKeyAttributeIdAndKeyIdAndKeyDataSourceSystem(long attributesId, long productId, long dataSourceSystem);

    /**
     * Gets a MasterDataExtensionAttribute by KeyId, AttributeId, itemProdIdCode and  AttributeValueText is Not Null.
     *
     * @param attributeId the attributeId.
     * @param id the scan code.
     * @param itemProdIdCode the itemProdIdCode.
     * @return the MasterDataExtensionAttribute object.
     */
    MasterDataExtensionAttribute findOneByKeyAttributeIdAndKeyIdAndKeyItemProdIdCodeAndAttributeValueTextNotNull(long attributeId, long id, String itemProdIdCode);

    /**
     * Get the list of MasterDataExtensionAttributes by id, dataSourceSystem, itemProdIdCode and usrInrfcGrpCd.
     * @param id
     * @param dataSourceSystem
     * @param itemProdIdCode
     * @param usrInrfcGrpCd
     * @return the list od MasterDataExtensionAttribute.
     */
    List<MasterDataExtensionAttribute> findByKeyIdAndKeyDataSourceSystemAndKeyItemProdIdCodeAndEcommerUserGroupAttributesKeyUsrInrfcGrpCdOrderByKeyAttributeIdAscKeySequenceNumber(long id, long dataSourceSystem, String itemProdIdCode, String usrInrfcGrpCd);

    /**
     * Get the list of MasterDataExtensionAttributes by id, dataSourceSystem, itemProdIdCode.
     * @param id
     * @param itemProdIdCode
     * @param dataSourceSystem
     * @return
     */
    List<MasterDataExtensionAttribute> findByKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystemOrderByKeyAttributeId(long id, String itemProdIdCode, long dataSourceSystem);

    /**
     * Get the list of MasterDataExtensionAttributes by id, dataSourceSystem, itemProdIdCode and order by attributeId, sequenceNumber.
     * @param id
     * @param itemProdIdCode
     * @param dataSourceSystem
     * @return
     */
    List<MasterDataExtensionAttribute> findByKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystemOrderByKeyAttributeIdAscKeySequenceNumberAsc(long id, String itemProdIdCode, long dataSourceSystem);

    /**
     * Get master data extention by attribute id, product id and source system id.
     * @param attributesIds the list of attribute id to find
     * @param productId    the product id to find
     * @param dataSourceSystem the source system to find
     * @return MasterDataExtensionAttribute object.
     */
    List<MasterDataExtensionAttribute> findByKeyAttributeIdInAndKeyIdAndKeyDataSourceSystemOrderByKeyAttributeId(List<Long>
                                                                                                        attributesIds,
                                                                                            long productId, long dataSourceSystem);

    @Query("Select mdea from MasterDataExtensionAttribute mdea where mdea.key.attributeId = :attributeId and mdea.key.id = :keyId and mdea.key.itemProdIdCode = :itemProdIdCode and mdea.key.dataSourceSystem = :dataSourceSystem and mdea.attributeCodeId = :attributeCodeId")
    List<MasterDataExtensionAttribute> findExistingAttributeValue(
            @Param(value="attributeId") long attributeId,
            @Param(value="keyId") long keyId,
            @Param(value="itemProdIdCode") String itemProdIdCode,
            @Param(value="dataSourceSystem") long dataSourceSystem,
            @Param(value="attributeCodeId") long attributeCodeId);
}