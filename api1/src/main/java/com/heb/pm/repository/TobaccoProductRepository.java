package com.heb.pm.repository;

import com.heb.pm.entity.TobaccoProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TobaccoProductRepository extends JpaRepository<TobaccoProduct, Long> {


	TobaccoProduct findByProdId(Long prodId);
}
