package com.hellokoding.springboot.jpa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hellokoding.springboot.jpa.book.*;
import com.hellokoding.springboot.jpa.metamodelEntity.CostOwner_;
import com.hellokoding.springboot.jpa.metamodelEntity.ProductBrandCostOwner_;
import com.hellokoding.springboot.jpa.metamodelEntity.ProductBrand_;
import com.hellokoding.springboot.jpa.metamodelEntity.TopToTop_;
import com.hellokoding.springboot.jpa.service.ServiceForAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sun.plugin2.util.PojoUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@SpringBootApplication
public class JpaApplication implements CommandLineRunner {
   /* @Autowired
    private BookCategoryRepository bookCategoryRepository;

    @Autowired
    private BookRepository bookRepository;*/

   @Autowired
    ServiceForAll serviceForAll;
    @Autowired
    private EntityManager entityManager;

    /**
     * Resolver for Product brand object.
     */
   /* private class ProductBrandCostOwnerResolver implements LazyObjectResolver<ProductBrandCostOwner> {

        *//**
         * Resolves all of the associate product brand tiers information.
         *
         * @param productBrandCostOwner the product brand page to fetch the product brand tiers for.
         *//*
        @Override
        public void fetch(ProductBrandCostOwner productBrandCostOwner){
            productBrandCostOwner.getCostOwner().getTopToTop().getTopToTopName();
            productBrandCostOwner.getProductBrand().getProductBrandDescription();
        }
*/
//        public void fetch(Iterable<ProductBrand> productBrands) {
//            productBrands.forEach((productBrand) -> {
//                productBrand.getProductBrandCostOwners().size();
//                if(productBrand.getProductBrandCostOwners()!=null){
//                    productBrand.getProductBrandCostOwners().forEach(costOwner ->{
//                        if(costOwner!=null) {
//                            costOwner.getCostOwner().getTopToTop();
//                            if (costOwner.getCostOwner().getTopToTop() != null)
//                                costOwner.getCostOwner().getTopToTop().getTopToTopId();
//                        }
//
//                    });
//                }
//
//            });
//        }
   /* }*/


    public static void main(String[] args) {
        SpringApplication.run(JpaApplication.class, args);
    }

    @Override
    public void run(String... args) {

        String PROD_BRND = "productBrand";
        String PROD_BRND_DES = "productBrandDescription";
        String PROD_BRND_ID = "productBrandId";
        /**
         * Holds the property name of cost owner.
         */
        String CST_OWNR = "costOwner";
        String CST_OWNR_NM = "costOwnerName";
        String CST_OWNR_ID = "costOwnerId";
        /**
         * Holds the property name of top 2 top.
         */
        String T2T = "topToTop";
        String T2T_NM = "topToTopName";
        String T2T_ID = "topToTopId";
        String PERCENT_SIGN = "%";

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<KhoaLeProductBrandCostOwner> criteriaQuery = criteriaBuilder.createQuery(KhoaLeProductBrandCostOwner.class);
        Root<ProductBrandCostOwner> productBrandCostOwnerRoot = criteriaQuery.from(ProductBrandCostOwner.class);
        Join<ProductBrandCostOwner , ProductBrand> productBrandJoin = productBrandCostOwnerRoot.join(PROD_BRND);
        Join<ProductBrandCostOwner , CostOwner> costOwnerJoin = productBrandCostOwnerRoot.join(CST_OWNR);
        Join<CostOwner ,TopToTop> topToTopJoin = costOwnerJoin.join(T2T);


        criteriaQuery.multiselect(topToTopJoin.get(T2T_NM) , productBrandJoin.get(PROD_BRND_DES));
        TypedQuery<KhoaLeProductBrandCostOwner> tQuery = entityManager.createQuery(criteriaQuery);

        // Execute the query.
        List<KhoaLeProductBrandCostOwner> results = tQuery.getResultList();

        ObjectMapper Obj = new ObjectMapper();

        try {

            // get Oraganisation object as a json string
            String jsonStr = Obj.writeValueAsString(results);

            // Displaying JSON String
            System.out.println(jsonStr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println(results.size());








       /* criteriaQuery.select(productBrandCostOwnerRoot);*/
       /* TypedQuery<ProductBrandCostOwner> tQuery = this.entityManager.createQuery(criteriaQuery);

        // Execute the query.
        List<ProductBrandCostOwner> results = tQuery.getResultList();
        //System.out.print(PojoUtil.toJson(results));

        ObjectMapper Obj = new ObjectMapper();

        try {

            // get Oraganisation object as a json string
            String jsonStr = Obj.writeValueAsString(results);

            // Displaying JSON String
            System.out.println(jsonStr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println(results.size());*/
    }
}
