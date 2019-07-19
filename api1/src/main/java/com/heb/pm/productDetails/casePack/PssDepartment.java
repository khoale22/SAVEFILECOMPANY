/*
 *  PssDepartment
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.casePack;

import com.heb.pm.entity.PssDepartmentCode;
import com.heb.pm.entity.MerchandiseType;
import com.heb.pm.entity.SubDepartment;

/**
 * This represents a pss department. A pss department is a substitue sub department. If a store sells flowers but
 * doesn't have a floral department then a pss department would come into effect. There can be up to 4 pss departments
 * because that is how much OMI limited it by.
 *
 * @author l730832
 * @since 2.8.0
 */
public class PssDepartment {

	/**
	 * The information for the substitute sub department(pss department).
	 */
	private SubDepartment subDepartment;

	/**
	 * The pss department code holds the codes and descriptions for pss departments. It is essentially the code table for pss department along
	 * with various other things.
	 */
	private PssDepartmentCode pssDepartmentCode;

	/**
	 * The merchandise type of the pss department.
	 */
	private MerchandiseType merchandiseType;

	/**
	 * The item id.
	 */
	private String itmId;

	/**
	 * The item type code.
	 */
	private String itmTypCd;

	/**
	 * The index of department.
	 */
	private int index;

	/**
	 * Returns the SubDepartment. The information for the substitute sub department(pss department).
	 *
	 * @return SubDepartment
	 */
	public SubDepartment getSubDepartment() {
		return subDepartment;
	}

	/**
	 * Sets the SubDepartment
	 *
	 * @param subDepartment The SubDepartment
	 */
	public void setSubDepartment(SubDepartment subDepartment) {
		this.subDepartment = subDepartment;
	}

	/**
	 * Returns the PssDepartmentCode. The PssDepartmentCode holds the codes for pss departments. It is essentially the code table
	 * for pss department along with various other things.
	 *
	 * @return PssDepartmentCode
	 */
	public PssDepartmentCode getPssDepartmentCode() {
		return pssDepartmentCode;
	}

	/**
	 * Sets the PssDepartmentCode
	 *
	 * @param pssDepartmentCode The PssDepartmentCode
	 */
	public void setPssDepartmentCode(PssDepartmentCode pssDepartmentCode) {
		this.pssDepartmentCode = pssDepartmentCode;
	}

	/**
	 * Returns the MerchandiseType. The merchandise type of the pss department.
	 *
	 * @return MerchandiseType
	 */
	public MerchandiseType getMerchandiseType() {
		return merchandiseType;
	}

	/**
	 * Sets the MerchandiseType
	 *
	 * @param merchandiseType The MerchandiseType
	 */
	public void setMerchandiseType(MerchandiseType merchandiseType) {
		this.merchandiseType = merchandiseType;
	}

	/**
	 * Sets the itmId
	 */
	public String getItmId() {
		return itmId;
	}

	/**
	 * @return Gets the value of itmId and returns itmId
	 */
	public void setItmId(String itmId) {
		this.itmId = itmId;
	}

	/**
	 * Sets the itmTypCd
	 */
	public String getItmTypCd() {
		return itmTypCd;
	}

	/**
	 * @return Gets the value of itmTypCd and returns itmTypCd
	 */
	public void setItmTypCd(String itmTypCd) {
		this.itmTypCd = itmTypCd;
	}

	/**
	 * Sets the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return Gets the value of index and returns index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Compares another object to this one. This is a deep compare.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PssDepartment that = (PssDepartment) o;

		if (subDepartment != null ? !subDepartment.equals(that.subDepartment) : that.subDepartment != null)
			return false;
		return pssDepartmentCode != null ? pssDepartmentCode.equals(that.pssDepartmentCode) : that.pssDepartmentCode == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = subDepartment != null ? subDepartment.hashCode() : 0;
		result = 31 * result + (pssDepartmentCode != null ? pssDepartmentCode.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "PssDepartment{" +
				"subDepartment=" + subDepartment +
				", pssDepartmentCode=" + pssDepartmentCode +
				'}';
	}
}
