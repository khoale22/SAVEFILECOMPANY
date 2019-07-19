package com.heb.pm.entity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.*;

import javax.annotation.Nonnull;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents a generic entity relationship.
 *
 * @author vn73545
 * @since 2.18.4
 */
@Entity
@Table(name = "enty_rlshp")
@TypeDefs({
	@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
	@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ExportGenericEntityRelationship implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private GenericEntityRelationshipKey key;

	@Column(name="dflt_parnt_sw")
	private Boolean defaultParent;

	@Column(name="dsply_sw")
	private Boolean display;

	@Column(name="seq_nbr")
	private Long sequence;

	@Column(name="eff_dt")
	private LocalDate effectiveDate;

	@Column(name="exprn_dt")
	private LocalDate expirationDate;

	@Column(name="actv_sw")
	private Boolean active;

	@Column(name="cre8_uid")
	private String createUserId;

	@Column(name="lst_updt_uid")
	private String lastUpdateUserId;
	
	@Column(name="parnt_enty_id", insertable = false, updatable = false)
	private Long parentEntityId;

	@Column(name="child_enty_id", insertable = false, updatable = false)
	private Long childEntityId;

	@Column(name="hier_cntxt_cd", insertable = false, updatable = false)
	@Type(type="fixedLengthCharPK")
	private String hierarchyContext;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "child_enty_id", referencedColumnName = "enty_id", insertable = false, updatable = false, nullable = false),
		@JoinColumn(name = "hier_cntxt_cd", referencedColumnName = "hier_cntxt_cd", insertable = false, updatable = false, nullable = false)
	})
	private GenericEntityDescription childDescription;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "parnt_enty_id", referencedColumnName = "enty_id", insertable = false, updatable = false, nullable = false),
		@JoinColumn(name = "hier_cntxt_cd", referencedColumnName = "hier_cntxt_cd", insertable = false, updatable = false, nullable = false)
	})
	private GenericEntityDescription parentDescription;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "parnt_enty_id", referencedColumnName = "child_enty_id", insertable = false, updatable = false),
		@JoinColumn(name = "hier_cntxt_cd", referencedColumnName = "hier_cntxt_cd", insertable = false, updatable = false)
	})
	private List<ExportGenericEntityRelationship> childRelationships;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "child_enty_id", referencedColumnName = "parnt_enty_id", insertable = false, updatable = false),
		@JoinColumn(name = "hier_cntxt_cd", referencedColumnName = "hier_cntxt_cd", insertable = false, updatable = false)
	})
	private List<ExportGenericEntityRelationship> parentRelationships;
	
	@Where(clause = "enty_typ_cd = 'CUSTH'")
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parnt_enty_id", referencedColumnName = "enty_id", insertable = false, updatable = false)
	private GenericEntity genericParentEntity;

	/**
	 * Get the genericParentEntity.
	 *
	 * @return the genericParentEntity
	 */
	public GenericEntity getGenericParentEntity() {
		return genericParentEntity;
	}

	/**
	 * Set the genericParentEntity.
	 *
	 * @param genericParentEntity the genericParentEntity to set
	 */
	public void setGenericParentEntity(GenericEntity genericParentEntity) {
		this.genericParentEntity = genericParentEntity;
	}

	/**
	 * Get the key.
	 *
	 * @return the key
	 */
	public GenericEntityRelationshipKey getKey() {
		return key;
	}

	/**
	 * Set the key.
	 *
	 * @param key the key to set
	 */
	public void setKey(GenericEntityRelationshipKey key) {
		this.key = key;
	}

	/**
	 * Get the defaultParent.
	 *
	 * @return the defaultParent
	 */
	public Boolean getDefaultParent() {
		return defaultParent;
	}

	/**
	 * Set the defaultParent.
	 *
	 * @param defaultParent the defaultParent to set
	 */
	public void setDefaultParent(Boolean defaultParent) {
		this.defaultParent = defaultParent;
	}

	/**
	 * Get the display.
	 *
	 * @return the display
	 */
	public Boolean getDisplay() {
		return display;
	}

	/**
	 * Set the display.
	 *
	 * @param display the display to set
	 */
	public void setDisplay(Boolean display) {
		this.display = display;
	}

	/**
	 * Get the sequence.
	 *
	 * @return the sequence
	 */
	public Long getSequence() {
		return sequence;
	}

	/**
	 * Set the sequence.
	 *
	 * @param sequence the sequence to set
	 */
	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	/**
	 * Get the effectiveDate.
	 *
	 * @return the effectiveDate
	 */
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * Set the effectiveDate.
	 *
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * Get the expirationDate.
	 *
	 * @return the expirationDate
	 */
	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	/**
	 * Set the expirationDate.
	 *
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * Get the active.
	 *
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * Set the active.
	 *
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * Get the createUserId.
	 *
	 * @return the createUserId
	 */
	public String getCreateUserId() {
		return createUserId;
	}

	/**
	 * Set the createUserId.
	 *
	 * @param createUserId the createUserId to set
	 */
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * Get the lastUpdateUserId.
	 *
	 * @return the lastUpdateUserId
	 */
	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	/**
	 * Set the lastUpdateUserId.
	 *
	 * @param lastUpdateUserId the lastUpdateUserId to set
	 */
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}

	/**
	 * Get the childDescription.
	 *
	 * @return the childDescription
	 */
	public GenericEntityDescription getChildDescription() {
		return childDescription;
	}

	/**
	 * Set the childDescription.
	 *
	 * @param childDescription the childDescription to set
	 */
	public void setChildDescription(GenericEntityDescription childDescription) {
		this.childDescription = childDescription;
	}

	/**
	 * Get the parentDescription.
	 *
	 * @return the parentDescription
	 */
	public GenericEntityDescription getParentDescription() {
		return parentDescription;
	}

	/**
	 * Set the parentDescription.
	 *
	 * @param parentDescription the parentDescription to set
	 */
	public void setParentDescription(GenericEntityDescription parentDescription) {
		this.parentDescription = parentDescription;
	}

	/**
	 * Get the childRelationships.
	 *
	 * @return the childRelationships
	 */
	public List<ExportGenericEntityRelationship> getChildRelationships() {
		return childRelationships;
	}

	/**
	 * Set the childRelationships.
	 *
	 * @param childRelationships the childRelationships to set
	 */
	public void setChildRelationships(List<ExportGenericEntityRelationship> childRelationships) {
		this.childRelationships = childRelationships;
	}

	/**
	 * Get the parentRelationships.
	 *
	 * @return the parentRelationships
	 */
	public List<ExportGenericEntityRelationship> getParentRelationships() {
		return parentRelationships;
	}

	/**
	 * Set the parentRelationships.
	 *
	 * @param parentRelationships the parentRelationships to set
	 */
	public void setParentRelationships(List<ExportGenericEntityRelationship> parentRelationships) {
		this.parentRelationships = parentRelationships;
	}
}
