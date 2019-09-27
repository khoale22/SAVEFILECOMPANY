package com.heb.batch.wic.utils.oracle;
//db2ToOraclecHanges by vn76717

import org.hibernate.annotations.TypeDef;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StringType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

@TypeDef(name = "fixedLengthCharPK", typeClass = OracleFixedLengthCharTypePK.class)
public class OracleFixedLengthCharTypePK extends OracleCharTypeBase {

	@Override
	public int[] sqlTypes() {
	    return new int[] { Types.CHAR };
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
		String val = StringType.INSTANCE.nullSafeGet(rs, names[0], session);
		return val == null ? null : val.trim();
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws SQLException {
		String val = (String) value;
		if (st instanceof org.apache.commons.dbcp2.DelegatingPreparedStatement) {
			st = (PreparedStatement)(( org.apache.commons.dbcp2.DelegatingPreparedStatement)st).getDelegate();
			oracle.jdbc.OraclePreparedStatement oraclePreparedStmpt = (oracle.jdbc.OraclePreparedStatement)st;
			oraclePreparedStmpt.setFixedCHAR(index, val);
		} else {
			if(val ==  null || val.trim().length() == 0){
				val = " ";
			}
			st.setString(index, val);
		}

	}

  }
