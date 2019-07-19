/*
 * StoreDepartmentAudit
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

        import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
        import org.hibernate.annotations.Type;
        import org.hibernate.annotations.TypeDef;
        import org.hibernate.annotations.TypeDefs;
        import org.springframework.data.domain.Sort;

        import javax.persistence.*;
        import java.io.Serializable;
        import java.util.List;

/**
 * Represents sub-departments in the product hierarchy.
 *
 * @author vn70633
 * @since 2.0.5
 */
@Entity

@Table(name = "str_dept")

@TypeDefs({
        @TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class StoreDepartmentAudit implements Serializable {

    // default constructor
    public StoreDepartmentAudit(){super();}

    // copy constructor
    // this does not call 'setDepartmentMaster' or 'setItemClasses' to prevent infinite loops -- if these methods need
    // to be called, they need to be done after creating all of the objects
    public StoreDepartmentAudit(StoreDepartmentAudit storeDepartmentAudit){
        super();
        this.setKey(new SubDepartmentKey(storeDepartmentAudit.getKey()));
        this.setName(storeDepartmentAudit.getName());
        this.setReportGroupCode(storeDepartmentAudit.getReportGroupCode());
        this.setGrossProfitLow(storeDepartmentAudit.getGrossProfitLow());
        this.setGrossProfitHigh(storeDepartmentAudit.getGrossProfitHigh());
        this.setShrinkLow(storeDepartmentAudit.getShrinkLow());
        this.setShrinkHigh(storeDepartmentAudit.getShrinkHigh());
    }

    private static final long serialVersionUID = 1L;
    /**
     * The constant DISPLAY_NAME_FORMAT.
     */
    public static final String DISPLAY_NAME_FORMAT = "%s[%s%s]";
    /**
     * The constant NORMALIZED_ID_FORMAT.
     */
    public static final String NORMALIZED_ID_FORMAT = "%s%s";

    /**
     * The constant DEPARTMENT_SORT_FIELD.
     */
    public static final String DEPARTMENT_SORT_FIELD = "name";

    @EmbeddedId
    private SubDepartmentKey key;

    @Column(name="dept_nm")
    //db2o changes  vn00907
    @Type(type="fixedLengthChar")
    private String name;

    @Column(name="rept_grp_cd")
    private Long reportGroupCode;

    @Column(name="grprft_lo_pct", precision = 7, scale = 4)
    private Double grossProfitLow;

    @Column(name="grprft_hi_pct", precision = 7, scale = 4)
    private Double grossProfitHigh;

    @Column(name="shrnk_lo_pct", precision = 7, scale = 4)
    private Double shrinkLow;

    @Column(name="shrnk_hi_pct", precision = 7, scale = 4)
    private Double shrinkHigh;

    @JsonIgnoreProperties({"subDepartmentList","viewableSellingRestrictions","viewableShippingRestrictions"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "str_dept_nbr", referencedColumnName = "str_dept_nbr", insertable = false, updatable = false)
    private Department departmentMaster;

    @Transient
    @JsonIgnoreProperties("subDepartmentMaster")
    private List<ItemClass> itemClasses;

    /**
     * Gets report group code.
     *
     * @return the report group code
     */
    public Long getReportGroupCode() {
        return reportGroupCode;
    }

    /**
     * Sets report group code.
     *
     * @param reportGroupCode the report group code
     */
    public void setReportGroupCode(Long reportGroupCode) {
        this.reportGroupCode = reportGroupCode;
    }

    /**
     * Gets gross profit low.
     *
     * @return the gross profit low
     */
    public Double getGrossProfitLow() {
        return grossProfitLow;
    }

    /**
     * Sets gross profit low.
     *
     * @param grossProfitLow the gross profit low
     */
    public void setGrossProfitLow(Double grossProfitLow) {
        this.grossProfitLow = grossProfitLow;
    }

    /**
     * Gets gross profit high.
     *
     * @return the gross profit high
     */
    public Double getGrossProfitHigh() {
        return grossProfitHigh;
    }

    /**
     * Sets gross profit high.
     *
     * @param grossProfitHigh the gross profit high
     */
    public void setGrossProfitHigh(Double grossProfitHigh) {
        this.grossProfitHigh = grossProfitHigh;
    }

    /**
     * Gets shrink low.
     *
     * @return the shrink low
     */
    public Double getShrinkLow() {
        return shrinkLow;
    }

    /**
     * Sets shrink low.
     *
     * @param shrinkLow the shrink low
     */
    public void setShrinkLow(Double shrinkLow) {
        this.shrinkLow = shrinkLow;
    }

    /**
     * Gets shrink high.
     *
     * @return the shrink high
     */
    public Double getShrinkHigh() {
        return shrinkHigh;
    }

    /**
     * Sets shrink high.
     *
     * @param shrinkHigh the shrink high
     */
    public void setShrinkHigh(Double shrinkHigh) {
        this.shrinkHigh = shrinkHigh;
    }

    /**
     * Gets item classes.
     *
     * @return the item classes
     */
    public List<ItemClass> getItemClasses() {
        return itemClasses;
    }

    /**
     * Sets item classes.
     *
     * @param itemClasses the item classes
     */
    public void setItemClasses(List<ItemClass> itemClasses) {
        this.itemClasses = itemClasses;
    }

    /**
     * Gets department master.
     *
     * @return the department master
     */
    public Department getDepartmentMaster() {
        return departmentMaster;
    }

    /**
     * Sets department master.
     *
     * @param departmentMaster the department master
     */
    public void setDepartmentMaster(Department departmentMaster) {
        this.departmentMaster = departmentMaster;
    }

    /**
     * Returns the key for the object.
     *
     * @return The key for the object.
     */
    public SubDepartmentKey getKey() {
        return key;
    }

    /**
     * Sets the key for the object.
     *
     * @param key The key for the object.
     */
    public void setKey(SubDepartmentKey key) {
        this.key = key;
    }

    /**
     * Returns the name of the department or sub-department.
     *
     * @return The name of the department or sub-department.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the department or sub-department.
     *
     * @param name name of the department or sub-department.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the sub-department as it should be displayed on the GUI.
     *
     * @return A String representation of the sub-department as it is meant to be displayed on the GUI.
     */
    public String getDisplayName() {

        return String.format(SubDepartment.DISPLAY_NAME_FORMAT,
                this.name.trim(), this.key.getDepartment().trim(), this.key.getSubDepartment().trim());
    }

    /**
     * Returns a unique ID for this sub-department as a string. This can be used in things like lists where
     * you need a unique key and cannot access the components of the key directly.
     *
     * @return A unique ID for this sub-department as a string
     */
    public String getNormalizedId() {
        return String.format(SubDepartment.NORMALIZED_ID_FORMAT, this.key.getDepartment().trim(),
                this.key.getSubDepartment().trim());
    }

    /**
     * Compares another object to this one. The key is the only thing used to determine equality.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreDepartmentAudit)) return false;

        StoreDepartmentAudit that = (StoreDepartmentAudit) o;

        return !(key != null ? !key.equals(that.key) : that.key != null);

    }

    /**
     * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
     * they will (probably) have different hashes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    /**
     * Returns a String representation of the object.
     *
     * @return A String representation of the object.
     */
    @Override
    public String toString() {
        return "SubDepartment{" +
                "key=" + key +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * Returns the default sort order for the SubDepartment table.
     *
     * @return The default sort order for the SubDepartment table.
     */
    public static Sort getDefaultSort() {
        return  new Sort(
                new Sort.Order(Sort.Direction.ASC, SubDepartment.DEPARTMENT_SORT_FIELD)
        );
    }
}

