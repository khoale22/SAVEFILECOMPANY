package com.heb.pm.codeTable.generic;

import com.heb.pm.codeTable.factory.CodeTableFrontEnd;
import com.heb.pm.entity.CodeTable;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a code table name and a list of code table front ends that represent records of the table name.
 *
 * @author s573181
 * @since 2.21.0
 */
public class GenericCodeTableUpdate implements Serializable {

	private List<CodeTableFrontEnd> codeTables;
	private String tableName;

	/**
	 * Returns the code tables.
	 *
	 * @return the code tables.
	 */
	public List<CodeTableFrontEnd> getCodeTables() {
		return codeTables;
	}

	/**
	 * Sets the code tables.
	 *
	 * @param codeTables the code tables.
	 * @return the GenericCodeTableUpdate.
	 */
	public GenericCodeTableUpdate setCodeTables(List<CodeTableFrontEnd> codeTables) {
		this.codeTables = codeTables;
		return this;
	}

	/**
	 * Returns the table name.
	 *
	 * @return the table name.
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Sets the table name.
	 *
	 * @param tableName the table name.
	 * @return the GenericCodeTableUpdate.
	 */
	public GenericCodeTableUpdate setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}
}
