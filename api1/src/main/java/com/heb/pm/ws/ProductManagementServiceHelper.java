/*
 * ProductManagementServiceHelper
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB
 */
package com.heb.pm.ws;

import com.heb.pm.entity.ItemWarehouseComments;
import com.heb.xmlns.ei.whselocitm.WhseLocItm;

import com.heb.pm.entity.ItemWarehouseComments;
import com.heb.xmlns.ei.whselocitm.WhseLocItm;

import java.util.List;

/**
 * Create all request for Product Management Service.
 *
 * @author vn70633
 * @since 2.7.0
 */
class ProductManagementServiceHelper {

    private static final String ACTION_DELETE = "D";
    private static final String ACTION_ADD = " ";

    /**
     * Create WhseLocItem Request for update Line Item Comment.
     * @param itemId Item ID.
     * @param itemType Item type.
     * @param whLocNbr Warehouse number.
     * @param warehouseType Warehouse type.
     * @param lineItemComment Line item comment.
     * @param userID UserID.
     * @return WhseLocItem Request
     */
    public static WhseLocItm createWhseLocItmRequestForRemark(long itemId,String itemType, int whLocNbr,String warehouseType,
                                                              String lineItemComment, String userID){
        WhseLocItm request = new WhseLocItm();
        if(lineItemComment.length() > 0){
            request.setACTIONCD(ProductManagementServiceHelper.ACTION_ADD);
        }else{
            request.setACTIONCD(ProductManagementServiceHelper.ACTION_DELETE);
        }
        request.setITMKEYTYPCD(itemType);
        request.setITMID(String.valueOf(itemId));
        request.setWHSELOCTYPCD(warehouseType);
        request.setWHSELOCNBR(String.valueOf(whLocNbr));
        request.setPOCMT(lineItemComment);
        request.setVCUSRID(userID);
        return request;
    }


    /**
     *  Create ItemWarehouseComments request for update Remark and Comment.
     * @param itemId Item Id.
     * @param itemType Item type.
     * @param whLocNbr Warehouse number.
     * @param warehouseType Warehouse type.
     * @param itemWarehouseComments Item warehouse comment object.
     * @return ItemWarehouseComments request.
     */
    public static com.heb.xmlns.ei.itemwarehousecomments.ItemWarehouseCommentsUpdate createItemWarehouseCommentsForRemark(
            long itemId, String itemType, int whLocNbr, String warehouseType, ItemWarehouseComments itemWarehouseComments){
        com.heb.xmlns.ei.itemwarehousecomments.ItemWarehouseCommentsUpdate requestObject = new com.heb.xmlns.ei.itemwarehousecomments.ItemWarehouseCommentsUpdate();
        if(itemWarehouseComments != null){
            if(itemWarehouseComments.getItemCommentContents() != null && itemWarehouseComments.getItemCommentContents().length() > 0){
                requestObject.setITMWHSECMTTXT(itemWarehouseComments.getItemCommentContents());
                requestObject.setACTIONCD(ProductManagementServiceHelper.ACTION_ADD);
            }else{
                requestObject.setACTIONCD(ProductManagementServiceHelper.ACTION_DELETE);
            }
            requestObject.setITMID(String.valueOf(itemId));
            requestObject.setITMKEYTYPCD(itemType);
            requestObject.setWHSELOCTYPCD(warehouseType);
            requestObject.setWHSELOCNBR(String.valueOf(whLocNbr));
            requestObject.setITMCMTTYPCD(ItemWarehouseComments.STRING_REMRK);
            requestObject.setITMWHSECMTNBR(String.valueOf(itemWarehouseComments.getKey().getItemCommentNumber()));
        }
        return requestObject;
    }

}

