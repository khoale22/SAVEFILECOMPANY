package com.heb.pm.codeTable.generic;

import com.heb.pm.codeTable.CodeTableRepositoryFactory;
import com.heb.pm.codeTable.factory.CodeTableFrontEnd;
import com.heb.pm.entity.CodeTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Business functions for generic code tables.
 *
 * @author m314029
 * @since 2.21.0
 */
@Service
public class GenericCodeTableService {

	@Autowired
	private CodeTableRepositoryFactory factory;

	/**
	 * Finds all records for a given code table by table name.
	 *
	 * @param tableName Table that contains code table values.
	 * @return All generic code tables matching the request.
	 */
	public List<? extends CodeTable> findAllByTable(String tableName) {
		return this.factory.getRepository(tableName).findAll();
	}

	/**
	 * Returns a CodeTable record by id and by table name.
	 *
	 * @param tableName Table that contains code table values.
	 * @param id code id.
	 * @return a CodeTable record by id and table name.
	 */
	public CodeTable findCodeByTableNameAndId(String tableName, String id) {
		return this.factory.getRepository(tableName).findOne(id);
	}

	/**
	 * Saves all entities by table name and code table front ends.
	 *
	 * @param tableName the table name.
	 * @param codeTables the code table front ends.
	 * @return a list CodeTable record by id and table name.
	 */
	public List<? extends CodeTable> saveAll(String tableName, List<CodeTableFrontEnd> codeTables) {
		return this.factory.getRepository(tableName).save(this.factory.toEntities(tableName, codeTables));
	}

	/**
	 * Deletes code table records by table name and code id.
	 *
	 * @param tableName the table name.
	 * @param id the code id.
	 */
	public void deleteByTableNameAndId(String tableName, String id) {
		this.factory.getRepository(tableName).delete(id);
	}
}
