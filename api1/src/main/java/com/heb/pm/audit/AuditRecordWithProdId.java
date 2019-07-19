package com.heb.pm.audit;

import com.heb.util.audit.AuditRecord;

/**
 * The type Audit record with prodId.
 * This record represents an AuditRecord that also has a Product ID.
 * For example, Product Relationship audit records
 *
 */
public class AuditRecordWithProdId extends AuditRecord {

	/**
	 * Default constructor.
	 */
	AuditRecordWithProdId() {
		super();
	}

	/**
	 * Constructor being passed an AuditRecord and a upc.
	 *
	 * @param auditRecord An already filled out audit record.
	 * @param prodId the product ID for this audit record.
	 */
	public AuditRecordWithProdId(AuditRecord auditRecord, Long prodId) {
		super(auditRecord.getChangedOn(), auditRecord.getChangedBy(),
				auditRecord.getAction(), auditRecord.getAttributeName());
		this.setChangedFrom(auditRecord.getChangedFrom());
		this.setChangedTo(auditRecord.getChangedTo());
		this.setProdId(prodId);
	}

	private Long prodId;

	public Long getProdId() {
		return prodId;
	}

	public void setProdId(Long prodId) {
		this.prodId = prodId;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "AuditRecordWithProdId{" +
				"changedBy='" + this.getChangedBy() + '\'' +
				", changedFrom='" + this.getChangedFrom() + '\'' +
				", changedTo='" + this.getChangedTo() + '\'' +
				", changedOn='" + this.getChangedOn() + '\'' +
				", attributeName='" + this.getAttributeName() + '\'' +
				"prodId=" + prodId +
				'}';
	}
}
