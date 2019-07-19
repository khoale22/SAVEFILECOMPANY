package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.io.Serializable;

/**
 * Represents a dynamic attribute of a IngredientStatementDetailsKey.
 *
 * @author m594201
 * @since 2.0.9
 */
@Embeddable
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthVarChar", typeClass = com.heb.pm.util.oracle.OracleEmptySpaceVarCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class IngredientStatementDetailsKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int FOUR_BYTES = 32;
    private static final int PRIME_NUMBER = 31;

    @Column(name = "pd_ingrd_stmt_no")
    private long statementNumber;

    @Column(name = "pd_ingrd_cd")
    @Type(type="fixedLengthCharPK")
    private String ingredientCode;

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
     * Gets statement number.
     *
     * @return the statement number
     */
    public long getStatementNumber() {
        return statementNumber;
    }

    /**
     * Sets statement number.
     *
     * @param statementNumber the statement number
     */
    public void setStatementNumber(long statementNumber) {
        this.statementNumber = statementNumber;
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

		IngredientStatementDetailsKey that = (IngredientStatementDetailsKey) o;

		if (statementNumber != that.statementNumber) return false;
		return ingredientCode != null ? ingredientCode.equals(that.ingredientCode) : that.ingredientCode == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = (int) (statementNumber ^ (statementNumber >>> IngredientStatementDetailsKey.FOUR_BYTES));
		result = IngredientStatementDetailsKey.PRIME_NUMBER * result +
				(ingredientCode != null ? ingredientCode.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "IngredientStatementDetailsKey{" +
				"statementNumber=" + statementNumber +
				", ingredientCode='" + ingredientCode + '\'' +
				'}';
	}
}