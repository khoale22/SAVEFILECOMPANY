package com.heb.pm.entity;

import java.io.Serializable;

/**
 * This Interface is used to implement an object that is a code table.
 *
 * @author m314029
 * @since 2.21.0
 */
public interface CodeTable extends Serializable {

	String getId();
	CodeTable setId(String id);
	String getDescription();
	CodeTable setDescription(String description);
}
