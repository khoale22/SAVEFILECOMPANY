package com.heb.pm.entity;

import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import java.io.Serializable;
/**
 * The key for the customer friendly descriptions
 * that will be tied to the product master
 * Created by s753601 on 3/27/2017.
 * @since 2.4.0
 */
@Embeddable
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
//dB2Oracle changes vn00907
public class ProductDescriptionKey implements Serializable{

    private static final long serialVersionUID = 1L;

	private static final int FOUR_BYTES = 32;
	private static final int PRIME_NUMBER = 31;

    @Column(name="prod_id")
    private long productId;

    @Column(name = "lang_typ_cd")
    @Type(type="fixedLengthCharPK")    
    private String languageType;

    @Column(name = "des_typ_cd")
	@Type(type="fixedLengthCharPK")
	private String descriptionType;

    /**
     * Returns the product ID
     *
     * @return product ID
     */
    public long getProductId() {
        return productId;
    }

    /**
     * Sets a new product id
     *
     * @param productId the new value for the product id
     */
    public void setProductId(long productId) {
        this.productId = productId;
    }

    /**
     * Returns the langugage the description is in
     *
     * @return the description's language
     */
    public String getLanguageType() {
        return languageType;
    }

    /**
     * Updates the language for a description
     *
     * @param languageType the new descriptioin language
     */
    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

	/**
	 * Returns the type of description
	 * @return description type
	 */
	public String getDescriptionType() {
		return descriptionType;
	}

	/**
	 * Updates the description type
	 * @param descriptionType new description*/
	public void setDescriptionType(String descriptionType) {
		this.descriptionType = descriptionType;
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

        ProductDescriptionKey that = (ProductDescriptionKey) o;

        if (productId != that.productId) return false;
        return languageType != null ? languageType.equals(that.languageType) : that.languageType == null;
    }

    /**
     * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
     * they will (probably) have different hashes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = (int) (productId ^ (productId >>> ProductDescriptionKey.FOUR_BYTES));
        result = ProductDescriptionKey.PRIME_NUMBER * result + (languageType != null ? languageType.hashCode() : 0);
        return result;
    }

    /**
     * Returns a String representation of the object.
     *
     * @return A String representation of the object.
     */
    @Override
    public String toString() {
        return "ProductDescriptionKey{" +
                "productId=" + productId +
                ", languageType='" + languageType + '\'' +
                '}';
    }
}
