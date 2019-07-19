package com.heb.pm.entity;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generic entity. These are commonly used in a relational hierarchy.
 *
 * @author m314029
 * @since 2.12.0
 */
@Entity
//Ignore GenericEntitys have entityId  are one of 2865,2866,2867,2868,2869,2870,2871,2872,2873,2874,3024,398469,404548,415760,427806,456463
@Where(clause = "enty_id not in (2865,2866,2867,2868,2869,2870,2871,2872,2873,2874,3024,398469,404548,415760,427806,456463)")
@Table(name = "enty")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class GenericEntity implements Serializable {

	public static final int NOOP = 0;
	public static final int INSERT = 1;
	public static final int UPDATE = 2;
	public static final int DELETE = 3;

	private static final long serialVersionUID = 1L;
	// Entity types
	public enum EntyType {
		BRICK("BRICK", null),
		CUSTH("CUSTH", null),
		PROD("PROD", 16L),
		UPC("UPC", 17L),
		PGRP("PGRP", 25L);
		private String name;
		private Long baseId;
		EntyType(String name, Long baseId) {
			this.name = name;
			this.baseId = baseId;
		}

		/**
		 * Gets name.
		 *
		 * @return the name
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * Gets base id.
		 *
		 * @return the base id
		 */
		public Long getBaseId() {
			return this.baseId;
		}
	}
	// default constructor
	public GenericEntity(){super();}

	// copy constructor
	public GenericEntity(GenericEntity genericEntity){
		super();
		this.setId(genericEntity.getId());
		this.setType(genericEntity.getType());
		this.setAbbreviation(genericEntity.getAbbreviation());
		this.setDisplayText(genericEntity.getDisplayText());
		this.setDisplayNumber(genericEntity.getDisplayNumber());
	}

	@Id
	@Column(name="enty_id")
	private Long id;

	@Column(name="enty_typ_cd")
	@Type(type="fixedLengthCharPK")
	private String type;

	@Column(name="enty_abb")
	@Type(type="fixedLengthChar")
	private String abbreviation;

	@Column(name="enty_dsply_txt")
	@Type(type="fixedLengthChar")
	private String displayText;

	@Column(name="enty_dsply_nbr")
	private Long displayNumber;

	@Column(name="cre8_uid")
	private String createUserId;

	@Column(name="lst_updt_uid")
	private String lastUpdateUserId;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name="enty_id", referencedColumnName = "enty_id", insertable = false, updatable = false)
	private List<GenericEntityDescription> descriptions;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name="child_enty_id", referencedColumnName = "enty_id", insertable = false, updatable = false)
	private List<ExportGenericEntityRelationship> exportGenericEntityRelationships;

	@Transient
	private int action;

	@Transient
	int countProduct;

	@Transient
	String hierarchyPathDisplay;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name="enty_id", referencedColumnName = "enty_id", insertable = false, updatable = false)
	private List<EntityAttribute> entityAttributes;

	/**
	 * Get the exportGenericEntityRelationships.
	 *
	 * @return the exportGenericEntityRelationships
	 */
	public List<ExportGenericEntityRelationship> getExportGenericEntityRelationships() {
		return exportGenericEntityRelationships;
	}

	/**
	 * Set the exportGenericEntityRelationships.
	 *
	 * @param exportGenericEntityRelationships the exportGenericEntityRelationships to set
	 */
	public void setExportGenericEntityRelationships(
			List<ExportGenericEntityRelationship> exportGenericEntityRelationships) {
		this.exportGenericEntityRelationships = exportGenericEntityRelationships;
	}

	/**
	 * Gets id. This is the id for a generic entity.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets id.
	 *
	 * @param id the id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets type. This is the entity type code for a generic entity.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets type.
	 *
	 * @param type the type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets abbreviation. This is the abbreviation for a generic entity.
	 *
	 * @return the abbreviation
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Sets abbreviation.
	 *
	 * @param abbreviation the abbreviation
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * Gets display text. This is the display text for a generic entity.
	 *
	 * @return the display text
	 */
	public String getDisplayText() {
		return displayText;
	}

	/**
	 * Sets display text.
	 *
	 * @param displayText the display text
	 */
	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	/**
	 * Gets display number. This is the display number for a generic entity.
	 *
	 * @return the display number
	 */
	public Long getDisplayNumber() {
		return displayNumber;
	}

	/**
	 * Sets display number.
	 *
	 * @param displayNumber the display number
	 */
	public void setDisplayNumber(Long displayNumber) {
		this.displayNumber = displayNumber;
	}

	/**
	 * Gets countProduct.
	 *
	 * @return the countProduct
	 */
	public int getCountProduct() {
		return countProduct;
	}
	/**
	 * Sets countProduct.
	 *
	 * @param countProduct the countProduct
	 */
	public void setCountProduct(int countProduct) {
		this.countProduct = countProduct;
	}

	/**
	 * Gets Hierarchy Path to show on view.
	 *
	 * @return the Hierarchy Path.
	 */
	public String getHierarchyPathDisplay() {
		return hierarchyPathDisplay;
	}

	/**
	 * set Hierarchy Path to show on view.
	 * @param hierarchyPathDisplay
	 */
	public void setHierarchyPathDisplay(String hierarchyPathDisplay) {
		this.hierarchyPathDisplay = hierarchyPathDisplay;
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

		GenericEntity genericEntity = (GenericEntity) o;

		return id != null ? id.equals(genericEntity.id) : genericEntity.id == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
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
	 * Returns the action to perform on this object.
	 *
	 * @return The action to perform on this object.
	 */
	public int getAction() {
		return action;
	}

	/**
	 * Sets the action to perform on this object.
	 *
	 * @param action The action to perform on this object.
	 */
	public void setAction(int action) {
		this.action = action;
	}

	/**
	 * Returns the descriptions tied to this entity.
	 *
	 * @return The descriptions tied to this entity.
	 */
	public List<GenericEntityDescription> getDescriptions() {
		if (this.descriptions == null) {
			this.descriptions = new ArrayList<>();
		}
		return descriptions;
	}

	/**
	 * Sets the descriptions tied to this entity.
	 *
	 * @param descriptions The descriptions tied to this entity.
	 */
	public void setDescriptions(List<GenericEntityDescription> descriptions) {
		this.descriptions = descriptions;
	}

	/**
	 * Returns EntityAttributes.
	 *
	 * @return The EntityAttributes.
	 **/
	public List<EntityAttribute> getEntityAttributes() {
		return entityAttributes;
	}

	/**
	 * Sets the EntityAttributes.
	 *
	 * @param entityAttributes The EntityAttributes.
	 **/
	public GenericEntity setEntityAttributes(List<EntityAttribute> entityAttributes) {
		this.entityAttributes = entityAttributes;
		return this;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */


	@Override
	public String toString() {
		return "GenericEntity{" +
				"id=" + id +
				", type='" + type + '\'' +
				", abbreviation='" + abbreviation + '\'' +
				", displayText='" + displayText + '\'' +
				", displayNumber=" + displayNumber +
				", createUserId='" + createUserId + '\'' +
				", lastUpdateUserId='" + lastUpdateUserId + '\'' +
				", countProduct=" + countProduct +
				'}';
	}
}
