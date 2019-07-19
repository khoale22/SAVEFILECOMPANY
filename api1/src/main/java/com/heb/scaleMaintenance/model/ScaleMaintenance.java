package com.heb.scaleMaintenance.model;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetail;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceUpc;

import java.io.Serializable;

/**
 * Represents a scale maintenance object containing product information (ScaleMaintenanceUpc) and authorization/retail
 * information (ScaleMaintenanceAuthorizeRetail). This is used to group these two separate pieces of information into
 * one object.
 *
 * @author m314029
 * @since 2.17.8
 */
public class ScaleMaintenance implements Serializable {

	private static final long serialVersionUID = 1L;

	private ScaleMaintenanceUpc scaleMaintenanceUpc;

	private ScaleMaintenanceAuthorizeRetail authorizeRetail;

	/**
	 * Returns ScaleMaintenanceUpc.
	 *
	 * @return The ScaleMaintenanceUpc.
	 **/
	public ScaleMaintenanceUpc getScaleMaintenanceUpc() {
		return scaleMaintenanceUpc;
	}

	/**
	 * Sets the ScaleMaintenanceUpc.
	 *
	 * @param scaleMaintenanceUpc The ScaleMaintenanceUpc.
	 **/
	public ScaleMaintenance setScaleMaintenanceUpc(ScaleMaintenanceUpc scaleMaintenanceUpc) {
		this.scaleMaintenanceUpc = scaleMaintenanceUpc;
		return this;
	}

	/**
	 * Returns AuthorizeRetail.
	 *
	 * @return The AuthorizeRetail.
	 **/
	public ScaleMaintenanceAuthorizeRetail getAuthorizeRetail() {
		return authorizeRetail;
	}

	/**
	 * Sets the AuthorizeRetail.
	 *
	 * @param authorizeRetail The AuthorizeRetail.
	 **/
	public ScaleMaintenance setAuthorizeRetail(ScaleMaintenanceAuthorizeRetail authorizeRetail) {
		this.authorizeRetail = authorizeRetail;
		return this;
	}
}
