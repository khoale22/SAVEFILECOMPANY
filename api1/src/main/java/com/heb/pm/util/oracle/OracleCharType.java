package com.heb.pm.util.oracle;
//db2ToOraclecHanges by vn76717
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StringType;
import org.hibernate.usertype.UserType;


public class OracleCharType implements UserType {

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
	    return new String(((String) o));
	}
	
	
	public boolean isMutable() {    
	    return false;
	}
	
	public Object assemble(Serializable cached, Object owner) {
	    return cached;
	}
	
	
	public Serializable disassemble(Object value) {
	    return (Serializable) value;
	}
	
	public Object replace(Object original, Object target, Object owner) {
	    return original;
	}
	
	public int hashCode(Object obj) {
	    return obj.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet inResultSet, String[] names,
			SessionImplementor sessionImplementor, Object arg3) throws HibernateException,
			SQLException {
		// TODO Auto-generated method stub
		String val = StringType.INSTANCE.nullSafeGet(inResultSet, names[0], sessionImplementor);
	    //System.out.println("From nullSafeGet method valu is "+val);
		//LOG.info("Value in getbefore trim:"+val);
	    return val == null ? null : val.trim();
	}

	@Override
	public void nullSafeSet(PreparedStatement inPreparedStatement, Object o, int i,
			SessionImplementor arg3) throws HibernateException, SQLException {
		// TODO Auto-generated method stub
		  String val = (String) o;
		  if(val ==  null || val.trim().length() == 0){
			  val = " ";
		  }
		  org.apache.commons.dbcp2.DelegatingStatement delgatingStmt = (org.apache.commons.dbcp2.DelegatingStatement)inPreparedStatement;
		  oracle.jdbc.OraclePreparedStatement oraclePreparedStmpt = (oracle.jdbc.OraclePreparedStatement)delgatingStmt.getInnermostDelegate();
		  oraclePreparedStmpt.setFixedCHAR(i, val);
		   
	}

  }
