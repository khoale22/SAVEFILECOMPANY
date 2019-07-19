/*
 * CandidateProductMaster
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Entity for the ps_product_master table.
 * Created by vn70516
 *
 * @since 2.12.0
 */
@Entity
@Table(name = "ps_product_master")
@TypeDefs({
        @TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class)
})
public class CandidateProductMaster implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String STRING_DEFAULT_BLANK = " ";
    public static final String SCALE_SW_DEFAULT = "N";
    public static int SCAN_DESCRIPTION_MAX_LENGTH = 12;
    /**
     * The ps prod id.
     */
    @Id
    @Column(name = "ps_prod_id")
    @GeneratedValue(generator = "candidateProductIdSequence")
    @SequenceGenerator(name = "candidateProductIdSequence", sequenceName = "ps_product_master_seq")
    private Long candidateProductId;

    @Column(name = "prod_id")
    private Long productId;

    @Column(name = "prod_eng_des")
    @Type(type = "fixedLengthChar")
    private String description;

    @Column(name = "prod_spnsh_des")
    private String spanishDescription;

    @Column(name = "pd_omi_com_cls_cd")
    private Integer classCode;

    @Column(name = "pd_omi_com_cd")
    private Integer commodityCode;

    @Column(name = "pd_omi_sub_com_cd")
    private Integer subCommodityCode;

    @Column(name = "str_dept_nbr")
    private Long departmentCode;

    @Column(name = "str_sub_dept_id")
    @Type(type = "fixedLengthChar")
    private String subDepartmentCode;

    @Column(name = "retl_link_cd")
    private Long retailLink;

    @Column(name = "show_clrs_sw")
    private String showCalories = " ";

    @Column(name = "ps_work_id")
    private Long workRequestId;
    /**
     * The lst updt usr id.
     */
    @Column(name = "LST_UPDT_USR_ID", nullable = false, length = 8)
    private String lstUpdtUsrId;

    @Column(name="MIN_CUST_ORD_QTY")
    private BigDecimal minCustOrdQty;
    /**
     * The new data sw.
     */
    @Column(name = "NEW_DATA_SW")
    private boolean newDataSw;

    /**
     * The sals tax sw.
     */
    @Column(name = "SOLD_SEPLY_SW")
    private String soldSeplySwitch = " ";

	/**
     * The sals tax sw.
     */
    @Column(name = "SALS_TAX_SW")
    private String retailTaxSwitch = " ";

    /**
     * The dsd dept sw.
     */
    @Column(name = "DSD_DEPT_SW")
    private String dsdDeptSwitch = " ";

    /**
     * The price required
     */
    @Column(name = "IM_PRC_REQ_FLAG")
    private String priceRequiredFlag = " ";
    /**
     * The wic sw.
     */
    @Column(name = "WIC_SW")
    private String wicSwitch = " ";

    /**
     * The premarked prc sw.
     */
    @Column(name = "PREMARKED_PRC_SW")
    private String premarkedPrice = " ";

    @Column(name = "STRIP_FLAG")
    private String stripFlag = " ";

    @Column(name = "FRC_TARE_SW")
    private String forceTare = " ";

    /**
     * The prprc for nbr.
     */
    @Column(name = "PRPRC_FOR_NBR", nullable = false)
    private Integer prprcForNbr = Integer.valueOf(1);

    @Column(name = "PKG_TXT", nullable = false, length = 30)
    private String packagingText = " ";

    /**
     * The qty rstr sw.
     */
    @Column(name = "QTY_RSTR_SW")
    private String qtyRestrictSwitch = " ";

    /**
     * The wt sw.
     */
    @Column(name = "WT_SW")
    private String wtSwitch = " ";
    /**
     * The avc sw.
     */
    @Column(name = "AVC_SW")
    private String avcSwitch = " ";

    /**
     * The stk sw.
     */
    @Column(name = "STK_SW")
    private String stackableSwitch = " ";

    /**
     * The spec sheet att sw.
     */
    @Column(name = "SPEC_SHEET_ATT_SW")
    private String specialSheetAttribute = " ";

    /**
     * The unstamped sw.
     */
    @Column(name = "UNSTAMPED_SW")
    private String unstampedSwitch = " ";

    /**
     * The mrk prc in str sw.
     */
    @Column(name = "MRK_PRC_IN_STR_SW")
    private String markPriceInStr = " ";

    /**
     * The fd stmp sw.
     */
    @Column(name = "FD_STMP_SW")
    private String foodStampSwitch = " ";

    /**
     * The sesnl sw.
     */
    @Column(name = "SESNL_SW")
    private String sesnalySwitch = " ";

    /**
     * The tbco prod sw.
     */
    @Column(name = "TBCO_PROD_SW")
    private String tobaccoProductSwitch = " ";

    /**
     * The rx prod sw.
     */
    @Column(name = "RX_PROD_SW")
    private String rxProductFlag = " ";

    /**
     * The alcohol prod sw.
     */
    @Column(name = "ALCOHOL_PROD_SW")
    private String alcoholProductSwitch = " ";
    /**
     * The gnrc prod sw.
     */
    @Column(name = "GNRC_PROD_SW")
    private String genericProductSwitch = " ";

    /**
     * The priv lbl sw.
     */
    @Column(name = "PRIV_LBL_SW")
    private String privateLabelSwitch = " ";

    /**
     * The scanable itm sw.
     */
    @Column(name = "SCANABLE_ITM_SW")
    private String scanableItemSwitch = " ";
    /**
     * The scale sw.
     */
    @Column(name = "SCALE_SW")
    private String scaleSwitch = " ";

    /**
     * The healthy living sw.
     */
    @Column(name = "HEALTHY_LIVING_SW")
    private String healthyLivingSwitch = " ";

    /**
     * The dsd deleted sw.
     */
    @Column(name = "DSD_DELETED_SW")
    private String dsdDeletedSwitch = " ";

    /**
     * The actvt flag.
     */
    @Column(name = "ACTVT_FLAG")
    private String activeFlag = " ";

    /**
     * The mac eac sw.
     */
    @Column(name = "MAC_EAC_SW")
    private String macEacSwitch = " ";

    /**
     * The drg fact pan sw.
     */
    @Column(name = "DRG_FACT_PAN_SW")
    private String drugFactPanSwitch = " ";

    /**
     * The eas sw.
     */
    @Column(name = "EAS_SW")
    private String easSwitch = " ";

    /**
     * The ingrd sw.
     */
    @Column(name = "INGRD_SW")
    private String ingradientSwitch = " ";

    @Column(name="WINE_PROD_SW")
    private String wineProductSwitch = " ";

    @Column(name="WINE_VINTAGE_YY", nullable = true)
    private Integer wineVintageYy;

    @Column(name="WINEMKR_ID", nullable = true)
    private Integer wineMakerId;

    @Column(name="WINE_RGN_ID", nullable = true)
    private Integer wineRegionId;

    @Column(name="VARTL_ID", nullable = true)
    private Integer varTlId;

    @Column(name="MIN_INVEN_THRH_CNT", nullable = true)
    private Integer minInventoryThresholdCount;

    @Column(name="tag_typ_cd")
    private String tagType;

    /**
     * The alcohol pct amt.
     */
    @Column(name = "ALCOHOL_PCT_AMT", precision = 6, scale = 3)
    private BigDecimal alcoholPercentAmount;

    @Column(name = "BDM_CD", nullable = false)
    @Type(type="fixedLengthCharPK")
    private String bdmCode;

    @Column(name = "prod_brnd_id")
    private Long prodBrandId;

    @Column(name = "PSS_DEPT")
    private Integer pssDepartment;

    /**
     * The tax_qual_cd
     */
    @Column(name = "TAX_QUAL_CD", nullable = true, length = 10)
    private String taxQualityCode;

    /**
     * The lst updt ts.
     */
