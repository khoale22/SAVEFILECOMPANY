package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ImageMetaData.class)
public abstract class ImageMetaData_ {

	public static volatile SingularAttribute<ImageMetaData, String> imageFormatCode;
	public static volatile SingularAttribute<ImageMetaData, String> imageAltText;
	public static volatile SingularAttribute<ImageMetaData, String> imageAceptable;
	public static volatile SingularAttribute<ImageMetaData, String> imagePriorityCode;
	public static volatile SingularAttribute<ImageMetaData, String> imageStatusCode;
	public static volatile SingularAttribute<ImageMetaData, String> imageCategoryCode;
	public static volatile SingularAttribute<ImageMetaData, CustomerProductGroup> customerProductGroup;
	public static volatile SingularAttribute<ImageMetaData, LocalDateTime> lastUpdateDate;
	public static volatile SingularAttribute<ImageMetaData, String> lastUpdateUserId;
	public static volatile SingularAttribute<ImageMetaData, Long> sourceSystemId;
	public static volatile SingularAttribute<ImageMetaData, SourceSystem> sourceSystem;
	public static volatile SingularAttribute<ImageMetaData, ImagePriority> imagePriority;
	public static volatile SingularAttribute<ImageMetaData, String> uriText;
	public static volatile SingularAttribute<ImageMetaData, Boolean> active;
	public static volatile SingularAttribute<ImageMetaData, ImageCategory> imageCategory;
	public static volatile SingularAttribute<ImageMetaData, ImageStatus> imageStatus;
	public static volatile SingularAttribute<ImageMetaData, String> imageSizeY;
	public static volatile SingularAttribute<ImageMetaData, String> imageSizeX;
	public static volatile SingularAttribute<ImageMetaData, Boolean> activeOnline;
	public static volatile SingularAttribute<ImageMetaData, String> imageSourceName;
	public static volatile SingularAttribute<ImageMetaData, String> imageTypeCode;
	public static volatile SingularAttribute<ImageMetaData, ImageMetaDataKey> key;
	public static volatile SingularAttribute<ImageMetaData, GenericEntity> entity;
	public static volatile SingularAttribute<ImageMetaData, LocalDateTime> createDate;

}

