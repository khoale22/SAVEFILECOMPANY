package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductScanImageURI.class)
public abstract class ProductScanImageURI_ {

	public static volatile SingularAttribute<ProductScanImageURI, Boolean> activeSwitch;
	public static volatile SingularAttribute<ProductScanImageURI, String> imageFormat;
	public static volatile SingularAttribute<ProductScanImageURI, String> imagePriorityCode;
	public static volatile SingularAttribute<ProductScanImageURI, LocalDateTime> revisedTimeStamp;
	public static volatile SingularAttribute<ProductScanImageURI, String> imageCategoryCode;
	public static volatile SingularAttribute<ProductScanImageURI, SourceSystem> sourceSystem;
	public static volatile SingularAttribute<ProductScanImageURI, String> imglndscpsw;
	public static volatile SingularAttribute<ProductScanImageURI, ImageCategory> imageCategory;
	public static volatile SingularAttribute<ProductScanImageURI, ImageStatus> imageStatus;
	public static volatile SingularAttribute<ProductScanImageURI, String> imageURI;
	public static volatile SingularAttribute<ProductScanImageURI, String> imageTypeCode;
	public static volatile SingularAttribute<ProductScanImageURI, String> imageAccepted;
	public static volatile SingularAttribute<ProductScanImageURI, ImageType> imageType;
	public static volatile SingularAttribute<ProductScanImageURI, ProductScanImageURIKey> key;
	public static volatile SingularAttribute<ProductScanImageURI, String> altTag;
	public static volatile SingularAttribute<ProductScanImageURI, String> revisedByID;
	public static volatile SingularAttribute<ProductScanImageURI, String> imageStatusCode;
	public static volatile ListAttribute<ProductScanImageURI, ProductScanImageBanner> productScanImageBannerList;
	public static volatile SingularAttribute<ProductScanImageURI, Integer> xAxisResolution;
	public static volatile SingularAttribute<ProductScanImageURI, Integer> sourceSystemId;
	public static volatile SingularAttribute<ProductScanImageURI, String> lastModifiedBy;
	public static volatile SingularAttribute<ProductScanImageURI, ImagePriority> imagePriority;
	public static volatile SingularAttribute<ProductScanImageURI, LocalDateTime> lastModifiedOn;
	public static volatile SingularAttribute<ProductScanImageURI, Boolean> activeOnline;
	public static volatile SingularAttribute<ProductScanImageURI, ImageSource> imageSource;
	public static volatile SingularAttribute<ProductScanImageURI, LocalDateTime> createdDate;
	public static volatile SingularAttribute<ProductScanImageURI, String> createdBy;
	public static volatile ListAttribute<ProductScanImageURI, ImageMetaData> imageMetaDataList;
	public static volatile SingularAttribute<ProductScanImageURI, String> imageStatusReason;
	public static volatile SingularAttribute<ProductScanImageURI, Integer> yAxisResolution;
	public static volatile SingularAttribute<ProductScanImageURI, SellingUnit> sellingUnit;
	public static volatile SingularAttribute<ProductScanImageURI, String> applicationSource;

}

