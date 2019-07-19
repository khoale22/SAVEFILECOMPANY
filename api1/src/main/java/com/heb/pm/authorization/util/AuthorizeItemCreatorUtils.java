/*
 * AuthorizeItemCreatorUtils.java
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.authorization.util;

import com.heb.pm.authorization.AuthorizeItem;
import com.heb.pm.authorization.xml.*;
import com.heb.pm.entity.ApLocation;
import org.apache.commons.lang.StringUtils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * This class is used to setup the values to object for preparing convert to xml.
 * @author vn70529
 * @since 2.23.0
 */
public class AuthorizeItemCreatorUtils {
    /**
     * Sets G3 header data for G3_HDR_REC tag.
     * @param storeId the id of store.
     * @param g3HeaderRec G3HDRREC object
     */
    public static void setG3HeaderRec(String storeId, G3HDRREC g3HeaderRec) {
        G3HDRREC.G3HDRRECCD hdRecCode = ObjectFactory.createG3HDRRECG3HDRRECCD();
        hdRecCode.setHD(AuthorizationConstants.G3_HDR_REC_CODE);
        g3HeaderRec.setG3HDRRECCD(hdRecCode);
        g3HeaderRec.setHDRAPLYTYPCD(AuthorizationConstants.G3_HDR_APPLY_TYPE_CODE);
        g3HeaderRec.setHDRRSULTPTRCD(AuthorizationConstants.G3_HDR_RESULT_PTR_CODE);
        g3HeaderRec.setHDRAPLYDT(AuthorizationUtils.convertDateToXmlGregorianCalendar(new Date(), AuthorizationConstants.DATE_FORMAT_YYYYMMDD));
        g3HeaderRec.setHDRAPLYTM(AuthorizationUtils.convertDateToXmlGregorianCalendar(new Date(), AuthorizationConstants.DATE_FORMAT_HHMMSS));
        g3HeaderRec.setHDRSTRNBR(AuthorizationUtils.convertStringToBigInteger(storeId));
    }

