package com.heb.pm.repository;

import com.heb.pm.entity.CandidateItemScanCode;
import com.heb.pm.entity.CandidateItemScanCodeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CandidateItemScanCodeRepository extends JpaRepository<CandidateItemScanCode,CandidateItemScanCodeKey> {
	@Query(value = "select candidateItemScanCode.key.upc from CandidateItemScanCode candidateItemScanCode where candidateItemScanCode.key.upc in :upcsGenLst")
	List<Long> findCaseUpcsByUpcGenList(@Param("upcsGenLst") List<Long> upcsGenLst);
}
