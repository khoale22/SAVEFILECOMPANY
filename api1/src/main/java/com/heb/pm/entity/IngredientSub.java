package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a dynamic attribute of a IngredientStatementDetail.
 *
 * @author m594201
 * @since 2.0.9
 */
@Entity
@Table(name = "sl_soi_ingredient")
public class IngredientSub implements Serializable{

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private IngredientSubKey key;

    @Column(name = "soi_sequence")
    private long soiSequence;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "so_ingredient_code", referencedColumnName = "ingredient_code", insertable = false,  updatable = false, nullable = false)
    private Ingredient ingredientMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_code", referencedColumnName = "ingredient_code", insertable = false,  updatable = false, nullable = false)
    private Ingredient subIngredient;

    /**
     * Gets the ingredient master.
     * @return the ingredient master
     */
    public Ingredient getIngredientMaster() {
        return ingredientMaster;
    }

    /**
     * Sets the ingredient master.
     * @param ingredientMaster the ingredient master
     */
    public void setIngredientMaster(Ingredient ingredientMaster) {
        this.ingredientMaster = ingredientMaster;
    }

    /**
     * Gets the sub ingredient.
     * @return the sub ingredient
     */
    public Ingredient getSubIngredient() {
        return subIngredient;
    }

    /**
     * Sets the sub ingredient.
     * @param subIngredient the sub ingredient
     */
    public void setSubIngredient(Ingredient subIngredient) {
        this.subIngredient = subIngredient;
    }

    /**
     * Gets soi key.
     *
     * @return the soi key
     */
    public IngredientSubKey getKey() {
        return key;
    }

    /**
     * Sets soi key.
     *
     * @param key the soi key
     */
    public void setKey(IngredientSubKey key) {
        this.key = key;
    }

    /**
     * Gets soi sequence.
     *
     * @return the soi sequence
     */
    public long getSoiSequence() {
        return soiSequence;
    }

    /**
     * Sets soi sequence.
     *
     * @param soiSequence the soi sequence
     */
    public void setSoiSequence(long soiSequence) {
        this.soiSequence = soiSequence;
    }

    /**
     * There are times when the key is empty (when adding a new one). This method will ensure
     * that the key has values.
     */
    @PrePersist
    public void setKeyValues() {
        if (this.getKey().getIngredientCode() == null || this.getKey().getIngredientCode().isEmpty() ||
                this.getKey().getIngredientCode().equals("0")) {
            this.getKey().setIngredientCode(this.subIngredient.getIngredientCode());
        }
        if (this.getKey().getSoIngredientCode() == null || this.getKey().getSoIngredientCode().isEmpty() ||
                this.getKey().getSoIngredientCode().equals("0")) {
            this.getKey().setSoIngredientCode(this.ingredientMaster.getIngredientCode());
        }
    }
}
