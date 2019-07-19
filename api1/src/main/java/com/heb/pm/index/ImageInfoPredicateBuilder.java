/*
 * ImageInfoPredicateBuilder
 *
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.index;

import com.heb.pm.entity.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class helps to build predicates for fetch ImageInfo.
 *
 * @author vn70529
 * @since 2.39.0
 */
@Service
public class ImageInfoPredicateBuilder {

    private static final String KEY_ATTRIBUTE = "key";
    private static final String ID_ATTRIBUTE = "id";


    /**
     * Used to build predicates(WHERE clause) for fetching ProductScanImageURI have been approved but not show on site for 10 months.
     *
     * @param root            The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder    JPA query builder used to construct the sub-query.
     * @param criteriaBuilder Used to construct the various parts of the SQL statement.
     * @return Predicate
     */
    public Predicate buildPredicate(Root<ProductScanImageURI> root, CriteriaQuery<ProductScanImageURI> queryBuilder,
                                    CriteriaBuilder criteriaBuilder) {
        Specification<ProductScanImageURI> spec = buildSpecification();
        return spec.toPredicate(root, queryBuilder, criteriaBuilder);
    }

    /**
     * Gives structure to the predicates to be built for searching ProductScanImageURI with
     * the images have been approved but not show on site for 10 months (activeSwitch = true and imageStatusCode = activate
     * and activeOnline = false and image type ='TGA' and change on less than 10 months).
     *
     * @return the Specification.
     */
    private Specification<ProductScanImageURI> buildSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            //Get image has active is true
            predicates.add(cb.equal(root.get(ProductScanImageURI_.activeSwitch), true));
            //Get image has status is approved
            predicates.add(cb.equal(cb.trim(root.get(ProductScanImageURI_.imageStatusCode)), ProductScanImageURI.IMAGE_STATUS_CD_APPROVED));
            //Get image has active online is false
            predicates.add(cb.equal(root.get(ProductScanImageURI_.activeOnline), false));
            //Get image has image format not type 'TGA'
            predicates.add(cb.exists(buildImageTypePredicate(root, query, cb)));
            //Get ProductScanImageURI exit in ProductScanImageURIAudit has status approved but not active online less than 10 months
            predicates.add(cb.exists(buildProductScanImageURIAuditSubqueryPredicate(root, query, cb)));

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));

        };
    }

    /**
     * Builds predicate to fetch ProductScanImageURI matching ImageType.
     *
     * @param root         The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder JPA query builder used to construct the sub-query.
     * @param cb           criteria builder, used to construct the various parts of the SQL statement.
     * @return the Subquery by ImageType
     */
    private Subquery<ImageType> buildImageTypePredicate(Root<ProductScanImageURI> root,
                                                        CriteriaQuery<?> queryBuilder, CriteriaBuilder cb) {
        Subquery<ImageType> imageTypeExist = queryBuilder.subquery(ImageType.class);
        Root<ImageType> poRoot = imageTypeExist.from(ImageType.class);
        imageTypeExist.select(poRoot.get(ID_ATTRIBUTE));
        Predicate[] predicates = new Predicate[2];
        predicates[0] = cb.equal(poRoot.get(ImageType_.id),
                root.get(ProductScanImageURI_.imageTypeCode));
        predicates[1] = cb.notEqual(cb.trim(poRoot.get(ImageType_.imageFormat)), ImageType.IMAGE_FORMAT_TGA);
        imageTypeExist.where(cb.and(predicates));
        return imageTypeExist;
    }

    /**
     * Builds predicate to fetch ProductScanImageURI matching ProductScanImageURIAudit.
     *
     * @param root         The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder JPA query builder used to construct the sub-query.
     * @param cb           criteria builder, used to construct the various parts of the SQL statement.
     * @return the Subquery by ProductScanImageURIAudit
     */
    private Subquery<ProductScanImageURIAudit> buildProductScanImageURIAuditSubqueryPredicate(Root<ProductScanImageURI> root,
                                                                                      CriteriaQuery<?> queryBuilder, CriteriaBuilder cb) {
        //Build Sub-query ProductScanImageURIAudit has image that it's approved change on after latest date of active online or not approved
        //and it must be less than 10 months from current date.
        //Ex - Current date is 14/6/2019 and we have the row on the ProductScanImageURIAudit as below:
        // id: 1 ; sequenceNumber: 1 ; activeSwitch: true; activeOnline:false;  status: Approved; changeOn: 13/9/2019 (1)
        // id: 1 ; sequenceNumber: 1 ; activeSwitch: true; activeOnline:false;  status: Approved; changeOn: 10/8/2018 (2)
        // id: 1 ; sequenceNumber: 1 ; activeSwitch: true; activeOnline:true;  status:  Approved; changeOn: 12/7/2018 (3)
        // id: 1 ; sequenceNumber: 1 ; activeSwitch: true; activeOnline:false; status:  Rejected; changeOn: 11/6/2018 (4)
        //Will return record (2) because has status is approved after latest date ActiveOnline is true (3); and less than 10 months from current date
        // id: 1 ; sequenceNumber: 1 ; activeSwitch: true; activeOnline:false;  status:  Approved; changeOn: 13/6/2019 (2)
        Subquery<ProductScanImageURIAudit> productScanImageURIAuditExist = queryBuilder.subquery(ProductScanImageURIAudit.class);
        Root<ProductScanImageURIAudit> poRoot = productScanImageURIAuditExist.from(ProductScanImageURIAudit.class);
        productScanImageURIAuditExist.select(poRoot.get(KEY_ATTRIBUTE).get(ID_ATTRIBUTE));

        List<Predicate> predicates = new ArrayList<>();
        //Join ProductScanImageURI with ProductScanImageURIAudit on ID and SequenceNumber
        predicates.add(cb.equal(poRoot.get(ProductScanImageURIAudit_.key).get(ProductScanImageURIAuditKey_.id),
                root.get(ProductScanImageURI_.key).get(ProductScanImageURIKey_.id)));
        predicates.add(cb.equal(poRoot.get(ProductScanImageURIAudit_.key).get(ProductScanImageURIAuditKey_.sequenceNumber),
                root.get(ProductScanImageURI_.key).get(ProductScanImageURIKey_.sequenceNumber)));
        //Get image has active is true
        predicates.add(cb.equal(poRoot.get(ProductScanImageURIAudit_.activeSwitch), true));
        //Get image has status is approved
        predicates.add(cb.equal(cb.trim(poRoot.get(ProductScanImageURIAudit_.imageStatusCode)), ProductScanImageURI.IMAGE_STATUS_CD_APPROVED));
        //Get image has active online is false
        predicates.add(cb.equal(poRoot.get(ProductScanImageURIAudit_.activeOnline), false));

        //Get image has change on less than 10 months
        LocalDateTime date = LocalDateTime.now().minusMonths(10);
        predicates.add(cb.lessThan(poRoot.get(ProductScanImageURIAudit_.key).get(ProductScanImageURIAuditKey_.changedOn), date));
        //Get image has change on to approved after latest date image is active online or not approved
        predicates.add(cb.greaterThan(poRoot.get(ProductScanImageURIAudit_.key).get(ProductScanImageURIAuditKey_.changedOn),
                buildMaxLastUpdateForImageSubQueryPredicate(root, queryBuilder, cb)));

        productScanImageURIAuditExist.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        productScanImageURIAuditExist.groupBy(poRoot.get(ProductScanImageURIAudit_.key).get(ProductScanImageURIAuditKey_.id), poRoot.get(ProductScanImageURIAudit_.key).get(ProductScanImageURIAuditKey_.sequenceNumber));
        return productScanImageURIAuditExist;
    }

    /**
     * Get max latest change on date that the image's active online (true) or the image status is not Approved.
     *
     * @param root         The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder JPA query builder used to construct the sub-query.
     * @param cb           criteria builder, used to construct the various parts of the SQL statement.
     * @return Subquery<LocalDateTime>
     */
    private Subquery<LocalDateTime> buildMaxLastUpdateForImageSubQueryPredicate(Root<ProductScanImageURI> root,
                                                                            CriteriaQuery<?> queryBuilder, CriteriaBuilder cb) {
        Subquery<LocalDateTime> productScanImageURIAuditExist = queryBuilder.subquery(LocalDateTime.class);
        Root<ProductScanImageURIAudit> poRoot = productScanImageURIAuditExist.from(ProductScanImageURIAudit.class);
        //Select max date of ProductScanImageURIAudit has status is approved or active online is true
        Expression maxExpr = cb.greatest(poRoot.get(ProductScanImageURIAudit_.key).get(ProductScanImageURIAuditKey_.changedOn));
        productScanImageURIAuditExist.select(maxExpr);

        List<Predicate> predicates = new ArrayList<>();
        //Join ProductScanImageURIAudit with ProductScanImageURI
        predicates.add(cb.equal(poRoot.get(ProductScanImageURIAudit_.key).get(ProductScanImageURIAuditKey_.id),
                root.get(ProductScanImageURI_.key).get(ProductScanImageURIKey_.id)));
        predicates.add(cb.equal(poRoot.get(ProductScanImageURIAudit_.key).get(ProductScanImageURIAuditKey_.sequenceNumber),
                root.get(ProductScanImageURI_.key).get(ProductScanImageURIKey_.sequenceNumber)));
        //Get image has active is true
        predicates.add(cb.equal(root.get(ProductScanImageURI_.activeSwitch), true));
        //Query image has status code is Approve or Active Online is true
        predicates.add(cb.or(cb.notEqual(cb.trim(poRoot.get(ProductScanImageURIAudit_.imageStatusCode)), ProductScanImageURI.IMAGE_STATUS_CD_APPROVED), cb.equal(poRoot.get(ProductScanImageURIAudit_.activeOnline), true)));
        productScanImageURIAuditExist.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        //Group by  ProductScanImageURIAudit with id and sequence number
        productScanImageURIAuditExist.groupBy(poRoot.get(ProductScanImageURIAudit_.key).get(ProductScanImageURIAuditKey_.id), poRoot.get(ProductScanImageURIAudit_.key).get(ProductScanImageURIAuditKey_.sequenceNumber));
        return productScanImageURIAuditExist;
    }
}