    /**
     * Sets the data for G3DEPTREC/G3_DEPT_REC tag.
     * @param authorizeItem the authorizeItem
     * @param deptRec the object receive data.
     */
    public static void setDeptDetails(AuthorizeItem authorizeItem, G3DEPTREC deptRec){
        G3DEPTREC.G3DEPTRECCD departmentRecCode = ObjectFactory.createG3DEPTRECG3DEPTRECCD();
        departmentRecCode.setDP(AuthorizationConstants.G3_DEPT_REC_CD);
        deptRec.setG3DEPTRECCD(departmentRecCode);
        deptRec.setCRUDCD(AuthorizationConstants.G3_DEPT_REC_CRUD_CODE);
        deptRec.setDEPTRSULTCD(AuthorizationConstants.G3_DEPT_REC_RSULT_CODE);
        if(StringUtils.isNotBlank(authorizeItem.getDepartmentNo())) {
            deptRec.setDEPTDEPTNUMBER(BigInteger.valueOf(Long.valueOf(getDeptSupDept(authorizeItem))));
        }
    }
    /**
     * Returns the string holds department and sub department.
     * @param authorizeItem the authorizeItem.
     * @return the  department and sub department.
     */
    private static String getDeptSupDept(AuthorizeItem authorizeItem){
        String dept = StringUtils.EMPTY;
        if(StringUtils.isNotBlank(authorizeItem.getDepartmentNo())){
            dept = authorizeItem.getDepartmentNo().trim();
        }
        // Format sub department
        Integer subDept = Integer.valueOf(AuthorizationUtils.getSubDepartmentNumber(authorizeItem.getSubDepartmentNo()));
        String subDeptAsString = String.valueOf(subDept);
        if(subDept < 10){
            subDeptAsString = String.format("0%s",subDeptAsString);
        }
        // join department and sub department.
        return String.format("%s%s",dept, subDeptAsString);
    }
    /**
     * Set data to G3PRODREC object for G3_PROD_REC tag.
     * @param authorizeItem authorizeItem object
     * @param g3ProductRec G3PRODREC object
     * @param productId id of product.
     */
    public static void setProductDetails(AuthorizeItem authorizeItem, G3PRODREC g3ProductRec, Long productId){
            // Set data for CDS_ITM_ID tag.
            CDSITMID cdsItemCode = ObjectFactory.createCDSITMID();
            // Set data for PROD_DEPT_NBR.
            G3PRODREC.PRODDEPTNBR g3ProductRecDeptNumber = ObjectFactory.createG3PRODRECPRODDEPTNBR();
            // Set data for CDS_PROD_HIER tag.
            CDSPRODHIER cpsProductHier = ObjectFactory.createCDSPRODHIER();
            // G3_PROD_REC_CD Tag
            G3PRODREC.G3PRODRECCD g3ProductRecCode = ObjectFactory.createG3PRODRECG3PRODRECCD();
            //set the values for G3_PROD_REC tag
            g3ProductRecCode.setSK(AuthorizationConstants.G3_PROD_REC_CODE);
            g3ProductRec.setG3PRODRECCD(g3ProductRecCode);
            g3ProductRec.setCRUDCD(AuthorizationConstants.G3_PROD_REC_CRUD_CODE); //add or replace
            g3ProductRec.setPRODRSULTCD(AuthorizationConstants.G3_PROD_RSULT_CODE);
            cdsItemCode.setRETLUNTGTIN(BigDecimal.valueOf(productId));
            g3ProductRec.setCDSITMID(cdsItemCode);
            g3ProductRec.setPRODSKUTYPCD(AuthorizationConstants.G3_PROD_SKU_TYP_CD);
            g3ProductRec.setPRODAUTHSW(AuthorizationConstants.G3_PROD_AUTH_SW);
            g3ProductRec.setPRODACTVSW(AuthorizationConstants.G3_PROD_ACTV_SW);
            g3ProductRec.setPRODDISCSW(AuthorizationConstants.G3_PROD_DISC_SW);
            g3ProductRec.setPRODSTATCD(AuthorizationConstants.G3_PROD_STAT_CD);
            g3ProductRec.setPRODPRIMUPCID(BigDecimal.valueOf(authorizeItem.getProductPrimaryScanCodeId()));
            g3ProductRec.setPRODPRIMDES( authorizeItem.getProductDescription());
            if(StringUtils.isNotBlank(authorizeItem.getDepartmentNo())){
                cpsProductHier.setDEPTNBR(BigDecimal.valueOf(Long.valueOf(getDeptSupDept(authorizeItem))));
            }
            g3ProductRecDeptNumber.setCDSPRODHIER(cpsProductHier);
            g3ProductRec.setPRODDEPTNBR(g3ProductRecDeptNumber);
            g3ProductRec.setPRODVNDRID(BigInteger.valueOf(authorizeItem.getVendorId()));
            g3ProductRec.setPRODPACKQTY(authorizeItem.getMasterPack());
            g3ProductRec.setPRODREPTCD(AuthorizationConstants.G3_PROD_REPT_CD);
    }

