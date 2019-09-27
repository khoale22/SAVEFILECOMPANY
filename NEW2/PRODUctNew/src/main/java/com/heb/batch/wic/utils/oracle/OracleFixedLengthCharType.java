/*
 * ProductScanCodes
 *
 * Copyright (c) 2018 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */
package com.heb.batch.wic.utils.oracle;

import org.hibernate.annotations.TypeDef;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StringType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Represents a Oracle Fixed Length Char Type class.
 *
 * @author vn55306
 * @since 1.0.0
 */
@TypeDef(name = "fixedLengthChar", typeClass = OracleFixedLengthCharType.class)
public class OracleFixedLengthCharType extends OracleCharTypeBase {

	@Override
	public int[] sqlTypes() {
	    return new int[] { Types.CHAR };
	}

	@Override
	public boolean equals(Object x, Object y) {
        if(null != x && null != y){
               String strX = String.valueOf(x).trim();
               String strY = String.valueOf(y).trim();
               return strX.equals(strY);
        }else{
               return (x == y);
        }
	}


	@Override
	public Object nullSafeGet(ResultSet inResultSet, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
		String val = StringType.INSTANCE.nullSafeGet(inResultSet, names[0], session);
		return val == null ? null : val.trim();
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws SQLException {
		String val = (String) value;
		if(val ==  null || val.trim().length() == 0){
			val = " ";
		}
		st.setString(index, val);
	}

  }
