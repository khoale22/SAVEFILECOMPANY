package com.heb.pm.repository;

import com.heb.pm.entity.MapPrice;
import com.heb.pm.entity.MapPriceKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA repository for map price.
 *
 * @author m314029
 * @since 2.13.0
 */
public interface MapPriceRepository extends JpaRepository<MapPrice, MapPriceKey> {

	/**
	 * Find all map prices that share the same upc, with expiration time greater than or equal to given expiration time,
	 * ordered by effective time.
	 *
	 * @param upc The upc to search for.
	 * @param expirationTime The local date time to search for expiration time greater than or equal to.
	 * @return List of all map prices matching the search criteria.
	 */
	List<MapPrice> findByKeyUpcAndExpirationTimeGreaterThanEqualOrderByKeyEffectiveTime(Long upc, LocalDateTime expirationTime);
}
