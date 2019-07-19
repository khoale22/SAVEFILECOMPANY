/*
 * ProductSubBrand
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.domain.Sort;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents a product line.
 *
 * @author m314029
 * @since 2.26.0
 */
@Entity
@Table(name = "PROD_LIN")
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class ProductLine implements Serializable {

    private static final long serialVersionUID = 1L;
	private static final String DISPLAY_NAME_FORMAT = "%s[%s]";
    private static final String DEFAULT_PRODUCT_LINE_SORT_FIELD = "id";

    @Id
    @Column(name = "PROD_LIN_CD")
    private String id;

	@Type(type="fixedLengthCharPK")
    @Column(name = "PROD_LIN_DES")
    private String description;


	/**
	 * Returns Id.
	 *
	 * @return The Id.
	 **/
	public String getId() {
		return id;
	}

	/**
	 * Sets the Id.
	 *
	 * @param id The Id.
	 **/
	public ProductLine setId(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Returns Description.
	 *
	 * @return The Description.
	 **/
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the Description.
	 *
	 * @param description The Description.
	 **/
	public ProductLine setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
     * Returns the default sort order for the product line table.
     *
     * @return The default sort order for the product line table.
     */
    public static Sort getDefaultSort() {
        return new Sort(
                new Sort.Order(Sort.Direction.ASC, ProductLine.DEFAULT_PRODUCT_LINE_SORT_FIELD)
        );
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

		ProductLine that = (ProductLine) o;

		return id != null ? id.equals(that.id) : that.id == null;
	}

	public String getDisplayName() {
		return String.format(ProductLine.DISPLAY_NAME_FORMAT, this.description,this.id);
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductLine{" +
				"id='" + id + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
