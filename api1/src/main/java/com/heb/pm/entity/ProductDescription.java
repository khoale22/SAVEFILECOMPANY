package com.heb.pm.entity;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Ties customer friendly descriptions to the product master
 *
 * Created by s753601 on 3/24/2017.
 * @since 2.4.0
 */
@Entity
@Table(name="prod_desc_txt")
// dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class ProductDescription implements Serializable{

    private static final long serialVersionUID = 1L;

	public static final String ENGLISH = "ENG";
	public static final String SPANISH = "SPN";

    @EmbeddedId
    private ProductDescriptionKey key;

    @Column(name="prod_des")
    private String description;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="des_typ_cd", referencedColumnName = "des_typ_cd", insertable = false, updatable = false),
	})
	private DescriptionType descriptionType;

    /**
     * Returns the key for the ProductDescription object
     *
     * @return the eky for the ProductDescription object
     */
    public ProductDescriptionKey getKey() {
        return key;
    }

    /**
     * Sets the key for the ProductDescription object
     *
     * @param key The key for the ProductDescription object
     */
    public void setKey(ProductDescriptionKey key) {
        this.key = key;
    }

    /**
     * Returns the description of the product
     * @return the description of the product
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for the product
     * @param description the new description for the product
     */
    public void setDescription(String description) {
        this.description = description;
    }

	/**
	 * Returns the DescriptionType. The description type holds all of the information for the current description type
	 * including the length. The code table for description type.
	 *
	 * @return DescriptionType
	 */
	public DescriptionType getDescriptionType() {
		return descriptionType;
	}

	/**
	 * Sets the DescriptionType
	 *
	 * @param descriptionType The DescriptionType
	 */
	public void setDescriptionType(DescriptionType descriptionType) {
		this.descriptionType = descriptionType;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductDescription{" +
				"key=" + key +
				", description='" + description + '\'' +
				'}';
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

		ProductDescription that = (ProductDescription) o;

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
	 * Creates a ProductDescription from the required parameters.
	 *
	 * @param productId The product ID the description is for.
	 * @param descriptionType The type of description it is.
	 * @param languageType The language code of the description.
	 * @param description The description.
	 * @return A ProductDescription
	 */
	public static ProductDescription from(long productId, String descriptionType, String languageType, String description) {

		ProductDescription productDescription = new ProductDescription();
		productDescription.setKey(new ProductDescriptionKey());
		productDescription.getKey().setProductId(productId);
		productDescription.getKey().setDescriptionType(descriptionType);
		productDescription.getKey().setLanguageType(languageType);
		productDescription.setDescription(description);
		return productDescription;
	}

}