//   @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LST_UPDT_TS",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, length = 0)
    private LocalDateTime lastUpdateTs;

    /**
     * The scan des.
     */
    @Column(name = "SCAN_DES", length = 12)
    private String scanDescription;

    /**
     * The prim scn cd.
     */
    @Column(name = "PRIM_SCN_CD", precision = 17, scale = 0)
    private Long primaryScanCode;

    /**
     * The pd hilite prnt cd.
     */
    @Column(name = "PD_HILITE_PRNT_CD", precision = 5)
    private Long actionCode;

    /**
     * The product desc line1.
     */
    @Column(name = "PRODUCT_DESC_LINE1", length = 50)
    private String englishDescriptionOne;

    /**
     * The product desc line2.
     */
    @Column(name = "PRODUCT_DESC_LINE2", length = 50)
    private String englishDescriptionTwo;

    /**
     * The spanish desc line1.
     */
    @Column(name = "SPANISH_DESC_LINE1", length = 50)
    private String spanishDescriptionOne;

    /**
     * The spanish desc line2.
     */
    @Column(name = "SPANISH_DESC_LINE2", length = 50)
    private String spanishDescriptionTwo;

    /**
     * The grade nbr.
     */
    @Column(name = "GRADE_NBR", precision = 3, scale = 0)
    private Integer gradeNumber;

    /**
     * The net wt.
     */
    @Column(name = "NET_WT", precision = 9, scale = 4)
    private Double netWeight;

    /**
     * The pd safe hand cd.
     */
    @Column(name = "PD_SAFE_HAND_CD", precision = 5)
    private Long graphicsCode;


    /**
     * The ingr statement num.
     */
    @Column(name = "INGR_STATEMENT_NUM", precision = 7, scale = 0)
    private Long ingredientStatement;
    /**
     * The sals rstr cd.
     */
    @Column(name = "SALS_RSTR_CD", length = 5)
    private String salesRestrictCode;

    /**
     * The sl lbl frmat 1 cd.
     */
    @Column(name = "SL_LBL_FRMAT_1_CD", precision = 5)
    private Long firstLabelFormat;
    /**
     * The sl lbl frmat 2 cd.
     */
    @Column(name = "SL_LBL_FRMAT_2_CD", precision = 5)
    private Long secondLabelFormat;

    /**
     * The pd ntrnt stmt no.
     */
    @Column(name = "PD_NTRNT_STMT_NO", precision = 7, scale = 0)
    private Long nutrientStatement;

    /**
     * The tare prepack.
     */
    @Column(name = "TARE_PREPACK", precision = 3)
    private Double prePackTare;

    /**
     * The tare serv counter.
     */
    @Column(name = "TARE_SERV_COUNTER", precision = 3)
    private Double serviceCounterTare;
    /**
     * The shelf life.
     */
    @Column(name = "SHELF_LIFE", precision = 3, scale = 0)
    private Integer shelfLifeDays;

    /**
     * The color cd.
     */
    @Column(name = "COLOR_CD", length = 10)
    private String colorCode;

    /**
     * The fam1 cd.
     */
    @Column(name = "FAM_1_CD", precision = 10, scale = 0)
    private Long family1Code;

    /**
     * The fam2 cd.
     */
    @Column(name = "FAM_2_CD", precision = 10, scale = 0)
    private Long family2Code;
    /**
     * The fsa cd.
     */
    @Column(name = "FSA_CD", length = 5)
    private String fsaCode;

    /**
     * The premrk prc amt.
     */
    @Column(name = "PREMRK_PRC_AMT", precision = 9)
    private Double premarkPriceAmount;

    /**
     * The max shlf life dd.
     */
    @Column(name = "MAX_SHLF_LIFE_DD")
    private Long maxShelfLifeDays;

    /**
     * The sesnly Id.
     */
    @Column(name = "SESNLY_ID")
    private Long sesnlyId;
    /**
     * The sesnlyId yy.
     */
    @Column(name = "SESNLY_YY", precision = 10, scale = 0)
    private Long seasonalityYear;

    /**
     * The prod modl txt.
     */
    @Column(name = "PROD_MODL_TXT", length = 20)
    private String productModelText;

    /**
     * The pse type cd.
     */
    @Column(name = "PSE_TYPE_CD", length = 1)
    private String pseTypeCode;

    /**
     * The ndc id.
     */
    @Column(name = "NDC_ID", length = 11)
    private String ndc;

    /**
     * The drug sch typ cd.
     */
    @Column(name = "DRUG_SCH_TYP_CD", length = 5)
    @Type(type = "fixedLengthCharPK")
    private String drugScheduleTypeCode;
    /**
     * The drug nm cd.
     */
    @Column(name = "DRUG_NM_CD", length = 5)
    @Type(type = "fixedLengthCharPK")
    private String drugNameCode;

    /**
     * The avg whlsl rx cst.
     */
    @Column(name = "AVG_WHLSL_RX_CST", precision = 11, scale = 4)
    private Double avgWhlslRxCst;

    /**
     * The direct rx cst.
     */
    @Column(name = "DIRECT_RX_CST", precision = 11, scale = 4)
    private Double directRxCst;

    /**
     * The tbco prod typ cd.
     */
    @Column(name = "TBCO_PROD_TYP_CD", length = 5)
    @Type(type = "fixedLengthCharPK")
    private String tobaccoProductTypeCode;

    /**
     * The cig tax amt.
     */
    @Column(name = "CIG_TAX_AMT", precision = 9, scale = 4)
    private BigDecimal cigTaxAmt;

    /**
     * The prod typ cd.
     */
    @Column(name = "PROD_TYP_CD", length = 5)
    private String productTypeCode;

    /**
     * The retl x4 qty.
     */
    @Column(name = "RETL_X4_QTY")
    private Integer retailX4Qty;
    /**
     * The retl prc amt.
     */
    @Column(name = "RETL_PRC_AMT", precision = 9)
    private BigDecimal retailPriceAmount;

    @Transient
    @JsonIgnore
    private Long t2tId;


    @Column(name = "self_mfg_sw")
    private String selfManufactured = " ";

    @Column(name = "crit_itm_sw")
    private String criticalItem = " ";

    @Column(name="csume_achol_str_sw")
    private String consumeAlcoholInStore = " ";

    @Column(name = "vertex_tax_cat_cd")
    private String vertexTaxCategoryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ps_work_id", referencedColumnName = "ps_work_id", insertable = false, updatable = false, nullable = false)
    private CandidateWorkRequest candidateWorkRequest;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sals_rstr_cd", referencedColumnName = "sals_rstr_cd", insertable = false, updatable = false, nullable = false)
    private SellingRestrictionCode sellingRestrictionCode;

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_brnd_id", insertable = false, updatable = false)
    private ProductBrand productBrand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "pd_omi_com_cls_cd", referencedColumnName = "pd_omi_com_cls_cd", insertable = false, updatable = false),
            @JoinColumn(name = "pd_omi_com_cd", referencedColumnName = "pd_omi_com_cd", insertable = false, updatable = false)
    })
    private ClassCommodity classCommodity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"viewableSellingRestrictions", "viewableShippingRestrictions"})
    @JoinColumns({
            @JoinColumn(name = "str_dept_nbr", referencedColumnName = "str_dept_nbr", insertable = false, updatable = false),
            @JoinColumn(name = "str_sub_dept_id", referencedColumnName = "str_sub_dept_id", insertable = false, updatable = false)
    })
    private SubDepartment subDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "pd_omi_com_cls_cd", referencedColumnName = "pd_omi_com_cls_cd", insertable = false, updatable = false),
            @JoinColumn(name = "pd_omi_com_cd", referencedColumnName = "pd_omi_com_cd", insertable = false, updatable = false),
            @JoinColumn(name = "pd_omi_sub_com_cd", referencedColumnName = "pd_omi_sub_com_cd", insertable = false, updatable = false)
    })
    private SubCommodity subCommodity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pd_omi_com_cls_cd", referencedColumnName = "item_cls_code", insertable = false, updatable = false)
    private ItemClass itemClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sl_lbl_frmat_1_cd", referencedColumnName = "sl_lbl_frmat_cd", insertable = false, updatable = false)
    private ScaleLabelFormat scaleLabelFormat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prim_ps_itm_id", referencedColumnName = "ps_itm_id", insertable = false, updatable = false)
    private CandidateItemMaster candidateItemMaster;

    @OneToMany(mappedBy = "candidateProductMaster", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CandidateProductMarketingClaim> productMarketingClaims;

    /* The ps_prod_scn_cd_ext. */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "candidateProductMaster",cascade = CascadeType.ALL)
    private List<CandidateScanCodeExtent> candidateScanCodeExtents;

	@OneToMany(fetch = FetchType.LAZY,
			mappedBy = "candidateProductMaster",cascade = CascadeType.ALL)
	private List<CandidateProductRelationship> candidateProductRelationships;

	@OneToMany(fetch = FetchType.LAZY,
			mappedBy = "candidateRelatedProductId",cascade = CascadeType.ALL)
	private List<CandidateProductRelationship> candidateProductRelatedRelationships;

	@Transient
	private Long productScanCodeVariant;

	/**
     * Called by Hibernate before this object is stored. This will set the ps_work_id to match the work request.
     */
    @PrePersist
    public void prepareForSave() {
        if (this.workRequestId == null) {
            this.workRequestId = this.getCandidateWorkRequest().getWorkRequestId();
        }
    }

    //bi-directional many-to-one association to CandidateDiscountThreshold
    @OneToMany(mappedBy="candidateProductMaster",cascade = CascadeType.ALL)
    private List<CandidateDiscountThreshold> candidateDiscountThresholds;

    //bi-directional many-to-one association to CandidateFulfillmentChannel
    @OneToMany(mappedBy="candidateProductMaster",cascade = CascadeType.ALL)
    private List<CandidateFulfillmentChannel> candidateFulfillmentChannels;

    //bi-directional many-to-one association to CandidateProductScore
    @OneToMany(mappedBy="candidateProductMaster",cascade = CascadeType.ALL)
    private List<CandidateProductScore> candidateProductScores;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "candidateProductMaster",cascade = CascadeType.ALL)
	private List<CandidateProductMarketingClaim> candidateProductMarketingClaim;

    /** The ps prod desc txts. */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "candidateProductMaster",cascade = CascadeType.ALL)
    private List<CandidateDescription> candidateDescriptions;

    /** The ps prod desc txts. */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "candidateProductMaster",cascade = CascadeType.ALL)
    private List<CandidateSellingUnit> candidateSellingUnits;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "candidateProductMaster", cascade = CascadeType.ALL)
    private List<CandidateProductOnline> candidateProductOnlines;

    @Transient
    private List<CandidateMasterDataExtensionAttribute> candidateMasterDataExtensionAttributes;
    /**
	 * Get the soldSeplySwitch.
	 *
	 * @return the soldSeplySwitch
	 */
	public String getSoldSeplySwitch() {
		return soldSeplySwitch;
	}

	/**
	 * Set the soldSeplySwitch.
	 *
	 * @param soldSeplySwitch the soldSeplySwitch to set
	 */
	public void setSoldSeplySwitch(String soldSeplySwitch) {
		this.soldSeplySwitch = soldSeplySwitch;
	}

	/**
	 * get candidate product related relationships
	 * @return list CandidateProductRelationship
	 */
	public List<CandidateProductRelationship> getCandidateProductRelatedRelationships() {
		return candidateProductRelatedRelationships;
	}

	/**
	 * set candidate product related relationships
	 * @param candidateProductRelatedRelationships
	 */
	public void setCandidateProductRelatedRelationships(List<CandidateProductRelationship> candidateProductRelatedRelationships) {
		this.candidateProductRelatedRelationships = candidateProductRelatedRelationships;
	}

	/**
	 * get candidate product relationship
	 * @return list CandidateProductRelationship
	 */
	public List<CandidateProductRelationship> getCandidateProductRelationships() {
		return candidateProductRelationships;
	}

	/**
	 * set candidate product relationship
	 * @param candidateProductRelationships
	 */
	public void setCandidateProductRelationships(List<CandidateProductRelationship> candidateProductRelationships) {
		this.candidateProductRelationships = candidateProductRelationships;
	}

	/**
	 * get product scan code variant
	 * @return product scan code
	 */
	public Long getProductScanCodeVariant() {
		return productScanCodeVariant;
	}

	/**
	 * set product scan code variant
	 * @param productScanCodeVariant
	 */
	public void setProductScanCodeVariant(Long productScanCodeVariant) {
		this.productScanCodeVariant = productScanCodeVariant;
	}
