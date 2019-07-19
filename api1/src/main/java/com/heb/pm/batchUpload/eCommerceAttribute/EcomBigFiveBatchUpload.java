/*
 * Copyright (c) 2015 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 * @author Phillip McGraw (m314029)
 */


package com.heb.pm.batchUpload.eCommerceAttribute;

import com.heb.pm.batchUpload.BatchUpload;
import com.heb.pm.batchUpload.parser.ProductAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
/**
 *
 */
public class EcomBigFiveBatchUpload extends BatchUpload {

    /**
     * Instantiates a new Excel upload ecom big five vo.
     */
    public EcomBigFiveBatchUpload(){

    }
    private static final long serialVersionUID = 3884054421968532653L;
    /** The column Constant in file upload. */
    public static final int COL_POS_UPC = 0;
    public static final String COL_NM_UPC = "UPC";
    public static final int COL_POS_BRAND = 1;
    public static final String COL_NM_BRAND = "Brand";
    public static final int COL_POS_SIZE = 2;
    public static final String COL_NM_SIZE = "Size";
    public static final int COL_POS_DISPLAY_NAME = 3;
    public static final String COL_NM_DISPLAY_NAME = "Display Name";
    public static final int COL_POS_ROMANCE_COPY = 4;
    public static final String COL_NM_ROMANCE_COPY = "Romance Copy";
    public static final int COL_POS_DIRECTIONS = 5;
    public static final String COL_NM_DIRECTIONS = "Directions";
    public static final int COL_POS_INGREDIENTS = 6;
    public static final String COL_NM_INGREDIENTS = "Ingredients";
    public static final int COL_POS_WARNINGS = 7;
    public static final String COL_NM_WARNINGS = "Warnings";
    public static final int COL_POS_IMAGE_URL = 8;
    public static final String COL_NM_IMAGE_URL = "Image URL";
    public static final int COL_POS_IMAGE_SOURCE = 9;
    public static final String DOMAIN_CODE_I = "I";
    public static final String DOMAIN_CODE_DEC = "DEC";
    public static final String COL_NM_IMAGE_SOURCE = "Image Source";

    public static final Map<Integer,String> uploadFileHeader;
    static {
        uploadFileHeader = new HashMap<>();
        uploadFileHeader.put(COL_POS_UPC, COL_NM_UPC);
        uploadFileHeader.put(COL_POS_BRAND, COL_NM_BRAND);
        uploadFileHeader.put(COL_POS_SIZE, COL_NM_SIZE);
        uploadFileHeader.put(COL_POS_DISPLAY_NAME, COL_NM_DISPLAY_NAME);
        uploadFileHeader.put(COL_POS_ROMANCE_COPY, COL_NM_ROMANCE_COPY);
        uploadFileHeader.put(COL_POS_DIRECTIONS, COL_NM_DIRECTIONS);
        uploadFileHeader.put(COL_POS_INGREDIENTS, COL_NM_INGREDIENTS);
        uploadFileHeader.put(COL_POS_WARNINGS, COL_NM_WARNINGS);
        uploadFileHeader.put(COL_POS_IMAGE_URL, COL_NM_IMAGE_URL);
        uploadFileHeader.put(COL_POS_IMAGE_SOURCE, COL_NM_IMAGE_SOURCE);
    }

    private String upcPlu;
    private String brand;
    private String size;
    private String displayName;
    private String romanceCopy;
    private String directions;
    private String ingredients;
    private String warnings;
    private String imageUrl;
    private String imageSource;
    private List<ProductAttribute> attributeList = new ArrayList<ProductAttribute>();

    public String getUpcPlu() {
        return upcPlu;
    }

    public void setUpcPlu(String upcPlu) {
        this.upcPlu = upcPlu;
    }
    public List<ProductAttribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<ProductAttribute> attributeList) {
        this.attributeList = attributeList;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRomanceCopy() {
        return romanceCopy;
    }

    public void setRomanceCopy(String romanceCopy) {
        this.romanceCopy = romanceCopy;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getWarnings() {
        return warnings;
    }

    public void setWarnings(String warnings) {
        this.warnings = warnings;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }
}
