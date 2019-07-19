package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * product group type entity.
 *
 * @author vn87351
 * @since 2.12.0
 */
@Entity
@Table(name = "prod_grp_typ")
public class ProductGroupType implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String PRODUCT_GROUP_TYPE_SUMMARY_FORMAT = "%s [%s]";
    private static final String DEFAULT_PRODUCT_GROUP_TYPE_SORT_FIELD = "productGroupType";

    @Id
    @Column(name = "prod_grp_typ_cd")
    private String productGroupTypeCode;

    @Column(name = "cust_prod_typ_nm")
    private String productGroupType;

    @Column(name = "str_dept_nbr")
    private String departmentNumberString;

    @Column(name = "str_sub_dept_id")
    private String subDepartmentId;

    @JsonIgnoreProperties("productGroupType")
    @OneToMany(mappedBy = "productGroupType",fetch = FetchType.LAZY)
    private List<ProductGroupChoiceType> productGroupChoiceTypes;

    @JsonIgnoreProperties("productGroupType")
    @OneToMany(mappedBy = "productGroupType",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<CustomerProductGroup> customerProductGroups;
    /**
     * Returns the productGroupTypeCode.
     *
     * @return the productGroupTypeCode.
     */
    public String getProductGroupTypeCode() {
        return productGroupTypeCode;
    }

    /**
     * Sets the productGroupTypeCode.
     *
     * @param productGroupTypeCode the productGroupTypeCode to set.
     */
    public void setProductGroupTypeCode(String productGroupTypeCode) {
        this.productGroupTypeCode = productGroupTypeCode;
    }

    /**
     * Returns the productGroupType.
     *
     * @return the productGroupType.
     */
    public String getProductGroupType() {
        return productGroupType;
    }

    /**
     * Sets the productGroupType.
     *
     * @param productGroupType the productGroupType to set.
     */
    public void setProductGroupType(String productGroupType) {
        this.productGroupType = productGroupType;
    }

    /**
     * Returns the departmentNumberString.
     *
     * @return the departmentNumberString.
     */
    public String getDepartmentNumberString() {
        return departmentNumberString;
    }

    /**
     * Sets the departmentNumberString.
     *
     * @param departmentNumberString the departmentNumberString to set.
     */
    public void setDepartmentNumberString(String departmentNumberString) {
        this.departmentNumberString = departmentNumberString;
    }

    /**
     * Returns the subDepartmentId.
     *
     * @return the subDepartmentId.
     */
    public String getSubDepartmentId() {
        return subDepartmentId;
    }

    /**
     * Sets the subDepartmentId.
     *
     * @param subDepartmentId the subDepartmentId to set.
     */
    public void setSubDepartmentId(String subDepartmentId) {
        this.subDepartmentId = subDepartmentId;
    }

    /**
     * get product group type summary.
     *
     * @return the formatted product group type summary.
     */
    public String getProductGroupTypeSummary() {
        return String.format(PRODUCT_GROUP_TYPE_SUMMARY_FORMAT, StringUtils.trimToEmpty(this.getProductGroupType()), String.valueOf(this.getProductGroupTypeCode()));
    }
    /**
     * Gets the list of product group choice types.
     *
     * @return the list of product group choice types.
     */
    public List<ProductGroupChoiceType> getProductGroupChoiceTypes() {
        return productGroupChoiceTypes;
    }

    /**
     * Sets the list of product group choice types.
     *
     * @param productGroupChoiceTypes the list of product group choice types.
     */
    public void setProductGroupChoiceTypes(List<ProductGroupChoiceType> productGroupChoiceTypes) {
        this.productGroupChoiceTypes = productGroupChoiceTypes;
    }

    /**
     * Get the list of cust product group.
     *
     * @return the list of cust product group
     */
    public List<CustomerProductGroup> getCustomerProductGroups() {
        return customerProductGroups;
    }

    /**
     * Sets the list of cust product group.
     *
     * @param customerProductGroups the list of cust product group.
     */
    public void setCustomerProductGroups(List<CustomerProductGroup> customerProductGroups) {
        this.customerProductGroups = customerProductGroups;
    }

    /**
     * The static method that get default sort by product group type code.
     *
     * @return the default sort by product group type code.
     */
    public static Sort getDefaultSort() {
        return new Sort(new Sort.Order(Sort.Direction.ASC, ProductGroupType.DEFAULT_PRODUCT_GROUP_TYPE_SORT_FIELD).ignoreCase());
    }

    /**
     * Compares this object to another for equality.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductGroupType)) return false;

        ProductGroupType that = (ProductGroupType) o;

        if (productGroupTypeCode != null ? !productGroupTypeCode.equals(that.productGroupTypeCode) : that.productGroupTypeCode != null)
            return false;
        if (productGroupType != null ? !productGroupType.equals(that.productGroupType) : that.productGroupType != null)
            return false;
        if (departmentNumberString != null ? !departmentNumberString.equals(that.departmentNumberString) : that.departmentNumberString != null)
            return false;
        return subDepartmentId != null ? subDepartmentId.equals(that.subDepartmentId) : that.subDepartmentId == null;
    }

    /**
     * Returns a hash code for this object. Equal objects have the same hash code. Unequal objects have
     * different hash codes.
     *
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = productGroupTypeCode != null ? productGroupTypeCode.hashCode() : 0;
        result = 31 * result + (productGroupType != null ? productGroupType.hashCode() : 0);
        result = 31 * result + (departmentNumberString != null ? departmentNumberString.hashCode() : 0);
        result = 31 * result + (subDepartmentId != null ? subDepartmentId.hashCode() : 0);
        return result;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return "ProductGroupType{" +
                "productGroupTypeCode='" + productGroupTypeCode + '\'' +
                ", productGroupType='" + productGroupType + '\'' +
                ", departmentNumberString='" + departmentNumberString + '\'' +
                ", subDepartmentId='" + subDepartmentId + '\'' +
                '}';
    }
}