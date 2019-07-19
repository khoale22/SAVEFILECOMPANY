package com.heb.pm.repository;

import com.heb.pm.entity.ProductMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JPA repository for the ProductMaster entity.
 *
 * @author d116773
 * @since 2.13.0
 */
public interface ProductMasterRepository  extends JpaRepository<ProductMaster, Long>{
    long countByProdBrandId(long id);

    @Query("from ProductMaster pm inner join pm.sellingUnits su where su.upc=:upc")
    ProductMaster findProductMasterByUpc(@Param("upc") long upc);
}
