package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.domain.Sort;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents meta data for a table.
 *
 * @author m314029
 * @since 2.21.0
 */
@Entity
@Table(name = "METADTA")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
})
public class AttributeMetaData implements Serializable {
	private static final long serialVersionUID = 623725831570810920L;
	private static final String NAME_FIELD = "name";

	@EmbeddedId
	private AttributeMetaDataKey key;

	@Column(name = "BUS_FRNDLY_NM")
	private String name;

	@Column(name = "FLD_DES")
	private String description;

	@Column(name = "HELP_TXT")
	private String helpText;

	@Column(name = "RESRC_ID")
	private String resourceTied;

	@Column(name = "CUST_FCNG_SW")
	private Boolean customerFacing;

	@Column(name = "GBL_SW")
	private Boolean global;

	@Column(name = "ATTR_ST_CD")
	private String attributeStatusCode;

	@Column(name = "ATTR_CD_TBL_NM")
	private String codeTableName;

	@Column(name = "CD_TBL_STD_SW")
	private Boolean codeTableStandard;

	@Column(name = "REQ_FLD_SW")
	private Boolean required;

	@Column(name = "AUDNC_CD")
	private String audienceCode;

	@Column(name = "ITM_PROD_KEY_CD")
	@Type(type = "fixedLengthChar")
	private String applicableTo;

	@Column(name = "SRC_SYSTEM_ID")
	private Long sourceSystemId;

	@Column(name = "ATTR_ADDED_DT")
	private LocalDate addedDate;

	@Column(name = "ATTR_ST_CHG_DT")
	private LocalDate attributeStatusChangedDate;

	@Column(name = "ATTR_DOMAIN_CD")
	private String domainCode;

	@Column(name = "ATTR_MIN_SZ_NBR")
	private Integer minimumLength;

	@Column(name = "ATTR_MAX_LN_NBR")
	private Integer maximumLength;

	@Column(name = "ATTR_PRCSN_NBR")
	private Integer precision;

	@Column(name = "CRE8_TS")
	private LocalDateTime created;

	@Column(name = "CRE8_UID")
	private String createdUserId;

	@Column(name = "LST_UPDT_TS")
	private LocalDateTime lastUpdated;

	@Column(name = "LST_UPDT_UID")
	private String lastUpdateUserId;

	/**
	 * Returns Key.
	 *
	 * @return The Key.
	 **/
	public AttributeMetaDataKey getKey() {
		return key;
	}

	/**
	 * Sets the Key.
	 *
	 * @param key The Key.
	 **/
	public AttributeMetaData setKey(AttributeMetaDataKey key) {
		this.key = key;
		return this;
	}

	/**
	 * Returns Name.
	 *
	 * @return The Name.
	 **/
	public String getName() {
		return name;
	}

	/**
	 * Sets the Name.
	 *
	 * @param name The Name.
	 **/
	public AttributeMetaData setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Returns Description.
	 *
	 * @return The Description.
	 **/
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the Description.
	 *
	 * @param description The Description.
	 **/
	public AttributeMetaData setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Returns HelpText.
	 *
	 * @return The HelpText.
	 **/
	public String getHelpText() {
		return helpText;
	}

	/**
	 * Sets the HelpText.
	 *
	 * @param helpText The HelpText.
	 **/
	public AttributeMetaData setHelpText(String helpText) {
		this.helpText = helpText;
		return this;
	}

	/**
	 * Returns ResourceTied.
	 *
	 * @return The ResourceTied.
	 **/
	public String getResourceTied() {
		return resourceTied;
	}

	/**
	 * Sets the ResourceTied.
	 *
	 * @param resourceTied The ResourceTied.
	 **/
	public AttributeMetaData setResourceTied(String resourceTied) {
		this.resourceTied = resourceTied;
		return this;
	}

	/**
	 * Returns CustomerFacing.
	 *
	 * @return The CustomerFacing.
	 **/
	public Boolean isCustomerFacing() {
		return customerFacing;
	}

	/**
	 * Sets the CustomerFacing.
	 *
	 * @param customerFacing The CustomerFacing.
	 **/
	public AttributeMetaData setCustomerFacing(Boolean customerFacing) {
		this.customerFacing = customerFacing;
		return this;
	}

	/**
	 * Returns Global.
	 *
	 * @return The Global.
	 **/
	public Boolean isGlobal() {
		return global;
	}

