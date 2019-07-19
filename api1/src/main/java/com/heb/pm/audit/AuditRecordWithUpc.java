package com.heb.pm.audit;

import com.heb.util.audit.AuditRecord;

/**
 * The type Audit record with upc.
 *
 * @author m314029
 */
public class AuditRecordWithUpc extends AuditRecord {

	/**
	 * Default constructor.
	 */
	public AuditRecordWithUpc() {
		super();
	}

	/**
	 * Constructor being passed an AuditRecord and a upc.
	 *
	 * @param auditRecord An already filled out audit record.
	 * @param upc the upc for this audit record with upc.
	 */
	public AuditRecordWithUpc(AuditRecord auditRecord, Long upc) {
		super(auditRecord.getChangedOn(), auditRecord.getChangedBy(),
				auditRecord.getAction(), auditRecord.getAttributeName());
		this.setChangedFrom(auditRecord.getChangedFrom());
		this.setChangedTo(auditRecord.getChangedTo());
		this.setUpc(upc);
	}

	private Long upc;

	/**
	 * Gets upc.
	 *
	 * @return the upc
	 */
	public Long getUpc() {
		return upc;
	}

	/**
	 * Sets upc.
	 *
	 * @param upc the upc
	 */
	public void setUpc(Long upc) {
		this.upc = upc;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "AuditRecordWithUpc{" +
				"changedBy='" + this.getChangedBy() + '\'' +
				", changedFrom='" + this.getChangedFrom() + '\'' +
				", changedTo='" + this.getChangedTo() + '\'' +
				", changedOn='" + this.getChangedOn() + '\'' +
				", attributeName='" + this.getAttributeName() + '\'' +
				"upc=" + upc +
				'}';
	}
}
