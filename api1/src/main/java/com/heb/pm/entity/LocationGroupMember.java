/*
 *  LocationGroupMember.java
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents a LOC_GRP_MEMBERS under DB2.
 *
 * @author vn70529
 * @since 2.23.0
 */
@Entity
@Table(name = "LOC_GRP_MEMBERS")
public class LocationGroupMember implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private LocationGroupMemberKey key;

	public LocationGroupMemberKey getKey() {
		return key;
	}

	public void setKey(LocationGroupMemberKey key) {
		this.key = key;
	}

	@Override
	public int hashCode() {
		int result = getKey() != null ? getKey().hashCode() : 0;
		return result;
	}

	@Override
	public String toString() {
		return "LocationGroupMember{" +
				"key=" + key +
				'}';
	}
}