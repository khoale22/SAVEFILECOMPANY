package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents Associate UPCs.
 *
 * @author s573181
 * @since 2.0.1
 */
@Entity
@Table(name = "pd_associated_upc")
public class AssociatedUpc implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int FOUR_BYTES = 32;

	@Id
	@Column(name = "pd_assoc_upc_no")
	private long upc;
	@Column(name="pd_upc_no")
	private long pdUpcNo;
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnoreProperties("associateUpcs")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pd_upc_no", insertable = false, updatable = false)
	private PrimaryUpc primaryUpc;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pd_assoc_upc_no", referencedColumnName = "scn_cd_id", insertable = false, updatable = false, nullable = false)
	private SellingUnit sellingUnit;

	/**
	 * Returns the associate's UPC.
	 *
	 * @return The associate's UPC.
	 */
	public long getUpc() {
		return upc;
	}

	/**
	 * Sets the associate's UPC.
	 *
	 * @param upc The associate's UPC.
	 */
	public void setUpc(long upc) {
		this.upc = upc;
	}

	/**
	 * Returns the primary UPC tied to this associate.
	 *
	 * @return The primary UPC tied to this associate.
	 */
	public PrimaryUpc getPrimaryUpc() {
		return primaryUpc;
	}

	/**
	 * Sets the primary UPC tied to this associate.
	 *
	 * @param primaryUpc The primary UPC tied to this associate.
	 */
	public void setPrimaryUpc(PrimaryUpc primaryUpc) {
		this.primaryUpc = primaryUpc;
	}

	/**
	 * Returns the SellingUnit tied to this associate UPC.
	 *
	 * @return The SellingUnit tied to this associate UPC.
	 */
	public SellingUnit getSellingUnit() {
		return sellingUnit;
	}

	/**
	 * Sets the SellingUnit tied to this associate UPC.
	 *
	 * @param sellingUnit The SellingUnit tied to this associate UPC.
	 */
	public void setSellingUnit(SellingUnit sellingUnit) {
		this.sellingUnit = sellingUnit;
	}
	/**
	 * Returns upd upc no
	 * @return upd upc no
	 */
	public long getPdUpcNo() {
		return pdUpcNo;
	}

	/**
	 * Sets upd upc no
	 * @param pdUpcNo the upd upc no.
	 */
	public void setPdUpcNo(long pdUpcNo) {
		this.pdUpcNo = pdUpcNo;
	}
	/**
	 * Compares another object to this one. If that object is an the associatedUpc, it uses they keys to determine if
	 * they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AssociatedUpc that = (AssociatedUpc) o;

		return upc == that.upc;

	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return (int) (upc ^ (upc >>> FOUR_BYTES));
	}

	/**
	 * Returns a printable representation of the object.
	 *
	 * @return A printable representation of the object.
	 */
	@Override
	public String toString() {
		return "AssociatedUpc{" +
				"upc='" + upc + '\'' +
				'}';
	}
}
