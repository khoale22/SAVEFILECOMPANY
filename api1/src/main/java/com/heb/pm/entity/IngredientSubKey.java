package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.io.Serializable;

/**
 * Represents a dynamic attribute of a IngredientSubKey
 *
 * @author m594201
 * @since 2.0.9
 */
@Embeddable
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class IngredientSubKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "so_ingredient_code")
  //db2o changes  vn00907
    @Type(type="fixedLengthCharPK")  
    private String soIngredientCode;

    @Column(name = "ingredient_code")
  //db2o changes  vn00907
  @Type(type="fixedLengthCharPK")  
    private String ingredientCode;

    /**
     * Gets so ingredient code.
     *
     * @return the so ingredient code
     */
    public String getSoIngredientCode() {
        return soIngredientCode;
    }

    /**
     * Sets so ingredient code.
     *
     * @param soIngredientCode the so ingredient code
     */
    public void setSoIngredientCode(String soIngredientCode) {
        this.soIngredientCode = soIngredientCode;
    }

    /**
     * Gets ingredient code.
     *
     * @return the ingredient code
     */
    public String getIngredientCode() {
        return ingredientCode;
    }

    /**
     * Sets ingredient code.
     *
     * @param ingredientCode the ingredient code
     */
    public void setIngredientCode(String ingredientCode) {
        this.ingredientCode = ingredientCode;
    }

	/**
	 * Compares another object to this one. This is a deep compare.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		IngredientSubKey that = (IngredientSubKey) o;

		if (soIngredientCode != null ? !soIngredientCode.equals(that.soIngredientCode) : that.soIngredientCode != null)
			return false;
		return ingredientCode != null ? ingredientCode.equals(that.ingredientCode) : that.ingredientCode == null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		int result = soIngredientCode != null ? soIngredientCode.hashCode() : 0;
		result = 31 * result + (ingredientCode != null ? ingredientCode.hashCode() : 0);
		return result;
	}
}
