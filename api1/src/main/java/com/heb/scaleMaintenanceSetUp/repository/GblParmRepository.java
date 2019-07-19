package com.heb.scaleMaintenanceSetUp.repository;


import com.heb.scaleMaintenance.entity.ScaleMaintenanceTracking;
import com.heb.scaleMaintenanceSetUp.entity.GblParm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



/**
 * Repository for scale maintenance tracking.
 *
 * @author m314029
 * @since 2.17.8
 */
public interface GblParmRepository extends JpaRepository<GblParm, Long> {

	
	String SELECT_ALL_QUERY = "select gbl from GblParm gbl where gbl.gblParmName = 'STR_DEPT_AUTH'";
	
	String SELECT_ALL_PANTRY_QUERY = "select gbl from GblParm gbl where gbl.gblParmName = 'PANTRY_STORES'";

	String SELECT_ALL_PAGE_QUERY = "select gbl from GblParm gbl where gbl.gblParmName = 'STR_DEPT_AUTH'";
	
	String SELECT_ALL_PAGE_PANTRY_QUERY = "select gbl from GblParm gbl where gbl.gblParmName = 'PANTRY_STORES'";
	
	String DELETE_STORE = "delete from GblParm gbl where gbl.gblParmName = 'STR_DEPT_AUTH' AND gbl.gblParmValTxt= :gblParmValTxt";
	
	String DELETE_PANTRY_STORE = "delete from GblParm gbl where gbl.gblParmName = 'PANTRY_STORES' AND gbl.gblParmValTxt= :gblParmValTxt";
	
	String UPDATE_STORE = "UPDATE GblParm gbl SET gbl.deptIds = :deptIds  where gbl.gblParmName = 'STR_DEPT_AUTH' AND gbl.gblParmValTxt= :gblParmValTxt";
	
	String FIND_STORE = "select gbl from GblParm gbl where gbl.gblParmName = 'STR_DEPT_AUTH' AND gbl.gblParmValTxt= :gblParmValTxt";

    String FIND_PANTRY_STORE = "select gbl from GblParm gbl where gbl.gblParmName = 'PANTRY_STORES' AND gbl.gblParmValTxt= :gblParmValTxt";
	
    /**
	 * @return
	 * This method is used to get the List of Stores and its associated departments 
	 */
	@Query(value = SELECT_ALL_QUERY)
	List<GblParm> findAllByPage(Pageable request);
	
	/**
	 * This Method is used to delete the Store
	 * @param itmId
	 * @return
	 */
	@Transactional
	@Modifying
	@Query(value = DELETE_STORE)
	void deleteStore(@Param("gblParmValTxt")String gblParmValTxt);
	
	/**
	 * @param gblParmValTxt
	 * @param deptIds
	 * This Method is used to update an existing store
	 * @return
	 */
	@Transactional
	@Modifying
	@Query(value = UPDATE_STORE)
	int updateStore(@Param("gblParmValTxt")String gblParmValTxt,@Param("deptIds")String deptIds);
	
	@Query(value = SELECT_ALL_PAGE_QUERY)
	Page<GblParm> findAllByPages(Pageable request);
	
	
	/**
	 * Get count of records for attribute
	 * @param entityId
	 * @param attributeId
	 * @return count
	 */
	@Query("select max(gbl.gblParmId) from GblParm gbl")
	int findMaxCount();
	
	/**
	 * This Method is used to find whether the store is already available in GBL Parm Table
	 * @param gblParmValTxt
	 * @return
	 */
	@Query(value = FIND_STORE)
	GblParm findStore(@Param("gblParmValTxt")String gblParmValTxt);
	
	
	@Query(value = SELECT_ALL_PANTRY_QUERY)
	List<GblParm> findAllPantryStoresByPage(Pageable request);
	
	@Query(value = SELECT_ALL_PAGE_PANTRY_QUERY)
	Page<GblParm> findAllPantryStoresByPages(Pageable request);
	
	/**
	 * This Method is used to find whether the store is already available in GBL Parm Table
	 * @param gblParmValTxt
	 * @return
	 */
	@Query(value = FIND_PANTRY_STORE)
	GblParm findPantryStore(@Param("gblParmValTxt")String gblParmValTxt);
	
	/**
	 * This Method is used to delete the Store
	 * @param itmId
	 * @return
	 */
	@Transactional
	@Modifying
	@Query(value = DELETE_PANTRY_STORE)
	void deletePantryStore(@Param("gblParmValTxt")String gblParmValTxt);
}
