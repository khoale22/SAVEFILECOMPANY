package com.heb.pm.repository;

import com.heb.pm.entity.ProductScanCodeExtent;
import com.heb.pm.entity.ProductScanCodeExtentKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve information about gladson dimensional data.
 *
 * @author m594201
 * @since 2.7.0
 */
public interface ProductScanCodeExtentRepository extends JpaRepository<ProductScanCodeExtent, ProductScanCodeExtentKey> {

    /**
     * Find by key scan code id and key prod ext data code in list.
     *
     * @param scanCode           the scan code id that is attached to the measurements given by the gladson measuring data.
     * @param dimensionDataCodes the dimension data codes that are tied to a description that then provides the measurement for that code.
     * @return the list of gladson dimensions.
     */
    List<ProductScanCodeExtent> findByKeyScanCodeIdAndKeyProdExtDataCodeIn(Long scanCode, List<String> dimensionDataCodes);

    /**
     * Find by key scan code id and key prod ext data code in list.
     *
     * @param scanCode           the scan code id that is attached to the measurements given by the gladson measuring data.
     * @param dimensionDataCodes the dimension data codes that are tied to a description that then provides the measurement for that code.
     * @return the list of gladson dimensions.
     */
    List<ProductScanCodeExtent> findByKeyScanCodeIdAndKeyProdExtDataCodeInOrderByKeyProdExtDataCodeDesc(Long scanCode, List<String> dimensionDataCodes);

    /**
     * Get the product scan code extent by scan code id and key product extent data id.
     *
     * @param scanCodeId      the scan code id to find.
     * @param prodExtDataCode the product extent data code to find.
     * @param sourceSystem    the source system to find.
     * @return the product scan code extent.
     */
    ProductScanCodeExtent findByKeyScanCodeIdAndKeyProdExtDataCodeAndSourceSystem(Long scanCodeId, String prodExtDataCode,
                                                                                  Integer sourceSystem);

    /**
     * Get the product scan code extent by scan code id and key product extent data id.
     *
     * @param scanCodeId the scan code id to find.
     * @param sourceSystem the source system.
     * @return the product scan code extent.
     */
    List<ProductScanCodeExtent> findByKeyScanCodeIdAndSourceSystem(Long scanCodeId, Integer sourceSystem);
}
