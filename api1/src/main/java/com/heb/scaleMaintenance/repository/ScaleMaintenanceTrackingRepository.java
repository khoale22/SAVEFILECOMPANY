package com.heb.scaleMaintenance.repository;


import com.heb.scaleMaintenance.entity.ScaleMaintenanceTracking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository for scale maintenance tracking.
 *
 * @author m314029
 * @since 2.17.8
 */
public interface ScaleMaintenanceTrackingRepository extends JpaRepository<ScaleMaintenanceTracking, Long> {

	String SELECT_ALL_QUERY = "select smt from ScaleMaintenanceTracking smt";

	/**
	 * By default, the JpaRepository findAll(Pageable) returns a page, which includes a count query. This method
	 * just gets the list corresponding to a given page.
	 *
	 * @param request Page related information for this request (i.e. page, page size, order)
	 * @return List of scale maintenance tracking.
	 */
	@Query(value = SELECT_ALL_QUERY)
	List<ScaleMaintenanceTracking> findAllByPage(Pageable request);
}