/**
	 * @return Gets the value of candidateProductMarketingClaim
	 */
	public List<CandidateProductMarketingClaim> getCandidateProductMarketingClaim() {
		return candidateProductMarketingClaim;
	}
	/**
	 * Sets the candidateProductMarketingClaim
	 */
	public CandidateProductMaster setCandidateProductMarketingClaim(List<CandidateProductMarketingClaim> candidateProductMarketingClaim) {
		this.candidateProductMarketingClaim = candidateProductMarketingClaim;
		return this;
	}
    /**
     * @return Gets the value of candidateProductId and returns candidateProductId
     */
    public void setCandidateProductId(Long candidateProductId) {
        this.candidateProductId = candidateProductId;
    }

    /**
     * Sets the candidateProductId
     */
    public Long getCandidateProductId() {
        return candidateProductId;
    }

    /**
     * @return Gets the value of workRequestId and returns workRequestId
     */
    public void setWorkRequestId(Long workRequestId) {
        this.workRequestId = workRequestId;
    }

    /**
     * Sets the workRequestId
     */
    public Long getWorkRequestId() {
        return workRequestId;
    }
    public String getLstUpdtUsrId() {
        return lstUpdtUsrId;
    }
    /**
     * Sets the lst updt usr id.
     *
     * @param lstUpdtUsrId the new lst updt usr id
     */
    public void setLstUpdtUsrId(String lstUpdtUsrId) {
        this.lstUpdtUsrId = lstUpdtUsrId;
    }
    /**
     * @return the newDataSw
     */
    public boolean getNewDataSw() {
        return newDataSw;
    }
    /**
     * @param newDataSw the newDataSw to set
     */
    public void setNewDataSw(boolean newDataSw) {
        this.newDataSw = newDataSw;
    }
    /**
     * @return the MinCustOrdQty
     */
    public BigDecimal getMinCustOrdQty() {
        return minCustOrdQty;
    }
    /**
     * @param minCustOrdQty the minCustOrdQty to set
     */
    public void setMinCustOrdQty(BigDecimal minCustOrdQty) {
        this.minCustOrdQty = minCustOrdQty;
    }
    /**
     * @return Gets the value of candidateWorkRequest and returns candidateWorkRequest
     */
    public void setCandidateWorkRequest(CandidateWorkRequest candidateWorkRequest) {
        this.candidateWorkRequest = candidateWorkRequest;
    }

    /**
     * Sets the candidateWorkRequest
     */
    public CandidateWorkRequest getCandidateWorkRequest() {
        return candidateWorkRequest;
    }

    /**
     * @return Gets the value of sellingRestrictionCode and returns sellingRestrictionCode
     */
    public void setSellingRestrictionCode(SellingRestrictionCode sellingRestrictionCode) {
        this.sellingRestrictionCode = sellingRestrictionCode;
    }

    /**
     * Sets the sellingRestrictionCode
     */
    public SellingRestrictionCode getSellingRestrictionCode() {
        return sellingRestrictionCode;
    }

    /**
     * @return Gets the value of itemClass and returns itemClass
     */
    public void setItemClass(ItemClass itemClass) {
        this.itemClass = itemClass;
    }

    /**
     * Sets the itemClass
     */
    public ItemClass getItemClass() {
        return itemClass;
    }

    /**
     * Gets prod brand.  A category of products that are all made by a particular company and all have a particular name.
     *
     * @return the prod brand category of products that are all made by a particular company and all have a particular name.
     */
    public ProductBrand getProductBrand() {
        return productBrand;
    }

    /**
     * Sets prod brand. A category of products that are all made by a particular company and all have a particular name.
     *
     * @param productBrand the prod brand category of products that are all made by a particular company and all have a particular name.
     */
    public void setProductBrand(ProductBrand productBrand) {
        this.productBrand = productBrand;
    }

    /**
     * Is show calories boolean.
     *
     * @return the boolean
     */
    public String isShowCalories() {
        return showCalories;
    }

    /**
     * Sets show calories.
     *
     * @param showCalories the show calories
     */
    public void setShowCalories(String showCalories) {
        this.showCalories = showCalories;
    }

    /**
     * Get department string string.
     *
     * @return the string
     */
    public String getDepartmentString() {
        if (this.departmentCode == null || this.subDepartmentCode == null) {
            return null;
        }
        return this.departmentCode + this.subDepartmentCode.trim();
    }

    /**
     * Gets retail link.
     *
     * @return the retail link code
     */
    public Long getRetailLink() {
        return retailLink;
    }

    /**
     * Sets retail link.
     *
     * @param retailLink the retail link code
     */
    public void setRetailLink(Long retailLink) {
        this.retailLink = retailLink;
    }

    /**
     * Returns the product description.
     *
     * @return description the product description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the product description.
     *
     * @param description the product description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the productId  associated with this item.
     *
     * @return The productId associated with this item.
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * Sets the productId associated with this item.
     *
     * @param productId The prodId associated with this item.
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * Returns the OMI Class for this object.
     *
     * @return The OMI Class for this object.
     */
    public Integer getClassCode() {
        return classCode;
    }

    /**
     * Sets the OMI Class for this object.
     *
     * @param classCode The Class for this object.
     */
    public void setClassCode(Integer classCode) {
        this.classCode = classCode;
    }

    /**
     * Sets the Commodity for this object.
     *
     * @param commodityCode The Commodity for this object.
     */
    public void setCommodityCode(Integer commodityCode) {
        this.commodityCode = commodityCode;
    }

    /**
     * Returns the Commodity for this object.
     *
     * @return The Commodity for this object.
     */
    public Integer getCommodityCode() {
        return commodityCode;
    }

    /**
     * Returns the sub commodity for this object.
     *
     * @return The sub commodity for this object.
     */
    public Integer getSubCommodityCode() {
        return subCommodityCode;
    }

    /**
     * Sets the sub commodity for this object.
     *
     * @param subCommodityCode The sub commodity for this object.
     */
    public void setSubCommodityCode(Integer subCommodityCode) {
        this.subCommodityCode = subCommodityCode;
    }

    /**
     * Returns the department code.
     *
     * @return The departmentCode for this object.
     */
    public Long getDepartmentCode() {
        return departmentCode;
    }

    /**
     * Sets the department code.
     *
     * @param departmentCode The departmentCode for this object.
     */
    public void setDepartmentCode(Long departmentCode) {
        this.departmentCode = departmentCode;
    }

    /**
     * Returns the sub department code.
     *
     * @return The sub department code for this object.
     */
    public String getSubDepartmentCode() {
        return subDepartmentCode;
    }

    /**
     * Sets the sub department code.
     *
     * @param subDepartmentCode The sub departmentCode for this object.
     */
    public void setSubDepartmentCode(String subDepartmentCode) {
        this.subDepartmentCode = subDepartmentCode;
    }

    /**
     * Returns the product master's class commodity.
     *
     * @return The product master's class commodity.
     */
    public ClassCommodity getClassCommodity() {
        return classCommodity;
    }

    /**
     * Sets the product master's class commodity.
     *
     * @param classCommodity The product master's class commodity.
     */
    public void setClassCommodity(ClassCommodity classCommodity) {
        this.classCommodity = classCommodity;
    }

    /**
     * Returns the product master's sub department.
     *
     * @return The product master's sub department.
     */
    public SubDepartment getSubDepartment() {
        return subDepartment;
    }

    /**
     * Sets the product master's sub department.
     *
     * @param subDepartment The product master's sub department.
     */
    public void setSubDepartment(SubDepartment subDepartment) {
        this.subDepartment = subDepartment;
    }

    /**
     * Returns the product master's sub commodity.
     *
     * @return The product master's sub commodity.
     */
    public SubCommodity getSubCommodity() {
        return subCommodity;
    }

    /**
     * Sets the product master's sub commodity.
     *
     * @param subCommodity The product master's sub commodity.
     */
    public void setSubCommodity(SubCommodity subCommodity) {
        this.subCommodity = subCommodity;
    }

    /**
     * Returns the spanish description.
     *
     * @return the spanish description.
     */
    public String getSpanishDescription() {
        return spanishDescription;
    }

    /**
     * Sets the spanish description.
     *
     * @param spanishDescription the spanish description.
     */
    public void setSpanishDescription(String spanishDescription) {
        this.spanishDescription = spanishDescription;
    }

    /**
     * @return Gets the value of scaleLabelFormat and returns scaleLabelFormat
     */
    public void setScaleLabelFormat(ScaleLabelFormat scaleLabelFormat) {
        this.scaleLabelFormat = scaleLabelFormat;
    }

    /**
     * Sets the scaleLabelFormat
     */
    public ScaleLabelFormat getScaleLabelFormat() {
        return scaleLabelFormat;
    }

    /**
     * @return Gets the value of candidateItemMaster and returns candidateItemMaster
     */
    public void setCandidateItemMaster(CandidateItemMaster candidateItemMaster) {
        this.candidateItemMaster = candidateItemMaster;
    }

    public boolean isNewDataSw() {
        return newDataSw;
    }

    public String isRetailTaxSwitch() {
        return retailTaxSwitch;
    }

    public void setRetailTaxSwitch(String retailTaxSwitch) {
        this.retailTaxSwitch = retailTaxSwitch;
    }

    public String isDsdDeptSwitch() {
        return dsdDeptSwitch;
    }

    public void setDsdDeptSwitch(String dsdDeptSwitch) {
        this.dsdDeptSwitch = dsdDeptSwitch;
    }

    public String isPriceRequiredFlag() {
        return priceRequiredFlag;
    }

    public void setPriceRequiredFlag(String priceRequiredFlag) {
        this.priceRequiredFlag = priceRequiredFlag;
    }


    public String isDrugFactPanSwitch() {
        return drugFactPanSwitch;
    }

    public void setDrugFactPanSwitch(String drugFactPanSwitch) {
        this.drugFactPanSwitch = drugFactPanSwitch;
    }

    public String isEasSwitch() {
        return easSwitch;
    }

    public void setEasSwitch(String easSwitch) {
        this.easSwitch = easSwitch;
    }

    public String isIngradientSwitch() {
        return ingradientSwitch;
    }

    public void setIngradientSwitch(String ingradientSwitch) {
        this.ingradientSwitch = ingradientSwitch;
    }
    /**
     * @return the wicSwitch
     */
    public String isWicSwitch() {
        return wicSwitch;
    }
    /**
     * @param wicSwitch the wicSwitch to set
     */
    public void setWicSwitch(String wicSwitch) {
        this.wicSwitch = wicSwitch;
    }
    /**
     * @return the premarkedPrice
     */
    public String isPremarkedPrice() {
        return premarkedPrice;
    }
    /**
     * @param premarkedPrice the premarkedPrice to set
     */
    public void setPremarkedPrice(String premarkedPrice) {
        this.premarkedPrice = premarkedPrice;
    }
    /**
     * @return the stripFlag
     */
    public String isStripFlag() {
        return stripFlag;
    }
    /**
     * @param stripFlag the stripFlag to set
     */
    public void setStripFlag(String stripFlag) {
        this.stripFlag = stripFlag;
    }
    /**
     * @return the forceTare
     */
    public String isForceTare() {
        return forceTare;
    }
    /**
     * @param forceTare the forceTare to set
     */
    public void setForceTare(String forceTare) {
        this.forceTare = forceTare;
    }
    /**
     * @return the prprcForNbr
     */
    public Integer getPrprcForNbr() {
        return prprcForNbr;
    }
    /**
     * @param prprcForNbr the prprcForNbr to set
     */
    public void setPrprcForNbr(Integer prprcForNbr) {
        this.prprcForNbr = prprcForNbr;
    }
    /**
     * @return the packagingText
     */
    public String getPackagingText() {
        return packagingText;
    }
    /**
     * @param packagingText the packagingText to set
     */
    public void setPackagingText(String packagingText) {
        this.packagingText = packagingText;
    }
    /**
     * @return the qtyRestrictSwitch
     */
    public String isQtyRestrictSwitch() {
        return qtyRestrictSwitch;
    }
    /**
     * @param qtyRestrictSwitch the qtyRestrictSwitch to set
     */
    public void setQtyRestrictSwitch(String qtyRestrictSwitch) {
        this.qtyRestrictSwitch = qtyRestrictSwitch;
    }
    /**
     * @return the wtSwitch
     */
    public String isWtSwitch() {
        return wtSwitch;
    }
    /**
     * @param wtSwitch the wtSwitch to set
     */
    public void setWtSwitch(String wtSwitch) {
        this.wtSwitch = wtSwitch;
    }
    /**
     * @return the avcSwitch
     */
    public String isAvcSwitch() {
        return avcSwitch;
    }

    /**
     * @param avcSwitch the avcSwitch to set
     */
    public void setAvcSwitch(String avcSwitch) {
        this.avcSwitch = avcSwitch;
    }
    /**
     * @return the stackableSwitch
     */
    public String isStackableSwitch() {
        return stackableSwitch;
    }
    /**
     * @param stackableSwitch the stackableSwitch to set
     */
    public void setStackableSwitch(String stackableSwitch) {
        this.stackableSwitch = stackableSwitch;
    }
    /**
     * @return the specialSheetAttribute
     */
    public String isSpecialSheetAttribute() {
        return specialSheetAttribute;
    }
    /**
     * @param specialSheetAttribute the specialSheetAttribute to set
     */
    public void setSpecialSheetAttribute(String specialSheetAttribute) {
        this.specialSheetAttribute = specialSheetAttribute;
    }
    /**
     * @return the unstampedSwitch
     */
    public String isUnstampedSwitch() {
        return unstampedSwitch;
    }
    /**
     * @param unstampedSwitch the unstampedSwitch to set
     */
    public void setUnstampedSwitch(String unstampedSwitch) {
        this.unstampedSwitch = unstampedSwitch;
    }
    /**
     * @return the markPriceInStr
     */
    public String isMarkPriceInStr() {
        return markPriceInStr;
    }
    /**
     * @param markPriceInStr the markPriceInStr to set
     */
    public void setMarkPriceInStr(String markPriceInStr) {
        this.markPriceInStr = markPriceInStr;
    }
    /**
     * @return the foodStampSwitch
     */
    public String isFoodStampSwitch() {
        return foodStampSwitch;
    }
    /**
     * @param foodStampSwitch the foodStampSwitch to set
     */
    public void setFoodStampSwitch(String foodStampSwitch) {
        this.foodStampSwitch = foodStampSwitch;
    }
    /**
     * @return the sesnalySwitch
     */
    public String isSesnalySwitch() {
        return sesnalySwitch;
    }
    /**
     * @param sesnalySwitch the sesnalySwitch to set
     */
    public void setSesnalySwitch(String sesnalySwitch) {
        this.sesnalySwitch = sesnalySwitch;
    }
    /**
     * @return the tobaccoProductSwitch
     */
    public String isTobaccoProductSwitch() {
        return tobaccoProductSwitch;
    }
    /**
     * @param tobaccoProductSwitch the tobaccoProductSwitch to set
     */
    public void setTobaccoProductSwitch(String tobaccoProductSwitch) {
        this.tobaccoProductSwitch = tobaccoProductSwitch;
    }
    /**
     * @return the rxProductFlag
     */
    public String isRxProductFlag() {
        return rxProductFlag;
    }
    /**
     * @param rxProductFlag the rxProductFlag to set
     */
    public void setRxProductFlag(String rxProductFlag) {
        this.rxProductFlag = rxProductFlag;
    }
    /**
     * @return the alcoholProductSwitch
     */
    public String isAlcoholProductSwitch() {
        return alcoholProductSwitch;
    }
    /**
     * @param alcoholProductSwitch the alcoholProductSwitch to set
     */
    public void setAlcoholProductSwitch(String alcoholProductSwitch) {
        this.alcoholProductSwitch = alcoholProductSwitch;
    }
    /**
     * @return the genericProductSwitch
     */
    public String isGenericProductSwitch() {
        return genericProductSwitch;
    }
    /**
     * @param genericProductSwitch the genericProductSwitch to set
     */
    public void setGenericProductSwitch(String genericProductSwitch) {
        this.genericProductSwitch = genericProductSwitch;
    }
    /**
     * @return the privateLabelSwitch
     */
    public String isPrivateLabelSwitch() {
        return privateLabelSwitch;
    }
    /**
     * @param privateLabelSwitch the privateLabelSwitch to set
     */
    public void setPrivateLabelSwitch(String privateLabelSwitch) {
        this.privateLabelSwitch = privateLabelSwitch;
    }
    /**
     * @return the scanableItemSwitch
     */
    public String isScanableItemSwitch() {
        return scanableItemSwitch;
    }
    /**
     * @param scanableItemSwitch the scanableItemSwitch to set
     */
    public void setScanableItemSwitch(String scanableItemSwitch) {
        this.scanableItemSwitch = scanableItemSwitch;
    }
    /**
     * @return the scaleSwitch
     */
    public String isScaleSwitch() {
        return scaleSwitch;
    }
    /**
     * @param scaleSwitch the scaleSwitch to set
     */
    public void setScaleSwitch(String scaleSwitch) {
        this.scaleSwitch = scaleSwitch;
    }
    /**
     * @return the healthyLivingSwitch
     */
    public String isHealthyLivingSwitch() {
        return healthyLivingSwitch;
    }
    /**
     * @param healthyLivingSwitch the healthyLivingSwitch to set
     */
    public void setHealthyLivingSwitch(String healthyLivingSwitch) {
        this.healthyLivingSwitch = healthyLivingSwitch;
    }
    /**
     * @return the dsdDeletedSwitch
     */
    public String isDsdDeletedSwitch() {
        return dsdDeletedSwitch;
    }
    /**
     * @param dsdDeletedSwitch the dsdDeletedSwitch to set
     */
    public void setDsdDeletedSwitch(String dsdDeletedSwitch) {
        this.dsdDeletedSwitch = dsdDeletedSwitch;
    }
    /**
     * @return the activeFlag
     */
    public String isActiveFlag() {
        return activeFlag;
    }
    /**
     * @param activeFlag the activeFlag to set
     */
    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }
    /**
     * @return the macEacSwitch
     */
    public String isMacEacSwitch() {
        return macEacSwitch;
    }
    /**
     * @param macEacSwitch the macEacSwitch to set
     */
    public void setMacEacSwitch(String macEacSwitch) {
        this.macEacSwitch = macEacSwitch;
    }
    /**
     * Returns product marketing claims for this candidate product master.
     *
     * @return Product marketing claims for this candidate product master.
     */
    public List<CandidateProductMarketingClaim> getProductMarketingClaims() {
        return productMarketingClaims;
    }

    /**
     * Sets the product marketing claims for this candidate product master.
     *
     * @param productMarketingClaims The product marketing claims for this candidate product master.
     */
    public void setProductMarketingClaims(List<CandidateProductMarketingClaim> productMarketingClaims) {
        this.productMarketingClaims = productMarketingClaims;
    }

    public String isWineProductSwitch() {
        return wineProductSwitch;
    }

    public void setWineProductSwitch(String wineProductSwitch) {
        this.wineProductSwitch = wineProductSwitch;
    }

    public Integer getWineVintageYy() {
        return wineVintageYy;
    }

    public void setWineVintageYy(Integer wineVintageYy) {
        this.wineVintageYy = wineVintageYy;
    }

    public Integer getWineMakerId() {
        return wineMakerId;
    }

    public void setWineMakerId(Integer wineMakerId) {
        this.wineMakerId = wineMakerId;
    }

    public Integer getWineRegionId() {
        return wineRegionId;
    }

    public void setWineRegionId(Integer wineRegionId) {
        this.wineRegionId = wineRegionId;
    }

    public Integer getVarTlId() {
        return varTlId;
    }

    public void setVarTlId(Integer varTlId) {
        this.varTlId = varTlId;
    }

    public Integer getMinInventoryThresholdCount() {
        return minInventoryThresholdCount;
    }

    public void setMinInventoryThresholdCount(Integer minInventoryThresholdCount) {
        this.minInventoryThresholdCount = minInventoryThresholdCount;
    }
    public BigDecimal getAlcoholPercentAmount() {
        return alcoholPercentAmount;
    }

    public void setAlcoholPercentAmount(BigDecimal alcoholPercentAmount) {
        this.alcoholPercentAmount = alcoholPercentAmount;
    }
    /**
     * Sets the candidateItemMaster
     */
    public CandidateItemMaster getCandidateItemMaster() {
        return candidateItemMaster;
    }

    /**
     * @return the candidateDiscountThresholds
     */
    public List<CandidateDiscountThreshold> getCandidateDiscountThresholds() {
        return candidateDiscountThresholds;
    }
    /**
     * @param candidateDiscountThresholds the candidateDiscountThresholds to set
     */
    public void setCandidateDiscountThresholds(List<CandidateDiscountThreshold> candidateDiscountThresholds) {
        this.candidateDiscountThresholds = candidateDiscountThresholds;
    }

    /**
     * Returns wheter or not this product is manufactured by HEB.
     *
     * @return 'Y' of this product is manufactured by HEB, 'N' if not, and ' ' if undefined..
     */
    public String getSelfManufactured() {
        return selfManufactured;
    }

    /**
     * Sets wheter or not this product is manufactured by HEB.
     *
     * @param selfManufactured 'Y' of this product is manufactured by HEB, 'N' if not, and ' ' if undefined..
     */
    public void setSelfManufactured(String selfManufactured) {
        this.selfManufactured = selfManufactured;
    }

    /**
     * Returns the vertex tax category for the record.
     *
     * @return The vertex tax category for the record.
     */
    public List<CandidateFulfillmentChannel> getCandidateFulfillmentChannels() {
        return candidateFulfillmentChannels;
    }
    /**
     * @param candidateFulfillmentChannels the candidateFulfillmentChannels to set
     */
    public void setCandidateFulfillmentChannels(List<CandidateFulfillmentChannel> candidateFulfillmentChannels) {
        this.candidateFulfillmentChannels = candidateFulfillmentChannels;
    }
    public List<CandidateProductScore> getCandidateProductScores() {
        return candidateProductScores;
    }

    public void setCandidateProductScores(List<CandidateProductScore> candidateProductScores) {
        this.candidateProductScores = candidateProductScores;
    }
    public String getBdmCode() {
        return bdmCode;
    }

    public void setBdmCode(String bdmCode) {
        this.bdmCode = bdmCode;
    }
    public Long getProdBrandId() {
        return prodBrandId;
    }

    public void setProdBrandId(Long prodBrandId) {
        this.prodBrandId = prodBrandId;
    }
    public Integer getPssDepartment() {
        return pssDepartment;
    }

    public void setPssDepartment(Integer pssDepartment) {
        this.pssDepartment = pssDepartment;
    }
    public String getTaxQualityCode() {
        return taxQualityCode;
    }

    public void setTaxQualityCode(String taxQualityCode) {
        this.taxQualityCode = taxQualityCode;
    }
    public String getSalesRestrictCode() {
        return salesRestrictCode;
    }

    public void setSalesRestrictCode(String salesRestrictCode) {
        this.salesRestrictCode = salesRestrictCode;
    }
    public LocalDateTime getLastUpdateTs() {
        return lastUpdateTs;
    }

    public void setLastUpdateTs(LocalDateTime lastUpdateTs) {
        this.lastUpdateTs = lastUpdateTs;
    }
    public String getScanDescription() {
        return scanDescription;
    }

    public void setScanDescription(String scanDescription) {
        this.scanDescription = scanDescription;
    }
    public Long getPrimaryScanCode() {
        return primaryScanCode;
    }

    public void setPrimaryScanCode(Long primaryScanCode) {
        this.primaryScanCode = primaryScanCode;
    }
    public Long getActionCode() {
        return actionCode;
    }

    public void setActionCode(Long actionCode) {
        this.actionCode = actionCode;
    }

    public List<CandidateDescription> getCandidateDescriptions() {

        if (this.candidateDescriptions == null) {
            this.candidateDescriptions = new LinkedList<>();
        }
        return candidateDescriptions;
    }

    public void setCandidateDescriptions(List<CandidateDescription> candidateDescriptions) {
        this.candidateDescriptions = candidateDescriptions;
    }
    public String getEnglishDescriptionOne() {
        return englishDescriptionOne;
    }

    public void setEnglishDescriptionOne(String englishDescriptionOne) {
        this.englishDescriptionOne = englishDescriptionOne;
    }

    public String getEnglishDescriptionTwo() {
        return englishDescriptionTwo;
    }

    public void setEnglishDescriptionTwo(String englishDescriptionTwo) {
        this.englishDescriptionTwo = englishDescriptionTwo;
    }
    public String getSpanishDescriptionOne() {
        return spanishDescriptionOne;
    }

    public void setSpanishDescriptionOne(String spanishDescriptionOne) {
        this.spanishDescriptionOne = spanishDescriptionOne;
    }

    public String getSpanishDescriptionTwo() {
        return spanishDescriptionTwo;
    }

    public void setSpanishDescriptionTwo(String spanishDescriptionTwo) {
        this.spanishDescriptionTwo = spanishDescriptionTwo;
    }
    public Integer getGradeNumber() {
        return gradeNumber;
    }

    public void setGradeNumber(Integer gradeNumber) {
        this.gradeNumber = gradeNumber;
    }
    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }
    public Long getGraphicsCode() {
        return graphicsCode;
    }

    public void setGraphicsCode(Long graphicsCode) {
        this.graphicsCode = graphicsCode;
    }
    public Long getIngredientStatement() {
        return ingredientStatement;
    }

    public void setIngredientStatement(Long ingredientStatement) {
        this.ingredientStatement = ingredientStatement;
    }
    public Long getFirstLabelFormat() {
        return firstLabelFormat;
    }

    public void setFirstLabelFormat(Long firstLabelFormat) {
        this.firstLabelFormat = firstLabelFormat;
    }

    public Long getSecondLabelFormat() {
        return secondLabelFormat;
    }

    public void setSecondLabelFormat(Long secondLabelFormat) {
        this.secondLabelFormat = secondLabelFormat;
    }
    public Long getNutrientStatement() {
        return nutrientStatement;
    }

    public void setNutrientStatement(Long nutrientStatement) {
        this.nutrientStatement = nutrientStatement;
    }
    public Double getPrePackTare() {
        return prePackTare;
    }

    public void setPrePackTare(Double prePackTare) {
        this.prePackTare = prePackTare;
    }
    public Double getServiceCounterTare() {
        return serviceCounterTare;
    }

    public void setServiceCounterTare(Double serviceCounterTare) {
        this.serviceCounterTare = serviceCounterTare;
    }
    public Integer getShelfLifeDays() {
        return shelfLifeDays;
    }

    public void setShelfLifeDays(Integer shelfLifeDays) {
        this.shelfLifeDays = shelfLifeDays;
    }
    public List<CandidateSellingUnit> getCandidateSellingUnits() {
        return candidateSellingUnits;
    }

    public void setCandidateSellingUnits(List<CandidateSellingUnit> candidateSellingUnits) {
        this.candidateSellingUnits = candidateSellingUnits;
    }

    public List<CandidateScanCodeExtent> getCandidateScanCodeExtents() {
        return candidateScanCodeExtents;
    }

    public void setCandidateScanCodeExtents(List<CandidateScanCodeExtent> candidateScanCodeExtents) {
        this.candidateScanCodeExtents = candidateScanCodeExtents;
    }
    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public Long getFamily1Code() {
        return family1Code;
    }

    public void setFamily1Code(Long family1Code) {
        this.family1Code = family1Code;
    }

    public Long getFamily2Code() {
        return family2Code;
    }

    public void setFamily2Code(Long family2Code) {
        this.family2Code = family2Code;
    }

    public String getFsaCode() {
        return fsaCode;
    }

    public void setFsaCode(String fsaCode) {
        this.fsaCode = fsaCode;
    }

    public String getVertexTaxCategoryCode() {
        return vertexTaxCategoryCode;
    }

    /**
     * Sets the vertex tax category for the record.
     *
     * @param vertexTaxCategoryCode The vertex tax category for the record.
     */
    public void setVertexTaxCategoryCode(String vertexTaxCategoryCode) {
        this.vertexTaxCategoryCode = vertexTaxCategoryCode;
    }

    public Double getPremarkPriceAmount() {
        return premarkPriceAmount;
    }

    public void setPremarkPriceAmount(Double premarkPriceAmount) {
        this.premarkPriceAmount = premarkPriceAmount;
    }

    public Long getMaxShelfLifeDays() {
        return maxShelfLifeDays;
    }

    public void setMaxShelfLifeDays(Long maxShelfLifeDays) {
        this.maxShelfLifeDays = maxShelfLifeDays;
    }

    public Long getSesnlyId() {
        return sesnlyId;
    }

    public void setSesnlyId(Long sesnlyId) {
        this.sesnlyId = sesnlyId;
    }

    public Long getSeasonalityYear() {
        return seasonalityYear;
    }

    public void setSeasonalityYear(Long seasonalityYear) {
        this.seasonalityYear = seasonalityYear;
    }

    public String getProductModelText() {
        return productModelText;
    }

    public void setProductModelText(String productModelText) {
        this.productModelText = productModelText;
    }

    public Long getT2tId() {
        return t2tId;
    }

    public void setT2tId(Long t2tId) {
        this.t2tId = t2tId;
    }
    public String getPseTypeCode() {
        return pseTypeCode;
    }

    public void setPseTypeCode(String pseTypeCode) {
        this.pseTypeCode = pseTypeCode;
    }

    public String getNdc() {
        return ndc;
    }

    public void setNdc(String ndc) {
        this.ndc = ndc;
    }

    public String getDrugScheduleTypeCode() {
        return drugScheduleTypeCode;
    }

    public void setDrugScheduleTypeCode(String drugScheduleTypeCode) {
        this.drugScheduleTypeCode = drugScheduleTypeCode;
    }
    public String getDrugNameCode() {
        return drugNameCode;
    }

    public void setDrugNameCode(String drugNameCode) {
        this.drugNameCode = drugNameCode;
    }

    public Double getAvgWhlslRxCst() {
        return avgWhlslRxCst;
    }

    public void setAvgWhlslRxCst(Double avgWhlslRxCst) {
        this.avgWhlslRxCst = avgWhlslRxCst;
    }

    public Double getDirectRxCst() {
        return directRxCst;
    }

    public void setDirectRxCst(Double directRxCst) {
        this.directRxCst = directRxCst;
    }

    public String getTobaccoProductTypeCode() {
        return tobaccoProductTypeCode;
    }

    public void setTobaccoProductTypeCode(String tobaccoProductTypeCode) {
        this.tobaccoProductTypeCode = tobaccoProductTypeCode;
    }

    public BigDecimal getCigTaxAmt() {
        return cigTaxAmt;
    }

    public void setCigTaxAmt(BigDecimal cigTaxAmt) {
        this.cigTaxAmt = cigTaxAmt;
    }
    public String getProductTypeCode() {
        return productTypeCode;
    }

    public void setProductTypeCode(String productTypeCode) {
        this.productTypeCode = productTypeCode;
    }

    public Integer getRetailX4Qty() {
        return retailX4Qty;
    }

    public void setRetailX4Qty(Integer retailX4Qty) {
        this.retailX4Qty = retailX4Qty;
    }

    public BigDecimal getRetailPriceAmount() {
        return retailPriceAmount;
    }

    public void setRetailPriceAmount(BigDecimal retailPriceAmount) {
        this.retailPriceAmount = retailPriceAmount;
    }
    public String getCriticalItem() {
        return criticalItem;
    }

    public void setCriticalItem(String criticalItem) {
        this.criticalItem = criticalItem;
    }

    public String getConsumeAlcoholInStore() {
        return consumeAlcoholInStore;
    }

    public void setConsumeAlcoholInStore(String consumeAlcoholInStore) {
        this.consumeAlcoholInStore = consumeAlcoholInStore;
    }

    /**
     * Returns the type of tag to print for this product.
     *
     * @return the type of tag to print for this product.
     */
    public String getTagType() {
        return tagType;
    }

    /**
     * Sets the type of tag to print for this product.
     *
     * @param tagType The type of tag to print for this product.
     */
    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    /**
     * Returns the list of candidate product onlines
     * @return the list of candidate product onlines
     */
    public List<CandidateProductOnline> getCandidateProductOnlines() {
        if (this.candidateProductOnlines == null) {
            this.candidateProductOnlines = new LinkedList<>();
        }
        return this.candidateProductOnlines;
    }

    /**
     * Sets the list of candidate product onlines
     * @param candidateProductOnlines the list of candidate product onlines
     */
    public void setCandidateProductOnlines(List<CandidateProductOnline> candidateProductOnlines) {
        this.candidateProductOnlines = candidateProductOnlines;
    }

    /**
     * Returns the list of candidate master data extension
     * @return the list of candidate master data extension
     */
    public List<CandidateMasterDataExtensionAttribute> getCandidateMasterDataExtensionAttributes() {
        return candidateMasterDataExtensionAttributes;
    }
    /**
     * Sets the list of candidate master data extension
     * @param candidateMasterDataExtensionAttributes the list of candidate master data extension
     */
    public void setCandidateMasterDataExtensionAttributes(List<CandidateMasterDataExtensionAttribute> candidateMasterDataExtensionAttributes) {
        this.candidateMasterDataExtensionAttributes = candidateMasterDataExtensionAttributes;
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
        if (!(o instanceof CandidateProductMaster)) return false;

        CandidateProductMaster that = (CandidateProductMaster) o;

        return !(candidateProductId != null ? !candidateProductId.equals(that.candidateProductId) : that.candidateProductId != null);

    }

    /**
     * Returns the hash code for this object.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        return candidateProductId != null ? candidateProductId.hashCode() : 0;
    }

    /**
     * Returns a printable representation of the object.
     *
     * @return A printable representation of the object.
     */
    @Override
    public String toString() {
        return "CandidateProductMaster{" +
                "candidateProductId=" + candidateProductId +
                ", productId=" + productId +
                ", description='" + description + '\'' +
                ", spanishDescription='" + spanishDescription + '\'' +
                ", classCode=" + classCode +
                ", commodityCode=" + commodityCode +
                ", subCommodityCode=" + subCommodityCode +
                ", departmentCode=" + departmentCode +
                ", subDepartmentCode='" + subDepartmentCode + '\'' +
                ", retailLink=" + retailLink +
                ", showCalories='" + showCalories + '\'' +
                ", workRequestId=" + workRequestId +
                ", selfManufactured='" + selfManufactured + '\'' +
                ", criticalItem='" + criticalItem + '\'' +
                ", consumeAlcoholInStore='" + consumeAlcoholInStore + '\'' +
                ", vertexTaxCategoryCode='" + vertexTaxCategoryCode + '\'' +
                ", candidateWorkRequest=" + candidateWorkRequest +
                ", sellingRestrictionCode=" + sellingRestrictionCode +
                ", productBrand=" + productBrand +
                ", classCommodity=" + classCommodity +
                ", subDepartment=" + subDepartment +
                ", subCommodity=" + subCommodity +
                ", itemClass=" + itemClass +
                ", scaleLabelFormat=" + scaleLabelFormat +
                ", candidateItemMaster=" + candidateItemMaster +
                '}';
    }
}
