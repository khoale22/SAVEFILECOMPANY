package com.heb.pm.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;


/**
 * The persistent class for the ATTR_CD database table.
 *
 */
@Entity
@Table(name="ATTR_CD")
public class AttributeCode implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int INSERT = 0;
	public static final int UPDATE = 1;
	public static final int DELETE = 2;

	@Id
	@Column(name="ATTR_CD_ID")
	private Long attributeCodeId;


	@Column(name="ATTR_VAL_CD")
	private String attributeValueCode;

	@Column(name="ATTR_VAL_TXT")
	private String attributeValueText;

	@Column(name="ATTR_VAL_XTRNL_ID")
	private String attributeValueXtrnlId;

	@Column(name="CRE8_TS")
	private LocalDateTime createDate;

	@Column(name="CRE8_UID")
	private String createUserId;

	@Column(name="LST_UPDT_TS")
	private LocalDateTime lastUpdateDate;

	@Column(name="LST_UPDT_UID")
	private String lastUpdateUserId;

	@Column(name="XTRNL_CD_SW")
	private Boolean xtrnlCodeSwitch;

	@Transient
	private int action;

	public Long getAttributeCodeId() {
		return attributeCodeId;
	}

	public void setAttributeCodeId(Long attributeCodeId) {
		this.attributeCodeId = attributeCodeId;
	}

	public String getAttributeValueCode() {
		return attributeValueCode;
	}

	public void setAttributeValueCode(String attributeValueCode) {
		this.attributeValueCode = attributeValueCode;
	}

	public String getAttributeValueText() {
		return attributeValueText;
	}

	public void setAttributeValueText(String attributeValueText) {
		this.attributeValueText = attributeValueText;
	}

	public String getAttributeValueXtrnlId() {
		return attributeValueXtrnlId;
	}

	public void setAttributeValueXtrnlId(String attributeValueXtrnlId) {
		this.attributeValueXtrnlId = attributeValueXtrnlId;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public LocalDateTime getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}

	public Boolean getXtrnlCodeSwitch() {
		return xtrnlCodeSwitch;
	}

	public void setXtrnlCodeSwitch(Boolean xtrnlCodeSwitch) {
		this.xtrnlCodeSwitch = xtrnlCodeSwitch;
	}

	/**
	 * Returns the action to perform with this entity.
	 *
	 * @return The action to perform with this entity.
	 */
	public int getAction() {
		return action;
	}

	/**
	 * Sets the action to perform with this entity.
	 *
	 * @param action The action to perform with this entity.
	 */
	public void setAction(int action) {
		this.action = action;
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
		if (!(o instanceof AttributeCode)) return false;

		AttributeCode that = (AttributeCode) o;

		return attributeCodeId != null ? attributeCodeId.equals(that.attributeCodeId) : that.attributeCodeId == null;
	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		return attributeCodeId != null ? attributeCodeId.hashCode() : 0;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "AttributeCode{" +
				"attributeCodeId=" + attributeCodeId +
				", attributeValueCode='" + attributeValueCode + '\'' +
				", attributeValueText='" + attributeValueText + '\'' +
				", attributeValueXtrnlId='" + attributeValueXtrnlId + '\'' +
				", createDate=" + createDate +
				", createUserId='" + createUserId + '\'' +
				", lastUpdateDate=" + lastUpdateDate +
				", lastUpdateUserId='" + lastUpdateUserId + '\'' +
				", xtrnlCodeSwitch=" + xtrnlCodeSwitch +
				", action=" + action +
				'}';
	}
}
