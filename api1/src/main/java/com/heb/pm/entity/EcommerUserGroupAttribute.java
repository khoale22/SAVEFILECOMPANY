package com.heb.pm.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the ECOM_USR_GRP_ATTR database table.
 * 
 */
@Entity
@Table(name="ECOM_USR_GRP_ATTR")
public class EcommerUserGroupAttribute implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String TAG = "TAG";
	public static final String SPEC = "SPEC";

	public static final int INSERT = 0;
	public static final int NOOP = 1;

	@EmbeddedId
	private EcommerUserGroupAttributeKey key;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "ATTR_ID", referencedColumnName = "ATTR_ID", insertable = false, updatable = false)
	})
	private Attribute attribute;

	@Column(name="SEQ_NBR")
	private Long sequence;

	@Transient
	private int action = INSERT;

	public EcommerUserGroupAttributeKey getKey() {
		return this.key;
	}

	public void setKey(EcommerUserGroupAttributeKey key) {
		this.key = key;
	}

	public Long getSequence() {
		return this.sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	public int getAction() {
		return action;
	}

	public EcommerUserGroupAttribute setAction(int action) {
		this.action = action;
		return this;
	}
}