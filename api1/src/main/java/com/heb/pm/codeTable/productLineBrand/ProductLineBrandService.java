package com.heb.pm.codeTable.productLineBrand;

import com.heb.pm.entity.*;
import com.heb.pm.entity.ProductLineBrand;
import com.heb.pm.entity.ProductLineBrandKey;
import com.heb.pm.repository.*;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductLineBrandService {

    private static final Logger logger = LoggerFactory.getLogger(ProductLineBrandService.class);

    @Autowired
    private ProductLineBrandRepository repository;

    @Autowired
    private ProductLineBrandRepositoryWithCount repositoryWithCount;

    @Autowired
    ProductMatRepository productMatRepository;

    @Autowired
    ProductMasterRepository productMasterRepository;

    private static final String QUERY_SIZE_MESSAGE = "found %d pages with %d entries each with id like %s and description like %s";

    private static final String DESCRIPTION_REGEX = "%%%s%%";

    private static final String TEXT_EXPORT_FORMAT = "\"%s\",";
    private static final String NEWLINE_TEXT_EXPORT_FORMAT = "\n";
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final String CSV_HEADING = "Line-Brand ID, Line-Brand";





    /**
     * Get all records of PROD_SUB_BRND table by heb pagination.
     *
     * @param page             the page number.
     * @param pageSize         the page size.
     * @param id               the product brand id to search.
     * @param description      the product line description to search.
     * @return the page of product brands.
     */
    public PageableResult<ProductLineBrand> findProductLineBrandsByPage(int page, int pageSize, String id,
                                                                        String description, boolean includeCount) {

        Sort s =  new Sort(
                new Sort.Order(Sort.Direction.ASC, "key.brandId"));
        Pageable pageRequest = new PageRequest(page, pageSize, s);
        PageableResult<ProductLineBrand> results;
        if (includeCount) {
            Page<ProductLineBrand> prodLineBrands = this.findProductLineBrandsWithCount(id, description, pageRequest);
            results = new PageableResult<>(pageRequest.getPageNumber(), prodLineBrands.getTotalPages(),
                    prodLineBrands.getTotalElements(), prodLineBrands.getContent());
        } else {
            List<ProductLineBrand> productSubBrands = this.findProductLineBrandsWithoutCount(id, description, pageRequest);
            results = new PageableResult<>(pageRequest.getPageNumber(), productSubBrands);
        }

        return results;
    }


    /**
     * Get all product lines with pagination.
     *
     * @param id The id to search.
     * @param description The description to search.
     * @param pageRequest The page request for pagination.
     * @return the page of product brands.
     */
    private Page<ProductLineBrand> findProductLineBrandsWithCount(String id, String description, Pageable pageRequest) {
        Page<ProductLineBrand> results;
        if (!StringUtils.isEmpty(id) && StringUtils.isEmpty(description)) {
            results = this.repositoryWithCount.findAllBySearchedBrand(
                    String.format(DESCRIPTION_REGEX, id.toUpperCase()),
                    pageRequest);
        } else if (StringUtils.isEmpty(id) && !StringUtils.isEmpty(description)) {
            results = this.repositoryWithCount.findAllBySearchedLine(
                    String.format(DESCRIPTION_REGEX, description.toUpperCase()),
                    pageRequest);
        } else if (!StringUtils.isEmpty(id) && !StringUtils.isEmpty(description)) {
            results = this.repositoryWithCount.findAllBySearchedLineAndSearchedBrand(
                    String.format(DESCRIPTION_REGEX, id.toUpperCase()),
                    String.format(DESCRIPTION_REGEX, description.toUpperCase()),
                    pageRequest);
        } else {
            results = this.repositoryWithCount.findAll(pageRequest);
        }
        this.logQuerySize(id, description, results.getTotalPages(), pageRequest.getPageSize());
        return results;
    }

    /**
     * Get all product lines with pagination.
     *
     * @param id The id to search.
     * @param description The description to search.
     * @param pageRequest The page request for pagination.
     * @return the page of product brands.
     */
    private List<ProductLineBrand> findProductLineBrandsWithoutCount(String id, String description, Pageable pageRequest) {
        List<ProductLineBrand> results;
        if (!StringUtils.isEmpty(id) && StringUtils.isEmpty(description)) {
            results = this.repository.findAllBySearchedBrand(
                    String.format(DESCRIPTION_REGEX, id.toUpperCase()),
                    pageRequest);
        } else if (StringUtils.isEmpty(id) && !StringUtils.isEmpty(description)) {
            results = this.repository.findAllBySearchedLine(
                    String.format(DESCRIPTION_REGEX, description.toUpperCase()),
                    pageRequest);
        }  else if (!StringUtils.isEmpty(id) && !StringUtils.isEmpty(description)) {
            results = this.repository.findAllBySearchedLineAndSearchedBrand(
                    String.format(DESCRIPTION_REGEX, id.toUpperCase()),
                    String.format(DESCRIPTION_REGEX, description.toUpperCase()),
                    pageRequest);
        } else {
            results = this.repository.findAllByPage(pageRequest);
        }
        return results;
    }


    /**
     * Filter product brands with pagination.
     *
     * @param page              the page number.
     * @param pageSize          the page size.
     * @param searchedBrand     the product brand description to search.
     * @return                  the page of product brands.
     */
    public PageableResult<ProductLineBrand> findAllByKeyBrandIdOrKeyBrandDescIgnoreCase(int page, int pageSize, String searchedBrand, boolean includeCount) {
        Pageable pageRequest = new PageRequest(page, pageSize);
        PageableResult<ProductLineBrand> results;
        if (includeCount) {
            Page<ProductLineBrand> productLineBrands = this.repositoryWithCount.findAllBySearchedBrand(
                    String.format(DESCRIPTION_REGEX, searchedBrand.toUpperCase()),
                    pageRequest);
            results = new PageableResult<>(pageRequest.getPageNumber(), productLineBrands.getTotalPages(),
                    productLineBrands.getTotalElements(), productLineBrands.getContent());
        } else {
            List<ProductLineBrand> productBrands = this.repository.findAllBySearchedBrand(searchedBrand, pageRequest);
            results = new PageableResult<>(pageRequest.getPageNumber(), productBrands);
        }
        return results;
    }

    /**
     * Filter product brands with pagination.
     *
     * @param page         the page number.
     * @param pageSize     the page size.
     * @param searchedLine the line id or description to search against.
     * @return the page of product brands.
     */
    public PageableResult<ProductLineBrand> findAllByKeyLineIdOrKeyLineDescIgnoreCase(int page, int pageSize, String searchedLine, boolean includeCount) {
        Pageable pageRequest = new PageRequest(page, pageSize);
        PageableResult<ProductLineBrand> results;
        if (includeCount) {
            Page<ProductLineBrand> productLineBrands = this.repositoryWithCount.findAllBySearchedLine(
                    String.format(DESCRIPTION_REGEX, searchedLine.toUpperCase()),
                    pageRequest);
            results = new PageableResult<>(pageRequest.getPageNumber(), productLineBrands.getTotalPages(),
                    productLineBrands.getTotalElements(), productLineBrands.getContent());
        } else {
            List<ProductLineBrand> productBrands = this.repository.findAllBySearchedLine(searchedLine, pageRequest);
            results = new PageableResult<>(pageRequest.getPageNumber(), productBrands);
        }
        return results;
    }


    /**
     * Deletes a product line. Throws exception if the product line is tied to a product.
     * @param key the ProductLineBrand key to extract values from.
     */
    public ProductLineBrandKey deleteProductLineBrand(ProductLineBrandKey key) {
        long id=key.getBrandId();
        String description=key.getLineCode();
        if(!hasNoAttachedProduct(id, description)) {
            throw new IllegalArgumentException("Products currently tied. " +
                    "Remove all attached products from Product Line Brand before attempting to delete.");
        }
        ProductLineBrand productLineBrandToDelete = this.repository.findOne(key);
        if(productLineBrandToDelete != null) {
            this.repository.delete(productLineBrandToDelete);
        }
        return key;
    }

    private boolean hasNoAttachedProduct(long id, String description){
        boolean isLineEmpty=(productMatRepository.countByProductLineCode(description)==0);
        boolean isBrandEmpty=(productMasterRepository.countByProdBrandId(id)==0);
        return (isLineEmpty&&isBrandEmpty);
    }

    public ProductLineBrandKey addProductLineBrand(ProductLineBrandKey productLineBrandKey, String userId)
    {
        if(!productLineBrandAlreadyExists(productLineBrandKey)){
            ProductLineBrand productLineBrand = new ProductLineBrand().setUserId(userId).setTimeStamp(LocalDateTime.now()).setKey(productLineBrandKey);
            this.repository.save(productLineBrand);
            return productLineBrandKey;
        }
        throw new IllegalArgumentException("Product currently exists. " +
                "Enter unique id-description pair.");
    }

    public boolean productLineBrandAlreadyExists(ProductLineBrandKey key){
        return(this.repository.findOne(key) != null);
    }


    public void exportCsv(HttpServletResponse response, String subBrandId, String subBrandName){
        Pageable pageRequest = new PageRequest(0, DEFAULT_PAGE_SIZE);
        Page<ProductLineBrand> results = findProductLineBrandsWithCount(subBrandId, subBrandName, pageRequest);
        try{
            for (int pageNo = 0; pageNo < results.getTotalPages(); pageNo++) {
                if (pageNo == 0) {
                    response.getOutputStream().println(this.getHeading());
                }
                response.getOutputStream().print(this.createCsv(
                        pageNo, DEFAULT_PAGE_SIZE,
                        subBrandId, subBrandName));
            }}catch (IOException e) {
            ProductLineBrandService.logger.error(e.getMessage());
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }
    }
    /**
     * Creates a CSV string from a list of product sub brands.
     *
     * @param pageNo        a list of product sub brands.
     * @param pageSize      a list of product sub brands.
     * @param subBrandId    a list of product sub brands.
     * @param subBrandName  a list of product sub brands.
     * @return a CSV string with product sub brand information.
     */
    public String createCsv(int pageNo, int pageSize, String subBrandId, String subBrandName) {
        PageableResult<ProductLineBrand> productLineBrands = findProductLineBrandsByPage(pageNo, pageSize,
                subBrandId, subBrandName, Boolean.FALSE);
        StringBuilder csv = new StringBuilder();
        for (ProductLineBrand productLineBrand : productLineBrands.getData()) {
            csv.append(String.format(TEXT_EXPORT_FORMAT, productLineBrand.getUserId()));
            csv.append(String.format(TEXT_EXPORT_FORMAT, productLineBrand.getKey().getLineCode()));

            csv.append(NEWLINE_TEXT_EXPORT_FORMAT);
        }
        return csv.toString();
    }

    /**
     * Logs total number of pages, pagesize, id, and description.
     *
     * @param id            the id to search.
     * @param description   the description to search.
     * @return the page of product brands.
     */
    private void logQuerySize(String id, String description, int pages, int pagesize)
    {
        ProductLineBrandService.logger.info(String.format(ProductLineBrandService.QUERY_SIZE_MESSAGE,
                pages, pagesize, id, description));
    }

    /**
     * Returns the heading to the CSV.
     *
     * @return The heading to the CSV.
     */
    private String getHeading() {
        return CSV_HEADING;
    }

}
