/*
 * ProductScanCodeWicDocument
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */

package com.heb.batch.wic.index;

import com.heb.batch.wic.entity.ProductScanCodeWic;
import com.heb.batch.wic.entity.ProductScanCodeWicKey;
import com.heb.batch.wic.utils.WicConstants;
import com.heb.batch.wic.utils.WicUtil;
import com.heb.batch.wic.webservice.vo.ProductScanCodeWicVO;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * Wraps a ProductScanCodeWic for storage in an index.
 *
 * @author vn55306
 * @since 1.0.0
 */
@Document(indexName = "product-scan-code-wic")
public class ProductScanCodeWicDocument {
	@Id
	private String id;
	@Field(type = FieldType.Long)
	private Long wicAplId;
	@Field(type = FieldType.Long)
	private Long upc;
	@Field(type = FieldType.Long)
	private Long wicCategoryId;
	@Field(type = FieldType.Long)
	private Long wicSubCategoryId;
	@Field(type = FieldType.Text)
	private String lebSwitch;
	@Field(type = FieldType.Text)
	private String wicDescription;
	@Field(type = FieldType.Double)
	private Double wicPackageSize;
    @Field(type = FieldType.Date)
    private Date effDt;
    @Field(type = FieldType.Date)
    private Date endDt;
    @Field(type = FieldType.Text)
    private String wicUntTxt;
    @Field(type = FieldType.Double)
    private Double wicBnFtQty;
    @Field(type = FieldType.Text)
    private String wicBnftUntTxt;
    @Field(type = FieldType.Double)
    private Double wicPrcAmt;
    @Field(type = FieldType.Text)
    private String wicPrcCd;
    @Field(type = FieldType.Text)
    private String wicCrdAcptId;
    @Field(type = FieldType.Date)
    private Date cre8Ts;
    @Field(type = FieldType.Text)
    private String cre8UId;
    @Field(type = FieldType.Integer)
	private Integer upcPluLength;
	@Field(type = FieldType.Integer)
	private Integer purchaseIndicator;
	@Field(type = FieldType.Text)
	private String manualVoucherIndicator;

	@Override
	public String toString() {
		return "ProductScanCodeWicDocument{" +
				"id='" + id + '\'' +
				", wicAplId=" + wicAplId +
				", upc=" + upc +
				", wicCategoryId=" + wicCategoryId +
				", wicSubCategoryId=" + wicSubCategoryId +
				", lebSwitch='" + lebSwitch + '\'' +
				", wicDescription='" + wicDescription + '\'' +
				", wicPackageSize=" + wicPackageSize +
				", effDt=" + effDt +
				", endDt=" + endDt +
				", wicUntTxt='" + wicUntTxt + '\'' +
				", wicBnFtQty=" + wicBnFtQty +
				", wicBnftUntTxt='" + wicBnftUntTxt + '\'' +
				", wicPrcAmt=" + wicPrcAmt +
				", wicPrcCd='" + wicPrcCd + '\'' +
				", wicCrdAcptId='" + wicCrdAcptId + '\'' +
				", cre8Ts=" + cre8Ts +
				", cre8UId='" + cre8UId + '\'' +
				", lstUpdtTs=" + lstUpdtTs +
				", lstUpdtUId='" + lstUpdtUId + '\'' +
				", wicSw='" + wicSw + '\'' +
				", wicCategoryDesc='" + wicCategoryDesc + '\'' +
				", wicSubCategoryDesc='" + wicSubCategoryDesc + '\'' +
				", action='" + action + '\'' +
				'}';
	}

    @Field(type = FieldType.Date)
    private Date lstUpdtTs;
    @Field(type = FieldType.Text)
    private String lstUpdtUId;
	@Field(type = FieldType.Text)
	private String wicSw;
	private String wicCategoryDesc;
	private String wicSubCategoryDesc;
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	private String action;
	/**
	 * Get the ID
	 * 
	 * @return Id
	 */
	public String getId() {
		if(this.id == null)
			this.id = generateId(this.wicAplId, this.upc, this.wicCategoryId, this.wicSubCategoryId);
		return this.id;
	}

