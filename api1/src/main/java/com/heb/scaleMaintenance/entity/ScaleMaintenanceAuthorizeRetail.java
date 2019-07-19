package com.heb.scaleMaintenance.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity for scale maintenance authorize and retail.
 *
 * @author m314029
 * @since 2.17.8
 */
@Entity
@Table(name = "SCL_MAINT_AUTH_RETL")
public class ScaleMaintenanceAuthorizeRetail implements Serializable{

	private static final long serialVersionUID = -8319541696396600148L;

	@EmbeddedId
	private ScaleMaintenanceAuthorizeRetailKey key;

	@Column(name = "RETL_PRC_AMT", updatable = false)
	private Double retail;

	@Column(name = "WT_SW", updatable = false)
	private Boolean weighed;

	@Column(name = "AUTHD_SW", updatable = false)
	private Boolean authorized;

	@Column(name = "X4_QTY", updatable = false)
	private Integer byCountQuantity;

	@Column(name = "MSG_TXT")
	private String message;

	@Column(name = "CRE8_TS", updatable = false)
	@ColumnDefault(value = "NOW()")
	private LocalDateTime createTime;

	/**
	 * Returns Key.
	 *
	 * @return The Key.
	 **/
	public ScaleMaintenanceAuthorizeRetailKey getKey() {
		return key;
	}

	/**
	 * Sets the Key.
	 *
	 * @param key The Key.
	 **/
	public ScaleMaintenanceAuthorizeRetail setKey(ScaleMaintenanceAuthorizeRetailKey key) {
		this.key = key;
		return this;
	}

	/**
	 * Returns Retail.
	 *
	 * @return The Retail.
	 **/
	public Double getRetail() {
		return retail;
	}

	/**
	 * Sets the Retail.
	 *
	 * @param retail The Retail.
	 **/
	public ScaleMaintenanceAuthorizeRetail setRetail(Double retail) {
		this.retail = retail;
		return this;
	}

	/**
	 * Returns Weighed.
	 *
	 * @return The Weighed.
	 **/
	public Boolean getWeighed() {
		return weighed;
	}

	/**
	 * Sets the Weighed.
	 *
	 * @param weighed The Weighed.
	 **/
	public ScaleMaintenanceAuthorizeRetail setWeighed(Boolean weighed) {
		this.weighed = weighed;
		return this;
	}

	/**
	 * Returns Authorized.
	 *
	 * @return The Authorized.
	 **/
	public Boolean getAuthorized() {
		return authorized;
	}

	/**
	 * Sets the Authorized.
	 *
	 * @param authorized The Authorized.
	 **/
	public ScaleMaintenanceAuthorizeRetail setAuthorized(Boolean authorized) {
		this.authorized = authorized;
		return this;
	}

	/**
	 * Returns ByCountQuantity.
	 *
	 * @return The ByCountQuantity.
	 **/
	public Integer getByCountQuantity() {
		return byCountQuantity;
	}

	/**
	 * Sets the ByCountQuantity.
	 *
	 * @param byCountQuantity The ByCountQuantity.
	 **/
	public ScaleMaintenanceAuthorizeRetail setByCountQuantity(Integer byCountQuantity) {
		this.byCountQuantity = byCountQuantity;
		return this;
	}

	/**
	 * Returns Message.
	 *
	 * @return The Message.
	 **/
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the Message.
	 *
	 * @param message The Message.
	 **/
	public ScaleMaintenanceAuthorizeRetail setMessage(String message) {
		this.message = message;
		return this;
	}

	/**
	 * Returns CreateTime.
	 *
	 * @return The CreateTime.
	 **/
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * Sets the CreateTime.
	 *
	 * @param createTime The CreateTime.
	 **/
	public ScaleMaintenanceAuthorizeRetail setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
		return this;
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

		ScaleMaintenanceAuthorizeRetail that = (ScaleMaintenanceAuthorizeRetail) o;

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
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ScaleMaintenanceAuthorizeRetail{" +
				"key=" + key +
				", retail=" + retail +
				", weighed=" + weighed +
				", authorized=" + authorized +
				", byCountQuantity=" + byCountQuantity +
				", message='" + message + '\'' +
				", createTime=" + createTime +
				'}';
	}
}
