package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.io.Serializable;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the PROD_FLFL_CHNL database table.
 * 
 */
@Entity
@Table(name="PROD_FLFL_CHNL")
public class ProductFullfilmentChanel implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	@EmbeddedId
	private ProductFullfilmentChanelKey key;

	@Column(name="EFF_DT")
	@JsonFormat(
			pattern=DATE_FORMAT,
			shape=JsonFormat.Shape.STRING)
	private LocalDate effectDate;

	@Column(name="EXPRN_DT")
	@JsonFormat(
			pattern=DATE_FORMAT,
			shape=JsonFormat.Shape.STRING)
	private LocalDate expirationDate;

	@Column(name="LST_UPDT_TS")
	private LocalDateTime lastUpdateTime;

	@Column(name="LST_UPDT_UID")
	private String lastUpdateUserId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="sals_chnl_cd", referencedColumnName = "sals_chnl_cd", insertable = false, updatable = false),
			@JoinColumn(name="flfl_chnl_cd", referencedColumnName = "flfl_chnl_cd", insertable = false, updatable = false)
	})
	@NotFound(action = NotFoundAction.IGNORE)
	private FulfillmentChannel fulfillmentChannel;

	@Transient
	private String actionCode;

	public ProductFullfilmentChanel() {
	}

	public ProductFullfilmentChanelKey getKey() {
		return key;
	}

	public void setKey(ProductFullfilmentChanelKey key) {
		this.key = key;
	}

	public LocalDate getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(LocalDate effectDate) {
		this.effectDate = effectDate;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	public LocalDateTime getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}

	/**
	 * @return Gets the value of fulfillmentChannel and returns fulfillmentChannel
	 */
	public void setFulfillmentChannel(FulfillmentChannel fulfillmentChannel) {
		this.fulfillmentChannel = fulfillmentChannel;
	}

	/**
	 * Sets the fulfillmentChannel
	 */
	public FulfillmentChannel getFulfillmentChannel() {
		return fulfillmentChannel;
	}

	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}
}