	/**
	 * Sets the ID
	 * 
	 * @param id The Id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the ID by WicApprovedProductListId, UPC, WicCategoryId and WicSubCategoryId
	 *
	 * @param wicAplId the WicApprovedProductListId
	 * @param upc the UPC
	 * @param catId the WicCategoryId
	 * @param subCatId the WicSubCategoryId
	 */
	public void setId(Long wicAplId, Long upc, Long catId, Long subCatId) {
		this.id = generateId(wicAplId, upc, catId, subCatId);
	}

	/**
	 * Sets the ID by WicApprovedProductListId, UPC, WicCategoryId and WicSubCategoryId
	 *
	 * @param wicAplId the WicApprovedProductListId
	 * @param upc the UPC
	 * @param catId the WicCategoryId
	 * @param subCatId the WicSubCategoryId
	 */
	public void setId(String wicAplId, String upc, String catId, String subCatId) {
		this.id = generateId(wicAplId, upc, catId, subCatId);
	}

	/**
	 * Generate Id by WicApprovedProductListId, UPC, WicCategoryId and WicSubCategoryId
	 *
	 * @param wicAplId the WicApprovedProductListId
	 * @param upc the UPC
	 * @param catId the WicCategoryId
	 * @param subCatId the WicSubCategoryId
	 * @return the ID
	 */
	public static String generateId(Long wicAplId, Long upc, Long catId, Long subCatId) {
		return String.format("%017d%017d%02d%03d", wicAplId, upc, catId, subCatId);
	}

	/**
	 * Generate Id by WicApprovedProductListId, UPC, WicCategoryId and WicSubCategoryId
	 *
	 * @param wicAplId the WicApprovedProductListId
	 * @param upc the UPC
	 * @param catId the WicCategoryId
	 * @param subCatId the WicSubCategoryId
	 * @return the ID
	 */
	public static String generateId(String wicAplId, String upc, String catId, String subCatId) {
		return String.format("%017d%017d%02d%03d", Long.valueOf(wicAplId), Long.valueOf(upc), Long.valueOf(catId), Long.valueOf(subCatId));
	}

	/**
	 * Returns the WicApprovedProductListId. The approved product list id for the current product.
	 *
	 * @return WicApprovedProductListId wic approved product list id
	 */
	public Long getWicAplId() {
		return wicAplId;
	}

	/**
	 * Sets the WicApprovedProductListId
	 *
	 * @param wicAplId The WicApprovedProductListId
	 */
	public void setWicAplId(Long wicAplId) {
		this.wicAplId = wicAplId;
	}

	/**
	 * Returns the selling unit's UPC. UPC is used generically here. This could be a UPC, PLU, EAN, etc.
	 *
	 * @return The selling unit's UPC. UPC is used generically here. This could be a UPC, PLU, EAN, etc.
	 */
	public Long getUpc() {
		return upc;
	}

	/**
	 * Sets the selling unit's UPC.
	 *
	 * @param upc The selling unit's UPC.
	 */
	public void setUpc(Long upc) {
		this.upc = upc;
	}

	/**
	 * Returns the WicCategoryId
	 *
	 * @return WicCategoryId
	 */
	public Long getWicCategoryId() {
		return wicCategoryId;
	}

	/**
	 * Sets the WicCategoryId
	 *
	 * @param wicCategoryId The WicCategoryId
	 */
	public void setWicCategoryId(Long wicCategoryId) {
		this.wicCategoryId = wicCategoryId;
	}

	/**
	 * Returns the WicSubCategoryId
	 *
	 * @return WicSubCategoryId
	 */
	public Long getWicSubCategoryId() {
		return wicSubCategoryId;
	}

	/**
	 * Sets the WicSubCategoryId
	 *
	 * @param wicSubCategoryId The WicSubCategoryId
	 */
	public void setWicSubCategoryId(Long wicSubCategoryId) {
		this.wicSubCategoryId = wicSubCategoryId;
	}

	/**
	 * Returns the LebSwitch. The LEB switch determines whether or not it is LEB.
	 *
	 * @return LebSwitch
	 */
	public String getLebSwitch() {
		return lebSwitch;
	}

	/**
	 * Sets the LebSwitch
	 *
	 * @param lebSwitch The LebSwitch
	 */
	public void setLebSwitch(String lebSwitch) {
		this.lebSwitch = lebSwitch;
	}

