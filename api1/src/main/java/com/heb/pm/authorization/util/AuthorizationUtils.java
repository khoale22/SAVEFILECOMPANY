/*
 * AuthorizationUtils.java
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.authorization.util;

import org.apache.commons.lang.StringUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.*;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Holds util methods for authorization.
 * @author vn70529
 * @since 2.23.0
 */
public class AuthorizationUtils {
    /**
     * This method returns the current with format.
     * @return current date
     */
    public static String getCurrentDateByFormat(String format) {
        return ZonedDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }
    /**
     * Format upc to upc for plu with pre two zero digits
     * @param upc the upc to format.
     * @return plu upc.
     */
    public static String formatPluByUpc(String upc) {
        String unitUpc = upc;
        if(unitUpc!=null && unitUpc.trim().length() == 4 )
        {
            unitUpc = String.format("%s%s%s",AuthorizationConstants.PREFIX_PLU,AuthorizationConstants.ZERO_VALUE, upc);
        } else if(unitUpc!=null && unitUpc.trim().length()== 5 )
        {
            unitUpc = String.format("%s%s",AuthorizationConstants.PREFIX_PLU, upc);
        }
        if (unitUpc!=null &&  unitUpc.trim().length() > 0) {
            int unitUPCLength = unitUpc.length();
            int padSize = 13 - unitUPCLength;
            int i = 0;
            while (i < padSize) {
                unitUpc = String.format("%s%s",unitUpc, AuthorizationConstants.ZERO_VALUE);
                i++;
            }
        }
        return unitUpc;
    }
    /**
     * Returns sub department number by sub department no.
     * @param subDepartmentNo the sub department no.
     * @return sub department number
     */
    public static String getSubDepartmentNumber(String subDepartmentNo){
        if(StringUtils.isEmpty(subDepartmentNo)) {
            return StringUtils.EMPTY;
        }
        subDepartmentNo = subDepartmentNo.trim();
        String subDeptNbr = StringUtils.EMPTY;
        int subDeptPosition = 0;
        // split sub dept id string to the list of sub department ids.
        List<String> subDepartmentIds = new ArrayList<String>();
        StringTokenizer str = new StringTokenizer(AuthorizationConstants.SUB_DEPARTMENT_ID,
                AuthorizationConstants.COMMA);
        while(str.hasMoreTokens()) {
            subDepartmentIds.add(str.nextToken());
        }
        // Get position number from 0,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z
        // by character.
        for(int i = 0; i < subDepartmentIds.size(); i++) {
            if(subDepartmentIds.get(i).equalsIgnoreCase(subDepartmentNo.trim())) {
                subDeptPosition = i;
                break;
            }
        }
        if(subDeptPosition != 0) {
            subDeptNbr = formatDecimal(subDeptPosition, AuthorizationConstants.SUB_DEPARTMENT_NUMBER_FORMAT);
        }
        return subDeptNbr;
    }
    /**
     * This method generates and returns the batch number used in
     * scales record for XML. The number sequence is stored
     * in the log file
     * @param batchInitialNo default initial no
     * @param batchResetNumber the max initial no to reset.
     * @param batchLogPath the path of batch log path.
     * @param batchLogName the batch log name.
     * @return the batch number.
     */
    public static BigInteger getBatchNumber(String batchInitialNo, String batchResetNumber, String batchLogPath, String batchLogName) throws Exception {
        // Default value for batch no in the first time.
        final String batchNo = readBatchNoFromLogFile(batchInitialNo, batchLogPath, batchLogName);
        int batchNoAsInteger = Integer.parseInt(batchNo);
        // Reset batch no to default value when it equals to batchResetNumber
        if (batchNoAsInteger == Integer.parseInt(batchResetNumber)) {
            //reset the value to start again
            batchNoAsInteger = Integer.parseInt(batchInitialNo);
        }
        // Increase batch no up to one value.
        batchNoAsInteger++;
        updateBatchNoToLogFile(batchNoAsInteger, batchLogPath, batchLogName);
        return BigInteger.valueOf(batchNoAsInteger);
    }

    /**
     * Return batch no from batch log file.
     * @param batchInitialNo batch Initial No.
     * @param batchLogPath the path of batch log path.
     * @param batchLogName the batch log name.
     * @return Return batch no.
     */
    private static String readBatchNoFromLogFile(String batchInitialNo, String batchLogPath, String batchLogName){
        String value = batchInitialNo;
        String strLine = "";
        DataInputStream in = null;
        try {
            // Read batch no from log file.
            FileInputStream fStream = new FileInputStream(batchLogPath + batchLogName);
            // Get the object of DataInputStream
            in = new DataInputStream(fStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            //Read File Line By Line
            while((strLine = br.readLine()) != null) {
                value = strLine.toString().trim();
            }
            //Close the input stream
        } catch(Exception e) {
        }
        finally {
            if(in != null){
                try {
                    in.close();
                }catch (Exception e){}
            }
        }
        return value;
    }

    /**
     * Update batch no to batch log file.
     * @param batchNo the batch no to update.
     * @param batchLogPath the path of batch log path.
     * @param batchLogName the batch log name.
     */
    private static void updateBatchNoToLogFile(int batchNo, String batchLogPath, String batchLogName) throws Exception{
        StringBuilder filePath = new StringBuilder();
        StringBuilder logMsg = new StringBuilder();
        filePath.append(batchLogPath);
        filePath.append(batchLogName);
        BufferedWriter out = null;
        try{
            out = new BufferedWriter(new FileWriter(
                    filePath.toString(), false));
            logMsg.append(batchNo);
            out.write(logMsg.toString());
        } catch(IOException ioe) {
            throw ioe;
        }finally {
            if(out != null){
                out.close();
            }
        }
    }
    /**
     * Convert java date to XmlGregorianCalendar
     * @param date java date
     * @param format format date
     * @return XmlGregorianCalendar;
     */
    public static XMLGregorianCalendar convertDateToXmlGregorianCalendar(Date date, String format){
        DateFormat dateFormat=new SimpleDateFormat(format);
        String strDate = dateFormat.format(date);
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(strDate);
        }
        catch (  DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Format decimal by format string.
     * @param val the value to format.
     * @param format the format string.
     * @return the string has been formatted.
     */
    public static String formatDecimal(Integer val, String format) {
        return new DecimalFormat(format).format(val);
    }

    /**
     * Convert String to big integer.
     * @param val the number string
     * @return the big integer.
     */
    public static BigInteger convertStringToBigInteger(String val){
        if(StringUtils.isEmpty(val)){
            return null;
        }
        return BigInteger.valueOf(Long.valueOf(val));
    }
    /**
     * Calculate unit retail for authorize item
     * @param retailFor the retail for.
     * @param retail the retail.
     * @return double
     */
    public static double calculateUnitRetail(double retailFor, double retail){
        return (double) Math.round((retailFor/ retail)*100)/100;
    }
}
