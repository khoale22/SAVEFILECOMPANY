/*
 * AttributeDomain.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The persistent class for the ATTR_DOMAIN database table.
 * @author a786878
 * @since 2.16.0
 *
 */
@Entity
@Table(name="ATTR_DOMAIN")
public class AttributeDomain implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ATTR_DOMAIN_CD")
	private String attributeDomainCode;

	@Column(name="ATTR_DOMAIN_DES")
	private String attributeDomainDescription;

	@Column(name="CRE8_UID")
	private String createUserId;

	@Column(name="CRE8_TS")
	private LocalDateTime createDate;

	@Column(name="LST_UPDT_UID")
	private String lastUpdateUserId;

	@Column(name="LST_UPDT_TS")
	private LocalDateTime lastUpdateDate;

	public AttributeDomain() {
	}

	/**
	 * Represents the values of the known attribute domains.
	 */
	public enum Code {

		DECIMAL("DEC"),
		DATE("DT"),
		INTEGER("I"),
		IMAGE("IMG"),
		STRING("S"),
		TIMESTAMP("TS");

		String code;

		/**
		 * Called to create a value.
		 * @param code The value of the code in the table.
		 */
		Code(String code) {
			this.code = code;
		}

		/**
		 * Returns the code that represents each value (the value that will go into the database).
		 *
		 * @return The code that represents each value.
		 */
		public String getCode() {
			return this.code;
		}

		public static Code getDomainById(String id) {
			for (AttributeDomain.Code domain : AttributeDomain.Code.values()) {
				if (domain.getCode().equals(id)) {
					return domain;
				}
			}
			return null;
		}
	}

	public String getAttributeDomainCode() {
		return attributeDomainCode;
	}

	public void setAttributeDomainCode(String attributeDomainCode) {
		this.attributeDomainCode = attributeDomainCode;
	}

	public String getAttributeDomainDescription() {
		return attributeDomainDescription;
	}

	public void setAttributeDomainDescription(String attributeDomainDescription) {
		this.attributeDomainDescription = attributeDomainDescription;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}

	public LocalDateTime getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	@Override
	public String toString() {
		return "AttributeDomain{" +
				"attributeDomainCode='" + attributeDomainCode + '\'' +
				", attributeDomainDescription='" + attributeDomainDescription + '\'' +
				", createUserId='" + createUserId + '\'' +
				", createDate=" + createDate +
				", lastUpdateUserId='" + lastUpdateUserId + '\'' +
				", lastUpdateDate=" + lastUpdateDate +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AttributeDomain that = (AttributeDomain) o;
		return Objects.equals(attributeDomainCode, that.attributeDomainCode) &&
				Objects.equals(attributeDomainDescription, that.attributeDomainDescription) &&
				Objects.equals(createUserId, that.createUserId) &&
				Objects.equals(createDate, that.createDate) &&
				Objects.equals(lastUpdateUserId, that.lastUpdateUserId) &&
				Objects.equals(lastUpdateDate, that.lastUpdateDate);
	}

	@Override
	public int hashCode() {

		return Objects.hash(attributeDomainCode, attributeDomainDescription, createUserId, createDate, lastUpdateUserId, lastUpdateDate);
	}
}
