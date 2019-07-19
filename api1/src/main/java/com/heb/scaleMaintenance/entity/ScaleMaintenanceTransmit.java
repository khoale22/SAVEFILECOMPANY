package com.heb.scaleMaintenance.entity;

import com.heb.scaleMaintenance.model.EPlumMessage;
import com.heb.scaleMaintenance.model.EPlumMessageConverter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.function.Function;

/**
 * Entity for scale maintenance transmit.
 *
 * @author m314029
 * @since 2.17.8
 */
@Entity
@Table(name = "SCL_MAINT_TRANSMIT")
public class ScaleMaintenanceTransmit implements Serializable, TopLevelEntity<ScaleMaintenanceTransmit> {
	private static final long serialVersionUID = -1796093679455522818L;
	private static final String STORE_SORT_FIELD = "key.store";

	@EmbeddedId
	private ScaleMaintenanceTransmitKey key;

	@Column(name = "EPLUM_BAT_ID", updatable = false)
	private Long ePlumBatchId;

	@Column(name = "EPLUM_TRANS_ID")
	private String ePlumTransmissionId;

	@Column(name = "EPLUM_DTA_JSON")
	@Convert(converter = EPlumMessageConverter.class)
	private EPlumMessage ePlumMessage;

	@Column(name = "STAT_ID")
	private Integer statusCode;

	@Column(name = "RESP_MSG")
	private String responseMessage;

	@Column(name = "CRE8_TS", updatable = false)
	@ColumnDefault(value = "NOW()")
	private LocalDateTime createTime;

	@Column(name = "LST_UPDT_TS")
	private LocalDateTime lastUpdatedTime;

	@Transient
	private String result;

	@ManyToOne
	@JoinColumn(name = "STAT_ID", referencedColumnName = "STAT_ID", insertable = false, updatable = false, nullable = false)
	private Status status;
	/**
	 * Returns Key.
	 *
	 * @return The Key.
	 **/
	public ScaleMaintenanceTransmitKey getKey() {
		return key;
	}

	/**
	 * Sets the Key.
	 *
	 * @param key The Key.
	 **/
	public ScaleMaintenanceTransmit setKey(ScaleMaintenanceTransmitKey key) {
		this.key = key;
		return this;
	}

	/**
	 * Returns ePlumMessage.
	 *
	 * @return The ePlumMessage.
	 **/
	public EPlumMessage getePlumMessage() {
		return ePlumMessage;
	}

	/**
	 * Sets the ePlumMessage.
	 *
	 * @param ePlumMessage The ePlumMessage.
	 **/
	public ScaleMaintenanceTransmit setePlumMessage(EPlumMessage ePlumMessage) {
		this.ePlumMessage = ePlumMessage;
		return this;
	}

	/**
	 * Returns ePlumBatchId.
	 *
	 * @return The ePlumBatchId.
	 **/
	public Long getePlumBatchId() {
		return ePlumBatchId;
	}

	/**
	 * Sets the ePlumBatchId.
	 *
	 * @param ePlumBatchId The ePlumBatchId.
	 **/
	public ScaleMaintenanceTransmit setePlumBatchId(Long ePlumBatchId) {
		this.ePlumBatchId = ePlumBatchId;
		return this;
	}

	/**
	 * Returns ePlumTransmissionId.
	 *
	 * @return The ePlumTransmissionId.
	 **/
	public String getePlumTransmissionId() {
		return ePlumTransmissionId;
	}

	/**
	 * Sets the ePlumTransmissionId.
	 *
	 * @param ePlumTransmissionId The ePlumTransmissionId.
	 **/
	public ScaleMaintenanceTransmit setePlumTransmissionId(String ePlumTransmissionId) {
		this.ePlumTransmissionId = ePlumTransmissionId;
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
	public ScaleMaintenanceTransmit setStatusCode(Integer statusCode) {
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
	public ScaleMaintenanceTransmit setStatus(Status status) {
		this.status = status;
		return this;
	}

	/**
	 * Returns ResponseMessage.
	 *
	 * @return The ResponseMessage.
	 **/
	public String getResponseMessage() {
		return responseMessage;
	}

	/**
	 * Sets the ResponseMessage.
	 *
	 * @param responseMessage The ResponseMessage.
	 **/
	public ScaleMaintenanceTransmit setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
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
	public ScaleMaintenanceTransmit setCreateTime(LocalDateTime createTime) {
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
	public ScaleMaintenanceTransmit setLastUpdatedTime(LocalDateTime lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
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
	public ScaleMaintenanceTransmit setResult(String result) {
		this.result = result;
		return this;
	}

	/**
	 * Returns the default sort order for the prod_del table.
	 *
	 * @return The default sort order for the prod_del table.
	 */
	public static Sort getDefaultSort() {
		return  new Sort(
				new Sort.Order(Sort.Direction.ASC, ScaleMaintenanceTransmit.STORE_SORT_FIELD)
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

		ScaleMaintenanceTransmit that = (ScaleMaintenanceTransmit) o;

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
		return "ScaleMaintenanceTransmit{" +
				"key=" + key +
				", ePlumBatchId=" + ePlumBatchId +
				", ePlumTransmissionId='" + ePlumTransmissionId + '\'' +
				", ePlumMessage='" + ePlumMessage + '\'' +
				", statusCode=" + statusCode +
				", responseMessage='" + responseMessage + '\'' +
				", createTime=" + createTime +
				", lastUpdatedTime=" + lastUpdatedTime +
				", result='" + result + '\'' +
				", status=" + status +
				'}';
	}

	@Override
	public <R> R map(Function<? super ScaleMaintenanceTransmit, ? extends R> mapper) {
		return mapper.apply(this);
	}

	@Override
	public ScaleMaintenanceTransmit save(Function<? super ScaleMaintenanceTransmit, ? extends ScaleMaintenanceTransmit> saver) {
		return saver.apply(this);
	}
}
