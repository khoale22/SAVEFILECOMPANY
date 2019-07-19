package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents a dynamic attribute of a IngredientStatementDetail.
 *
 * @author m594201
 * @since 2.0.9
 */
@Entity
@Table(name = "sl_ingrd_stmt_dtl")
public class IngredientStatementDetail implements Serializable{

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private IngredientStatementDetailsKey key;

    @Column(name = "pd_ingrd_pct")
    private double ingredientPercentage;

    @JsonIgnoreProperties("statementDetails")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pd_ingrd_cd", referencedColumnName = "ingredient_code", insertable = false, nullable = false, updatable = false)
    private Ingredient ingredient;

    @JsonIgnoreProperties("ingredientStatementDetails")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pd_ingrd_stmt_no", referencedColumnName = "pd_ingrd_stmt_no", insertable = false, updatable = false, nullable = false)
    private IngredientStatementHeader ingredientStatementHeader;

    /**
     * Gets ingredient statement header.
     *
     * @return the ingredient statement header
     */
    public IngredientStatementHeader getIngredientStatementHeader() {
        return ingredientStatementHeader;
    }

    /**
     * Sets ingredient statement header.
     *
     * @param ingredientStatementHeader the ingredient statement header
     */
    public void setIngredientStatementHeader(IngredientStatementHeader ingredientStatementHeader) {
        this.ingredientStatementHeader = ingredientStatementHeader;
    }

    /**
     * Gets ingredient.
     *
     * @return the ingredient
     */
    public Ingredient getIngredient() {
        return ingredient;
    }

    /**
     * Sets ingredient.
     *
     * @param ingredient the ingredient
     */
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public IngredientStatementDetailsKey getKey() {
        return key;
    }

    /**
     * Sets key.
     *
     * @param key the key
     */
    public void setKey(IngredientStatementDetailsKey key) {
        this.key = key;
    }

    /**
     * Gets ingredient percentage.
     *
     * @return the ingredient percentage
     */
    public double getIngredientPercentage() {
        return ingredientPercentage;
    }

    /**
     * Sets ingredient percentage.
     *
     * @param ingredientPercentage the ingredient percentage
     */
    public void setIngredientPercentage(double ingredientPercentage) {
        this.ingredientPercentage = ingredientPercentage;
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
        if (o == null || getClass() != o.getClass()) return false;

        IngredientStatementDetail that = (IngredientStatementDetail) o;

        return key != null ? key.equals(that.key) : that.key == null;
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
        return "IngredientStatementDetail{" +
                "key=" + key +
                ", ingredientPercentage=" + ingredientPercentage +
                '}';
    }
}