    /**
     * Set data to G3CONSMUNTREC for G3_CONSM_UNT_REC tag.
     * @param authorizeItem authorizeItem object.
     * @param g3ConsRec G3CONSMUNTREC object.
     * @param isRetailTaxSwitch isRetailTaxSwitch flag.
     * @param isFoodStampSwitch isFoodStampSwitch flag
     */
    public static void setConsumerUnitDetails(AuthorizeItem authorizeItem, G3CONSMUNTREC g3ConsRec, Boolean isRetailTaxSwitch, Boolean isFoodStampSwitch){
        G3CONSMUNTREC.G3CURECCD g3ConsRecCuRecCode = ObjectFactory.createG3CONSMUNTRECG3CURECCD();
        G3CONSMUNTREC.CUUSR16 g3ConsRecCuUsr16 = ObjectFactory.createG3CONSMUNTRECCUUSR16();
        CDSPRODHIER cdsProdHier = ObjectFactory.createCDSPRODHIER();
        g3ConsRecCuRecCode.setIT(AuthorizationConstants.G3_CONSM_REC_CD);
        g3ConsRec.setG3CURECCD(g3ConsRecCuRecCode);
        g3ConsRec.setCRUDCD(AuthorizationConstants.G3_CONSM_UNT_REC_CRUD_CODE);
        g3ConsRec.setCURSULTCD(AuthorizationConstants.G3_CONSM_UNT_REC_RESULT_CODE);
        g3ConsRec.setCUUPC(AuthorizationUtils.convertStringToBigInteger(authorizeItem.getUpc()));
        if( authorizeItem.getItemType() != null && authorizeItem.getItemType().trim().equalsIgnoreCase(AuthorizationConstants.ITEM_TYPE_DSD))
        {
            //CU_ODR_CD:   New Item Setup: All new items are considered DSD items, hence send 0's
            g3ConsRec.setCUODRCD(BigInteger.ZERO);
        } else {
            //CU_ODR_CD:   For Item Auth if Type  != DSD set as WHS.
            g3ConsRec.setCUODRCD(AuthorizationUtils.convertStringToBigInteger(authorizeItem.getItemId()));
        }
        g3ConsRec.setCUEFFDT(AuthorizationUtils.convertDateToXmlGregorianCalendar(new Date(), AuthorizationConstants.DATE_FORMAT_YYYYMMDD));
        g3ConsRec.setCUTAGCNTLCD(BigInteger.ZERO);
        g3ConsRec.setCUEXTDES(authorizeItem.getProductDescription());
        if(StringUtils.isNotBlank(authorizeItem.getItemSize())) {
            g3ConsRec.setCUPACKSZ(authorizeItem.getItemSize().trim());
        }
        if(StringUtils.isNotBlank(authorizeItem.getDepartmentNo()) ){
            cdsProdHier.setDEPTNBR(BigDecimal.valueOf(Long.valueOf(getDeptSupDept(authorizeItem))));
        }
        g3ConsRec.setCDSPRODHIER(cdsProdHier);
        if(authorizeItem.getPssDepartmentOne()!= null) {
            g3ConsRec.setCUSCANPSSDEPT(BigInteger.valueOf(authorizeItem.getPssDepartmentOne()));
        }
        g3ConsRec.setCUDES(authorizeItem.getProductDescription());
        // To fix Food Stamp and Sales tax issue in Item Auth
        g3ConsRec.getCUTAXSW().add(isRetailTaxSwitch?AuthorizationConstants.G3_CONSM_CU_TAX_SW_ACTIVE:BigInteger.ZERO);
        g3ConsRec.setCUFDSTMP(isFoodStampSwitch?AuthorizationConstants.G3_CONSM_CU_FD_STMP_ACTIVE:BigInteger.ZERO);
        g3ConsRec.getCUFAMCD().add(BigInteger.ZERO);
        if(StringUtils.isNotBlank(authorizeItem.getScnTypCd()) &&
                authorizeItem.getScnTypCd().trim().equalsIgnoreCase(AuthorizationConstants.ITEM_TYPE_PLU)) {
            if( authorizeItem.getUpc().endsWith(AuthorizationConstants.DECIMAL_00000_FORMAT) &&  authorizeItem.getUpc().startsWith(AuthorizationConstants.PREFIX_PLU))
            {
                g3ConsRec.setCUSCLITMSW(BigInteger.ZERO);
            }
            else
            {
                g3ConsRec.setCUSCLITMSW(AuthorizationConstants.G3_CONSM_CU_SCL_ITM_SW_ACTIVE);
            }
        }
        else if(authorizeItem.getFlexWeightSwitch() )
        {
            g3ConsRec.setCUSCLITMSW(AuthorizationConstants.G3_CONSM_CU_SCL_ITM_SW_ACTIVE);
        }
        else
        {
            g3ConsRec.setCUSCLITMSW(BigInteger.ZERO); //UPC
        }
        BigInteger retail = BigInteger.valueOf(authorizeItem.getRetail());
        g3ConsRec.setCUXFORQTY(retail);
        g3ConsRec.setCURETLPRCAMT(BigDecimal.valueOf(authorizeItem.getRetailFor()));
        g3ConsRec.setCUDSDXFORQTY(retail);
        g3ConsRec.setCUDSDPRCAMT(g3ConsRec.getCURETLPRCAMT());
        g3ConsRec.setCUMXMATNUM(BigInteger.ZERO);
        g3ConsRec.setCUTAXEXMTAMT(BigDecimal.valueOf(0.00));
        g3ConsRec.setCUCOUPCD(AuthorizationConstants.G3_CCU_COUP_CD);
        g3ConsRec.setCUPOSITM(AuthorizationConstants.G3_CONSM_CU_POS_ITM);
        g3ConsRecCuUsr16.setCUOLDPRCAMT(BigDecimal.ZERO);
        g3ConsRec.setCUUSR16(g3ConsRecCuUsr16);
        g3ConsRec.setCUPRODID(AuthorizationUtils.convertStringToBigInteger(authorizeItem.getUpc()));
        g3ConsRec.setCUDSDITMSW(AuthorizationConstants.G3_CONSM_CU_DSD_ITM_SW);
    }

