package com.heb.pm.entity;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.io.Serializable;

/**
 * Represents a dynamic attribute of an Ingredient Category
 *
 * @author m594201
 * @since 2.0.9
 */
@Entity
@Table(name = "sl_ingrd_cat_grp")
// dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleCharType.class), 
@TypeDef(name = "fixedLengthVarChar", typeClass = com.heb.pm.util.oracle.OracleEmptySpaceVarCharType.class) 
}) 


// dB2Oracle changes vn00907
public class IngredientCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String DISPLAY_NAME_FORMAT = "%s[%d]";

    @Id
    @Column(name = "pd_ingrd_cat_cd")
    private long categoryCode;

    @Column(name = "pd_ingrd_cat_des")
    @Type(type="fixedLengthChar")    
    private String categoryDescription;

    /**
     *
     * Gets category code
     *
     * @return the category code
     */
    public long getCategoryCode() {
        return categoryCode;
    }

    /**
     * Sets category code.
     *
     * @param categoryCode the category code
     */
    public void setCategoryCode(long categoryCode) {
        this.categoryCode = categoryCode;
    }

    /**
     * Gets category description
     *
     * @return the category description
     */
    public String getCategoryDescription() {
        return categoryDescription;
    }

    /**
     * Sets category description.
     *
     * @param categoryDescription the category description
     */
    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    /**
     * Get display text string.
     *
     * @return the string
     */
    public String getDisplayText(){

        return String.valueOf(this.categoryCode).concat("-").concat(this.categoryDescription);
    }

    /**
     * Get normalized id string.
     *
     * @return the string
     */
    public String getNormalizedId(){return String.valueOf(this.categoryCode);}

    /**
     * Get display name string.
     *
     * @return the string
     */
    public String getDisplayName(){
        return String.format(IngredientCategory.DISPLAY_NAME_FORMAT, this.categoryDescription.trim(),
                this.categoryCode);
    }
}
