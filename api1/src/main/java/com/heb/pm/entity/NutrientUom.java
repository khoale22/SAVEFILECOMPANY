package com.heb.pm.entity;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import org.springframework.data.domain.Sort;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a dynamic attribute of a Nutrients unit of measure data.
 * @author m594201
 * @since 2.1.0
 */
@Entity
@Table(name = "pd_unit_measure")
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class NutrientUom implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String SORT_FIELD_SCALE_NUTRIENT_UOM_CD = "nutrientUomCode";
    public static final String SORT_FIELD_SCALE_NUTRIENT_UOM_CD_DES = "nutrientUomDescription";

	private static final String DISPLAY_NAME_FORMAT = "[%d]%s";

    private static final String INDEX_KEY_FORMAT = "%05d";

    /**
     * Maintenance Function for adding a unit of measure.
     */
    public static final char ADD = 'A';
    /**
     * Maintenance Function for modifying a unit of measure.
     */
    public static final char CHANGE = 'C';
    /**
     * Maintenance Function for removing a unit of measure.
     */
    public static final char DELETE = 'D';

    /**
     * System of Measure constant for the imperial system.
     */
    public static final char IMPERIAL = 'C';
    /**
     * System of Measure constant for the metric system.
     */
    public static final char METRIC = 'M';

    /**
     * Form constant for solids.
     */
    public static final char SOLID = 'S';
    /**
     * Form constant for liquids.
     */
    public static final char LIQUID = 'L';

	@Id
	@Column(name = "pd_lbl_uom_cd")
	private Long nutrientUomCode;

	@Column(name = "pd_uom_des")
	@Type(type="fixedLengthCharPK")
	private String nutrientUomDescription;

    @Column(name = "pd_uom_maint_sw")
    private Character maintenanceFunction;

    @Column(name = "pd_uom_maint_dt")
    private LocalDate maintenanceDate;

    @Column(name = "pd_uom_ext_des")
    private String extendedDescription;

    @Column(name = "pd_uom_typ_sw")
    private char systemOfMeasure;

    @Column(name = "pd_uom_form_sw")
    private char form;

    @Column(name="CRE8_TS")
    private LocalDateTime createDate;

    @Column(name="CRE8_UID")
    private String createUserId;

    @Column(name="LST_UPDT_TS")
    private LocalDateTime lastUpdateDate;

    @Column(name="LST_UPDT_UID")
    private String lastUpdateUserId;

    /**
     * Gets nutrient uom description.
     *
     * @return the nutrient uom description
     */
    public String getNutrientUomDescription() {
        return nutrientUomDescription;
    }

    /**
     * Sets nutrient uom description.
     *
     * @param nutrientUomDescription the nutrient uom description
     */
    public void setNutrientUomDescription(String nutrientUomDescription) {
        this.nutrientUomDescription = nutrientUomDescription;
    }

    /**
     * Returns the unique ID for this code.
     *
     * @return The unique ID for this code.
     */
    public Long getNutrientUomCode() {
        return nutrientUomCode;
    }

    /**
     * Sets the unique ID for this code.
     *
     * @param nutrientUomCode The unique ID for this code.
     */
    public void setNutrientUomCode(Long nutrientUomCode) {
        this.nutrientUomCode = nutrientUomCode;
    }

    /**
     * Returns the type of maintenance needs to be performed on this record. Constants are defined for the various
     * values. C means the measure has changed. A means the measure has been added. D means the measure is slated to
     * be deleted.
     *
     * @return The type of maintenance needs to be performed on this record.
     */
    public Character getMaintenanceFunction() {
        return maintenanceFunction;
    }

    /**
     * Sets the type of maintenance needs to be performed on this record.
     *
     * @param maintenanceFunction The type of maintenance needs to be performed on this record.
     */
    public void setMaintenanceFunction(Character maintenanceFunction) {
        this.maintenanceFunction = maintenanceFunction;
    }

    /**
     * Returns the date this record was last modified.
     *
     * @return The date this record was last modified.
     */
    public LocalDate getMaintenanceDate() {
        return maintenanceDate;
    }

    /**
     * Sets the date this record was last modified.
     *
     * @param maintenanceDate The date this record was last modified.
     */
    public void setMaintenanceDate(LocalDate maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    /**
     * Returns the extended description for this unit of measure.
     *
     * @return The extended description for this unit of measure.
     */
    public String getExtendedDescription() {
        return extendedDescription;
    }

    /**
     * Sets the extended description for this unit of measure.
     *
     * @param extendedDescription The extended description for this unit of measure.
     */
    public void setExtendedDescription(String extendedDescription) {
        this.extendedDescription = extendedDescription;
    }

    /**
     * Returns the system of measure for this unit of measure. It is either imperial or metric. Copnstants are defined
     * for these.
     *
     * @return The system of measure for this unit of measure.
     */
    public char getSystemOfMeasure() {
        return systemOfMeasure;
    }

    /**
     * Returns the system of measure for this unit of measure. It is either imperial or metric. Copnstants are defined
     * for these.
     *
     * @return The system of measure for this unit of measure.
     */
    public String getSystemOfMeasureDisplayText() {
        return systemOfMeasure == 'C' ? "Common" : "Metric";
    }

    /**
     * Sets the system of measure for this unit of measure.
     *
     * @param systemOfMeasure The system of measure for this unit of measure.
     */
    public void setSystemOfMeasure(char systemOfMeasure) {
        this.systemOfMeasure = systemOfMeasure;
    }

    /**
     * Returns the state of matter this unit of measure. Either a solid or a liquid. Constants are defined
     * for these.
     *
     * @return The state of matter this unit of measure.
     */
    public char getForm() {
        return form;
    }

    /**
     * Sets the state of matter this unit of measure.
     *
     * @param form The state of matter this unit of measure.
     */
    public void setForm(char form) {
        this.form = form;
    }

    /**
     * Get display name string.
     *
     * @return the string
     */
    public String getDisplayName(){
        return String.format(NutrientUom.DISPLAY_NAME_FORMAT, this.nutrientUomCode,
                this.nutrientUomDescription.trim());
    }

    /**
     * Gets normalized uom id.
     *
     * @return the normalized uom id
     */
    public String getNormalizedUomId() {
        return String.format(NutrientUom.INDEX_KEY_FORMAT, this.getNutrientUomCode());
    }
    /**
     * Returns the default sort order.
     *
     * @return The default sort order.
     */
    public static Sort getDefaultSort() {
        return new Sort(new Sort.Order(Sort.Direction.ASC, SORT_FIELD_SCALE_NUTRIENT_UOM_CD));
    }
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdateUserId() {
        return lastUpdateUserId;
    }

    public void setLastUpdateUserId(String lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "NutrientUom{" +
                "nutrientUomCode=" + nutrientUomCode +
                ", nutrientUomDescription='" + nutrientUomDescription + '\'' +
                ", maintenanceFunction=" + maintenanceFunction +
                ", maintenanceDate=" + maintenanceDate +
                ", extendedDescription='" + extendedDescription + '\'' +
                ", systemOfMeasure=" + systemOfMeasure +
                ", form=" + form +
                '}';
    }
}
