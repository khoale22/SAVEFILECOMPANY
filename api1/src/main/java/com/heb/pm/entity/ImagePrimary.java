package com.heb.pm.entity;

/**
 * Object that holds the metadata for an image primary
 * @author vn86116
 * @version 2.13.0
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "IMG_PRTY")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ImagePrimary {

	@Id
	@Column(name = "IMG_PRTY_CD")
	@Type(type = "fixedLengthCharPK")
	private String id;

	@Column(name = "IMG_PRTY_ABB")
	private String imagePrimaryAbb;

	@Column(name = "IMG_PRTY_DES")
	private String imagePrimaryDescription;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImagePrimaryAbb() {
		return imagePrimaryAbb;
	}

	public void setImagePrimaryAbb(String imagePropertyAbb) {
		this.imagePrimaryAbb = imagePropertyAbb;
	}

	public String getImagePrimaryDescription() {
		return imagePrimaryDescription;
	}

	public void setImagePropertyDescription(String imagePrimaryDescription) {
		this.imagePrimaryDescription = imagePrimaryDescription;
	}
}
