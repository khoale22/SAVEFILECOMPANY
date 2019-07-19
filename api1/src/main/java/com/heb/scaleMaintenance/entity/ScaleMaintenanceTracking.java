package com.heb.scaleMaintenance.entity;

import com.heb.scaleMaintenance.model.ScaleMaintenanceLoadParameters;
import com.heb.scaleMaintenance.model.ScaleMaintenanceLoadParametersConverter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.function.Function;

/**
 * Entity for scale maintenance tracking.
 *
 * @author m314029
 * @since 2.17.8
 */
@Entity
@Table(name = "SCL_MAINT_TRKG")
public class ScaleMaintenanceTracking implements TopLevelEntity<ScaleMaintenanceTracking>, Serializable {

	private static final long serialVersionUID = 8805862154194412474L;
	private static final String CREATED_TIME_SORT_FIELD = "createTime";
	private static final String TRANSACTION_ID_SORT_FIELD = "transactionId";

	@Id@GeneratedValue(generator = "scaleTrackingIdSequence")
	@SequenceGenerator(name = "scaleTrackingIdSequence", sequenceName = "scl_maint_trkg_seq", allocationSize = 1, initialValue = 1)
	@Column(name = "TRX_ID", updatable = false)
	private Long transactionId;

	@Column(name = "JSON_VER_TXT", updatable = false)
	private String jsonVersion;

	@Column(name = "USR_ID", updatable = false)
	private String userId;

	@Column(name = "CRE8_TS", updatable = false)
	@ColumnDefault(value = "NOW()")
	private LocalDateTime createTime;

	@Column(name = "LST_UPDT_TS")
	private LocalDateTime lastUpdatedTime;

	@Column(name = "STAT_ID")
	private Integer statusCode;

	@Column(name = "SCL_TRANS_TYP_ID",updatable = false)
	private Integer scaleTransactionTypeCode;

	@Column(name = "LD_PARM_JSON",updatable = false)
	@Convert(converter = ScaleMaintenanceLoadParametersConverter.class)
	private ScaleMaintenanceLoadParameters loadParametersAsJson;

	@Transient
	private String result;

	@ManyToOne
	@JoinColumn(name = "SCL_TRANS_TYP_ID", referencedColumnName = "SCL_TRANS_TYP_ID", insertable = false, updatable = false, nullable = false)
	private ScaleTransactionType scaleTransactionType;

	@ManyToOne
	@JoinColumn(name = "STAT_ID", referencedColumnName = "STAT_ID", insertable = false, updatable = false, nullable = false)
	private Status status;

	/**
	 * Returns TransactionId.
	 *
	 * @return The TransactionId.
	 **/
	public Long getTransactionId() {
		return transactionId;
	}

