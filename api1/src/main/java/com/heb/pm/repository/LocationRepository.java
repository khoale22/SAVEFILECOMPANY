package com.heb.pm.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.heb.pm.entity.Location;
import com.heb.pm.entity.LocationKey;

/**
 * Repository for scale maintenance tracking.
 *
 * @author m314029
 * @since 2.17.8
 */
public interface LocationRepository extends JpaRepository<Location, LocationKey> {
	
	String FIND_ACTIVE_STORE = "select loc from Location  loc where loc.key.locationNumber =" +
			" :locationNumber and  loc.key.locationType = :locationType and loc.inactiveSW= 'A'" ; 
	
	/**
	 * This Method is used to get the Active Store Based on the Input provided
	 * @param locationNumber
	 * @param locationType
	 * @return
	 */
	@Query(value = FIND_ACTIVE_STORE)
	Location getActiveStores(@Param("locationNumber")int locationNumber,@Param("locationType")String locationType);
	
}