	/**
	 * Returns the WicDescription. The wic description of the current wic.
	 *
	 * @return WicDescription
	 */
	public String getWicDescription() {
		return wicDescription;
	}

	/**
	 * Sets the WicDescription
	 *
	 * @param wicDescription The WicDescription
	 */
	public void setWicDescription(String wicDescription) {
		this.wicDescription = wicDescription;
	}

	/**
	 * Returns the WicPackageSize. The Size of the WIC UPC
	 *
	 * @return WicPackageSize
	 */
	public Double getWicPackageSize() {
		return wicPackageSize;
	}

	/**
	 * Sets the WicPackageSize
	 *
	 * @param wicPackageSize The WicPackageSize
	 */
	public void setWicPackageSize(Double wicPackageSize) {
		this.wicPackageSize = wicPackageSize;
	}

	/**
	 * Returns the Effective Date.
	 *
	 * @return effDt the Effective Date
	 */
    public Date getEffDt() {
        return effDt;
    }

	/**
	 * Sets the Effective Date
	 *
	 * @param effDt The Effective Date
	 */
    public void setEffDt(Date effDt) {
        this.effDt = effDt;
    }

	/**
	 * Returns the End Date.
	 *
	 * @return endDt the End Date
	 */
    public Date getEndDt() {
        return endDt;
    }

	/**
	 * Sets the End Date
	 *
	 * @param endDt The End Date
	 */
    public void setEndDt(Date endDt) {
        this.endDt = endDt;
    }

	/**
	 * Returns the Unit of Measure.
	 *
	 * @return wicUntTxt The Unit of Measure
	 */
    public String getWicUntTxt() {
        return wicUntTxt;
    }

	/**
	 * Sets the Unit of Measure.
	 *
	 * @param wicUntTxt The Unit of Measure
	 */
    public void setWicUntTxt(String wicUntTxt) {
        this.wicUntTxt = wicUntTxt;
    }

	/**
	 * Returns the Benefit Quantity.
	 *
	 * @return wicBnFtQty The Benefit Quantity
	 */
    public Double getWicBnFtQty() {
        return wicBnFtQty;
    }

	/**
	 * Sets the Benefit Quantity.
	 *
	 * @param wicBnFtQty The Benefit Quantity
	 */
    public void setWicBnFtQty(Double wicBnFtQty) {
        this.wicBnFtQty = wicBnFtQty;
    }


    /**
     * @return the CRE8_TS
     */
    public Date getCre8Ts() {
        return cre8Ts;
    }

    /**
     * Sets the CRE8_TS
     *
     * @param cre8Ts the CRE8_TS
     */
    public void setCre8Ts(Date cre8Ts) {
        this.cre8Ts = cre8Ts;
    }

	/**
	 * Returns the Item Price Amount.
	 *
	 * @return wicPrcAmt The Item Price Amount
	 */
	public Double getWicPrcAmt() {
		return wicPrcAmt;
	}

	/**
	 * Sets the Item Price Amount.
	 *
	 * @param wicPrcAmt The Item Price Amount
	 */
	public void setWicPrcAmt(Double wicPrcAmt) {
		this.wicPrcAmt = wicPrcAmt;
	}

    /**
     * @return the CRE8_UID
     */
    public String getCre8UId() {
        return cre8UId;
    }

    /**
     * Sets the CRE8_UID
     *
     * @param cre8UId the CRE8_UID
     */
    public void setCre8UId(String cre8UId) {
        this.cre8UId = cre8UId;
    }

    /**
     * @return the LST_UPDT_TS
     */
    public Date getLstUpdtTs() {
        return lstUpdtTs;
    }

    /**
     * Sets the LST_UPDT_TS
     *
     * @param lstUpdtTs the LST_UPDT_TS
     */
    public void setLstUpdtTs(Date lstUpdtTs) {
        this.lstUpdtTs = lstUpdtTs;
    }

    /**
     * @return the LST_UPDT_UID
     */
    public String getLstUpdtUId() {
        return lstUpdtUId;
    }

