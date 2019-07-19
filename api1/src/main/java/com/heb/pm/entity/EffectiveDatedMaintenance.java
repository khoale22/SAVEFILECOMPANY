package com.heb.pm.entity;

/**
 * Represents a record in the EFF_DTD_MAINT table.
 *
 * @author d116773
 * @since 2.13.0
 */

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="EFF_DTD_MAINT")
public class EffectiveDatedMaintenance {

	// table name constants
	public static final String TABLE_NAME_GOODS_PRODUCT = "GOODS_PROD";

	// column name constants
	public static final String COLUMN_NAME_VERTEX_TAX_CATEGORY_CODE = "VERTEX_TAX_CAT_CD";
	public static final String COLUMN_NAME_SALES_TAX_SWITCH = "SALS_TAX_SW";
	public static final String COLUMN_NAME_FOOD_STAMP_SWITCH = "FD_STMP_SW";
	public static final String COLUMN_NAME_FSA_CODE = "FSA_CD";

	@EmbeddedId
	private EffectiveDatedMaintenanceKey key;

	@Column(name="eff_dt")
	private LocalDate effectiveDate;

	@Column(name="prod_id")
	private Long productId;

	@Column(name="fld_val_txt")
	private String textValue;

	@Column(name="cre8_uid")
	private String createUserId;

	@Column(name="lst_updt_uid")
	private String lastUpdateUserId;

	@Column(name="ps_work_id")
	private Long workRequestId;

	@Column(name = "lst_updt_ts")
	private LocalDateTime lastUpdateTimeStamp;

	@Column(name = "cre8_ts")
	private LocalDateTime createTimeStamp;

	@ManyToOne
	@JoinColumn(name="ps_work_id", referencedColumnName = "ps_work_id", insertable = false, updatable = false)
	private CandidateWorkRequest workRequest;

	/**
	 * Returns the key for the record.
	 *
	 * @return The key for the record.
	 */
	public EffectiveDatedMaintenanceKey getKey() {
		return key;
	}

	/**
	 * Sets the key for the record.
	 *
	 * @param key The key for the record.
	 */
	public void setKey(EffectiveDatedMaintenanceKey key) {
		this.key = key;
	}

	/**
	 * Returns the date the change should go into effect.
	 *
	 * @return The date the change should go into effect.
	 */
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * Sets the date the change should go into effect.
	 *
	 * @param effectiveDate The date the change should go into effect.
	 */
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * If this record is to update a product, returns the ID of the product.
	 *
	 * @return The ID of the product this record is to update.
	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * Sets the ID of the product this record is to update.
	 *
	 * @param productId The ID of the product this record is to update.
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	/**
	 * If the field being updated contains a text value, this is the value to put into the  table.
	 *
	 * @return The text value to update the field with.
	 */
	public String getTextValue() {
		return textValue;
	}

	/**
	 * Sets the text value to update the field with.
	 *
	 * @param textValue The text value to update the field with.
	 */
	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

	/**
	 * Returns the ID of the user who created this record.
	 *
	 * @return The ID of the user who created this record.
	 */
	public String getCreateUserId() {
		return createUserId;
	}

	/**
	 * Sets the ID of the user who created this record.
	 *
	 * @param createUserId The ID of the user who created this record.
	 */
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * Returns the ID of the last user to touch this record.
	 *
	 * @return The ID of the last user to touch this record.
	 */
	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	/**
	 * Sets the ID of the last user to touch this record.
	 *
	 * @param lastUpdateUserId The ID of the last user to touch this record.
	 */
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}

	/**
	 * Returns the work request this record is tied to.
	 *
	 * @return The work request this record is tied to.
	 */
	public Long getWorkRequestId() {
		return workRequestId;
	}

	/**
	 * Sets the work request this record is tied to.
	 *
	 * @param workRequestId The work request this record is tied to.
	 */
	public void setWorkRequestId(Long workRequestId) {
		this.workRequestId = workRequestId;
	}

	/**
	 * Returns a candidate work request this record is tied to.
	 *
	 * @return A candidate work request this record is tied to.
	 */
	public CandidateWorkRequest getWorkRequest() {
		return workRequest;
	}

	/**
	 * Sets a candidate work request this record is tied to.
	 *
	 * @param workRequest A candidate work request this record is tied to.
	 */
	public void setWorkRequest(CandidateWorkRequest workRequest) {
		this.workRequest = workRequest;
	}

	/**
	 * Gets last update time stamp.
	 *
	 * @return the last update time stamp
	 */
	public LocalDateTime getLastUpdateTimeStamp() {
		return lastUpdateTimeStamp;
	}

	/**
	 * Sets last update time stamp.
	 *
	 * @param lastUpdateTimeStamp the last update time stamp
	 */
	public void setLastUpdateTimeStamp(LocalDateTime lastUpdateTimeStamp) {
		this.lastUpdateTimeStamp = lastUpdateTimeStamp;
	}

	/**
	 * Gets create time stamp.
	 *
	 * @return the create time stamp
	 */
	public LocalDateTime getCreateTimeStamp() {
		return createTimeStamp;
	}

	/**
	 * Sets create time stamp.
	 *
	 * @param createTimeStamp the create time stamp
	 */
	public void setCreateTimeStamp(LocalDateTime createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	/**
	 * Called by JPA before this object is saved.
	 */
	@PrePersist
	public void prepareForSave() {
		// If this is tied to a work request, make sure the value of the work request id is set.
		if (this.workRequestId == null && this.getWorkRequest() != null) {
			this.workRequestId = this.getWorkRequest().getWorkRequestId();
		}
	}

	/**
	 * Compares this object to another for equality.
	 *
	 * @param o The object to compare to.
	 * @return True if the objects are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof EffectiveDatedMaintenance)) return false;

		EffectiveDatedMaintenance that = (EffectiveDatedMaintenance) o;

		return !(key != null ? !key.equals(that.key) : that.key != null);

	}

	/**
	 * Returns a hash code for the object.
	 *
	 * @return A hash code for the object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a string representation for this object.
	 *
	 * @return A string representation for this object.
	 */
	@Override
	public String toString() {
		return "EffectiveDatedMaintenance{" +
				"key=" + key +
				", effectiveDate=" + effectiveDate +
				", productId=" + productId +
				", textValue='" + textValue + '\'' +
				", createUserId='" + createUserId + '\'' +
				", lastUpdateUserId='" + lastUpdateUserId + '\'' +
				", workRequestId=" + workRequestId +
				", lastUpdateTimeStamp=" + lastUpdateTimeStamp +
				", createTimeStamp=" + createTimeStamp +
				'}';
	}
}
