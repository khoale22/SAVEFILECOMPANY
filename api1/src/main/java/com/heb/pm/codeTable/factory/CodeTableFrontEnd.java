package com.heb.pm.codeTable.factory;

import com.heb.pm.entity.CodeTable;

/**
 * Represents an implementation of a code table (so that it can be passed by the frontend to the backend).
 *
 * @author s573181
 * @since 2.21.0
 */
public class CodeTableFrontEnd implements CodeTable {

	private String id;
	private String description;

	/**
	 * Returns the id.
	 *
	 * @return the id.
	 */
	@Override
	public String getId() {
		return this.id;
	}

	/**
	 * Sets the ID.
	 *
	 * @param id the id.
	 * @return the code table.
	 */
	@Override
	public CodeTable setId(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Returns the description.
	 *
	 * @return the description.
	 */
	@Override
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the description
	 * @return the code table.
	 */
	@Override
	public CodeTable setDescription(String description) {
		this.description = description;
		return this;
	}
}
