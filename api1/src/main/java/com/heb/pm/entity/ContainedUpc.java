package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContainedUpc implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long pack;
	private Upc upc;

	/**
	 * Returns the pack.
	 *
	 * @return the pack.
	 */
	public Long getPack() {
		return pack;
	}

	/**
	 * Sets the pack.
	 *
	 * @param pack the pack.
	 * @return the updated ContainedUpc.
	 */
	public ContainedUpc setPack(Long pack) {
		this.pack = pack;
		return this;
	}

	/**
	 * Returns the Upc.
	 *
	 * @return the Upc.
	 */
	public Upc getUpc() {
		return upc;
	}

	/**
	 * Sets the Upc.
	 *
	 * @param upc the Upc.
	 * @return the updated ContainedUpc.
	 */
	public ContainedUpc setUpc(Upc upc) {
		this.upc = upc;
		return this;
	}
}
