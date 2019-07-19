package com.heb.pm.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;


/**
 * The persistent class for the GRP_ATTR database table.
 * 
 */
@Entity
@Table(name="GRP_ATTR")
public class GroupAttribute implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private GroupAttributeKey key;

	@Column(name="CRE8_TS")
	private LocalDateTime createTime;

	@Column(name="CRE8_UID")
	private String createUserId;

	@Column(name="LST_UPDT_TS")
	private LocalDateTime latsUpldateTime;

	@Column(name="LST_UPDT_UID")
	private String latsUpldateId;

	@Column(name="SEQ_NBR")
	private Long seqence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RLSHP_GRP_ID", referencedColumnName = "RLSHP_GRP_ID", insertable = false, updatable = false)
    private RelationshipGroup relationshipGroup;

	public GroupAttributeKey getKey() {
		return key;
	}

	public void setKey(GroupAttributeKey key) {
		this.key = key;
	}

	/**
	 * @return the relationshipGroup
	 */
	public RelationshipGroup getRelationshipGroup() {
		return relationshipGroup;
	}

	/**
	 * @param relationshipGroup the relationshipGroup to set
	 */
	public void setRelationshipGroup(RelationshipGroup relationshipGroup) {
		this.relationshipGroup = relationshipGroup;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public LocalDateTime getLatsUpldateTime() {
		return latsUpldateTime;
	}

	public void setLatsUpldateTime(LocalDateTime latsUpldateTime) {
		this.latsUpldateTime = latsUpldateTime;
	}

	public String getLatsUpldateId() {
		return latsUpldateId;
	}

	public void setLatsUpldateId(String latsUpldateId) {
		this.latsUpldateId = latsUpldateId;
	}

	public Long getSeqence() {
		return seqence;
	}

	public void setSeqence(Long seqence) {
		this.seqence = seqence;
	}


}