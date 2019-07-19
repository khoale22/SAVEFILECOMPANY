/*
 * Body
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.mediaMasterMessage;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * The type Body that goes into a MediaMasterMessage.
 *
 * @author m314029
 * @since 2.4.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Body", propOrder = {
		"menuLabel"
})
public class Body implements Serializable {
	private final static long serialVersionUID = 100L;

	/**
	 * The Menu label.
	 */
	@XmlElement(name = "MENULABEL", required = true, namespace = "http://xmlns.heb.com/ei/MENULABEL")
	protected MenuLabel menuLabel;

	/**
	 * Gets menu label.
	 *
	 * @return the menu label
	 */
	public MenuLabel getMenuLabel() {
		return menuLabel;
	}

	/**
	 * Sets menu label.
	 *
	 * @param menuLabel the menu label
	 */
	public void setMenuLabel(MenuLabel menuLabel) {
		this.menuLabel = menuLabel;
	}
}
