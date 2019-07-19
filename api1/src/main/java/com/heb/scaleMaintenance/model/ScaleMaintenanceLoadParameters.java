package com.heb.scaleMaintenance.model;

import java.io.Serializable;
import java.util.Date;
import java.util.function.Function;

/**
 * Java object to hold scale maintenance load Parameters
 *
 * @author m314029
 * @since 2.17.8
 */
public class ScaleMaintenanceLoadParameters implements TopLevelModel<ScaleMaintenanceLoadParameters>, Serializable {

	private static final long serialVersionUID = -2806056497053826346L;

	public static final String CURRENT_VERSION = "1.0.0";

	public ScaleMaintenanceLoadParameters(){
		super();
	}
    //Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	public ScaleMaintenanceLoadParameters(String upcs, String stores,String targetStore,String departmentNbr,Date effectiveDate,String effDt){
		this.upcs = upcs;
		this.stores = stores;
		this.targetStore = targetStore;
		this.departmentNbr = departmentNbr;
		this.effectiveDate = effectiveDate;
		this.effDt = effDt;
	}

	private String upcs;
	private String stores;
	private String targetStore;
	private String departmentNbr;
	private Date effectiveDate;
	private String effDt;
	
	//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

	/**
	 * Returns Upcs.
	 *
	 * @return The Upcs.
	 **/
	public String getUpcs() {
		return upcs;
	}

	/**
	 * Sets the Upcs.
	 *
	 * @param upcs The Upcs.
	 **/
	public ScaleMaintenanceLoadParameters setUpcs(String upcs) {
		this.upcs = upcs;
		return this;
	}

	/**
	 * Returns Stores.
	 *
	 * @return The Stores.
	 **/
	public String getStores() {
		return stores;
	}
	
	//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

	/**
	 * @return the targetStore
	 */
	public String getTargetStore() {
		return targetStore;
	}

	/**
	 * @param targetStore the targetStore to set
	 */
	public void setTargetStore(String targetStore) {
		this.targetStore = targetStore;
	}

	/**
	 * @return the departmentNbr
	 */
	public String getDepartmentNbr() {
		return departmentNbr;
	}

	/**
	 * @param departmentNbr the departmentNbr to set
	 */
	public void setDepartmentNbr(String departmentNbr) {
		this.departmentNbr = departmentNbr;
	}

	/**
	 * @return the effectiveDate
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * Sets the Stores.
	 *
	 * @param stores The Stores.
	 **/
	public ScaleMaintenanceLoadParameters setStores(String stores) {
		this.stores = stores;
		return this;
	}

	/**
	 * @return the effDt
	 */
	public String getEffDt() {
		return effDt;
	}

	/**
	 * @param effDt the effDt to set
	 */
	public void setEffDt(String effDt) {
		this.effDt = effDt;
	}

	@Override
	public <R> R map(Function<? super ScaleMaintenanceLoadParameters, ? extends R> mapper) {
		return mapper.apply(this);
	}

	@Override
	public ScaleMaintenanceLoadParameters validate(Function<? super ScaleMaintenanceLoadParameters, ? extends ScaleMaintenanceLoadParameters> validator) {
		return validator.apply(this);
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ScaleMaintenanceLoadParameters{" +
				"upcs='" + upcs + '\'' +
				", stores='" + stores + '\'' +
				", targetStore='" + targetStore + '\'' +
				", departmentNbr='" + departmentNbr + '\'' +
				", effectiveDate='" + effectiveDate + '\'' +
				'}';
	}
	
	//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
}
