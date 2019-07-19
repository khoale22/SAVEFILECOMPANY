package com.heb.pm.repository;

import com.heb.pm.entity.SubCommodity;
import com.heb.pm.entity.SubCommodityKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JPA repository for SubCommodity.
 *
 * @author d116773
 * @since 2.0.2
 */
public interface SubCommodityRepository extends JpaRepository<SubCommodity, SubCommodityKey> {
    List<SubCommodity> findByKeySubCommodityCodeAndSubCommodityActive(Integer subCommodityCOde,Character subCommodityActive);

    /**
     * Returns the list of sub-commodities that exist that are part of a user's search criteria in their search session.
     *
     * @param sessionId The ID of the user's session.
     * @return A list of sub-commodities that match the list the user has provided.
     */
    @Query(value = "select subCommodity.key.subCommodityCode from SubCommodity subCommodity " +
            "where exists (select searchCriteria.subCommodityCode " +
            "from SearchCriteria searchCriteria " +
            "where subCommodity.key.subCommodityCode = searchCriteria.subCommodityCode and " +
            "searchCriteria.sessionId = :sessionId)")
    List<Integer> findAllForSearch(@Param("sessionId") String sessionId);

	/**
	 * Finds all sub commodities sharing the given commodity code.
	 *
	 * @param commodityCode Commodity code to search for.
	 * @return List of sub commodities matching search.
	 */
	List<SubCommodity> findByKeyCommodityCode(Integer commodityCode);

	/**
	 * Find subcommodity by subCommodityCode.
	 *
	 * @param subCommodityCode the subCommodityCode
	 * @return Subcommodity
	 */
	SubCommodity findByKeySubCommodityCode(Integer subCommodityCode);
}
