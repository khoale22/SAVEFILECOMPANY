/*
 *  CandidateItemMaster
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Represents a candidate item master.
 *
 * @author vn00602
 * @since 2.12.0
 */
@Entity
@Table(name = "ps_item_master")
@TypeDefs({
        @TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class)
})
public class CandidateItemMaster implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String ITEM_KEY_TYPE_DSD="DSD";
    public static final String ITEM_KEY_TYPE_ITMCD="ITMCD";
    public static final String PURCHASE_STATUS_A="A";
    public static final String PURCHASE_STATUS_D="D";
    public static final String STRING_EDC_WHS="101";
    public static final String DEFAULT_EMPTY_STRING = " ";
    public static final String DC_CHNL_TYP_CD_WHS="WHS";
    @Id
    @Column(name = "PS_ITM_ID", unique = true, nullable = false)
    @GeneratedValue(strategy = SEQUENCE, generator = "ps_item_master_seq")
    @SequenceGenerator(name = "ps_item_master_seq", sequenceName = "PS_ITEM_MASTER_SEQ", allocationSize = 1)
    private Integer candidateItemId;

    @Column(name = "ITM_KEY_TYP_CD", length = 5)
    @Type(type = "fixedLengthChar")
    private String itemKeyType;

    @Column(name = "DC_CHNL_TYP_CD", length = 5)
    @Type(type = "fixedLengthChar")
    private String dcChannelType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PS_WORK_ID", referencedColumnName = "PS_WORK_ID", insertable = false, updatable = false, nullable = false)
    private CandidateWorkRequest candidateWorkRequest;

    @Column(name = "SPCL_TAX_CD", length = 5)
    @Type(type = "fixedLengthChar")
    private String specialTax;

    @Column(name = "PLUS_ITM_TYP_CD")
    private String plusItemType;

    @Column(name = "ITM_SZ_UOM_CD", length = 5)
    @Type(type = "fixedLengthChar")
    private String itemSizeUom;

    @Column(name = "ITM_MDSE_TYP_CD")
    private String itemMerchandiseType;

    @Column(name = "SRC_CD")
    private String sourceCode;

    @Column(name = "ITEM_DES", length = 30)
    @Type(type = "fixedLengthChar")
    private String itemDescription;

    @Column(name = "SHRT_ITM_DES", length = 20)
    private String shortItemDescription;

    @Column(name = "ITEM_SIZE_TXT", length = 12)
    private String itemSizeText;

    @Column(name = "ITM_SZ_QTY", precision = 9)
    private Double itemSizeQuantity;

    @Column(name = "ADDED_DT")
    private LocalDate addedDate;

    @Column(name = "ADDED_USR_ID", nullable = false, length = 8)
    @Type(type = "fixedLengthChar")
    private String addedUserId;

    @Column(name = "DSCON_TRX_SW")
    private boolean discontinueTracking;

    @Column(name = "DSCON_DT")
    private LocalDate discontinueDate;

    @Column(name = "DSCON_USR_ID", length = 8)
    private String discontinueUserId;

    @Column(name = "DELETE_DT")
    private LocalDate deleteDate;

    @Column(name = "SPLR_CS_LIF_DD")
    private Integer supplierCaseLifeDays;

    @Column(name = "ON_RCPT_LIF_DD")
    private Integer onReceiptLifeDays;

    @Column(name = "WHSE_REACTION_DD")
    private Integer warehouseReactionDays;

    @Column(name = "GUARN_TO_STR_DD")
    private Integer guaranteeToStoreDays;

    @Column(name = "DT_CNTRLD_ITM_SW", nullable = false)
    private boolean dateControlledItem;

    @Column(name = "PD_OMI_COM_CD", precision = 10)
    private Integer commodityCode;

    @Column(name = "PD_OMI_SUB_COM_CD", precision = 10)
    private Integer subCommodityCode;

    @Column(name = "PD_OMI_COM_CLS_CD", precision = 3)
    private Short classCode;

    @Column(name = "IMS_COM_CD", precision = 10)
    private Long imsCommodityCode;

    @Column(name = "IMS_SUB_COM_CD", length = 1)
    private String imsSubCommodityCode;

    @Column(name = "CASE_UPC", precision = 18)
    private Long caseUpc;

    @Column(name = "ORDERING_UPC", precision = 18)
    private Long orderingUpc;

    @Column(name = "USDA_NBR")
    private Integer usdaNumber;

    @Column(name = "MEX_AUTH_CD", length = 1)
    private String mexicoAuthorizationCode;

    @Column(name = "MEX_BRDR_AUTH_CD", length = 1)
    private String mexicanBorderAuthorizationCode;

    @Column(name = "MAX_SHIP_QTY")
    private Integer maxShipQuantity;

    @Column(name = "ABC_AUTH_CD", length = 1)
    private String abcAuthorizationCode;

    @Column(name = "NEW_ITM_SW", nullable = false)
    private boolean newItem;

    @Column(name = "REPACK_SW", nullable = false)
    private boolean repack;

    @Column(name = "CRIT_ITM_IND", nullable = false, length = 1)
    @Type(type = "fixedLengthChar")
    private String criticalItemIndicator;

    @Column(name = "NEV_OUT_SW", nullable = false)
    private boolean nevOut;

    @Column(name = "CTCH_WT_SW", nullable = false)
    private boolean catchWeight;

    @Column(name = "LOW_VEL_SW", nullable = false)
    private boolean lowVel;

    @Column(name = "SHPR_ITM_SW", nullable = false)
    private boolean supplierItem;

    @Column(name = "SRS_HNDLG_CD", length = 1)
    private String srsHandlingCode;

    @Column(name = "CROSS_DOCK_ITM_SW", nullable = false)
    private boolean crossDockItem;

    @Column(name = "RPLAC_ORD_QTY_SW", nullable = false)
    private boolean replaceOrderQuantity;

    @Column(name = "LTR_OF_CR_SW", nullable = false)
    private boolean letterOfCredit;

    @Column(name = "VAR_WT_SW", nullable = false)
    private boolean variableWeight;

    @Column(name = "FWDBY_APPR_SW", nullable = false)
    private boolean forwardBuyApproved;

    @Column(name = "CATTLE_SW", nullable = false)
    private boolean cattle;

    @Column(name = "HEB_ITM_PK")
    private Long pack;

    @Column(name = "RETL_SALS_PK")
    private Integer retailSalesPack;

    @Column(name = "DIM_CK_IND", length = 1)
    private String dimCkInd;

    @Column(name = "AVG_WHLSL_CST", precision = 11, scale = 4)
    private Double averageWholesaleCost;

    @Column(name = "DSD_ITM_SW", nullable = false)
    private boolean dsdItem;

    @Column(name = "UPC_MAP_SW", nullable = false)
    private boolean upcMap;

    @Column(name = "DEPOSIT_SW", nullable = false)
    private boolean deposit;

    @Column(name = "LST_UPDT_TS", nullable = false)
    private LocalDateTime lastUpdateDate;

    @Column(name = "LST_UPDT_USR_ID", nullable = false, length = 8)
    @Type(type = "fixedLengthChar")
    private String lastUpdateUserId;

    @Column(name = "DEPT_ID_1")
    private Integer departmentIdOne;

    @Column(name = "SUB_DEPT_ID_1", length = 1)
    private String subDepartmentIdOne;

    @Column(name = "DEPT_MDSE_TYP_1", length = 1)
    private String merchandiseTypeCodeOne;

    @Column(name = "PSS_DEPT_1")
    private Integer pssDepartmentCodeOne;

    @Column(name = "DEPT_ID_2")
    private Integer departmentIdTwo;

    @Column(name = "SUB_DEPT_ID_2", length = 1)
    private String subDepartmentIdTwo;

    @Column(name = "DEPT_MDSE_TYP_2", length = 1)
    private String merchandiseTypeCodeTwo;

    @Column(name = "PSS_DEPT_2")
    private Integer pssDepartmentCodeTwo;

    @Column(name = "DEPT_ID_3")
    private Integer departmentIdThree;

    @Column(name = "SUB_DEPT_ID_3", length = 1)
    private String subDepartmentIdThree;

    @Column(name = "DEPT_MDSE_TYP_3", length = 1)
    private String merchandiseTypeCodeThree;

    @Column(name = "PSS_DEPT_3")
    private Integer pssDepartmentCodeThree;

    @Column(name = "DEPT_ID_4")
    private Integer departmentIdFour;

    @Column(name = "SUB_DEPT_ID_4", length = 1)
    private String subDepartmentIdFour;

    @Column(name = "DEPT_MDSE_TYP_4", length = 1)
    private String merchandiseTypeCodeFour;

    @Column(name = "PSS_DEPT_4")
    private Integer pssDepartmentCodeFour;

    @Column(name = "GROSS_WT", precision = 9, scale = 3)
    private Double grossWeight;

    @Column(name = "UNSTAMPED_TBCO_SW", nullable = false)
    private boolean unstampedTobacco;

    @Column(name = "ITM_ID", precision = 17)
    private Long itemCode;

    @Column(name = "INBND_SPEC_DD")
    private Integer inboundSpecDays;

    @Column(name = "PROR_DT")
    private LocalDate priorDate;

    @Column(name = "IN_STR_DT")
    private LocalDate inStoreDate;

    @Column(name = "WHSE_FLSH_DT")
    private LocalDate warehouseFlushDate;

    @Column(name = "SELL_YY")
    private Integer sellByYear;

    @Column(name = "CRTN_MRKNG_TXT", length = 30)
    private String cartonMarking;

    @Column(name = "PRODN_MIN_ORD_QTY", precision = 7)
    private Integer minOrderQuantity;

    @Column(name = "PRODN_MIN_ORD_DES", length = 20)
    private String minimumOrderDescription;

    @Column(name = "SEASN_TXT", length = 20)
    private String season;

    @Column(name = "CNTAN_SZ_CD", length = 7)
    private String containerSizeCode;

    @Column(name = "INCO_TRM_CD", length = 3)
    private String incomeTermsCode;

    @Column(name = "PCKUP_PNT_TXT", length = 20)
    private String pickupPoint;

    @Column(name = "HTS_NBR", precision = 10)
    private Long htsNumber;

    @Column(name = "CNTRY_OF_ORIG_NM", length = 30)
    private String countryOfOrigin;

    @Column(name = "DUTY_RT_PCT", precision = 9, scale = 4)
    private Double dutyPercent;

    @Column(name = "DUTY_CNFRM_TXT", length = 20)
    private String dutyConfirmation;

    @Column(name = "FRT_CNFRM_TXT", length = 20)
    private String freightConfirmation;

    @Column(name = "AGENT_COMSN_PCT", precision = 9, scale = 4)
    private Double agentCommissionPercent;

    @Column(name = "COLOR_DES", length = 50)
    private String colorDescription;

    @Column(name = "MSTR_PK_QTY")
    private Integer masterPackQuantity;

    @Column(name = "MAX_SHLF_LIFE_DD")
    private Integer maxShelfLifeDays;

    @Column(name = "MRT_SW")
    private boolean mrt;

    @Column(name = "NEW_DATA_SW", nullable = false)
    private boolean newData;

    @Column(name = "SHP_CS_LN", precision = 7)
    private Double shipLength;

    @Column(name = "SHP_CS_WD", precision = 7)
    private Double shipWidth;

    @Column(name = "SHP_CS_HT", precision = 7)
    private Double shipHeight;

    @Column(name = "SHP_CU", precision = 9, scale = 3)
    private Double shipCube;

    @Column(name = "SHP_NEST_CU", precision = 9, scale = 3)
    private Double shipNestCube;

    @Column(name = "SHP_NEST_MAX_QTY")
    private Integer shipNestMaxQuantity;

    @Column(name = "SHP_WT", precision = 9, scale = 3)
    private Double shipWeight;

    @Column(name = "SHP_INR_PK", precision = 10)
    private Long shipInrPack;

    @Column(name = "VEND_LN", precision = 7)
    private Double length;

    @Column(name = "VEND_WD", precision = 7)
    private Double width;

    @Column(name = "VEND_HT", precision = 7)
    private Double height;

    @Column(name = "VEND_WT", precision = 7)
    private Double weight;

    @Column(name = "VEND_CU", precision = 9, scale = 4)
    private Double cube;

    @Column(name = "VEND_NEST_CU", precision = 9, scale = 3)
    private Double nestCube;

    @Column(name = "VEND_NEST_MAX")
    private Integer nestMax;

    @Column(name = "VEND_PK_QTY")
    private Integer packQuantity;

    @Column(name = "CS_UNT_FACTR_1", precision = 9, scale = 4)
    private Double csUnitFactoryOne;

    @Column(name = "ORD_CTLG_NBR", precision = 5)
    private Integer orderCatalogNumber;

    @Column(name = "ABC_ITM_CAT_NO")
    private Integer abcItemCategoryNo;

    @Column(name = "PRCH_STAT_CD", length = 5)
    private String purchaseStatus;

    @Column(name = "DSPLY_RDY_PAL_SW", nullable = false)
    private boolean displayReadyUnit;

    @Column(name = "STD_SUBST_LOGIC_SW")
    private boolean alwaysSubWhenOut;

    @Column(name = "SRS_AFF_TYP_CD", length = 1)
    private String typeOfDRU;

    @Column(name = "PROD_FCNG_NBR")
    private Long rowsFacing;

    @Column(name = "PROD_ROW_DEEP_NBR")
    private Long rowsDeep;

    @Column(name = "PROD_ROW_HI_NBR")
    private Long rowsHigh;

    @Column(name = "NBR_OF_ORINT_NBR", precision = 3)
    private Long orientation;
    /**
     * The one touch typ cd.
     */
    @Column(name = "ONE_TOUCH_TYP_CD", length = 2)
    private String oneTouchTypCode;

    @Column(name = "PS_WORK_ID", nullable = false)
    private Long candidateWorkRequestId;

    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ONE_TOUCH_TYP_CD", referencedColumnName = "one_touch_typ_cd", insertable = false, updatable = false, nullable = false)
	private OneTouchType oneTouchType;    
    
    @JsonIgnoreProperties("candidateItemMaster")
	@OneToMany(mappedBy = "candidateItemMaster", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private List<CandidateWarehouseLocationItem> candidateWarehouseLocationItems;

    @JsonIgnoreProperties("candidateItemMaster")
    @OneToMany(mappedBy = "candidateItemMaster", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<CandidateVendorLocationItem> candidateVendorLocationItems;

    @JsonIgnoreProperties("candidateItemMaster")
    @OneToMany(mappedBy = "candidateItemMaster", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<CandidateItemScanCode> candidateItemScanCodes;

    @OneToMany(mappedBy = "candidateItemMasterByPsItmId", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<CandidateRelatedItem> candidateRelatedItems;


    /**
     * Gets the ps itm id.
     *
     * @return the ps itm id
     */
    public Integer getCandidateItemId() {
        return this.candidateItemId;
    }

    /**
     * Sets the ps itm id.
     *
     * @param candidateItemId the new ps itm id
     */
    public void setCandidateItemId(Integer candidateItemId) {
        this.candidateItemId = candidateItemId;
    }

    /**
     * Gets the itm key typ cd.
     *
     * @return the itm key typ cd
     */
    public String getItemKeyType() {
        return this.itemKeyType;
    }

    /**
     * Sets the itm key typ cd.
     *
     * @param itemKeyType the new itm key typ cd
     */
    public void setItemKeyType(String itemKeyType) {
        this.itemKeyType = itemKeyType;
    }

    /**
     * Gets the dc chnl typ cd.
     *
     * @return the dc chnl typ cd
     */
    public String getDcChannelType() {
        return this.dcChannelType;
    }

    /**
     * Sets the dc chnl typ cd.
     *
     * @param dcChannelType the new dc chnl typ cd
     */
    public void setDcChannelType(String dcChannelType) {
        this.dcChannelType = dcChannelType;
    }

    /**
     * Gets the ps work rqst.
     *
     * @return the ps work rqst
     */
    public CandidateWorkRequest getCandidateWorkRequest() {
        return this.candidateWorkRequest;
    }

    /**
     * Sets the ps work rqst.
     *
     * @param candidateWorkRequest the new ps work rqst
     */
    public void setCandidateWorkRequest(CandidateWorkRequest candidateWorkRequest) {
        this.candidateWorkRequest = candidateWorkRequest;
    }

    /**
     * Gets the spcl tax cd.
     *
     * @return the spcl tax cd
     */
    public String getSpecialTax() {
        return this.specialTax;
    }

    /**
     * Sets the spcl tax cd.
     *
     * @param specialTax the new spcl tax cd
     */
    public void setSpecialTax(String specialTax) {
        this.specialTax = specialTax;
    }

    /**
     * Gets the plus itm typ cd.
     *
     * @return the plus itm typ cd
     */
    public String getPlusItemType() {
        return this.plusItemType;
    }

    /**
     * Sets the plus itm typ cd.
     *
     * @param plusItemType the new plus itm typ cd
     */
    public void setPlusItemType(String plusItemType) {
        this.plusItemType = plusItemType;
    }

    /**
     * Gets the itm sz uom cd.
     *
     * @return the itm sz uom cd
     */
    public String getItemSizeUom() {
        return this.itemSizeUom;
    }

    /**
     * Sets the itm sz uom cd.
     *
     * @param itemSizeUom the new itm sz uom cd
     */
    public void setItemSizeUom(String itemSizeUom) {
        this.itemSizeUom = itemSizeUom;
    }

    /**
     * Gets the itm mdse typ cd.
     *
     * @return the itm mdse typ cd
     */
    public String getItemMerchandiseType() {
        return this.itemMerchandiseType;
    }

    /**
     * Sets the itm mdse typ cd.
     *
     * @param itemMerchandiseType the new itm mdse typ cd
     */
    public void setItemMerchandiseType(String itemMerchandiseType) {
        this.itemMerchandiseType = itemMerchandiseType;
    }

    /**
     * Gets the src cd.
     *
     * @return the src cd
     */
    public String getSourceCode() {
        return this.sourceCode;
    }

    /**
     * Sets the src cd.
     *
     * @param sourceCode the new src cd
     */
    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    /**
     * Gets the item des.
     *
     * @return the item des
     */
    public String getItemDescription() {
        return this.itemDescription;
    }

    /**
     * Sets the item des.
     *
     * @param itemDescription the new item des
     */
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    /**
     * Gets the shrt itm des.
     *
     * @return the shrt itm des
     */
    public String getShortItemDescription() {
        return this.shortItemDescription;
    }

    /**
     * Sets the shrt itm des.
     *
     * @param shortItemDescription the new shrt itm des
     */
    public void setShortItemDescription(String shortItemDescription) {
        this.shortItemDescription = shortItemDescription;
    }

    /**
     * Gets the item size txt.
     *
     * @return the item size txt
     */
    public String getItemSizeText() {
        return this.itemSizeText;
    }

    /**
     * Sets the item size txt.
     *
     * @param itemSizeText the new item size txt
     */
    public void setItemSizeText(String itemSizeText) {
        this.itemSizeText = itemSizeText;
    }

    /**
     * Gets the itm sz qty.
     *
     * @return the itm sz qty
     */
    public Double getItemSizeQuantity() {
        return this.itemSizeQuantity;
    }

    /**
     * Sets the itm sz qty.
     *
     * @param itemSizeQuantity the new itm sz qty
     */
    public void setItemSizeQuantity(Double itemSizeQuantity) {
        this.itemSizeQuantity = itemSizeQuantity;
    }

    /**
     * Gets the added dt.
     *
     * @return the added dt
     */
    public LocalDate getAddedDate() {
        return this.addedDate;
    }

    /**
     * Sets the added dt.
     *
     * @param addedDate the new added dt
     */
    public void setAddedDate(LocalDate addedDate) {
        this.addedDate = addedDate;
    }

    /**
     * Gets the added usr id.
     *
     * @return the added usr id
     */
    public String getAddedUserId() {
        return this.addedUserId;
    }

    /**
     * Sets the added usr id.
     *
     * @param addedUserId the new added usr id
     */
    public void setAddedUserId(String addedUserId) {
        this.addedUserId = addedUserId;
    }

    /**
     * Gets the dscon trx sw.
     *
     * @return the dscon trx sw
     */
    public boolean isDiscontinueTracking() {
        return this.discontinueTracking;
    }

    /**
     * Sets the dscon trx sw.
     *
     * @param discontinueTracking the new dscon trx sw
     */
    public void setDiscontinueTracking(boolean discontinueTracking) {
        this.discontinueTracking = discontinueTracking;
    }

    /**
     * Gets the dscon dt.
     *
     * @return the dscon dt
     */
    public LocalDate getDiscontinueDate() {
        return this.discontinueDate;
    }

    /**
     * Sets the dscon dt.
     *
     * @param discontinueDate the new dscon dt
     */
    public void setDiscontinueDate(LocalDate discontinueDate) {
        this.discontinueDate = discontinueDate;
    }

    /**
     * Gets the dscon usr id.
     *
     * @return the dscon usr id
     */
    public String getDiscontinueUserId() {
        return this.discontinueUserId;
    }

    /**
     * Sets the dscon usr id.
     *
     * @param discontinueUserId the new dscon usr id
     */
    public void setDiscontinueUserId(String discontinueUserId) {
        this.discontinueUserId = discontinueUserId;
    }

    /**
     * Gets the delete dt.
     *
     * @return the delete dt
     */
    public LocalDate getDeleteDate() {
        return this.deleteDate;
    }

    /**
     * Sets the delete dt.
     *
     * @param deleteDate the new delete dt
     */
    public void setDeleteDate(LocalDate deleteDate) {
        this.deleteDate = deleteDate;
    }

    /**
     * Gets the splr cs lif dd.
     *
     * @return the splr cs lif dd
     */
    public Integer getSupplierCaseLifeDays() {
        return this.supplierCaseLifeDays;
    }

    /**
     * Sets the splr cs lif dd.
     *
     * @param supplierCaseLifeDays the new splr cs lif dd
     */
    public void setSupplierCaseLifeDays(Integer supplierCaseLifeDays) {
        this.supplierCaseLifeDays = supplierCaseLifeDays;
    }

    /**
     * Gets the on rcpt lif dd.
     *
     * @return the on rcpt lif dd
     */
    public Integer getOnReceiptLifeDays() {
        return this.onReceiptLifeDays;
    }

    /**
     * Sets the on rcpt lif dd.
     *
     * @param onReceiptLifeDays the new on rcpt lif dd
     */
    public void setOnReceiptLifeDays(Integer onReceiptLifeDays) {
        this.onReceiptLifeDays = onReceiptLifeDays;
    }

    /**
     * Gets the whse reaction dd.
     *
     * @return the whse reaction dd
     */
    public Integer getWarehouseReactionDays() {
        return this.warehouseReactionDays;
    }

    /**
     * Sets the whse reaction dd.
     *
     * @param warehouseReactionDays the new whse reaction dd
     */
    public void setWarehouseReactionDays(Integer warehouseReactionDays) {
        this.warehouseReactionDays = warehouseReactionDays;
    }

    /**
     * Gets the guarn to str dd.
     *
     * @return the guarn to str dd
     */
    public Integer getGuaranteeToStoreDays() {
        return this.guaranteeToStoreDays;
    }

    /**
     * Sets the guarn to str dd.
     *
     * @param guaranteeToStoreDays the new guarn to str dd
     */
    public void setGuaranteeToStoreDays(Integer guaranteeToStoreDays) {
        this.guaranteeToStoreDays = guaranteeToStoreDays;
    }

    /**
     * Gets the dt cntrld itm sw.
     *
     * @return the dt cntrld itm sw
     */
    public boolean isDateControlledItem() {
        return this.dateControlledItem;
    }

    /**
     * Sets the dt cntrld itm sw.
     *
     * @param dateControlledItem the new dt cntrld itm sw
     */
    public void setDateControlledItem(boolean dateControlledItem) {
        this.dateControlledItem = dateControlledItem;
    }

    /**
     * Gets the pd omi com cd.
     *
     * @return the pd omi com cd
     */
    public Integer getCommodityCode() {
        return this.commodityCode;
    }

    /**
     * Sets the pd omi com cd.
     *
     * @param commodityCode the new pd omi com cd
     */
    public void setCommodityCode(Integer commodityCode) {
        this.commodityCode = commodityCode;
    }

    /**
     * Gets the pd omi sub com cd.
     *
     * @return the pd omi sub com cd
     */
    public Integer getSubCommodityCode() {
        return this.subCommodityCode;
    }

    /**
     * Sets the pd omi sub com cd.
     *
     * @param subCommodityCode the new pd omi sub com cd
     */
    public void setSubCommodityCode(Integer subCommodityCode) {
        this.subCommodityCode = subCommodityCode;
    }

    /**
     * Gets the pd omi com cls cd.
     *
     * @return the pd omi com cls cd
     */
    public Short getClassCode() {
        return this.classCode;
    }

    /**
     * Sets the pd omi com cls cd.
     *
     * @param classCode the new pd omi com cls cd
     */
    public void setClassCode(Short classCode) {
        this.classCode = classCode;
    }

    /**
     * Gets the ims com cd.
     *
     * @return the ims com cd
     */
    public Long getImsCommodityCode() {
        return this.imsCommodityCode;
    }

    /**
     * Sets the ims com cd.
     *
     * @param imsCommodityCode the new ims com cd
     */
    public void setImsCommodityCode(Long imsCommodityCode) {
        this.imsCommodityCode = imsCommodityCode;
    }

    /**
     * Gets the ims sub com cd.
     *
     * @return the ims sub com cd
     */
    public String getImsSubCommodityCode() {
        return this.imsSubCommodityCode;
    }

    /**
     * Sets the ims sub com cd.
     *
     * @param imsSubCommodityCode the new ims sub com cd
     */
    public void setImsSubCommodityCode(String imsSubCommodityCode) {
        this.imsSubCommodityCode = imsSubCommodityCode;
    }

    /**
     * Gets the case upc.
     *
     * @return the case upc
     */
    public Long getCaseUpc() {
        return this.caseUpc;
    }

    /**
     * Sets the case upc.
     *
     * @param caseUpc the new case upc
     */
    public void setCaseUpc(Long caseUpc) {
        this.caseUpc = caseUpc;
    }

    /**
     * Gets the ordering upc.
     *
     * @return the ordering upc
     */
    public Long getOrderingUpc() {
        return this.orderingUpc;
    }

    /**
     * Sets the ordering upc.
     *
     * @param orderingUpc the new ordering upc
     */
    public void setOrderingUpc(Long orderingUpc) {
        this.orderingUpc = orderingUpc;
    }

    /**
     * Gets the usda nbr.
     *
     * @return the usda nbr
     */
    public Integer getUsdaNumber() {
        return this.usdaNumber;
    }

    /**
     * Sets the usda nbr.
     *
     * @param usdaNumber the new usda nbr
     */
    public void setUsdaNumber(Integer usdaNumber) {
        this.usdaNumber = usdaNumber;
    }

    /**
     * Gets the mex auth cd.
     *
     * @return the mex auth cd
     */
    public String getMexicoAuthorizationCode() {
        return this.mexicoAuthorizationCode;
    }

    /**
     * Sets the mex auth cd.
     *
     * @param mexicoAuthorizationCode the new mex auth cd
     */
    public void setMexicoAuthorizationCode(String mexicoAuthorizationCode) {
        this.mexicoAuthorizationCode = mexicoAuthorizationCode;
    }

    /**
     * Gets the mex brdr auth cd.
     *
     * @return the mex brdr auth cd
     */
    public String getMexicanBorderAuthorizationCode() {
        return this.mexicanBorderAuthorizationCode;
    }

    /**
     * Sets the mex brdr auth cd.
     *
     * @param mexicanBorderAuthorizationCode the new mex brdr auth cd
     */
    public void setMexicanBorderAuthorizationCode(String mexicanBorderAuthorizationCode) {
        this.mexicanBorderAuthorizationCode = mexicanBorderAuthorizationCode;
    }

    /**
     * Gets the max ship qty.
     *
     * @return the max ship qty
     */
    public Integer getMaxShipQuantity() {
        return this.maxShipQuantity;
    }

    /**
     * Sets the max ship qty.
     *
     * @param maxShipQuantity the new max ship qty
     */
    public void setMaxShipQuantity(Integer maxShipQuantity) {
        this.maxShipQuantity = maxShipQuantity;
    }

    /**
     * Gets the abc auth cd.
     *
     * @return the abc auth cd
     */
    public String getAbcAuthorizationCode() {
        return this.abcAuthorizationCode;
    }

    /**
     * Sets the abc auth cd.
     *
     * @param abcAuthorizationCode the new abc auth cd
     */
    public void setAbcAuthorizationCode(String abcAuthorizationCode) {
        this.abcAuthorizationCode = abcAuthorizationCode;
    }

    /**
     * Gets the new itm sw.
     *
     * @return the new itm sw
     */
    public boolean isNewItem() {
        return this.newItem;
    }

    /**
     * Sets the new itm sw.
     *
     * @param newItem the new new itm sw
     */
    public void setNewItem(boolean newItem) {
        this.newItem = newItem;
    }

    /**
     * Gets the repack sw.
     *
     * @return the repack sw
     */
    public boolean isRepack() {
        return this.repack;
    }

    /**
     * Sets the repack sw.
     *
     * @param repack the new repack sw
     */
    public void setRepack(boolean repack) {
        this.repack = repack;
    }

    /**
     * Gets the crit itm ind.
     *
     * @return the crit itm ind
     */
    public String getCriticalItemIndicator() {
        return this.criticalItemIndicator;
    }

    /**
     * Sets the crit itm ind.
     *
     * @param criticalItemIndicator the new crit itm ind
     */
    public void setCriticalItemIndicator(String criticalItemIndicator) {
        this.criticalItemIndicator = criticalItemIndicator;
    }

    /**
     * Gets the nev out sw.
     *
     * @return the nev out sw
     */
    public boolean isNevOut() {
        return this.nevOut;
    }

    /**
     * Sets the nev out sw.
     *
     * @param nevOut the new nev out sw
     */
    public void setNevOut(boolean nevOut) {
        this.nevOut = nevOut;
    }

    /**
     * Gets the ctch wt sw.
     *
     * @return the ctch wt sw
     */
    public boolean isCatchWeight() {
        return this.catchWeight;
    }

    /**
     * Sets the ctch wt sw.
     *
     * @param catchWeight the new ctch wt sw
     */
    public void setCatchWeight(boolean catchWeight) {
        this.catchWeight = catchWeight;
    }

    /**
     * Gets the low vel sw.
     *
     * @return the low vel sw
     */
    public boolean isLowVel() {
        return this.lowVel;
    }

    /**
     * Sets the low vel sw.
     *
     * @param lowVel the new low vel sw
     */
    public void setLowVel(boolean lowVel) {
        this.lowVel = lowVel;
    }

    /**
     * Gets the shpr itm sw.
     *
     * @return the shpr itm sw
     */
    public boolean isSupplierItem() {
        return this.supplierItem;
    }

    /**
     * Sets the shpr itm sw.
     *
     * @param supplierItem the new shpr itm sw
     */
    public void setSupplierItem(boolean supplierItem) {
        this.supplierItem = supplierItem;
    }

    /**
     * Gets the srs hndlg cd.
     *
     * @return the srs hndlg cd
     */
    public String getSrsHandlingCode() {
        return this.srsHandlingCode;
    }

    /**
     * Sets the srs hndlg cd.
     *
     * @param srsHandlingCode the new srs hndlg cd
     */
    public void setSrsHandlingCode(String srsHandlingCode) {
        this.srsHandlingCode = srsHandlingCode;
    }

    /**
     * Gets the cross dock itm sw.
     *
     * @return the cross dock itm sw
     */
    public boolean isCrossDockItem() {
        return this.crossDockItem;
    }

    /**
     * Sets the cross dock itm sw.
     *
     * @param crossDockItem the new cross dock itm sw
     */
    public void setCrossDockItem(boolean crossDockItem) {
        this.crossDockItem = crossDockItem;
    }

    /**
     * Gets the rplac ord qty sw.
     *
     * @return the rplac ord qty sw
     */
    public boolean isReplaceOrderQuantity() {
        return this.replaceOrderQuantity;
    }

    /**
     * Sets the rplac ord qty sw.
     *
     * @param replaceOrderQuantity the new rplac ord qty sw
     */
    public void setReplaceOrderQuantity(boolean replaceOrderQuantity) {
        this.replaceOrderQuantity = replaceOrderQuantity;
    }

    /**
     * Gets the ltr of cr sw.
     *
     * @return the ltr of cr sw
     */
    public boolean isLetterOfCredit() {
        return this.letterOfCredit;
    }

    /**
     * Sets the ltr of cr sw.
     *
     * @param letterOfCredit the new ltr of cr sw
     */
    public void setLetterOfCredit(boolean letterOfCredit) {
        this.letterOfCredit = letterOfCredit;
    }

    /**
     * Gets the var wt sw.
     *
     * @return the var wt sw
     */
    public boolean isVariableWeight() {
        return this.variableWeight;
    }

    /**
     * Sets the var wt sw.
     *
     * @param variableWeight the new var wt sw
     */
    public void setVariableWeight(boolean variableWeight) {
        this.variableWeight = variableWeight;
    }

    /**
     * Gets the fwdby appr sw.
     *
     * @return the fwdby appr sw
     */
    public boolean isForwardBuyApproved() {
        return this.forwardBuyApproved;
    }

    /**
     * Sets the fwdby appr sw.
     *
     * @param forwardBuyApproved the new fwdby appr sw
     */
    public void setForwardBuyApproved(boolean forwardBuyApproved) {
        this.forwardBuyApproved = forwardBuyApproved;
    }

    /**
     * Gets the cattle sw.
     *
     * @return the cattle sw
     */
    public boolean isCattle() {
        return this.cattle;
    }

    /**
     * Sets the cattle sw.
     *
     * @param cattle the new cattle sw
     */
    public void setCattle(boolean cattle) {
        this.cattle = cattle;
    }

    /**
     * Gets the heb itm pk.
     *
     * @return the heb itm pk
     */
    public Long getPack() {
        return this.pack;
    }

    /**
     * Sets the heb itm pk.
     *
     * @param pack the new heb itm pk
     */
    public void setPack(Long pack) {
        this.pack = pack;
    }

    /**
     * Gets the retl sals pk.
     *
     * @return the retl sals pk
     */
    public Integer getRetailSalesPack() {
        return this.retailSalesPack;
    }

    /**
     * Sets the retl sals pk.
     *
     * @param retailSalesPack the new retl sals pk
     */
    public void setRetailSalesPack(Integer retailSalesPack) {
        this.retailSalesPack = retailSalesPack;
    }

    /**
     * Gets the dim ck ind.
     *
     * @return the dim ck ind
     */
    public String getDimCkInd() {
        return this.dimCkInd;
    }

    /**
     * Sets the dim ck ind.
     *
     * @param dimCkInd the new dim ck ind
     */
    public void setDimCkInd(String dimCkInd) {
        this.dimCkInd = dimCkInd;
    }

    /**
     * Gets the avg whlsl cst.
     *
     * @return the avg whlsl cst
     */
    public Double getAverageWholesaleCost() {
        return this.averageWholesaleCost;
    }

    /**
     * Sets the avg whlsl cst.
     *
     * @param averageWholesaleCost the new avg whlsl cst
     */
    public void setAverageWholesaleCost(Double averageWholesaleCost) {
        this.averageWholesaleCost = averageWholesaleCost;
    }

    /**
     * Gets the dsd itm sw.
     *
     * @return the dsd itm sw
     */
    public boolean isDsdItem() {
        return this.dsdItem;
    }

    /**
     * Sets the dsd itm sw.
     *
     * @param dsdItem the new dsd itm sw
     */
    public void setDsdItem(boolean dsdItem) {
        this.dsdItem = dsdItem;
    }

    /**
     * Gets the upc map sw.
     *
     * @return the upc map sw
     */
    public boolean isUpcMap() {
        return this.upcMap;
    }

    /**
     * Sets the upc map sw.
     *
     * @param upcMap the new upc map sw
     */
    public void setUpcMap(boolean upcMap) {
        this.upcMap = upcMap;
    }

    /**
     * Gets the deposit sw.
     *
     * @return the deposit sw
     */
    public boolean isDeposit() {
        return this.deposit;
    }

    /**
     * Sets the deposit sw.
     *
     * @param deposit the new deposit sw
     */
    public void setDeposit(boolean deposit) {
        this.deposit = deposit;
    }

    /**
     * Gets the lst updt ts.
     *
     * @return the lst updt ts
     */
    public LocalDateTime getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    /**
     * Sets the lst updt ts.
     *
     * @param lastUpdateDate the new lst updt ts
     */
    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * Gets the lst updt usr id.
     *
     * @return the lst updt usr id
     */
    public String getLastUpdateUserId() {
        return this.lastUpdateUserId;
    }

    /**
     * Sets the lst updt usr id.
     *
     * @param lastUpdateUserId the new lst updt usr id
     */
    public void setLastUpdateUserId(String lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }

    /**
     * Gets the dept id1.
     *
     * @return the dept id1
     */
    public Integer getDepartmentIdOne() {
        return this.departmentIdOne;
    }

    /**
     * Sets the dept id1.
     *
     * @param departmentIdOne the new dept id1
     */
    public void setDepartmentIdOne(Integer departmentIdOne) {
        this.departmentIdOne = departmentIdOne;
    }

    /**
     * Gets the sub dept id1.
     *
     * @return the sub dept id1
     */
    public String getSubDepartmentIdOne() {
        return this.subDepartmentIdOne;
    }

    /**
     * Sets the sub dept id1.
     *
     * @param subDepartmentIdOne the new sub dept id1
     */
    public void setSubDepartmentIdOne(String subDepartmentIdOne) {
        this.subDepartmentIdOne = subDepartmentIdOne;
    }

    /**
     * Gets the dept mdse typ1.
     *
     * @return the dept mdse typ1
     */
    public String getMerchandiseTypeCodeOne() {
        return this.merchandiseTypeCodeOne;
    }

    /**
     * Sets the dept mdse typ1.
     *
     * @param merchandiseTypeCodeOne the new dept mdse typ1
     */
    public void setMerchandiseTypeCodeOne(String merchandiseTypeCodeOne) {
        this.merchandiseTypeCodeOne = merchandiseTypeCodeOne;
    }

    /**
     * Gets the pss dept1.
     *
     * @return the pss dept1
     */
    public Integer getPssDepartmentCodeOne() {
        return this.pssDepartmentCodeOne;
    }

    /**
     * Sets the pss dept1.
     *
     * @param pssDepartmentCodeOne the new pss dept1
     */
    public void setPssDepartmentCodeOne(Integer pssDepartmentCodeOne) {
        this.pssDepartmentCodeOne = pssDepartmentCodeOne;
    }

    /**
     * Gets the dept id2.
     *
     * @return the dept id2
     */
    public Integer getDepartmentIdTwo() {
        return this.departmentIdTwo;
    }

    /**
     * Sets the dept id2.
     *
     * @param departmentIdTwo the new dept id2
     */
    public void setDepartmentIdTwo(Integer departmentIdTwo) {
        this.departmentIdTwo = departmentIdTwo;
    }

    /**
     * Gets the sub dept id2.
     *
     * @return the sub dept id2
     */
    public String getSubDepartmentIdTwo() {
        return this.subDepartmentIdTwo;
    }

    /**
     * Sets the sub dept id2.
     *
     * @param subDepartmentIdTwo the new sub dept id2
     */
    public void setSubDepartmentIdTwo(String subDepartmentIdTwo) {
        this.subDepartmentIdTwo = subDepartmentIdTwo;
    }

    /**
     * Gets the dept mdse typ2.
     *
     * @return the dept mdse typ2
     */
    public String getMerchandiseTypeCodeTwo() {
        return this.merchandiseTypeCodeTwo;
    }

    /**
     * Sets the dept mdse typ2.
     *
     * @param merchandiseTypeCodeTwo the new dept mdse typ2
     */
    public void setMerchandiseTypeCodeTwo(String merchandiseTypeCodeTwo) {
        this.merchandiseTypeCodeTwo = merchandiseTypeCodeTwo;
    }

    /**
     * Gets the pss dept2.
     *
     * @return the pss dept2
     */
    public Integer getPssDepartmentCodeTwo() {
        return this.pssDepartmentCodeTwo;
    }

    /**
     * Sets the pss dept2.
     *
     * @param pssDepartmentCodeTwo the new pss dept2
     */
    public void setPssDepartmentCodeTwo(Integer pssDepartmentCodeTwo) {
        this.pssDepartmentCodeTwo = pssDepartmentCodeTwo;
    }

    /**
     * Gets the dept id3.
     *
     * @return the dept id3
     */
    public Integer getDepartmentIdThree() {
        return this.departmentIdThree;
    }

    /**
     * Sets the dept id3.
     *
     * @param departmentIdThree the new dept id3
     */
    public void setDepartmentIdThree(Integer departmentIdThree) {
        this.departmentIdThree = departmentIdThree;
    }

    /**
     * Gets the sub dept id3.
     *
     * @return the sub dept id3
     */
    public String getSubDepartmentIdThree() {
        return this.subDepartmentIdThree;
    }

    /**
     * Sets the sub dept id3.
     *
     * @param subDepartmentIdThree the new sub dept id3
     */
    public void setSubDepartmentIdThree(String subDepartmentIdThree) {
        this.subDepartmentIdThree = subDepartmentIdThree;
    }

    /**
     * Gets the dept mdse typ3.
     *
     * @return the dept mdse typ3
     */
    public String getMerchandiseTypeCodeThree() {
        return this.merchandiseTypeCodeThree;
    }

    /**
     * Sets the dept mdse typ3.
     *
     * @param merchandiseTypeCodeThree the new dept mdse typ3
     */
    public void setMerchandiseTypeCodeThree(String merchandiseTypeCodeThree) {
        this.merchandiseTypeCodeThree = merchandiseTypeCodeThree;
    }

    /**
     * Gets the pss dept3.
     *
     * @return the pss dept3
     */
    public Integer getPssDepartmentCodeThree() {
        return this.pssDepartmentCodeThree;
    }

    /**
     * Sets the pss dept3.
     *
     * @param pssDepartmentCodeThree the new pss dept3
     */
    public void setPssDepartmentCodeThree(Integer pssDepartmentCodeThree) {
        this.pssDepartmentCodeThree = pssDepartmentCodeThree;
    }

    /**
     * Gets the dept id4.
     *
     * @return the dept id4
     */
    public Integer getDepartmentIdFour() {
        return this.departmentIdFour;
    }

    /**
     * Sets the dept id4.
     *
     * @param departmentIdFour the new dept id4
     */
    public void setDepartmentIdFour(Integer departmentIdFour) {
        this.departmentIdFour = departmentIdFour;
    }

    /**
     * Gets the sub dept id4.
     *
     * @return the sub dept id4
     */
    public String getSubDepartmentIdFour() {
        return this.subDepartmentIdFour;
    }

    /**
     * Sets the sub dept id4.
     *
     * @param subDepartmentIdFour the new sub dept id4
     */
    public void setSubDepartmentIdFour(String subDepartmentIdFour) {
        this.subDepartmentIdFour = subDepartmentIdFour;
    }

    /**
     * Gets the dept mdse typ4.
     *
     * @return the dept mdse typ4
     */
    public String getMerchandiseTypeCodeFour() {
        return this.merchandiseTypeCodeFour;
    }

    /**
     * Sets the dept mdse typ4.
     *
     * @param merchandiseTypeCodeFour the new dept mdse typ4
     */
    public void setMerchandiseTypeCodeFour(String merchandiseTypeCodeFour) {
        this.merchandiseTypeCodeFour = merchandiseTypeCodeFour;
    }

    /**
     * Gets the pss dept4.
     *
     * @return the pss dept4
     */
    public Integer getPssDepartmentCodeFour() {
        return this.pssDepartmentCodeFour;
    }

    /**
     * Sets the pss dept4.
     *
     * @param pssDepartmentCodeFour the new pss dept4
     */
    public void setPssDepartmentCodeFour(Integer pssDepartmentCodeFour) {
        this.pssDepartmentCodeFour = pssDepartmentCodeFour;
    }

    /**
     * Gets the gross wt.
     *
     * @return the gross wt
     */
    public Double getGrossWeight() {
        return this.grossWeight;
    }

    /**
     * Sets the gross wt.
     *
     * @param grossWeight the new gross wt
     */
    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    /**
     * Gets the unstamped tbco sw.
     *
     * @return the unstamped tbco sw
     */
    public boolean isUnstampedTobacco() {
        return this.unstampedTobacco;
    }

    /**
     * Sets the unstamped tbco sw.
     *
     * @param unstampedTobacco the new unstamped tbco sw
     */
    public void setUnstampedTobacco(boolean unstampedTobacco) {
        this.unstampedTobacco = unstampedTobacco;
    }

    /**
     * Gets the itm id.
     *
     * @return the itm id
     */
    public Long getItemCode() {
        return this.itemCode;
    }

    /**
     * Sets the itm id.
     *
     * @param itemCode the new itm id
     */
    public void setItemCode(Long itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * Gets the inbnd spec dd.
     *
     * @return the inbnd spec dd
     */
    public Integer getInboundSpecDays() {
        return this.inboundSpecDays;
    }

    /**
     * Sets the inbnd spec dd.
     *
     * @param inboundSpecDays the new inbnd spec dd
     */
    public void setInboundSpecDays(Integer inboundSpecDays) {
        this.inboundSpecDays = inboundSpecDays;
    }

    /**
     * Gets the pror dt.
     *
     * @return the pror dt
     */
    public LocalDate getPriorDate() {
        return this.priorDate;
    }

    /**
     * Sets the pror dt.
     *
     * @param priorDate the new pror dt
     */
    public void setPriorDate(LocalDate priorDate) {
        this.priorDate = priorDate;
    }

    /**
     * Gets the in str dt.
     *
     * @return the in str dt
     */
    public LocalDate getInStoreDate() {
        return this.inStoreDate;
    }

    /**
     * Sets the in str dt.
     *
     * @param inStoreDate the new in str dt
     */
    public void setInStoreDate(LocalDate inStoreDate) {
        this.inStoreDate = inStoreDate;
    }

    /**
     * Gets the whse flsh dt.
     *
     * @return the whse flsh dt
     */
    public LocalDate getWarehouseFlushDate() {
        return this.warehouseFlushDate;
    }

    /**
     * Sets the whse flsh dt.
     *
     * @param warehouseFlushDate the new whse flsh dt
     */
    public void setWarehouseFlushDate(LocalDate warehouseFlushDate) {
        this.warehouseFlushDate = warehouseFlushDate;
    }

    /**
     * Gets the sell yy.
     *
     * @return the sell yy
     */
    public Integer getSellByYear() {
        return this.sellByYear;
    }

    /**
     * Sets the sell yy.
     *
     * @param sellByYear the new sell yy
     */
    public void setSellByYear(Integer sellByYear) {
        this.sellByYear = sellByYear;
    }

    /**
     * Gets the crtn mrkng txt.
     *
     * @return the crtn mrkng txt
     */
    public String getCartonMarking() {
        return this.cartonMarking;
    }

    /**
     * Sets the crtn mrkng txt.
     *
     * @param cartonMarking the new crtn mrkng txt
     */
    public void setCartonMarking(String cartonMarking) {
        this.cartonMarking = cartonMarking;
    }

    /**
     * Gets the prodn min ord qty.
     *
     * @return the prodn min ord qty
     */
    public Integer getMinOrderQuantity() {
        return this.minOrderQuantity;
    }

    /**
     * Sets the prodn min ord qty.
     *
     * @param minOrderQuantity the new prodn min ord qty
     */
    public void setMinOrderQuantity(Integer minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }

    /**
     * Gets the prodn min ord des.
     *
     * @return the prodn min ord des
     */
    public String getMinimumOrderDescription() {
        return this.minimumOrderDescription;
    }

    /**
     * Sets the prodn min ord des.
     *
     * @param minimumOrderDescription the new prodn min ord des
     */
    public void setMinimumOrderDescription(String minimumOrderDescription) {
        this.minimumOrderDescription = minimumOrderDescription;
    }

    /**
     * Gets the seasn txt.
     *
     * @return the seasn txt
     */
    public String getSeason() {
        return this.season;
    }

    /**
     * Sets the seasn txt.
     *
     * @param season the new seasn txt
     */
    public void setSeason(String season) {
        this.season = season;
    }

    /**
     * Gets the cntan sz cd.
     *
     * @return the cntan sz cd
     */
    public String getContainerSizeCode() {
        return this.containerSizeCode;
    }

    /**
     * Sets the cntan sz cd.
     *
     * @param containerSizeCode the new cntan sz cd
     */
    public void setContainerSizeCode(String containerSizeCode) {
        this.containerSizeCode = containerSizeCode;
    }

    /**
     * Gets the inco trm cd.
     *
     * @return the inco trm cd
     */
    public String getIncomeTermsCode() {
        return this.incomeTermsCode;
    }

    /**
     * Sets the inco trm cd.
     *
     * @param incomeTermsCode the new inco trm cd
     */
    public void setIncomeTermsCode(String incomeTermsCode) {
        this.incomeTermsCode = incomeTermsCode;
    }

    /**
     * Gets the pckup pnt txt.
     *
     * @return the pckup pnt txt
     */
    public String getPickupPoint() {
        return this.pickupPoint;
    }

    /**
     * Sets the pckup pnt txt.
     *
     * @param pickupPoint the new pckup pnt txt
     */
    public void setPickupPoint(String pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    /**
     * Gets the hts nbr.
     *
     * @return the hts nbr
     */
    public Long getHtsNumber() {
        return this.htsNumber;
    }

    /**
     * Sets the hts nbr.
     *
     * @param htsNumber the new hts nbr
     */
    public void setHtsNumber(Long htsNumber) {
        this.htsNumber = htsNumber;
    }

    /**
     * Gets the cntry of orig nm.
     *
     * @return the cntry of orig nm
     */
    public String getCountryOfOrigin() {
        return this.countryOfOrigin;
    }

    /**
     * Sets the cntry of orig nm.
     *
     * @param countryOfOrigin the new cntry of orig nm
     */
    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    /**
     * Gets the duty rt pct.
     *
     * @return the duty rt pct
     */
    public Double getDutyPercent() {
        return this.dutyPercent;
    }

    /**
     * Sets the duty rt pct.
     *
     * @param dutyPercent the new duty rt pct
     */
    public void setDutyPercent(Double dutyPercent) {
        this.dutyPercent = dutyPercent;
    }

    /**
     * Gets the duty cnfrm txt.
     *
     * @return the duty cnfrm txt
     */
    public String getDutyConfirmation() {
        return this.dutyConfirmation;
    }

    /**
     * Sets the duty cnfrm txt.
     *
     * @param dutyConfirmation the new duty cnfrm txt
     */
    public void setDutyConfirmation(String dutyConfirmation) {
        this.dutyConfirmation = dutyConfirmation;
    }

    /**
     * Gets the frt cnfrm txt.
     *
     * @return the frt cnfrm txt
     */
    public String getFreightConfirmation() {
        return this.freightConfirmation;
    }

    /**
     * Sets the frt cnfrm txt.
     *
     * @param freightConfirmation the new frt cnfrm txt
     */
    public void setFreightConfirmation(String freightConfirmation) {
        this.freightConfirmation = freightConfirmation;
    }

    /**
     * Gets the agent comsn pct.
     *
     * @return the agent comsn pct
     */
    public Double getAgentCommissionPercent() {
        return this.agentCommissionPercent;
    }

    /**
     * Sets the agent comsn pct.
     *
     * @param agentCommissionPercent the new agent comsn pct
     */
    public void setAgentCommissionPercent(Double agentCommissionPercent) {
        this.agentCommissionPercent = agentCommissionPercent;
    }

    /**
     * Gets the color des.
     *
     * @return the color des
     */
    public String getColorDescription() {
        return this.colorDescription;
    }

    /**
     * Sets the color des.
     *
     * @param colorDescription the new color des
     */
    public void setColorDescription(String colorDescription) {
        this.colorDescription = colorDescription;
    }

    /**
     * Gets the mstr pk qty.
     *
     * @return the mstr pk qty
     */
    public Integer getMasterPackQuantity() {
        return this.masterPackQuantity;
    }

    /**
     * Sets the mstr pk qty.
     *
     * @param masterPackQuantity the new mstr pk qty
     */
    public void setMasterPackQuantity(Integer masterPackQuantity) {
        this.masterPackQuantity = masterPackQuantity;
    }

    /**
     * Gets the max shlf life dd.
     *
     * @return the max shlf life dd
     */
    public Integer getMaxShelfLifeDays() {
        return this.maxShelfLifeDays;
    }

    /**
     * Sets the max shlf life dd.
     *
     * @param maxShelfLifeDays the new max shlf life dd
     */
    public void setMaxShelfLifeDays(Integer maxShelfLifeDays) {
        this.maxShelfLifeDays = maxShelfLifeDays;
    }

    /**
     * Gets the mrt sw.
     *
     * @return the mrt sw
     */
    public boolean isMrt() {
        return this.mrt;
    }

    /**
     * Sets the mrt sw.
     *
     * @param mrt the new mrt sw
     */
    public void setMrt(boolean mrt) {
        this.mrt = mrt;
    }

    /**
     * Gets the new data sw.
     *
     * @return the new data sw
     */
    public boolean isNewData() {
        return this.newData;
    }

    /**
     * Sets the new data sw.
     *
     * @param newData the new new data sw
     */
    public void setNewData(boolean newData) {
        this.newData = newData;
    }

    /**
     * Gets the shp cs ln.
     *
     * @return the shp cs ln
     */
    public Double getShipLength() {
        return this.shipLength;
    }

    /**
     * Sets the shp cs ln.
     *
     * @param shipLength the new shp cs ln
     */
    public void setShipLength(Double shipLength) {
        this.shipLength = shipLength;
    }

    /**
     * Gets the shp cs wd.
     *
     * @return the shp cs wd
     */
    public Double getShipWidth() {
        return this.shipWidth;
    }

    /**
     * Sets the shp cs wd.
     *
     * @param shipWidth the new shp cs wd
     */
    public void setShipWidth(Double shipWidth) {
        this.shipWidth = shipWidth;
    }

    /**
     * Gets the shp cs ht.
     *
     * @return the shp cs ht
     */
    public Double getShipHeight() {
        return this.shipHeight;
    }

    /**
     * Sets the shp cs ht.
     *
     * @param shipHeight the new shp cs ht
     */
    public void setShipHeight(Double shipHeight) {
        this.shipHeight = shipHeight;
    }

    /**
     * Gets the shp cu.
     *
     * @return the shp cu
     */
    public Double getShipCube() {
        return this.shipCube;
    }

    /**
     * Sets the shp cu.
     *
     * @param shipCube the new shp cu
     */
    public void setShipCube(Double shipCube) {
        this.shipCube = shipCube;
    }

    /**
     * Gets the shp nest cu.
     *
     * @return the shp nest cu
     */
    public Double getShipNestCube() {
        return this.shipNestCube;
    }

    /**
     * Sets the shp nest cu.
     *
     * @param shipNestCube the new shp nest cu
     */
    public void setShipNestCube(Double shipNestCube) {
        this.shipNestCube = shipNestCube;
    }

    /**
     * Gets the shp nest max qty.
     *
     * @return the shp nest max qty
     */
    public Integer getShipNestMaxQuantity() {
        return this.shipNestMaxQuantity;
    }

    /**
     * Sets the shp nest max qty.
     *
     * @param shipNestMaxQuantity the new shp nest max qty
     */
    public void setShipNestMaxQuantity(Integer shipNestMaxQuantity) {
        this.shipNestMaxQuantity = shipNestMaxQuantity;
    }

    /**
     * Gets the shp wt.
     *
     * @return the shp wt
     */
    public Double getShipWeight() {
        return this.shipWeight;
    }

    /**
     * Sets the shp wt.
     *
     * @param shipWeight the new shp wt
     */
    public void setShipWeight(Double shipWeight) {
        this.shipWeight = shipWeight;
    }

    /**
     * Gets the shp inr pk.
     *
     * @return the shp inr pk
     */
    public Long getShipInrPack() {
        return this.shipInrPack;
    }

    /**
     * Sets the shp inr pk.
     *
     * @param shipInrPack the new shp inr pk
     */
    public void setShipInrPack(Long shipInrPack) {
        this.shipInrPack = shipInrPack;
    }

    /**
     * Gets the vend ln.
     *
     * @return the vend ln
     */
    public Double getLength() {
        return this.length;
    }

    /**
     * Sets the vend ln.
     *
     * @param length the new vend ln
     */
    public void setLength(Double length) {
        this.length = length;
    }

    /**
     * Gets the vend wd.
     *
     * @return the vend wd
     */
    public Double getWidth() {
        return this.width;
    }

    /**
     * Sets the vend wd.
     *
     * @param width the new vend wd
     */
    public void setWidth(Double width) {
        this.width = width;
    }

    /**
     * Gets the vend ht.
     *
     * @return the vend ht
     */
    public Double getHeight() {
        return this.height;
    }

    /**
     * Sets the vend ht.
     *
     * @param height the new vend ht
     */
    public void setHeight(Double height) {
        this.height = height;
    }

    /**
     * Gets the vend wt.
     *
     * @return the vend wt
     */
    public Double getWeight() {
        return this.weight;
    }

    /**
     * Sets the vend wt.
     *
     * @param weight the new vend wt
     */
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    /**
     * Gets the vend cu.
     *
     * @return the vend cu
     */
    public Double getCube() {
        return this.cube;
    }

    /**
     * Sets the vend cu.
     *
     * @param cube the new vend cu
     */
    public void setCube(Double cube) {
        this.cube = cube;
    }

    /**
     * Gets the vend nest cu.
     *
     * @return the vend nest cu
     */
    public Double getNestCube() {
        return this.nestCube;
    }

    /**
     * Sets the vend nest cu.
     *
     * @param nestCube the new vend nest cu
     */
    public void setNestCube(Double nestCube) {
        this.nestCube = nestCube;
    }

    /**
     * Gets the vend nest max.
     *
     * @return the vend nest max
     */
    public Integer getNestMax() {
        return this.nestMax;
    }

    /**
     * Sets the vend nest max.
     *
     * @param nestMax the new vend nest max
     */
    public void setNestMax(Integer nestMax) {
        this.nestMax = nestMax;
    }

    /**
     * Gets the vend pk qty.
     *
     * @return the vend pk qty
     */
    public Integer getPackQuantity() {
        return this.packQuantity;
    }

    /**
     * Sets the vend pk qty.
     *
     * @param packQuantity the new vend pk qty
     */
    public void setPackQuantity(Integer packQuantity) {
        this.packQuantity = packQuantity;
    }

    /**
     * Gets the cs unt factr1.
     *
     * @return the cs unt factr1
     */
    public Double getCsUnitFactoryOne() {
        return this.csUnitFactoryOne;
    }

    /**
     * Sets the cs unt factr1.
     *
     * @param csUnitFactoryOne the new cs unt factr1
     */
    public void setCsUnitFactoryOne(Double csUnitFactoryOne) {
        this.csUnitFactoryOne = csUnitFactoryOne;
    }

    /**
     * Gets the ord ctlg nbr.
     *
     * @return the ord ctlg nbr
     */
    public Integer getOrderCatalogNumber() {
        return orderCatalogNumber;
    }

    /**
     * Sets the ord ctlg nbr.
     *
     * @param orderCatalogNumber the new ord ctlg nbr
     */
    public void setOrderCatalogNumber(Integer orderCatalogNumber) {
        this.orderCatalogNumber = orderCatalogNumber;
    }

    /**
     * Gets the abc itm cat no.
     *
     * @return the abc itm cat no
     */
    public Integer getAbcItemCategoryNo() {
        return abcItemCategoryNo;
    }

    /**
     * Sets the abc itm cat no.
     *
     * @param abcItemCategoryNo the new abc itm cat no
     */
    public void setAbcItemCategoryNo(Integer abcItemCategoryNo) {
        this.abcItemCategoryNo = abcItemCategoryNo;
    }

    /**
     * Gets the purchase status.
     *
     * @return the purchase status
     */
    public String getPurchaseStatus() {
        return purchaseStatus;
    }

    /**
     * Sets the purchase status.
     *
     * @param purchaseStatus the new purchase status
     */
    public void setPurchaseStatus(String purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

    /**
     * Gets the dsply dry pal sw.
     *
     * @return the dsply dry pal sw
     */
    public boolean isDisplayReadyUnit() {
        return displayReadyUnit;
    }

    /**
     * Sets the dsply dry pal sw.
     *
     * @param displayReadyUnit the new dsply dry pal sw
     */
    public void setDisplayReadyUnit(boolean displayReadyUnit) {
        this.displayReadyUnit = displayReadyUnit;
    }

    /**
     * Gets the std subst logic sw.
     *
     * @return the std subst logic sw
     */
    public boolean isAlwaysSubWhenOut() {
        return alwaysSubWhenOut;
    }

    /**
     * Sets the std subst logic sw.
     *
     * @param alwaysSubWhenOut the new std subst logic sw
     */
    public void setAlwaysSubWhenOut(boolean alwaysSubWhenOut) {
        this.alwaysSubWhenOut = alwaysSubWhenOut;
    }

    /**
     * Gets the srs aff typ cd.
     *
     * @return the srs aff typ cd
     */
    public String getTypeOfDRU() {
        return typeOfDRU;
    }

    /**
     * Sets the srs aff typ cd.
     *
     * @param typeOfDRU the new srs aff typ cd
     */
    public void setTypeOfDRU(String typeOfDRU) {
        this.typeOfDRU = typeOfDRU;
    }

    /**
     * Gets the prod fcng nbr.
     *
     * @return the prod fcng nbr
     */
    public Long getRowsFacing() {
        return rowsFacing;
    }

    /**
     * Sets the prod fcng nbr.
     *
     * @param rowsFacing the new prod fcng nbr
     */
    public void setRowsFacing(Long rowsFacing) {
        this.rowsFacing = rowsFacing;
    }

    /**
     * Gets the prod row deep nbr.
     *
     * @return the prod row deep nbr
     */
    public Long getRowsDeep() {
        return rowsDeep;
    }

    /**
     * Sets the prod row deep nbr.
     *
     * @param rowsDeep the new prod row deep nbr
     */
    public void setRowsDeep(Long rowsDeep) {
        this.rowsDeep = rowsDeep;
    }

    /**
     * Gets the prod row hi nbr.
     *
     * @return the prod row hi nbr
     */
    public Long getRowsHigh() {
        return rowsHigh;
    }

    /**
     * Sets the prod row hi nbr.
     *
     * @param rowsHigh the new prod row hi nbr
     */
    public void setRowsHigh(Long rowsHigh) {
        this.rowsHigh = rowsHigh;
    }

    /**
     * Gets the nbr of orint nbr.
     *
     * @return the nbr of orint nbr
     */
    public Long getOrientation() {
        return orientation;
    }

    /**
     * Sets the nbr of orint nbr.
     *
     * @param orientation the new nbr of orint nbr
     */
    public void setOrientation(Long orientation) {
        this.orientation = orientation;
    }

    public String getOneTouchTypCode() {
        return oneTouchTypCode;
    }
    public Long getCandidateWorkRequestId() {
        return candidateWorkRequestId;
    }

    public void setCandidateWorkRequestId(Long candidateWorkRequestId) {
        this.candidateWorkRequestId = candidateWorkRequestId;
    }
    public void setOneTouchTypCode(String oneTouchTypCode) {
        this.oneTouchTypCode = oneTouchTypCode;
    }
    /**
	 * Returns the OneTouchType. This pulls the one touch type entity according to the id from one_touch_typ_cd.
	 *
	 * @return OneTouchType
	 */
	public OneTouchType getOneTouchType() {
		return oneTouchType;
	}

	/**
	 * Sets the OneTouchType
	 *
	 * @param oneTouchType The OneTouchType
	 */
	public void setOneTouchType(OneTouchType oneTouchType) {
		this.oneTouchType = oneTouchType;
	}

	/**
	 * Gets the list of candidateWarehouseLocationItems.
	 *
	 * @return the candidateWarehouseLocationItems
	 */
	public List<CandidateWarehouseLocationItem> getCandidateWarehouseLocationItems() {
		return candidateWarehouseLocationItems;
	}

	/**
	 * Sets the list of candidateWarehouseLocationItems.
	 *
	 * @param candidateWarehouseLocationItems the candidateWarehouseLocationItems to set
	 */
	public void setCandidateWarehouseLocationItems(List<CandidateWarehouseLocationItem> candidateWarehouseLocationItems) {
		this.candidateWarehouseLocationItems = candidateWarehouseLocationItems;
	}
    public List<CandidateVendorLocationItem> getCandidateVendorLocationItems() {
        return candidateVendorLocationItems;
    }

    public void setCandidateVendorLocationItems(List<CandidateVendorLocationItem> candidateVendorLocationItems) {
        this.candidateVendorLocationItems = candidateVendorLocationItems;
    }
    public List<CandidateItemScanCode> getCandidateItemScanCodes() {
        return candidateItemScanCodes;
    }

    public void setCandidateItemScanCodes(List<CandidateItemScanCode> candidateItemScanCodes) {
        this.candidateItemScanCodes = candidateItemScanCodes;
    }

    public List<CandidateRelatedItem> getCandidateRelatedItems() {
        return candidateRelatedItems;
    }

    public void setCandidateRelatedItems(List<CandidateRelatedItem> candidateRelatedItems) {
        this.candidateRelatedItems = candidateRelatedItems;
    }
    /**
     * Compares this object to another for equality.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateItemMaster)) return false;

        CandidateItemMaster that = (CandidateItemMaster) o;

        return candidateItemId != null ? candidateItemId.equals(that.candidateItemId) : that.candidateItemId == null;
    }

    /**
     * Returns a hash code for this object. Equal objects have the same hash code. Unequal objects have
     * different hash codes.
     *
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        return candidateItemId != null ? candidateItemId.hashCode() : 0;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return "CandidateItemMaster{" +
                "candidateItemId=" + candidateItemId +
                ", caseUpc=" + caseUpc +
                ", orderingUpc=" + orderingUpc +
                ", newData=" + newData +
                '}';
    }
    /**
     * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
     * it is inserted into the work request table.
     */
    @PrePersist
    public void setWorkRequestId() {
        if (this.getCandidateWorkRequestId() == null) {
            this.setCandidateWorkRequestId(this.candidateWorkRequest.getWorkRequestId());
        }
    }
}