    /**
     * Sets data to G3VNDRREC object for G3_VNDR_REC tag.
     * @param authorizeItem authorizeItem object
     * @param apLocation apLocation object
     * @param g3VendorRec G3VNDRREC object
     */
    public static void setVendorDetails(AuthorizeItem authorizeItem, ApLocation apLocation, G3VNDRREC g3VendorRec){
        //Get AP Vendor details...
        G3VNDRREC.G3VNDRRECCD g3VendorRecCode = ObjectFactory.createG3VNDRRECG3VNDRRECCD();
        g3VendorRecCode.setSU(AuthorizationConstants.G3_VNDR_REC_CODE);
        g3VendorRec.setG3VNDRRECCD(g3VendorRecCode);
        g3VendorRec.setCRUDCD(AuthorizationConstants.G3_VNDR_REC_CRUD_CODE);
        g3VendorRec.setVNDRRSULTCD(AuthorizationConstants.G3_VNDR_RSULT_CODE);
        g3VendorRec.setVNDRVNDRID(BigInteger.valueOf(authorizeItem.getVendorId()));
        //If '1', allow detail receiving of delivery invoices.
        g3VendorRec.setVNDRRECFLAG(AuthorizationConstants.G3_VNDR_REC_FLAG);
        g3VendorRec.setVNDRDUNN(String.valueOf(apLocation.getDsdDbvNumber()));
        if(StringUtils.isNotBlank(apLocation.getRemitCoName())) {
            g3VendorRec.setVNDRNM(apLocation.getRemitCoName().trim());
        }
        if (StringUtils.isNotBlank(apLocation.getRemitAdr1()))
            g3VendorRec.setVNDRADD(apLocation.getRemitAdr1().trim());
        if (StringUtils.isNotBlank(apLocation.getRemitAdr2()) ){
            g3VendorRec.setVNDRCTY(apLocation.getRemitAdr2().trim());
        }
        if (StringUtils.isNotBlank(apLocation.getRemitState())) {
            g3VendorRec.setVNDRSTATE(apLocation.getRemitState().trim());
        }
        if ( StringUtils.isNotBlank(apLocation.getCorrContcName()) ) {
            g3VendorRec.setVNDRREP(apLocation.getCorrContcName().trim());
        }
        if ( apLocation.getRemitZip5Cd()!=null && apLocation.getRemitZip4Cd() !=null){
            // VNDRZIP = RMIT_ZIP5_CD + RMIT_ZIP4_CD (last 4)
            String strZIP4 = String.valueOf(apLocation.getRemitZip4Cd());
            if(strZIP4.length() > 4){
                strZIP4 = strZIP4.substring(strZIP4.length()-4, strZIP4.length());
            }
            //Formatting to proper length.
            String strZIP5 = AuthorizationUtils.formatDecimal(apLocation.getRemitZip5Cd(), AuthorizationConstants.DECIMAL_00000_FORMAT);
            g3VendorRec.setVNDRZIP(strZIP5 + strZIP4);
        }
        // VNDRPHN =  RMIT_AREA_CD +  RMIT_PHN_NBR
        g3VendorRec.setVNDRPHN(String.format("%d%d", apLocation.getRemitAreaCd(), apLocation.getRemitPhnNumber()));
        g3VendorRec.setVNDRTOT(AuthorizationConstants.G3_VNDR_TOT);
        g3VendorRec.setVNDRDTL(AuthorizationConstants.G3_VNDR_DTL);
        g3VendorRec.setVNDRSTYLE(AuthorizationConstants.G3_VNDR_STYLE);

        // To Fix issues with department and sub department accepted in Chain Track
        // As all the departments (vendor) not available in JoeVee this caused issues.
        // Decision is to get Item department and sub department and pass as vendor department
        // Sugesh,Sarat,Diana,Jack,Jon Keller all present and agreed for it. Krishna June 09 2010
        g3VendorRec.setVNDRDEPT(getDeptSupDept(authorizeItem));
        String strProcMode = apLocation.getDsdProcMode();
        if (strProcMode != null) {
            if ( (apLocation.getDsdProcMode().trim().equals(AuthorizationConstants.DSD_PROCESS_MODE))
                    && StringUtils.isNotBlank(apLocation.getDsdBnkCode()) &&
                    (apLocation.getDsdBnkCode().trim().equals(AuthorizationConstants.DSD_BNK_CODE))){
                strProcMode = AuthorizationConstants.ONE_VALUE;
            } else {
                strProcMode = AuthorizationConstants.ZERO_VALUE;
            }
        }
        g3VendorRec.setVNDRCOD(strProcMode);
        //DEFAULTED VALUES ACCORDING TO G3 COPY BOOK ...
        g3VendorRec.setVNDRDSD(AuthorizationConstants.ONE_VALUE);
        g3VendorRec.setVNDRCHECKINMETHD(AuthorizationConstants.ONE_VALUE);
        g3VendorRec.setVNDRNEXEVAL(AuthorizationConstants.ONE_VALUE);
        g3VendorRec.setVNDRDELMISCALLOWNCES(AuthorizationConstants.ONE_VALUE);
        g3VendorRec.setVNDRDELMISCCHRGS(AuthorizationConstants.ZERO_VALUE);
        g3VendorRec.setVNDRRETMISCCHRGS(AuthorizationConstants.ZERO_VALUE);
        g3VendorRec.setVNDRRETMISCALLOWANCES(AuthorizationConstants.ZERO_VALUE);
        if (null != apLocation.getDsdDbvLocationCode())
            g3VendorRec.setVNDRDUNNLOCCD( String.valueOf(apLocation.getDsdDbvLocationCode()));
        g3VendorRec.setVNDRRETONLY(AuthorizationConstants.ZERO_VALUE);
    }

