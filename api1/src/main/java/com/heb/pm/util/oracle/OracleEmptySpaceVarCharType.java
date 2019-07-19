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
import org.apache.log4j.Logger;

public class OracleEmptySpaceVarCharType implements UserType {
	
	 public static final String SPACE_STRING = " ";
	
	private static final Logger LOG = Logger
			.getLogger(OracleEmptySpaceVarCharType.class);

	public int[] sqlTypes() {
	    return new int[] { Types.VARCHAR };
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
	    return val == null ? null : val.trim();
	}

	@Override
	public void nullSafeSet(PreparedStatement inPreparedStatement, Object o, int i,
			SessionImplementor arg3) throws HibernateException, SQLException {
		// TODO Auto-generated method stub
		  String val = (String) o;
		  if(val ==  null || val.trim().length() == 0){
			  val = this.SPACE_STRING;
		  }
		   inPreparedStatement.setString(i, val);
		   //LOG.info("Value is set:"+val);
		
	}

  }
