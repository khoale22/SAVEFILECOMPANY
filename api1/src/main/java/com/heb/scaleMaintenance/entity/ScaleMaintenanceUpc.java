package com.heb.scaleMaintenance.entity;

import com.heb.scaleMaintenance.model.ScaleMaintenanceProduct;
import com.heb.scaleMaintenance.model.ScaleMaintenanceProductConverter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.function.Function;

/**
 * Entity for scale maintenance upc.
 *
 * @author m314029
 * @since 2.17.8
 */
@Entity
@Table(name = "SCL_MAINT_UPC")
public class ScaleMaintenanceUpc implements TopLevelEntity<ScaleMaintenanceUpc> {

	@EmbeddedId
	private ScaleMaintenanceUpcKey key;

	@Column(name = "JSON_VER_TXT", updatable = false)
	private String jsonVersion;

	@Column(name = "SCL_PROD_JSON", updatable = false)
	@Convert(converter = ScaleMaintenanceProductConverter.class)
	private ScaleMaintenanceProduct scaleProductAsJson;

	@Column(name = "MSG_TXT")
	private String message;

	@Column(name = "CRE8_TS", updatable = false)
	@ColumnDefault(value = "NOW()")
	private LocalDateTime createTime;

	/**
	 * Returns Key.
	 *
	 * @return The Key.
	 **/
	public ScaleMaintenanceUpcKey getKey() {
		return key;
	}

	/**
	 * Sets the Key.
	 *
	 * @param key The Key.
	 **/
	public ScaleMaintenanceUpc setKey(ScaleMaintenanceUpcKey key) {
		this.key = key;
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
	public ScaleMaintenanceUpc setJsonVersion(String jsonVersion) {
		this.jsonVersion = jsonVersion;
		return this;
	}

	/**
	 * Returns ScaleProductAsJson.
	 *
	 * @return The ScaleProductAsJson.
	 **/
	public ScaleMaintenanceProduct getScaleProductAsJson() {
		return scaleProductAsJson;
	}

	/**
	 * Sets the ScaleProductAsJson.
	 *
	 * @param scaleProductAsJson The ScaleProductAsJson.
	 **/
	public ScaleMaintenanceUpc setScaleProductAsJson(ScaleMaintenanceProduct scaleProductAsJson) {
		this.scaleProductAsJson = scaleProductAsJson;
		return this;
	}

	/**
	 * Returns Message.
	 *
	 * @return The Message.
	 **/
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the Message.
	 *
	 * @param message The Message.
	 **/
	public ScaleMaintenanceUpc setMessage(String message) {
		this.message = message;
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
	public ScaleMaintenanceUpc setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
		return this;
	}

	@Override
	public <R> R map(Function<? super ScaleMaintenanceUpc, ? extends R> mapper) {
		return mapper.apply(this);
	}

	@Override
	public ScaleMaintenanceUpc save(Function<? super ScaleMaintenanceUpc, ? extends ScaleMaintenanceUpc> saver) {
		return saver.apply(this);
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

		ScaleMaintenanceUpc that = (ScaleMaintenanceUpc) o;

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
		return "ScaleMaintenanceUpc{" +
				"key=" + key +
				", jsonVersion='" + jsonVersion + '\'' +
				", scaleProductAsJson=" + scaleProductAsJson +
				", message='" + message + '\'' +
				", createTime=" + createTime +
				'}';
	}
}