    /**
     * Set data to G3PKREC object for G3_PK_REC tag.
     * @param authorizeItem authorizeItem object.
     * @param g3PackagekRec G3PKREC object.
     */
    public static void setPackageDetails(AuthorizeItem authorizeItem, G3PKREC g3PackagekRec) {
        G3PKREC.G3PACKRECCD g3PackagekRecCd = ObjectFactory.createG3PKRECG3PACKRECCD();
        g3PackagekRecCd.setPK(AuthorizationConstants.G3_PK_REC_CODE);
        g3PackagekRec.setG3PACKRECCD(g3PackagekRecCd);
        g3PackagekRec.setCRUDCD(AuthorizationConstants.G3_PK_REC_CRUD_CODE);
        g3PackagekRec.setPACKRSULTCD(AuthorizationConstants.G3_PK_REC_RSULT_CODE);
        g3PackagekRec.setPKGPKTYPCD(AuthorizationConstants.G3_PK_REC_PKG_PK_TYP_CODE);
        g3PackagekRec.setPKGCUUPC(AuthorizationUtils.convertStringToBigInteger(authorizeItem.getUpc()));
        g3PackagekRec.setCUPKQTY(AuthorizationUtils.convertStringToBigInteger(authorizeItem.getMasterPack()));
    }

    /**
     * Set data to G3SPLRPACKREC object for G3_SPLR_PACK_REC tag.
     * @param authorizeItem authorizeItem object
     * @param g3SupPackRec G3SPLRPACKREC object.
     */
    public static void setSupplierPackDetails(AuthorizeItem authorizeItem, G3SPLRPACKREC g3SupPackRec) {
        if(authorizeItem.getVendorId() != null) {
            G3SPLRPACKREC.G3SPLRPKRECCD g3SupPackRecCode = ObjectFactory.createG3SPLRPACKRECG3SPLRPKRECCD();
            G3SPLRPACKREC.SPLRPKID g3SupPackRecId = ObjectFactory.createG3SPLRPACKRECSPLRPKID();
            G3SPLRPACKREC.SPLRPKORDCD g3SupPackOrdCode = ObjectFactory.createG3SPLRPACKRECSPLRPKORDCD();
            CDSITMID g3Cds = ObjectFactory.createCDSITMID();
            g3SupPackRecCode.setSP(AuthorizationConstants.G3_SPLR_PACK_REC_CODE);
            g3SupPackRec.setG3SPLRPKRECCD(g3SupPackRecCode);
            g3SupPackRec.setCRUDCD(AuthorizationConstants.G3_SPLR_PACK_REC_CRUD_CODE);
            g3SupPackRec.setSPLRPKRSULTCD(AuthorizationConstants.G3_SPLR_PACK_REC_RSULT_CODE);
            g3Cds.setRETLUNTGTIN(BigDecimal.valueOf(Long.valueOf(authorizeItem.getUpc())));
            g3SupPackRecId.setCDSITMID(g3Cds);
            g3SupPackRec.setSPLRPKID(g3SupPackRecId);
            g3SupPackRec.setSPLRITMVNDRNUM(BigInteger.valueOf(authorizeItem.getVendorId()));
            g3SupPackRec.setSPLRPKTYPCD(AuthorizationConstants.G3_SPLR_PK_TYP_CD);
            g3SupPackRec.setSPLRPKQTY(AuthorizationUtils.convertStringToBigInteger(authorizeItem.getMasterPack()));
            g3SupPackOrdCode.setSPLRITMVNDRCD(AuthorizationConstants.G3_SPLR_PACK_ITM_VNDR_CODE);
            g3SupPackRec.setSPLRPKORDCD(g3SupPackOrdCode);
            g3SupPackRec.setSPLRPKPRIMSW(BigInteger.ZERO);
            g3SupPackRec.setSPLRPKRECVAUTHSW(AuthorizationConstants.SPLR_PK_RECV_AUTH_SW_ACTIVE);
            g3SupPackRec.setSPLRPKCST(BigDecimal.valueOf(authorizeItem.getCaseListCost()));
        }
    }