	/**
	 * Sets the Global.
	 *
	 * @param global The Global.
	 **/
	public AttributeMetaData setGlobal(Boolean global) {
		this.global = global;
		return this;
	}

	/**
	 * Returns AttributeStatusCode.
	 *
	 * @return The AttributeStatusCode.
	 **/
	public String getAttributeStatusCode() {
		return attributeStatusCode;
	}

	/**
	 * Sets the AttributeStatusCode.
	 *
	 * @param attributeStatusCode The AttributeStatusCode.
	 **/
	public AttributeMetaData setAttributeStatusCode(String attributeStatusCode) {
		this.attributeStatusCode = attributeStatusCode;
		return this;
	}

	/**
	 * Returns CodeTableName.
	 *
	 * @return The CodeTableName.
	 **/
	public String getCodeTableName() {
		return codeTableName;
	}

	/**
	 * Sets the CodeTableName.
	 *
	 * @param codeTableName The CodeTableName.
	 **/
	public AttributeMetaData setCodeTableName(String codeTableName) {
		this.codeTableName = codeTableName;
		return this;
	}

	/**
	 * Returns CodeTableStandard.
	 *
	 * @return The CodeTableStandard.
	 **/
	public Boolean getCodeTableStandard() {
		return codeTableStandard;
	}

	/**
	 * Sets the CodeTableStandard.
	 *
	 * @param codeTableStandard The CodeTableStandard.
	 **/
	public AttributeMetaData setCodeTableStandard(Boolean codeTableStandard) {
		this.codeTableStandard = codeTableStandard;
		return this;
	}

	/**
	 * Returns Required.
	 *
	 * @return The Required.
	 **/
	public Boolean isRequired() {
		return required;
	}

	/**
	 * Sets the Required.
	 *
	 * @param required The Required.
	 **/
	public AttributeMetaData setRequired(Boolean required) {
		this.required = required;
		return this;
	}

	/**
	 * Returns AudienceCode.
	 *
	 * @return The AudienceCode.
	 **/
	public String getAudienceCode() {
		return audienceCode;
	}

	/**
	 * Sets the AudienceCode.
	 *
	 * @param audienceCode The AudienceCode.
	 **/
	public AttributeMetaData setAudienceCode(String audienceCode) {
		this.audienceCode = audienceCode;
		return this;
	}

	/**
	 * Returns ApplicableTo.
	 *
	 * @return The ApplicableTo.
	 **/
	public String getApplicableTo() {
		return applicableTo;
	}

	/**
	 * Sets the ApplicableTo.
	 *
	 * @param applicableTo The ApplicableTo.
	 **/
	public AttributeMetaData setApplicableTo(String applicableTo) {
		this.applicableTo = applicableTo;
		return this;
	}

	/**
	 * Returns SourceSystemId.
	 *
	 * @return The SourceSystemId.
	 **/
	public Long getSourceSystemId() {
		return sourceSystemId;
	}

	/**
	 * Sets the SourceSystemId.
	 *
	 * @param sourceSystemId The SourceSystemId.
	 **/
	public AttributeMetaData setSourceSystemId(Long sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
		return this;
	}

	/**
	 * Returns AddedDate.
	 *
	 * @return The AddedDate.
	 **/
	public LocalDate getAddedDate() {
		return addedDate;
	}

	/**
	 * Sets the AddedDate.
	 *
	 * @param addedDate The AddedDate.
	 **/
	public AttributeMetaData setAddedDate(LocalDate addedDate) {
		this.addedDate = addedDate;
		return this;
	}

	/**
	 * Returns AttributeStatusChangedDate.
	 *
	 * @return The AttributeStatusChangedDate.
	 **/
	public LocalDate getAttributeStatusChangedDate() {
		return attributeStatusChangedDate;
	}

	/**
	 * Sets the AttributeStatusChangedDate.
	 *
	 * @param attributeStatusChangedDate The AttributeStatusChangedDate.
	 **/
	public AttributeMetaData setAttributeStatusChangedDate(LocalDate attributeStatusChangedDate) {
		this.attributeStatusChangedDate = attributeStatusChangedDate;
		return this;
	}

	/**
	 * Returns DomainCode.
	 *
	 * @return The DomainCode.
	 **/
	public String getDomainCode() {
		return domainCode;
	}

