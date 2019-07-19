package com.heb.scaleMaintenanceSetUp.entity;

import java.io.Serializable;
import java.util.List;

public class StoreDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long storeNum;
	private String storeDesc;
	private List<String> deptIds;
	private String deptId1 = "";
	private String deptId2 = "";
	private String deptId3 = "";
	private String deptId4 = "";
	private String deptId5 = "";
	private String deptId6 = "";
	private String deptId7 = "";
	private String deptId8 = "";
	private String deptId9 = "";
	private String deptId10 = "";
	private String deptId11 = "";
	private String deptId12 = "";
	private String deptId13 = "";
	private String responseMessage;
	private String update;
	private String typCd;
	
	public long getStoreNum() {
		return storeNum;
	}
	public void setStoreNum(long storeNum) {
		this.storeNum = storeNum;
	}
	public String getStoreDesc() {
		return storeDesc;
	}
	public void setStoreDesc(String storeDesc) {
		this.storeDesc = storeDesc;
	}
	public List<String> getDeptIds() {
		return deptIds;
	}
	public void setDeptIds(List<String> deptIds) {
		this.deptIds = deptIds;
	}
	public String getDeptId1() {
		return deptId1;
	}
	public void setDeptId1(String deptId1) {
		this.deptId1 = deptId1;
	}
	public String getDeptId2() {
		return deptId2;
	}
	public void setDeptId2(String deptId2) {
		this.deptId2 = deptId2;
	}
	public String getDeptId3() {
		return deptId3;
	}
	public void setDeptId3(String deptId3) {
		this.deptId3 = deptId3;
	}
	public String getDeptId4() {
		return deptId4;
	}
	public void setDeptId4(String deptId4) {
		this.deptId4 = deptId4;
	}
	public String getDeptId5() {
		return deptId5;
	}
	public void setDeptId5(String deptId5) {
		this.deptId5 = deptId5;
	}
	public String getDeptId6() {
		return deptId6;
	}
	public void setDeptId6(String deptId6) {
		this.deptId6 = deptId6;
	}
	public String getDeptId7() {
		return deptId7;
	}
	public void setDeptId7(String deptId7) {
		this.deptId7 = deptId7;
	}
	public String getDeptId8() {
		return deptId8;
	}
	public void setDeptId8(String deptId8) {
		this.deptId8 = deptId8;
	}
	public String getDeptId9() {
		return deptId9;
	}
	public void setDeptId9(String deptId9) {
		this.deptId9 = deptId9;
	}
	public String getDeptId10() {
		return deptId10;
	}
	public void setDeptId10(String deptId10) {
		this.deptId10 = deptId10;
	}
	public String getDeptId11() {
		return deptId11;
	}
	public void setDeptId11(String deptId11) {
		this.deptId11 = deptId11;
	}
	public String getDeptId12() {
		return deptId12;
	}
	public void setDeptId12(String deptId12) {
		this.deptId12 = deptId12;
	}
	public String getDeptId13() {
		return deptId13;
	}
	public void setDeptId13(String deptId13) {
		this.deptId13 = deptId13;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public String getUpdate() {
		return update;
	}
	public void setUpdate(String update) {
		this.update = update;
	}
	/**
	 * @return the typCd
	 */
	public String getTypCd() {
		return typCd;
	}
	/**
	 * @param typCd the typCd to set
	 */
	public void setTypCd(String typCd) {
		this.typCd = typCd;
	}
	
}