	/**
	 * Sets the TransactionId.
	 *
	 * @param transactionId The TransactionId.
	 **/
	public ScaleMaintenanceTracking setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
		return this;
	}

	/**
	 * Returns JsonVersion.
	 *
	 * @return The JsonVersion.
	 **/
	public String getJsonVersion() {
		return jsonVersion;
	}

	/**
	 * Sets the JsonVersion.
	 *
	 * @param jsonVersion The JsonVersion.
	 **/
	public ScaleMaintenanceTracking setJsonVersion(String jsonVersion) {
		this.jsonVersion = jsonVersion;
		return this;
	}

	/**
	 * Returns UserId.
	 *
	 * @return The UserId.
	 **/
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the UserId.
	 *
	 * @param userId The UserId.
	 **/
	public ScaleMaintenanceTracking setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	/**
	 * Returns CreateTime.
	 *
	 * @return The CreateTime.
	 **/
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * Sets the CreateTime.
	 *
	 * @param createTime The CreateTime.
	 **/
	public ScaleMaintenanceTracking setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
		return this;
	}

	/**
	 * Returns LastUpdatedTime.
	 *
	 * @return The LastUpdatedTime.
	 **/
	public LocalDateTime getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	/**
	 * Sets the LastUpdatedTime.
	 *
	 * @param lastUpdatedTime The LastUpdatedTime.
	 **/
	public ScaleMaintenanceTracking setLastUpdatedTime(LocalDateTime lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
		return this;
	}

	/**
	 * Returns ScaleTransactionTypeCode.
	 *
	 * @return The ScaleTransactionTypeCode.
	 **/
	public Integer getScaleTransactionTypeCode() {
		return scaleTransactionTypeCode;
	}

	/**
	 * Sets the ScaleTransactionTypeCode.
	 *
	 * @param scaleTransactionTypeCode The ScaleTransactionTypeCode.
	 **/
	public ScaleMaintenanceTracking setScaleTransactionTypeCode(Integer scaleTransactionTypeCode) {
		this.scaleTransactionTypeCode = scaleTransactionTypeCode;
		return this;
	}

	/**
	 * Returns LoadParametersAsJson.
	 *
	 * @return The LoadParametersAsJson.
	 **/
	public ScaleMaintenanceLoadParameters getLoadParametersAsJson() {
		return loadParametersAsJson;
	}

	/**
	 * Sets the LoadParametersAsJson.
	 *
	 * @param loadParametersAsJson The LoadParametersAsJson.
	 **/
	public ScaleMaintenanceTracking setLoadParametersAsJson(ScaleMaintenanceLoadParameters loadParametersAsJson) {
		this.loadParametersAsJson = loadParametersAsJson;
		return this;
	}

	/**
	 * Returns Result.
	 *
	 * @return The Result.
	 **/
	public String getResult() {
		return result;
	}

	/**
	 * Sets the Result.
	 *
	 * @param result The Result.
	 **/
	public ScaleMaintenanceTracking setResult(String result) {
		this.result = result;
		return this;
	}

	/**
	 * Returns ScaleTransactionType.
	 *
	 * @return The ScaleTransactionType.
	 **/
	public ScaleTransactionType getScaleTransactionType() {
		return scaleTransactionType;
	}

	/**
	 * Sets the ScaleTransactionType.
	 *
	 * @param scaleTransactionType The ScaleTransactionType.
	 **/
	public ScaleMaintenanceTracking setScaleTransactionType(ScaleTransactionType scaleTransactionType) {
		this.scaleTransactionType = scaleTransactionType;
		return this;
	}

	/**
	 * Returns StatusCode.
	 *
	 * @return The StatusCode.
	 **/
	public Integer getStatusCode() {
		return statusCode;
	}

	/**
	 * Sets the StatusCode.
	 *
	 * @param statusCode The StatusCode.
	 **/
	public ScaleMaintenanceTracking setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
		return this;
	}

	/**
	 * Returns Status.
	 *
	 * @return The Status.
	 **/
	public Status getStatus() {
		return status;
	}

	/**
	 * Sets the Status.
	 *
	 * @param status The Status.
	 **/
	public ScaleMaintenanceTracking setStatus(Status status) {
		this.status = status;
		return this;
	}

	@Override
	public <R> R map(Function<? super ScaleMaintenanceTracking, ? extends R> mapper) {
		return mapper.apply(this);
	}

	@Override
	public ScaleMaintenanceTracking save(Function<? super ScaleMaintenanceTracking, ? extends ScaleMaintenanceTracking> saver) {
		return saver.apply(this);
	}

	/**
	 * Returns the default sort order for the prod_del table.
	 *
	 * @return The default sort order for the prod_del table.
	 */
	public static Sort getDefaultSort() {
		return  new Sort(
				new Sort.Order(Sort.Direction.DESC, ScaleMaintenanceTracking.CREATED_TIME_SORT_FIELD),
				new Sort.Order(Sort.Direction.DESC, ScaleMaintenanceTracking.TRANSACTION_ID_SORT_FIELD)
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

		ScaleMaintenanceTracking that = (ScaleMaintenanceTracking) o;

		return transactionId != null ? transactionId.equals(that.transactionId) : that.transactionId == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return transactionId != null ? transactionId.hashCode() : 0;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ScaleMaintenanceTracking{" +
				"transactionId=" + transactionId +
				", jsonVersion='" + jsonVersion + '\'' +
				", userId='" + userId + '\'' +
				", createTime=" + createTime +
				", lastUpdatedTime=" + lastUpdatedTime +
				", statusCode=" + statusCode +
				", scaleTransactionTypeCode=" + scaleTransactionTypeCode +
				", loadParametersAsJson=" + loadParametersAsJson +
				", result='" + result + '\'' +
				", scaleTransactionType=" + scaleTransactionType +
				", status=" + status +
				'}';
	}
}
