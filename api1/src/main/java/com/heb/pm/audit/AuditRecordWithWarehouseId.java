/*
 *  AuditRecordWithWarehouseId
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.audit;

import com.heb.util.audit.AuditRecord;

/**
 * The type Audit record with warehouse ID.
 * This record represents an AuditRecord that also has a warehouse ID.
 *
 *  @author a786878
 *  @since 2.15.0
 *
 */
public class AuditRecordWithWarehouseId extends AuditRecord {

	/**
	 * Default constructor.
	 */
	AuditRecordWithWarehouseId() {
		super();
	}

	/**
	 * Constructor being passed an AuditRecord and a warehouse ID.
	 *
	 * @param auditRecord An already filled out audit record.
	 * @param warehouseId the product ID for this audit record.
	 */
	public AuditRecordWithWarehouseId(AuditRecord auditRecord, Integer warehouseId) {
		super(auditRecord.getChangedOn(), auditRecord.getChangedBy(),
				auditRecord.getAction(), auditRecord.getAttributeName());
		this.setChangedFrom(auditRecord.getChangedFrom());
		this.setChangedTo(auditRecord.getChangedTo());
		this.setWarehouseId(warehouseId);
	}

	private Integer warehouseId;

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}
}