	/**
	 * Sets the DomainCode.
	 *
	 * @param domainCode The DomainCode.
	 **/
	public AttributeMetaData setDomainCode(String domainCode) {
		this.domainCode = domainCode;
		return this;
	}

	/**
	 * Returns MinimumLength.
	 *
	 * @return The MinimumLength.
	 **/
	public Integer getMinimumLength() {
		return minimumLength;
	}

	/**
	 * Sets the MinimumLength.
	 *
	 * @param minimumLength The MinimumLength.
	 **/
	public AttributeMetaData setMinimumLength(Integer minimumLength) {
		this.minimumLength = minimumLength;
		return this;
	}

	/**
	 * Returns MaximumLength.
	 *
	 * @return The MaximumLength.
	 **/
	public Integer getMaximumLength() {
		return maximumLength;
	}

	/**
	 * Sets the MaximumLength.
	 *
	 * @param maximumLength The MaximumLength.
	 **/
	public AttributeMetaData setMaximumLength(Integer maximumLength) {
		this.maximumLength = maximumLength;
		return this;
	}

	/**
	 * Returns Precision.
	 *
	 * @return The Precision.
	 **/
	public Integer getPrecision() {
		return precision;
	}

	/**
	 * Sets the Precision.
	 *
	 * @param precision The Precision.
	 **/
	public AttributeMetaData setPrecision(Integer precision) {
		this.precision = precision;
		return this;
	}

	/**
	 * Returns Created.
	 *
	 * @return The Created.
	 **/
	public LocalDateTime getCreated() {
		return created;
	}

	/**
	 * Sets the Created.
	 *
	 * @param created The Created.
	 **/
	public AttributeMetaData setCreated(LocalDateTime created) {
		this.created = created;
		return this;
	}

	/**
	 * Returns CreatedUserId.
	 *
	 * @return The CreatedUserId.
	 **/
	public String getCreatedUserId() {
		return createdUserId;
	}

	/**
	 * Sets the CreatedUserId.
	 *
	 * @param createdUserId The CreatedUserId.
	 **/
	public AttributeMetaData setCreatedUserId(String createdUserId) {
		this.createdUserId = createdUserId;
		return this;
	}

	/**
	 * Returns LastUpdated.
	 *
	 * @return The LastUpdated.
	 **/
	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * Sets the LastUpdated.
	 *
	 * @param lastUpdated The LastUpdated.
	 **/
	public AttributeMetaData setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
		return this;
	}

	/**
	 * Returns LastUpdateUserId.
	 *
	 * @return The LastUpdateUserId.
	 **/
	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	/**
	 * Sets the LastUpdateUserId.
	 *
	 * @param lastUpdateUserId The LastUpdateUserId.
	 **/
	public AttributeMetaData setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
		return this;
	}

	/**
	 * Returns the default sort for the class (by timestamp ascending).
	 *
	 * @return The default sort for this class.
	 */
	public static Sort getDefaultSort() {
		return new Sort(
				new Sort.Order(Sort.Direction.ASC, AttributeMetaData.NAME_FIELD)
		);
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

		AttributeMetaData metaData = (AttributeMetaData) o;

		return key != null ? key.equals(metaData.key) : metaData.key == null;
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
		return "MetaData{" +
				"key=" + key +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", helpText='" + helpText + '\'' +
				", resourceTied='" + resourceTied + '\'' +
				", customerFacing=" + customerFacing +
				", global=" + global +
				", attributeStatusCode='" + attributeStatusCode + '\'' +
				", codeTableName='" + codeTableName + '\'' +
				", codeTableStandard=" + codeTableStandard +
				", required=" + required +
				", audienceCode='" + audienceCode + '\'' +
				", applicableTo='" + applicableTo + '\'' +
				", sourceSystemId=" + sourceSystemId +
				", addedDate=" + addedDate +
				", attributeStatusChangedDate=" + attributeStatusChangedDate +
				", domainCode='" + domainCode + '\'' +
				", minimumLength=" + minimumLength +
				", maximumLength=" + maximumLength +
				", precision=" + precision +
				", created=" + created +
				", createdUserId='" + createdUserId + '\'' +
				", lastUpdated=" + lastUpdated +
				", lastUpdateUserId='" + lastUpdateUserId + '\'' +
				'}';
	}
}
