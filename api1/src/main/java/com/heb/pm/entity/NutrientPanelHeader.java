package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The entity for the NTRNT_PAN_HDR database table.
 *
 * @author vn73545
 * @since 2.15.0
 */
@Entity
@Table(name="NTRNT_PAN_HDR")
@TypeDefs({
	@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
	@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class NutrientPanelHeader implements Serializable {

	@EmbeddedId
	private NutrientPanelHeaderKey key;

	@Column(name="SRVNG_SZ_TXT")
	@Type(type="fixedLengthCharPK")
	private String servingSizeText;

	@Column(name="SPCR_TXT")
	@Type(type="fixedLengthCharPK")
	private String servingsPerContainerText;

	@Column(name = "XTRNL_ID")
	private String xtrnlId;

	@Column(name = "SRC_TS")
	private LocalDateTime sourceTime;

	/**
	 * Get the key.
	 *
	 * @return the key
	 */
	public NutrientPanelHeaderKey getKey() {
		return key;
	}

	/**
	 * Set the key.
	 *
	 * @param key the key to set
	 */
	public void setKey(NutrientPanelHeaderKey key) {
		this.key = key;
	}

	/**
	 * Get the servingSizeText.
	 *
	 * @return the servingSizeText
	 */
	public String getServingSizeText() {
		return servingSizeText;
	}

	/**
	 * Set the servingSizeText.
	 *
	 * @param servingSizeText the servingSizeText to set
	 */
	public void setServingSizeText(String servingSizeText) {
		this.servingSizeText = servingSizeText;
	}

	/**
	 * Get the servingsPerContainerText.
	 *
	 * @return the servingsPerContainerText
	 */
	public String getServingsPerContainerText() {
		return servingsPerContainerText;
	}

	/**
	 * Set the servingsPerContainerText.
	 *
	 * @param servingsPerContainerText the servingsPerContainerText to set
	 */
	public void setServingsPerContainerText(String servingsPerContainerText) {
		this.servingsPerContainerText = servingsPerContainerText;
	}

	/**
	 * Get the xtrnlId.
	 *
	 * @return the xtrnlId
	 */
	public String getXtrnlId() {
		return xtrnlId;
	}

	/**
	 * Set the xtrnlId.
	 *
	 * @param xtrnlId the xtrnlId to set
	 */
	public void setXtrnlId(String xtrnlId) {
		this.xtrnlId = xtrnlId;
	}

	/**
	 * Get the sourceTime.
	 *
	 * @return the sourceTime
	 */
	public LocalDateTime getSourceTime() {
		return sourceTime;
	}

	/**
	 * Set the sourceTime.
	 *
	 * @param sourceTime the sourceTime to set
	 */
	public void setSourceTime(LocalDateTime sourceTime) {
		this.sourceTime = sourceTime;
	}
}
