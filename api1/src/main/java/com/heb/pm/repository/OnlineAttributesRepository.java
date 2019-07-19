package com.heb.pm.repository;

import com.heb.pm.entity.DistributionFilter;
import com.heb.pm.entity.DistributionFilterKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for online attributes
 *
 * Created by m594201 on 10/19/2017.
 * @since 2.14.0
 */
@Repository
public interface OnlineAttributesRepository extends JpaRepository<DistributionFilter, DistributionFilterKey> {

	DistributionFilter findByKeyKeyId(Long prodId);

}
