/*
 * OracleCharTypeBase
 *
 * Copyright (c) 2018 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */
package com.heb.batch.wic.utils.oracle;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Represents a Oracle Char Types class.
 *
 * @author vn55306
 * @since 1.0.0
 */

public class OracleCharTypeBase implements UserType{

	public int[] sqlTypes() {
	    return new int[] { Types.CHAR };
	}
	
	
	
	public Class<String> returnedClass() {
	    return String.class;
	}
	
	public boolean equals(Object x, Object y) {
	    return (x == y) || (x != null && y != null && (x.equals(y)));
	}


	public Object deepCopy(Object o) {
	    if (o == null) {
	        return null;
	    }
		return String.valueOf(o);
	}
	
	
	public boolean isMutable() {    
	    return false;
	}
	
	public Object assemble(Serializable cached, Object owner) {
	    return cached;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) {
		return original;
	}


	public Serializable disassemble(Object value) {
	    return (Serializable) value;
	}

	public int hashCode(Object obj) {
	    return obj.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
		return null;
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws SQLException {
		//Implemented in sub class.
	}


}
