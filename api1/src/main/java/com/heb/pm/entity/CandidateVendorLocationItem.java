/*
 * CandidateVendorLocationItem
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a candidate vendor location item.
 *
 * @author vn70516
 * @since 2.12.0
 */
@Entity
@Table(name = "ps_vend_loc_itm")
@TypeDefs({
        @TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class CandidateVendorLocationItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String DISPLAY_NAME_FORMAT = "%s [%d]";
    public static final String INCO_TRM_CD_FOB ="FOB";
    public static final String VEND_LOC_TYP_CD_WHS = "V";
    public static final String VEND_LOC_TYP_CD_DSD = "D";
    public static final String DEFAULT_EMPTY_STRING = " ";
    public static final String SW_Y = "Y";
    @EmbeddedId
    private CandidateVendorLocationItemKey key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ps_itm_id", nullable = false, insertable = false, updatable = false)
    private CandidateItemMaster candidateItemMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "vend_loc_typ_cd", referencedColumnName = "loc_typ_cd", insertable = false, updatable = false, nullable = false),
            @JoinColumn(name = "vend_loc_nbr", referencedColumnName = "loc_nbr", insertable = false, updatable = false, nullable = false)
    })
    private Location location;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "vend_loc_nbr", referencedColumnName = "loc_nbr", insertable = false, updatable = false, nullable = false),
            @JoinColumn(name = "vend_loc_typ_cd", referencedColumnName = "loc_typ_cd", insertable = false, updatable = false, nullable = false)
    })
    @NotFound(action = NotFoundAction.IGNORE)
    private VendorLocation vendorLocation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "vend_loc_nbr", referencedColumnName = "loc_nbr", insertable = false, updatable = false, nullable = false),
            @JoinColumn(name = "vend_loc_typ_cd", referencedColumnName = "loc_typ_cd", insertable = false, updatable = false, nullable = false)
    })
    @NotFound(action = NotFoundAction.IGNORE)
    private DsdLocation dsdLocation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "sca_cd", referencedColumnName = "sca_cd", insertable = false, updatable = false, nullable = false)
    })
    private Sca sca;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "cst_own_id", referencedColumnName = "cst_own_id", insertable = false, updatable = false, nullable = false)
    })
    private CostOwner costOwner;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "CNTRY_ORIG_ID", referencedColumnName = "cntry_id", insertable = false, updatable = false, nullable = false)
    })
    private Country country;

    @Column(name = "vend_pal_tie")
    private Integer tie;

    @Column(name = "vend_pal_tier")
    private Integer tier;

    @Column(name = "vend_pal_qty")
    private Integer palletQuantity;

    @Column(name = "vend_pal_sz")
    private String palletSize;

    @Column(name = "vend_list_cst")
    private Double listCost;

    @Column(name = "ord_qty_rstr_cd")
    @Type(type = "fixedLengthChar")
    private String orderQuantityRestrictionCode;

    @Column(name = "sca_cd")
    @Type(type = "fixedLengthChar")
    private String scaCode;

    @Column(name = "cst_lnk_id")
    private Integer costLinkId;

    @Column(name = "vend_itm_id")
    @Type(type = "fixedLengthChar")
    private String vendItemId;

    @Column(name = "new_data_sw")
    private boolean newData;
    /**
     * The t2t Id.
     */
    @Column(name = "T2T_ID")
    private Long top2topId;

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
     * The cst own id id.
     */
    @Column(name = "CST_OWN_ID")
    private Integer costOwnerId;
    /**
     * The cntry orig id.
     */
    @Column(name = "CNTRY_ORIG_ID")
    private Integer countryOfOriginId;

    /**
     * The dept nbr.
     */
    @Column(name = "STR_DEPT_NBR", length = 5)
    private String deptNbr;

    /**
     * The sub dept key.
     */
    @Column(name = "STR_SUB_DEPT_ID", length = 5)
    private String subDeptId;

    /**
     * The agent comsn pct.
     */
    @Column(name = "AGENT_COMSN_PCT", precision = 5)
    private Double agentCommissionPercent;
    /**
     * The color.
     */
    @Column(name = "COLOR_DES", length = 50)
    private String color;

    /**
     * The cntan sz cd.
     */
    @Column(name = "CNTAN_SZ_CD", length = 7)
    private String containerSizeCode;
    /**
     * The import sw.
     */
    @Column(name = "IMPORT_SW")
    private boolean importSwitch;
    /**
     * The crtn mrkng txt.
     */
    @Column(name = "CRTN_MRKNG_TXT", length = 30)
    private String cartonMarking;
    /**
     * The duty cnfrm txt.
     */
    @Column(name = "DUTY_CNFRM_TXT", length = 20)
    private String dutyConfirmationText;
    /**
     * The duty info txt.
     */
    @Column(name = "DUTY_INFO_TXT", length = 20)
    private String dutyInfoText;
    /**
     * The duty rt pct.
     */
    @Column(name = "DUTY_RT_PCT", precision = 9, scale = 4)
    private Double dutyPercent;

    /**
     * The frt cnfrm txt.
     */
    @Column(name = "FRT_CNFRM_TXT", length = 20)
    private String freightConfirmationText;
    /**
     * The hts nbr.
     */
    @Column(name = "HTS_NBR", precision = 10, scale = 0)
    private Long htsNumber;

    /**
     * The hts2 nbr.
     */
    @Column(name = "HTS2_NBR", precision = 10, scale = 0)
    private Long hts2Number;

    /**
     * The hts3 nbr.
     */
    @Column(name = "HTS3_NBR", precision = 10, scale = 0)
    private Long hts3Number;
    /**
     * The inco trm cd.
     */
    @Column(name = "INCO_TRM_CD", length = 3)
    private String incoTermCode;

    /**
     * The in str dt.
     */
    @Column(name = "IN_STR_DT", length = 0)
    private LocalDate inStoreDate;
    /**
     * The min typ txt.
     */
    @Column(name = "MIN_TYP_TXT", length = 20)
    private String minTypeText;
    /**
     * The min qty.
     */
    @Column(name = "MIN_QTY")
    private Long minOrderQuantity;
    /**
     * The pckup pnt txt.
     */
    @Column(name = "PCKUP_PNT_TXT", length = 20)
    private String pickupPoint;
    /**
     * The pror dt.
     */
    @Column(name = "PROR_DT", length = 0)
    private LocalDate prorationDate;

    /**
     * The seasntxt.
     */
    @Column(name = "SEASN_TXT", length = 20)
    private String season;
    /**
     * The sellyy.
     */
    @Column(name = "SELL_YY")
    private Integer sellByYear;

    /**
     * The whse flsh dt.
     */
    @Column(name = "WHSE_FLSH_DT", length = 0)
    private LocalDate warehouseFlushDate;
    /**
     * The lst updt usr key.
     */
    @Column(name = "LST_UPDT_USR_ID", nullable = false, length = 8)
    private String lastUpdateUserId;

    /**
     * The lst updt ts.
     */
    @Column(name = "LST_UPDT_TS", nullable = false, length = 10)
    private LocalDateTime lastUpdatedate;
    /**
     * The samp provide sw.
     */
    @Column(name = "SAMP_PROVIDE_SW")
    @Type(type = "fixedLengthCharPK")
    private String sampProvideSwitch;
    /**
     * The guarnt sale sw.
     */
    @Column(name = "GUARNT_SALE_SW")
    private boolean guarntSaleSwitch;
    /**
     * The authd sw.
     */
    @Column(name = "AUTHD_SW")
    private boolean authdSwitch;

    /**
     * The expct wkly mvt.
     */
    @Column(name = "EXPCT_WKLY_MVT")
    private Long expctWklyMvt;
    @Transient
    private Double nestCube;

    @Transient
    private Integer nestMax;

    @Transient
    private Double length;

    @Transient
    private Double width;

    @Transient
    private Double height;

    @Transient
    private Double weight;

    @Transient
    private Double cube;

    @JsonIgnoreProperties("candidateVendorLocationItem")
    @OneToMany(mappedBy = "candidateVendorLocationItem",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<CandidateVendorItemStore> candidateVendorItemStores;

    @JsonIgnoreProperties("candidateVendorLocationItem")
    @OneToMany(mappedBy = "candidateVendorLocationItem",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<CandidateVendorItemFactory> candidateVendorItemFactorys;


    @JsonIgnoreProperties("candidateVendorLocationItem")
    @OneToMany(mappedBy = "candidateVendorLocationItem",fetch = FetchType.LAZY)
    private List<CandidateItemWarehouseVendor> candidateItemWarehouseVendors;
    /**
     * Returns the class as it should be displayed on the GUI.
     *
     * @return A String representation of the class as it is meant to be displayed on the GUI.
     */
    public String getDisplayName() {
        return String.format(CandidateVendorLocationItem.DISPLAY_NAME_FORMAT, this.getLocation().getLocationName().trim(),
                this.getKey().getVendorNumber());
    }
    /**
     * Gets the candidate vendor location item key.
     *
     * @return the vendor location item key
     */
    public CandidateVendorLocationItemKey getKey() {
        return key;
    }

    /**
     * Sets the candidate vendor location item key.
     *
     * @param key the vendor location item key
     */
    public void setKey(CandidateVendorLocationItemKey key) {
        this.key = key;
    }

    /**
     * Gets the location.
     *
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the location.
     *
     * @param location the location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Sets the vendorLocation
     */
    public VendorLocation getVendorLocation() {
        return vendorLocation;
    }

    /**
     * @return Gets the value of vendorLocation and returns vendorLocation
     */
    public void setVendorLocation(VendorLocation vendorLocation) {
        this.vendorLocation = vendorLocation;
    }

    /**
     * Sets the dsdLocation
     */
    public DsdLocation getDsdLocation() {
        return dsdLocation;
    }

    /**
     * @return Gets the value of dsdLocation and returns dsdLocation
     */
    public void setDsdLocation(DsdLocation dsdLocation) {
        this.dsdLocation = dsdLocation;
    }

    /**
     * Returns the Tie
     *
     * @return Tie
     */
    public Integer getTie() {
        return tie;
    }

    /**
     * Sets the Tie
     *
     * @param tie The Tie
     */

    public void setTie(Integer tie) {
        this.tie = tie;
    }

    /**
     * Returns the Tier
     *
     * @return Tier
     */
    public Integer getTier() {
        return tier;
    }

    /**
     * Sets the Tier
     *
     * @param tier The Tier
     */

    public void setTier(Integer tier) {
        this.tier = tier;
    }

    /**
     * Returns the PalletQuantity
     *
     * @return PalletQuantity
     */
    public Integer getPalletQuantity() {
        return palletQuantity;
    }

    /**
     * Sets the PalletQuantity
     *
     * @param palletQuantity The PalletQuantity
     */

    public void setPalletQuantity(Integer palletQuantity) {
        this.palletQuantity = palletQuantity;
    }

    /**
     * Returns the PalletSize
     *
     * @return PalletSize
     */
    public String getPalletSize() {
        return palletSize;
    }

    /**
     * Sets the PalletSize
     *
     * @param palletSize The PalletSize
     */

    public void setPalletSize(String palletSize) {
        this.palletSize = palletSize;
    }

    /**
     * Returns the ListCost
     *
     * @return ListCost
     */
    public Double getListCost() {
        return listCost;
    }

    /**
     * Sets the ListCost
     *
     * @param listCost The ListCost
     */

    public void setListCost(Double listCost) {
        this.listCost = listCost;
    }

    /**
     * Returns the OrderQuantityRestrictionCode
     *
     * @return OrderQuantityRestrictionCode
     */
    public String getOrderQuantityRestrictionCode() {
        return orderQuantityRestrictionCode;
    }

    /**
     * Sets the OrderQuantityRestrictionCode
     *
     * @param orderQuantityRestrictionCode The OrderQuantityRestrictionCode
     */

    public void setOrderQuantityRestrictionCode(String orderQuantityRestrictionCode) {
        this.orderQuantityRestrictionCode = orderQuantityRestrictionCode;
    }

    /**
     * Returns the ScaCode
     *
     * @return ScaCode
     */
    public String getScaCode() {
        return scaCode;
    }

    /**
     * Sets the ScaCode
     *
     * @param scaCode The ScaCode
     */

    public void setScaCode(String scaCode) {
        this.scaCode = scaCode;
    }

    /**
     * Returns the CostLinkId
     *
     * @return CostLinkId
     */
    public Integer getCostLinkId() {
        return costLinkId;
    }

    /**
     * Sets the CostLinkId
     *
     * @param costLinkId The CostLinkId
     */

    public void setCostLinkId(Integer costLinkId) {
        this.costLinkId = costLinkId;
    }

    /**
     * Returns the VendItemId
     *
     * @return VendItemId
     */
    public String getVendItemId() {
        return vendItemId;
    }

    /**
     * Sets the VendItemId
     *
     * @param vendItemId The VendItemId
     */

    public void setVendItemId(String vendItemId) {
        this.vendItemId = vendItemId;
    }

    /**
     * Get display name string.
     *
     * @return the string
     */
    public String getDisplayBicepVendorName() {

        return String.format(CandidateVendorLocationItem.DISPLAY_NAME_FORMAT, this.getLocation().getLocationName().trim(),
                this.key.getVendorNumber());
    }

    /**
     * Get display name string.
     *
     * @return the string
     */
    public String getDisplayApVendorName() {
        return String.format(CandidateVendorLocationItem.DISPLAY_NAME_FORMAT, this.getLocation().getLocationName().trim(),
                this.getLocation().getApVendorNumber());
    }

    /**
     * Returns the Sca
     *
     * @return Sca
     **/
    public Sca getSca() {
        return sca;
    }

    /**
     * Sets the Sca
     *
     * @param sca The Sca
     **/

    public void setSca(Sca sca) {
        this.sca = sca;
    }

    /**
     * Returns the CostOwner
     *
     * @return CostOwner
     **/
    public CostOwner getCostOwner() {
        return costOwner;
    }

    /**
     * Sets the CostOwner
     *
     * @param costOwner The CostOwner
     **/

    public void setCostOwner(CostOwner costOwner) {
        this.costOwner = costOwner;
    }

    /**
     * Returns the Country
     *
     * @return Country
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Sets the Country
     *
     * @param country The Country
     */

    public void setCountry(Country country) {
        this.country = country;
    }

    /**
     * Sets the newData
     */
    public boolean isNewData() {
        return newData;
    }
    public Long getTop2topId() {
        return top2topId;
    }

    public void setTop2topId(Long top2topId) {
        this.top2topId = top2topId;
    }

    /**
     * @return Gets the value of newData and returns newData
     */
    public void setNewData(boolean newData) {
        this.newData = newData;
    }

    /**
     * @return Gets the value of candidateItemMaster and returns candidateItemMaster
     */
    public void setCandidateItemMaster(CandidateItemMaster candidateItemMaster) {
        this.candidateItemMaster = candidateItemMaster;
    }

    /**
     * Sets the candidateItemMaster
     */
    public CandidateItemMaster getCandidateItemMaster() {
        return candidateItemMaster;
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
    public Integer getCostOwnerId() {
        return costOwnerId;
    }

    public void setCostOwnerId(Integer costOwnerId) {
        this.costOwnerId = costOwnerId;
    }

    public Integer getCountryOfOriginId() {
        return countryOfOriginId;
    }

    public void setCountryOfOriginId(Integer countryOfOriginId) {
        this.countryOfOriginId = countryOfOriginId;
    }
    public String getDeptNbr() {
        return deptNbr;
    }

    public void setDeptNbr(String deptNbr) {
        this.deptNbr = deptNbr;
    }

    public String getSubDeptId() {
        return subDeptId;
    }

    public void setSubDeptId(String subDeptId) {
        this.subDeptId = subDeptId;
    }

    public List<CandidateVendorItemStore> getCandidateVendorItemStores() {
        return candidateVendorItemStores;
    }

    public void setCandidateVendorItemStores(List<CandidateVendorItemStore> candidateVendorItemStores) {
        this.candidateVendorItemStores = candidateVendorItemStores;
    }
    public List<CandidateVendorItemFactory> getCandidateVendorItemFactorys() {
        return candidateVendorItemFactorys;
    }

    public void setCandidateVendorItemFactorys(List<CandidateVendorItemFactory> candidateVendorItemFactorys) {
        this.candidateVendorItemFactorys = candidateVendorItemFactorys;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getContainerSizeCode() {
        return containerSizeCode;
    }

    public void setContainerSizeCode(String containerSizeCode) {
        this.containerSizeCode = containerSizeCode;
    }

    public boolean isImportSwitch() {
        return importSwitch;
    }

    public void setImportSwitch(boolean importSwitch) {
        this.importSwitch = importSwitch;
    }

    public String getCartonMarking() {
        return cartonMarking;
    }

    public void setCartonMarking(String cartonMarking) {
        this.cartonMarking = cartonMarking;
    }

    public String getDutyConfirmationText() {
        return dutyConfirmationText;
    }

    public void setDutyConfirmationText(String dutyConfirmationText) {
        this.dutyConfirmationText = dutyConfirmationText;
    }

    public String getDutyInfoText() {
        return dutyInfoText;
    }

    public void setDutyInfoText(String dutyInfoText) {
        this.dutyInfoText = dutyInfoText;
    }

    public Double getDutyPercent() {
        return dutyPercent;
    }

    public void setDutyPercent(Double dutyPercent) {
        this.dutyPercent = dutyPercent;
    }

    public Long getHtsNumber() {
        return htsNumber;
    }

    public void setHtsNumber(Long htsNumber) {
        this.htsNumber = htsNumber;
    }

    public Long getHts2Number() {
        return hts2Number;
    }

    public void setHts2Number(Long hts2Number) {
        this.hts2Number = hts2Number;
    }

    public Long getHts3Number() {
        return hts3Number;
    }

    public void setHts3Number(Long hts3Number) {
        this.hts3Number = hts3Number;
    }

    public String getIncoTermCode() {
        return incoTermCode;
    }

    public void setIncoTermCode(String incoTermCode) {
        this.incoTermCode = incoTermCode;
    }

    public LocalDate getInStoreDate() {
        return inStoreDate;
    }

    public void setInStoreDate(LocalDate inStoreDate) {
        this.inStoreDate = inStoreDate;
    }

    public String getMinTypeText() {
        return minTypeText;
    }

    public void setMinTypeText(String minTypeText) {
        this.minTypeText = minTypeText;
    }

    public Long getMinOrderQuantity() {
        return minOrderQuantity;
    }

    public void setMinOrderQuantity(Long minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }

    public String getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(String pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public LocalDate getProrationDate() {
        return prorationDate;
    }

    public void setProrationDate(LocalDate prorationDate) {
        this.prorationDate = prorationDate;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public Integer getSellByYear() {
        return sellByYear;
    }

    public void setSellByYear(Integer sellByYear) {
        this.sellByYear = sellByYear;
    }

    public LocalDate getWarehouseFlushDate() {
        return warehouseFlushDate;
    }

    public void setWarehouseFlushDate(LocalDate warehouseFlushDate) {
        this.warehouseFlushDate = warehouseFlushDate;
    }

    public String getLastUpdateUserId() {
        return lastUpdateUserId;
    }

    public void setLastUpdateUserId(String lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }

    public LocalDateTime getLastUpdatedate() {
        return lastUpdatedate;
    }

    public void setLastUpdatedate(LocalDateTime lastUpdatedate) {
        this.lastUpdatedate = lastUpdatedate;
    }
    public Double getAgentCommissionPercent() {
        return agentCommissionPercent;
    }

    public void setAgentCommissionPercent(Double agentCommissionPercent) {
        this.agentCommissionPercent = agentCommissionPercent;
    }

    public String getFreightConfirmationText() {
        return freightConfirmationText;
    }

    public void setFreightConfirmationText(String freightConfirmationText) {
        this.freightConfirmationText = freightConfirmationText;
    }

    public String getSampProvideSwitch() {
        return sampProvideSwitch;
    }

    public void setSampProvideSwitch(String sampProvideSwitch) {
        this.sampProvideSwitch = sampProvideSwitch;
    }

    public boolean isGuarntSaleSwitch() {
        return guarntSaleSwitch;
    }

    public void setGuarntSaleSwitch(boolean guarntSaleSwitch) {
        this.guarntSaleSwitch = guarntSaleSwitch;
    }
    public boolean isAuthdSwitch() {
        return authdSwitch;
    }

    public void setAuthdSwitch(boolean authdSwitch) {
        this.authdSwitch = authdSwitch;
    }


    public Long getExpctWklyMvt() {
        return expctWklyMvt;
    }

    public void setExpctWklyMvt(Long expctWklyMvt) {
        this.expctWklyMvt = expctWklyMvt;
    }
    public List<CandidateItemWarehouseVendor> getCandidateItemWarehouseVendors() {
        return candidateItemWarehouseVendors;
    }

    public void setCandidateItemWarehouseVendors(List<CandidateItemWarehouseVendor> candidateItemWarehouseVendors) {
        this.candidateItemWarehouseVendors = candidateItemWarehouseVendors;
    }
    public Double getNestCube() {
        return nestCube;
    }

    public void setNestCube(Double nestCube) {
        this.nestCube = nestCube;
    }

    public Integer getNestMax() {
        return nestMax;
    }

    public void setNestMax(Integer nestMax) {
        this.nestMax = nestMax;
    }
    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getCube() {
        return cube;
    }

    public void setCube(Double cube) {
        this.cube = cube;
    }
    /**
     * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
     * it is inserted into the work request table.
     */
    @PrePersist
    public void setCandidateItemId() {
        if (this.getKey().getCandidateItemId() == null) {
            this.getKey().setCandidateItemId(this.candidateItemMaster.getCandidateItemId());
        }
    }
}