    /**
     * Sets the LST_UPDT_UID
     *
     * @param lstUpdtUId the LST_UPDT_UID
     */
    public void setLstUpdtUId(String lstUpdtUId) {
        this.lstUpdtUId = lstUpdtUId;
    }
	/**
	 * @return the wicCategoryDesc
	 */
	public String getWicCategoryDesc() {
		return wicCategoryDesc;
	}
	/**
	 * Sets the wicCategoryDesc
	 *
	 * @param wicCategoryDesc the wicCategoryDesc
	 */
	public void setWicCategoryDesc(String wicCategoryDesc) {
		this.wicCategoryDesc = wicCategoryDesc;
	}

	/**
	 * Returns the Card ID Code.
	 *
	 * @return wicCrdAcptId The Card ID Code
	 */
	public String getWicCrdAcptId() {
		return wicCrdAcptId;
	}

	/**
	 * Sets the Card ID Code.
	 *
	 * @param wicCrdAcptId The Card ID Code
	 */
	public void setWicCrdAcptId(String wicCrdAcptId) {
		this.wicCrdAcptId = wicCrdAcptId;
	}

	/**
	 * @return the wicSubCategoryDesc
	 */
	public String getWicSubCategoryDesc() {
		return wicSubCategoryDesc;
	}
	/**
	 * Sets the wicSubCategoryDesc
	 *
	 * @param wicSubCategoryDesc the wicSubCategoryDesc
	 */
	public void setWicSubCategoryDesc(String wicSubCategoryDesc) {
		this.wicSubCategoryDesc = wicSubCategoryDesc;
	}

	/**
	 * Returns the Price Type.
	 *
	 * @return wicPrcCd The Price Type
	 */
	public String getWicPrcCd() {
		return wicPrcCd;
	}

	/**
	 * Sets the Price Type.
	 *
	 * @param wicPrcCd The Price Type
	 */
	public void setWicPrcCd(String wicPrcCd) {
		this.wicPrcCd = wicPrcCd;
	}

	/**
	 * @return the wicSw
	 */
	public String getWicSw() {
		return wicSw;
	}
	/**
	 * Sets the wicSw
	 *
	 * @param wicSw the wicSw
	 */
	public void setWicSw(String wicSw) {
		this.wicSw = wicSw;
	}

	/**
	 * Returns the Benefit Unit.
	 *
	 * @return wicBnftUntTxt The Benefit Unit
	 */
	public String getWicBnftUntTxt() {
		return wicBnftUntTxt;
	}

	/**
	 * Sets the Benefit Unit
	 *
	 * @param wicBnftUntTxt The Benefit Unit
	 */
	public void setWicBnftUntTxt(String wicBnftUntTxt) {
		this.wicBnftUntTxt = wicBnftUntTxt;
	}

	/**
	 * Get the upcPluLength.
	 *
	 * @return the upcPluLength
	 */
	public Integer getUpcPluLength() {
		return upcPluLength;
	}

	/**
	 * Set the upcPluLength.
	 *
	 * @param upcPluLength the upcPluLength to set
	 */
	public void setUpcPluLength(Integer upcPluLength) {
		this.upcPluLength = upcPluLength;
	}

	/**
	 * Get the purchaseIndicator.
	 *
	 * @return the purchaseIndicator
	 */
	public Integer getPurchaseIndicator() {
		return purchaseIndicator;
	}

	/**
	 * Set the purchaseIndicator.
	 *
	 * @param purchaseIndicator the purchaseIndicator to set
	 */
	public void setPurchaseIndicator(Integer purchaseIndicator) {
		this.purchaseIndicator = purchaseIndicator;
	}

	/**
	 * Get the manualVoucherIndicator.
	 *
	 * @return the manualVoucherIndicator
	 */
	public String getManualVoucherIndicator() {
		return manualVoucherIndicator;
	}

	/**
	 * Set the manualVoucherIndicator.
	 *
	 * @param manualVoucherIndicator the manualVoucherIndicator to set
	 */
	public void setManualVoucherIndicator(String manualVoucherIndicator) {
		this.manualVoucherIndicator = manualVoucherIndicator;
	}

