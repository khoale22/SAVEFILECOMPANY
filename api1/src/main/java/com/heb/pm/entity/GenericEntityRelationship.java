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
 * @author m314029
 * @since 2.12.0
 */
@Entity
//Ignore GenericEntityRelationships have parentEntityId and childEntityId are one of 2865,2866,2867,2868,2869,2870,2871,2872,2873,2874,3024,398469,404548,415760,427806,456463
@Where(clause = "child_enty_id not in(2865,2866,2867,2868,2869,2870,2871,2872,2873,2874,3024,398469,404548,415760,427806,456463)" +
		" and parnt_enty_id not in(2865,2866,2867,2868,2869,2870,2871,2872,2873,2874,3024,398469,404548,415760,427806,456463)")
@Table(name = "enty_rlshp")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class GenericEntityRelationship implements Serializable, Comparable<GenericEntityRelationship> {
	private static final long serialVersionUID = 1L;

	//Ignore count GenericEntityRelationships have parentEntityId and childEntityId are one of 2865,2866,2867,2868,2869,2870,2871,2872,2873,2874,3024,398469,404548,415760,427806,456463
	private static final String COUNT_OF_FIRST_CHILD_AS_PRODUCT_FORMULA = "(SELECT count(e.ENTY_ID) from EMD.ENTY_RLSHP r1" +
			" JOIN EMD.ENTY e on r1.CHILD_ENTY_ID = e.ENTY_ID where r1.PARNT_ENTY_ID = CHILD_ENTY_ID and" +
			" child_enty_id not in(2865,2866,2867,2868,2869,2870,2871,2872,2873,2874,3024,398469,404548,415760,427806,456463) and" +
			" parnt_enty_id not in(2865,2866,2867,2868,2869,2870,2871,2872,2873,2874,3024,398469,404548,415760,427806,456463) and"+
			" r1.hier_cntxt_cd = hier_cntxt_cd and (e.ENTY_TYP_CD = 'PROD' or e.ENTY_TYP_CD = 'PGRP') and ROWNUM = 1)";

	// default constructor
	public GenericEntityRelationship(){super();}

	// copy constructor
	public GenericEntityRelationship(GenericEntityRelationship relationship){
		super();
		this.setKey(new GenericEntityRelationshipKey(relationship.getKey()));
		this.setDefaultParent(relationship.getDefaultParent());
		this.setDisplay(relationship.getDisplay());
		this.setEffectiveDate(relationship.getEffectiveDate());
		this.setExpirationDate(relationship.getExpirationDate());
		this.setSequence(relationship.getSequence());
		this.setActive(relationship.getActive());
		this.countOfProductChildren = relationship.countOfProductChildren;

		// set eager loaded objects
		if(relationship.getChildDescription() != null) {
			this.setChildDescription(new GenericEntityDescription(relationship.getChildDescription()));
		}
		if(relationship.getGenericChildEntity() != null) {
			this.setGenericChildEntity(new GenericEntity(relationship.getGenericChildEntity()));
		}
	}

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

	@Transient
	private boolean isCollapsed = true;

	@Transient
	private boolean removeSelected;

	@Transient
	private String parentDisplayNumber;

	// Gets count of the first generic entity matching the childRelationship's child id with the entity type of 'PROD'.
	// This is used to evaluate when you are at the lowest branch of the relational hierarchy. If the first found
	// is a product, they are all products. If the first found is not a product, all of them are not products.
	@Formula(value = GenericEntityRelationship.COUNT_OF_FIRST_CHILD_AS_PRODUCT_FORMULA)
	private int countOfProductChildren;

	// These were added to facilitate JPA mappings as it was not looking inside a GenericEntityRelationshipKey for just
	//  parentEntityId or childEntityId or hierarchyContext. Getters and setters not added as this should not be used
	// besides JPA mapping.
	@Column(name="parnt_enty_id", insertable = false, updatable = false)
	private Long parentEntityId;

	@Column(name="child_enty_id", insertable = false, updatable = false)
	private Long childEntityId;

	@Column(name="hier_cntxt_cd", insertable = false, updatable = false)
	@Type(type="fixedLengthCharPK")
	private String hierarchyContext;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumns({
			@JoinColumn(name = "child_enty_id", referencedColumnName = "enty_id", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name = "hier_cntxt_cd", referencedColumnName = "hier_cntxt_cd", insertable = false, updatable = false, nullable = false)
	})
	@NotFound(action = NotFoundAction.IGNORE)
	private GenericEntityDescription childDescription;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "parnt_enty_id", referencedColumnName = "enty_id", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name = "hier_cntxt_cd", referencedColumnName = "hier_cntxt_cd", insertable = false, updatable = false, nullable = false)
	})
	@NotFound(action = NotFoundAction.IGNORE)
	private GenericEntityDescription parentDescription;

	@OneToOne(fetch = FetchType.EAGER)
	@Where(clause = "enty_typ_cd = 'CUSTH'")
	@JoinColumn(name = "child_enty_id", referencedColumnName = "enty_id", insertable = false, updatable = false, nullable = false)
	private GenericEntity genericChildEntity;

	@OneToOne(fetch = FetchType.LAZY)
	@Where(clause = "enty_typ_cd = 'CUSTH'")
	@JoinColumn(name = "parnt_enty_id", referencedColumnName = "enty_id", insertable = false, updatable = false, nullable = false)
	private GenericEntity genericParentEntity;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "parnt_enty_id", referencedColumnName = "child_enty_id", insertable = false, updatable = false),
			@JoinColumn(name = "hier_cntxt_cd", referencedColumnName = "hier_cntxt_cd", insertable = false, updatable = false)
	})
	private List<GenericEntityRelationship> childRelationships;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "child_enty_id", referencedColumnName = "parnt_enty_id", insertable = false, updatable = false),
			@JoinColumn(name = "hier_cntxt_cd", referencedColumnName = "hier_cntxt_cd", insertable = false, updatable = false)
	})
	private List<GenericEntityRelationship> parentRelationships;

	@Transient
	private String action;

	@Transient
	private String path;
	@Transient
	private String pathStyle;
	@Transient
	private String matchBy;

	@Transient
	private Long productId;

	@Transient
	private boolean allowPrimaryPath;

	@Transient
	private boolean suggestHierarchy;

	@Transient
	private String actionCode;

	@Transient
	private Long primaryPath;

	@Transient
	private Long productEntity;

	/**
	 * This method returns whether or not to the relation is to be removed
	 * @return
	 */
	public boolean isRemoveSelected() {
		return removeSelected;
	}

	/**
	 * updates removeSelected
	 * @param removeSelected
	 */
	public void setRemoveSelected(boolean removeSelected) {
		this.removeSelected = removeSelected;
	}

	/**
	 * get parent description
	 * @return GenericEntityDescription
	 */
	public GenericEntityDescription getParentDescription() {
		return parentDescription;
	}
	/**
	 * set parent parent Description
	 * @param parentDescription the description
	 */
	public void setParentDescription(GenericEntityDescription parentDescription) {
		this.parentDescription = parentDescription;
	}

	/**
	 * Get hierarchy context.
	 *
	 * @return hierarchy context.
	 */
	public String getHierarchyContext() {
		return hierarchyContext;
	}

	/**
	 * Sets hierarchy context.
	 * @param hierarchyContext the hierarchy context.
	 *
	 */
	public void setHierarchyContext(String hierarchyContext) {
		this.hierarchyContext = hierarchyContext;
	}

	/**
	 * Gets lowest level.
	 * @return lowest level.
	 */
	public boolean isLowestLevel() {
		return lowestLevel;
	}

	/**
	 * Sets lowest level.
	 * @param lowestLevel the lowest level.
	 */
	public void setLowestLevel(boolean lowestLevel) {
		this.lowestLevel = lowestLevel;
	}

	@Transient
	private boolean lowestLevel;

	public String getDefaultParentValue() {
		return defaultParentValue;
	}

	public void setDefaultParentValue(String defaultParentValue) {
		this.defaultParentValue = defaultParentValue;
	}

	@Transient
	private String defaultParentValue;

	public int getCountOfProductChildren() {
		return countOfProductChildren;
	}

	public void setCountOfProductChildren(int countOfProductChildren) {
		this.countOfProductChildren = countOfProductChildren;
	}

	/**
	 * Gets child relationships. These are the generic entity relationships whose parent id matches this child id.
	 *
	 * @return the child relationships
	 */
	public List<GenericEntityRelationship> getChildRelationships() {
		return childRelationships;
	}

	/**
	 * Sets child relationships.
	 *
	 * @param childRelationships the child relationships
	 */
	public void setChildRelationships(List<GenericEntityRelationship> childRelationships) {
		this.childRelationships = childRelationships;
	}

	/**
	 * Gets generic child entity. This is the generic entity whose id matches this child id.
	 *
	 * @return the generic child entity
	 */
	public GenericEntity getGenericChildEntity() {
		return genericChildEntity;
	}

	/**
	 * Sets generic child entity.
	 *
	 * @param genericChildEntity the generic child entity
	 */
	public void setGenericChildEntity(GenericEntity genericChildEntity) {
		this.genericChildEntity = genericChildEntity;
	}

	/**
	 * Gets key. This is the key for a generic entity relationship.
	 *
	 * @return the key
	 */
	public GenericEntityRelationshipKey getKey() {
		return key;
	}

	/**
	 * Sets key.
	 *
	 * @param key the key
	 */
	public void setKey(GenericEntityRelationshipKey key) {
		this.key = key;
	}

	/**
	 * Gets default parent. This is whether or not this generic entity relationship is a default parent.
	 *
	 * @return the default parent
	 */
	public Boolean getDefaultParent() {
		return defaultParent;
	}

	/**
	 * Sets default parent.
	 *
	 * @param defaultParent the default parent
	 */
	public void setDefaultParent(Boolean defaultParent) {
		this.defaultParent = defaultParent;
	}

	/**
	 * Gets display. This is whether or not this generic entity relationship should be displayed.
	 *
	 * @return the display
	 */
	public Boolean getDisplay() {
		return display;
	}

	/**
	 * Sets display.
	 *
	 * @param display the display
	 */
	public void setDisplay(Boolean display) {
		this.display = display;
	}

	/**
	 * Gets sequence. This is the sequence number for this generic entity relationship.
	 *
	 * @return the sequence
	 */
	public Long getSequence() {
		return sequence;
	}

	/**
	 * Sets sequence.
	 *
	 * @param sequence the sequence
	 */
	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	/**
	 * Gets effective date. This is the effective date for this generic entity relationship.
	 *
	 * @return the effective date
	 */
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * Sets effective date.
	 *
	 * @param effectiveDate the effective date
	 */
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * Gets expiration date. This is the expiration date for this generic entity relationship.
	 *
	 * @return the expiration date
	 */
	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	/**
	 * Sets expiration date.
	 *
	 * @param expirationDate the expiration date
	 */
	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * Gets active. This is whether or not this generic entity relationship is active.
	 *
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * Sets active.
	 *
	 * @param active the active
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * Gets child description. This is the generic entity description whose id and hierarchy context match this
	 * child id and hierarchy context.
	 *
	 * @return the child description
	 */
	public GenericEntityDescription getChildDescription() {
		return childDescription;
	}

	/**
	 * Sets child description.
	 *
	 * @param childDescription the child description
	 */
	public void setChildDescription(GenericEntityDescription childDescription) {
		this.childDescription = childDescription;
	}

	/**
	 * This is a helper method to determine if this generic entity relationship has any product children. This is a
	 * computed value from the @Formula countOfProductChildren. It looks for one generic entity that shares an entity id
	 * matching the child id of a generic entity relationship whose parent id matches this child id. If that entity
	 * has an entity type of 'PROD', it is a product, therefore all entities at this level should also be products.
	 *
	 * @return the boolean True if count of product children > 0. False otherwise.
	 */
	public boolean isChildRelationshipOfProductEntityType(){
		return this.countOfProductChildren > 0;
	}

	/**
	 * isCollapsed flag is used to state if the link is open in the hierarchy
	 * @return isCollapsed
	 */
	public boolean getIsCollapsed() {
		return isCollapsed;
	}

	/**
	 * Updates iscollapsed.
	 * @param collapsed the new is collapsed.
	 */
	public void setIsCollapsed(boolean collapsed) {
		isCollapsed = collapsed;
	}

	/**
	 * Returns the Parent Entity component of a generic entity relationship.
	 * @return generic parent entity.
	 */
	public GenericEntity getGenericParentEntity() {
		return genericParentEntity;
	}

	/**
	 * Sets the genericParentEntity.
	 * @param genericParentEntity generic parent entity.
	 */
	public void setGenericParentEntity(GenericEntity genericParentEntity) {
		this.genericParentEntity = genericParentEntity;
	}
	/**
	 * Returns the action.
	 * @return the Action.
	 */
	public String getAction() {
		return action;
	}
	/**
	 * updates the action.
	 * @param action the action.
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * Returns the path.
	 * @return the path.
	 */
	public String getPath() {
		return path;
	}
	/**
	 * updates the path.
	 * @param path the path.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Sets the value of parentRelationships and returns parentRelationships.
	 */
	public void setParentRelationships(List<GenericEntityRelationship> parentRelationships) {
		this.parentRelationships = parentRelationships;
	}

	/**
	 * Returns the matchBy
	 * @return the matchBy
	 */
	public String getMatchBy() {
		return matchBy;
	}

	/**
	 * updates the matchBy
	 * @param matchBy the match by.
	 */
	public void setMatchBy(String matchBy) {
		this.matchBy = matchBy;
	}

	/**
	 * Gets the parentRelationships.
	 * @return Parent relationShips.
	 */
	public List<GenericEntityRelationship> getParentRelationships() {
		return parentRelationships;
	}

	/**
	 * This method implements comparable so lists of GenericEntityRelationship can be sorted by their child
	 * description's short description (String comparison).
	 *
	 * @param genericEntityRelationship The GenericEntityRelationship to compare against this GenericEntityRelationship.
	 * @return A negative integer, zero, or a positive integer as this object is less than, equal to, or greater than
	 * the passed in GenericEntityRelationship.
	 */
	@Override
	public int compareTo(@Nonnull GenericEntityRelationship genericEntityRelationship){
		int i;
		String leftSide = StringUtils.SPACE;
		if (this.getChildDescription() != null && this.getChildDescription().getShortDescription() != null) {
			leftSide = this.getChildDescription().getShortDescription();
		}
		String rightSide = StringUtils.SPACE;
		if (genericEntityRelationship.getChildDescription() != null &&
				genericEntityRelationship.getChildDescription().getShortDescription() != null) {
			rightSide = genericEntityRelationship.getChildDescription().getShortDescription();
		}
		return leftSide.compareTo(rightSide);
	}

	/**
	 * Returns the productId.
	 * @return the productId.
	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * updates the productId.
	 * @param productId the product Id.
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	/**
	 * Returns the allowPrimaryPath.
	 * @return the allowPrimaryPath.
	 */
	public boolean isAllowPrimaryPath() {
		return allowPrimaryPath;
	}

	/**
	 * updates the allowPrimaryPath.
	 * @param allowPrimaryPath the allowPrimaryPath.
	 */
	public void setAllowPrimaryPath(boolean allowPrimaryPath) {
		this.allowPrimaryPath = allowPrimaryPath;
	}

	/**
	 * Returns the suggestHierarchy
	 * @return the suggestHierarchy
	 */
	public boolean isSuggestHierarchy() {
		return suggestHierarchy;
	}

	/**
	 * updates the suggestHierarchy.
	 * @param suggestHierarchy the suggestHierarchy.
	 */
	public void setSuggestHierarchy(boolean suggestHierarchy) {
		this.suggestHierarchy = suggestHierarchy;
	}

	/**
	 * Returns the pathStyle.
	 * @return the pathStyle.
	 */
	public String getPathStyle() {
		return pathStyle;
	}

	/**
	 * updates the pathStyle.
	 * @param pathStyle the path style.
	 */
	public void setPathStyle(String pathStyle) {
		this.pathStyle = pathStyle;
	}

	/**
	 * Returns the one-pass ID of the user who created this record.
	 *
	 * @return The one-pass ID of the user who created this record.
	 */
	public String getCreateUserId() {
		return createUserId;
	}

	/**
	 * Sets the one-pass ID of the user who created this record.
	 *
	 * @param createUserId The one-pass ID of the user who created this record.
	 */
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * Returns the one-pass ID of the user who last updated this record.
	 *
	 * @return The one-pass ID of the user who last updated this record.
	 */
	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	/**
	 * Sets the one-pass ID of the user who last updated this record.
	 *
	 * @param lastUpdateUserId The one-pass ID of the user who last updated this record.
	 */
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}

	/**
	 * Returns the display number for the parent node. This is needed when doing batch processing as the parent may not
	 * be created when the relationship is. That is, the objects may exist, but not the database records. This is
	 * there to keep track from one part of the batch load to the next.
	 *
	 * @return The display number for the parent node.
	 */
	public String getParentDisplayNumber() {
		return parentDisplayNumber;
	}

	/**
	 * Sets the display number for the parent node.
	 *
	 * @param parentDisplayNumber The display number for the parent node.
	 */
	public void setParentDisplayNumber(String parentDisplayNumber) {
		this.parentDisplayNumber = parentDisplayNumber;
	}

	/**
	 * Returns the ActionCode. This represents an Add or remove. If it is adding a product then it will be a 'Y', else
	 * remove is a 'D'.
	 *
	 * @return ActionCode
	 */
	public String getActionCode() {
		return actionCode;
	}

	/**
	 * Sets the ActionCode
	 *
	 * @param actionCode The ActionCode
	 */
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}


	/**
	 * Returns the PrimaryPath
	 *
	 * @return PrimaryPath
	 */
	public Long getPrimaryPath() {
		return primaryPath;
	}

	/**
	 * Sets the PrimaryPath
	 *
	 * @param primaryPath The PrimaryPath
	 */
	public void setPrimaryPath(Long primaryPath) {
		this.primaryPath = primaryPath;
	}

	/**
	 * Returns the ProductEntity
	 *
	 * @return ProductEntity
	 */
	public Long getProductEntity() {
		return productEntity;
	}

	/**
	 * Sets the ProductEntity
	 *
	 * @param productEntity The ProductEntity
	 */
	public void setProductEntity(Long productEntity) {
		this.productEntity = productEntity;
	}

	/**
	 * Compares another object to this one. The key is the only thing used to determine equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GenericEntityRelationship that = (GenericEntityRelationship) o;

		return key != null ? key.equals(that.key) : that.key == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "GenericEntityRelationship{" +
				"key=" + key +
				", defaultParent=" + defaultParent +
				", display=" + display +
				", sequence=" + sequence +
				", effectiveDate=" + effectiveDate +
				", expirationDate=" + expirationDate +
				", active=" + active +
				", createUserId='" + createUserId + '\'' +
				", lastUpdateUserId='" + lastUpdateUserId + '\'' +
				", isCollapsed=" + isCollapsed +
				", parentDisplayNumber='" + parentDisplayNumber + '\'' +
				", countOfProductChildren=" + countOfProductChildren +
				", parentEntityId=" + parentEntityId +
				", childEntityId=" + childEntityId +
				", hierarchyContext='" + hierarchyContext + '\'' +
				", childDescription=" + childDescription +
				", genericChildEntity=" + genericChildEntity +
				", genericParentEntity=" + genericParentEntity +
				", action='" + action + '\'' +
				", path='" + path + '\'' +
				", pathStyle='" + pathStyle + '\'' +
				", matchBy='" + matchBy + '\'' +
				", productId=" + productId +
				", allowPrimaryPath=" + allowPrimaryPath +
				", suggestHierarchy=" + suggestHierarchy +
				", lowestLevel=" + lowestLevel +
				", defaultParentValue='" + defaultParentValue + '\'' +
				'}';
	}
}
