package com.heb.scaleMaintenanceSetUp.model;

import java.io.Serializable;
import java.util.List;

import com.heb.scaleMaintenance.entity.TopLevelEntity;
import com.heb.scaleMaintenanceSetUp.entity.GblParm;

/**
 * @author m314029
 * @since 2.20.0
 */
public class StoreDeptDetails implements  Serializable {

	private static final long serialVersionUID = 8805862154194412474L;

	private long storeNum;
	private List<String> deptids;
	
	public long getStoreNum() {
		return storeNum;
	}
	public void setStoreNum(long storeNum) {
		this.storeNum = storeNum;
	}
	public List<String> getDeptids() {
		return deptids;
	}
	public void setDeptids(List<String> deptids) {
		this.deptids = deptids;
	}
	
	@Override
	public String toString() {
		return "EPlumMessage [storeNum=" + storeNum + ", deptids=" + deptids + "]";
	}
}