	/**
	 * Set value of fields from the ProductScanCodeWicVO
	 * @param item
	 */
	public void setFieldFromProductScanCodeWicVOs(ProductScanCodeWicVO item) {
		boolean changeWicSw = false;
		this.setId(item.getWicAplId(), item.getScnCdId(), item.getWicCatId(), item.getWicSubCatId());
		this.setWicAplId(item.getWicAplId());
		this.setUpc(item.getScnCdId());
		this.setWicCategoryId(item.getWicCatId());
		this.setWicSubCategoryId(item.getWicSubCatId());
		if(item.getEffDt()!=null) {
			changeWicSw = true;
			this.setEffDt(item.getEffDt());
		}
		if(item.getEndDt()!=null) {
			changeWicSw = true;
			this.setEndDt(item.getEndDt());
		}
		if(item.getWicUntTxt()!=null) {
			this.setWicUntTxt(item.getWicUntTxt());
		}
		if(item.getWicBnftQty()!=null && item.getWicBnftQty()!= -1) {
			this.setWicBnFtQty(item.getWicBnftQty());
		}
		if(item.getWicBnftUntTxt()!=null) {
			this.setWicBnftUntTxt(item.getWicBnftUntTxt());
		}
		this.setFieldFromProductScanCodeWicVOExtends(item);
		if(changeWicSw) {
			this.setWicSw(WicUtil.checkWicSwitch(this) ? WicConstants.YES : WicConstants.NO);
		}
	}
	/**
	 * Set value of fields from the ProductScanCodeWicVO
	 * @param item
	 */
	private void setFieldFromProductScanCodeWicVOExtends(ProductScanCodeWicVO item) {
		if(item.getWicPrcAmt()!=null && item.getWicPrcAmt()!= -1) {
			this.setWicPrcAmt(item.getWicPrcAmt());
		}
		if(item.getWicPrcCd()!=null) {
			this.setWicPrcCd(item.getWicPrcCd());
		}
		if(item.getWicCrdAcptId()!=null) {
			this.setWicCrdAcptId(item.getWicCrdAcptId());
		}
		if(item.getWicProdDes()!=null) {
			this.setWicDescription(item.getWicProdDes());
		}
		if(item.getWicPkgSzQty()!=null && item.getWicPkgSzQty()!= -1) {
			this.setWicPackageSize(item.getWicPkgSzQty());
		}
		if(item.getLebSw()!=null) {
			this.setLebSwitch(item.getLebSw());
		}
		if(item.getCre8Uid()!=null) {
			this.setCre8UId(item.getCre8Uid());
		}
		this.setUpcPluLength(item.getUpcPluLength());
		this.setPurchaseIndicator(item.getPurchaseIndicator());
		if(item.getManualVoucherIndicator()!=null) {
			this.setManualVoucherIndicator(item.getManualVoucherIndicator());
		}
	}
	/**
	 * Set value of fields from the ProductScanCodeWic
	 * @param item
	 */
	public void setFields(ProductScanCodeWic item) {
		ProductScanCodeWicKey key = item.getKey();
		if (key != null) {
			this.setId(key.getWicApprovedProductListId(), key.getUpc(), key.getWicCategoryId(), key.getWicSubCatId());
			this.setWicAplId(key.getWicApprovedProductListId());
			this.setUpc(key.getUpc());
			this.setWicCategoryId(key.getWicCategoryId());
			this.setWicSubCategoryId(key.getWicSubCatId());
		}
		this.setEffDt(item.getEffDt());
		this.setEndDt(item.getEndDt());
		this.setWicUntTxt(item.getWicUntTxt());
		this.setWicBnFtQty(item.getWicBnFtQty());
		this.setWicBnftUntTxt(item.getWicBnftUntTxt());
		this.setWicPrcAmt(item.getWicPrcAmt());
		this.setWicPrcCd(item.getWicPrcCd());
		this.setWicCrdAcptId(item.getWicCrdAcptId());
		this.setWicDescription(item.getWicDescription());
		this.setWicPackageSize(item.getWicPackageSize());
		this.setLebSwitch(item.getLebSwitch());
		this.setCre8UId(item.getCre8UId());
		this.setWicSw(WicUtil.checkWicSwitch(this) ? WicConstants.YES:WicConstants.NO);
		this.setUpcPluLength(item.getUpcPluLength());
		this.setPurchaseIndicator(item.getPurchaseIndicator());
		this.setManualVoucherIndicator(item.getManualVoucherIndicator());
	}
}