package com.heb.pm.repository;

import com.heb.pm.entity.FlowType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository for Order Quantity Type Repository
 *
 * @author s753601
 * @since 2.8.0
 */
public interface FlowTypeRepository extends JpaRepository<FlowType, String>{

}
