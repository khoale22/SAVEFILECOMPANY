package com.heb.pm.entity;

import com.heb.util.audit.Audit;
import com.heb.util.audit.AuditableField;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This entity ties products and selling restrictions audits together
 */
@Entity
@Table(name="prod_sals_rstr_aud")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ProductRestrictionsAudit implements Audit, Serializable{

	@EmbeddedId
	private ProductRestrictionsAuditKey key;

	@AuditableField(displayName = "Restrictions", filter = {FilterConstants.RATING_RESTRICTIONS_AUDIT, FilterConstants.SHIPPING_HANDLING_AUDIT})
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SALS_RSTR_CD", referencedColumnName = "SALS_RSTR_CD", insertable = false, updatable = false, nullable = false)
	private SellingRestrictionCode restriction;

	/**
	 * Gets action code.
	 *
	 * @return the action code
	 */
	public String getAction() {
		return key.getAction();
	}

	/**
	 * Sets action code.
	 *
	 * @param actionCode the action code
	 */
	public void setAction(String actionCode) {
		key.setAction(actionCode);
	}

	/**
	 * Returns the unique identifier for a product's restriction
	 * @return key
	 */
	public ProductRestrictionsAuditKey getKey() {
		return key;
	}

	/**
	 * Updates the key
	 * @param key the new key
	 */
	public void setKey(ProductRestrictionsAuditKey key) {
		this.key = key;
	}

	/**
	 * Returns a detailed restriction
	 * @return restriction
	 */
	public SellingRestrictionCode getRestriction() {
		return restriction;
	}

	/**
	 * Updates the restriction
	 * @param restriction the new restriction
	 */
	public void setRestriction(SellingRestrictionCode restriction) {
		this.restriction = restriction;
	}

	/**
	 * Getter for changedBy
	 * @return changedBy
	 */
	public String getChangedBy() {
		return key.getChangedBy();
	}

	/**
	 * Setter for changedBy
	 * @param changedBy
	 */
	public void setChangedBy(String changedBy) {
		key.setChangedBy(changedBy);
	}

	/**
	 * Getter for changedOn attribute
	 * @return changedOn attribute
	 */
	@Override
	public LocalDateTime getChangedOn() {
		return key.getChangedOn();
	}

	/**
	 * Setter for changedOn
	 * @param changedOn The time the modification was done.
	 */
	public void setChangedOn(LocalDateTime changedOn) {
		this.key.setChangedOn(changedOn);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProductRestrictionsAudit that = (ProductRestrictionsAudit) o;
		return Objects.equals(key, that.key) &&
				Objects.equals(restriction, that.restriction);
	}

	@Override
	public int hashCode() {

		return Objects.hash(key, restriction);
	}

	@Override
	public String toString() {
		return "ProductRestrictionsAudit{" +
				"key=" + key +
				", restriction=" + restriction +
				'}';
	}
}
