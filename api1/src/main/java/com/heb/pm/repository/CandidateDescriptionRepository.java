package com.heb.pm.repository;

import com.heb.pm.entity.CandidateDescription;
import com.heb.pm.entity.CandidateDescriptionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CandidateDescriptionRepository extends JpaRepository<CandidateDescription, CandidateDescriptionKey>{

    /**
     * Returns the proposed description based on productId
     * @param productId
     * @return
     */
    @Query(value = "SELECT cd FROM CandidateDescription AS cd " +
            "LEFT JOIN cd.candidateProductMaster AS cp " +
            "LEFT JOIN cp.candidateWorkRequest AS cw " +
            "WHERE cd.descriptionType = 'SGNRP' AND cw.intent = 27 AND cw.productId = :productId AND cw.status <> '104'")
    CandidateDescription getProposedDescription(@Param("productId") Long productId);
}
