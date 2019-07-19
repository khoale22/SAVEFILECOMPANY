package com.heb.pm.entity;

import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents the key for the Tobacco product audit entity.
 */
@Embeddable
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class TobaccoProductAuditKey implements Serializable {
	private static final long serialVersionUID = 1L;

	public TobaccoProductAuditKey(){super();}

	@Column(name = "AUD_REC_CRE8_TS", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, length = 0)
	private LocalDateTime changedOn;

	@Column(name = "PROD_ID")
	private Long prodId;

	public LocalDateTime getChangedOn() {
		return changedOn;
	}

	public void setChangedOn(LocalDateTime changedOn) {
		this.changedOn = changedOn;
	}

	public Long getProdId() {
		return prodId;
	}

	public void setProdId(Long prodId) {
		this.prodId = prodId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TobaccoProductAuditKey that = (TobaccoProductAuditKey) o;
		return Objects.equals(changedOn, that.changedOn) &&
				Objects.equals(prodId, that.prodId);
	}

	@Override
	public int hashCode() {

		return Objects.hash(changedOn, prodId);
	}

	@Override
	public String toString() {
		return "TobaccoProductAuditKey{" +
				"changedOn=" + changedOn +
				", prodId=" + prodId +
				'}';
	}
}