    /**
     * Set data to SCALEREC for SCALE_REC tag.
     * @param authorizeItem authorizeItem object
     * @param scaleRec SCALEREC object.
     * @param scaleHdrRec SCALEHDRREC object.
     * @param scaleDtlRec SCALEDTLREC object.
     * @param storeId id of store.
     * @param productDescriptionPrefix the prefix of product description.
     * @param scaleDescriptionSize the scale Description Size
     * @param batchNumber the batch number
     */
    public static void setPluDetails(AuthorizeItem authorizeItem, SCALEREC scaleRec, SCALEHDRREC scaleHdrRec,
                                     SCALEDTLREC scaleDtlRec,
                                     String storeId, String productDescriptionPrefix, String scaleDescriptionSize, BigInteger batchNumber) {
        //set the values for <SCALE_DTL_REC> tag
        SCALEDTLREC.SCALEPKRECCD rec = new SCALEDTLREC.SCALEPKRECCD();
        rec.setSC(AuthorizationConstants.SCALE_REC_PK_REC_CODE);
        scaleDtlRec.setSCALEPKRECCD(rec);
        scaleDtlRec.setCRUDCD(AuthorizationConstants.SCALE_REC_CRUD_CODE);
        scaleDtlRec.setCOMMANDCODE(AuthorizationConstants.SCALE_REC_COMMAND_CODE);
        scaleDtlRec.setSTORENUMBER(storeId);
        scaleDtlRec.setDEPARTMENTNUMBER(getDeptSupDept(authorizeItem));
        scaleDtlRec.setPLUNO(BigDecimal.valueOf(Long.valueOf(authorizeItem.getUpc())));
        scaleDtlRec.setUPCCODE(BigDecimal.valueOf(Long.valueOf(authorizeItem.getItemId())));
        scaleDtlRec.setDESCONE(String.format("%s%s", productDescriptionPrefix, authorizeItem.getProductDescription()));
        scaleDtlRec.setDESCSIZE1(scaleDescriptionSize);
        scaleDtlRec.setUNITPRICE(BigDecimal.valueOf(authorizeItem.getUnitCost()));
        scaleDtlRec.setSHELFLIFE(AuthorizationConstants.SCALE_REC_SHELF_LIFE);
        //set the values for <SCALE_HDR_REC> tag
        scaleHdrRec.setBATCHNUMBER(batchNumber);
        Date currentDate = new Date();
        XMLGregorianCalendar time = AuthorizationUtils.convertDateToXmlGregorianCalendar(currentDate, AuthorizationConstants.DATE_FORMAT_HHMMSS);
        XMLGregorianCalendar date = AuthorizationUtils.convertDateToXmlGregorianCalendar(currentDate, AuthorizationConstants.DATE_FORMAT_YYYYMMDD);
        scaleHdrRec.setBATCHDATE(date);
        scaleHdrRec.setBATCHTIME(time);
        scaleHdrRec.setBATCHEFFECTIVEDATE(date);
        scaleHdrRec.setBATCHEFFECTIVETIME(time);
        scaleHdrRec.setSTORENUMBER(AuthorizationUtils.convertStringToBigInteger(storeId));
        //place the <SCALE_HDR_REC> & <SCALE_DTL_REC> inside <SCALE_REC> tag
        scaleRec.setSCALEHDRREC(scaleHdrRec);
        scaleRec.setSCALEDTLREC(scaleDtlRec);
    }
}
