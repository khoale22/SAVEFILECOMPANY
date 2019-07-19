package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents Seasonality data in the sesnly table.
 *
 * @author m594201
 * @since 2.12.0
 */
@Entity
@Table(name = "sesnly")
public class Seasonality implements Serializable {

	@Id
	@Column(name = "sesnly_id")
	private long seasonalityId;

	@Column(name = "sesnly_abb")
	private String seasonalityAbbriviation;

	@Column(name = "sesnly_nm")
	private String seasonalityName;

	/**
	 * Gets seasonality id.  The numeric identifier of the seasonality data row.
	 *
	 * @return the seasonality id which is a numeric identifier of the seasonality data row
	 */
	public long getSeasonalityId() {
		return seasonalityId;
	}

	/**
	 * Sets seasonality id. The numeric identifier of the seasonality data row
	 *
	 * @param seasonalityId the seasonality id which is a numeric identifier of the seasonality data row
	 */
	public void setSeasonalityId(long seasonalityId) {
		this.seasonalityId = seasonalityId;
	}

	/**
	 * Gets seasonality abbreviation.  The short hand to describe the seasonality which represents the product assigned as a seasonal product
	 *
	 * @return the seasonality abbreviation short hand to describe the seasonality which represents the product assigned as a seasonal product
	 */
	public String getSeasonalityAbbriviation() {
		return seasonalityAbbriviation;
	}

	/**
	 * Sets seasonality abbreviation.  The short hand to describe the seasonality which represents the product assigned as a seasonal product
	 *
	 * @param seasonalityAbbriviation the seasonality abbreviation short hand to describe the seasonality which represents the product assigned as a seasonal product
	 */
	public void setSeasonalityAbbriviation(String seasonalityAbbriviation) {
		this.seasonalityAbbriviation = seasonalityAbbriviation;
	}

	/**
	 * Gets seasonality name.  The name of the season that that is identified.
	 *
	 * @return the seasonality name of the season that that is identified.
	 */
	public String getSeasonalityName() {
		return seasonalityName;
	}

	/**
	 * Sets seasonality name.  The name of the season that that is identified.
	 *
	 * @param seasonalityName the seasonality name of the season that that is identified.
	 */
	public void setSeasonalityName(String seasonalityName) {
		this.seasonalityName = seasonalityName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Seasonality that = (Seasonality) o;

		return seasonalityId == that.seasonalityId;
	}

	@Override
	public int hashCode() {
		return (int) (seasonalityId ^ (seasonalityId >>> 32));
	}
}
