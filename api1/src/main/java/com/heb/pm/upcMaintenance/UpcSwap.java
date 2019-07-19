/*
 * UpcSwap
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.upcMaintenance;

import com.heb.tim.services.vo.PurchaseOrderVO;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used to represent a upc swap.
 *
 * @author m314029
 * @since 2.0.4
 */
public class UpcSwap implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long sourceUpc;
	private boolean isSourcePrimaryUpc;
	private Long selectSourcePrimaryUpc;
	private Long destinationPrimaryUpc;
	private boolean makeDestinationPrimaryUpc;
	private Long destinationPrimaryUpcSelected;
	private String statusMessage;
	private boolean errorFound;
	private PrimarySelectOption primarySelectOption;
	private List<Long> additionalUpcList;

	private SwappableEndPoint source;
	private SwappableEndPoint destination;

	private boolean destinationAltPack;

	/**
	 * The enum Primary select option.
	 */
	public enum PrimarySelectOption{
		/**
		 * The Just primary.
		 */
		JUST_PRIMARY("Just primary"),
		/**
		 * The Primary and associates.
		 */
		PRIMARY_AND_ASSOCIATES("Primary and associates"),
		/**
		 * The Just associates.
		 */
		JUST_ASSOCIATES("Just associates");

		private String name;

		PrimarySelectOption(String name) {
			this.name = name;
		}

		/**
		 * Gets name.
		 *
		 * @return the name
		 */
		public String getName() {
			return this.name;
		}
	}

	/**
	 * Returns the primary select option.
	 *
	 * @return The primary select option.
	 */
	public PrimarySelectOption getPrimarySelectOption() {
		return primarySelectOption;
	}

	/**
	 * Sets the primary select option.
	 *
	 * @param primarySelectOption The primary select option.
	 */
	public void setPrimarySelectOption(PrimarySelectOption primarySelectOption) {
		this.primarySelectOption = primarySelectOption;
	}

	/**
	 * Returns the additional upc list.
	 *
	 * @return The additional upc list.
	 */
	public List<Long> getAdditionalUpcList() {
		return additionalUpcList;
	}

	/**
	 * Sets the additional upc list.
	 *
	 * @param additionalUpcList The additional upc list.
	 */
	public void setAdditionalUpcList(List<Long> additionalUpcList) {
		this.additionalUpcList = additionalUpcList;
	}

	/**
	 * Returns the source upc for this UpcSwap.
	 *
	 * @return The sourceUpc of this UpcSwap.
	 */
	public Long getSourceUpc() {
		return sourceUpc;
	}

	/**
	 * Sets the source upc for this UpcSwap.
	 *
	 * @param sourceUpc The sourceUpc for this UpcSwap.
	 */
	public void setSourceUpc(Long sourceUpc) {
		this.sourceUpc = sourceUpc;
	}

	/**
	 * Returns whether or not the source upc is the primary upc for this UpcSwap.
	 *
	 * @return The isSourcePrimaryUpc of this UpcSwap.
	 */
	public boolean isSourcePrimaryUpc() {
		return isSourcePrimaryUpc;
	}

	/**
	 * Sets whether or not the source upc is the primary upc for this UpcSwap.
	 *
	 * @param isSourcePrimaryUpc The isSourcePrimaryUpc for this UpcSwap.
	 */
	public void setSourcePrimaryUpc(boolean isSourcePrimaryUpc) {
		this.isSourcePrimaryUpc = isSourcePrimaryUpc;
	}

	/**
	 * Returns the upc to set as primary upc of the source for this UpcSwap.
	 *
	 * @return The selectSourcePrimaryUpc of this UpcSwap.
	 */
	public Long getSelectSourcePrimaryUpc() {
		return selectSourcePrimaryUpc;
	}

	/**
	 * Sets the primary upc if the upc for the source is the primary upc for this UpcSwap.
	 *
	 * @param selectSourcePrimaryUpc The selectSourcePrimaryUpc for this UpcSwap.
	 */
	public void setSelectSourcePrimaryUpc(Long selectSourcePrimaryUpc) {
		this.selectSourcePrimaryUpc = selectSourcePrimaryUpc;
	}

	/**
	 * Returns the primary upc of the destination for this UpcSwap.
	 *
	 * @return The destinationPrimaryUpc of this UpcSwap.
	 */
	public Long getDestinationPrimaryUpc() {
		return destinationPrimaryUpc;
	}

	/**
	 * Sets the primary upc of the destination for this UpcSwap.
	 *
	 * @param destinationPrimaryUpc The destinationPrimaryUpc for this UpcSwap.
	 */
	public void setDestinationPrimaryUpc(Long destinationPrimaryUpc) {
		this.destinationPrimaryUpc = destinationPrimaryUpc;
	}

	/**
	 * Returns whether or not to make the source upc the primary upc of the destination for this UpcSwap.
	 *
	 * @return The makeDestinationPrimaryUpc of this UpcSwap.
	 */
	public boolean isMakeDestinationPrimaryUpc() {
		return makeDestinationPrimaryUpc;
	}

	/**
	 * Sets whether or not to make the source upc the primary upc of the destination for this UpcSwap.
	 *
	 * @param makeDestinationPrimaryUpc The makeDestinationPrimaryUpc for this UpcSwap.
	 */
	public void setMakeDestinationPrimaryUpc(boolean makeDestinationPrimaryUpc) {
		this.makeDestinationPrimaryUpc = makeDestinationPrimaryUpc;
	}

	/**
	 * Returns error message for the UpcSwap.
	 *
	 * @return The statusMessage for the UpcSwap.
	 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * Sets error message for the UpcSwap.
	 *
	 * @param statusMessage The statusMessage for the UpcSwap.
	 */
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	/**
	 * Returns whether upc swap has errors.
	 *
	 * @return The errorFound for the UpcSwap.
	 */
	public boolean isErrorFound() {
		return errorFound;
	}

	/**
	 * Sets whether upc swap has errors.
	 *
	 * @param errorFound The errorFound for the UpcSwap.
	 */
	public void setErrorFound(boolean errorFound) {
		this.errorFound = errorFound;
	}

	/**
	 * Returns the source for this UpcSwap.
	 *
	 * @return The source for this UpcSwap.
	 */
	public SwappableEndPoint getSource() {
		return source;
	}

	/**
	 * Sets the source for this UpcSwap.
	 *
	 * @param source The source for this UpcSwap.
	 */
	public void setSource(SwappableEndPoint source) {
		this.source = source;
	}

	/**
	 * Returns the destination for this UpcSwap.
	 *
	 * @return The destination for this UpcSwap.
	 */
	public SwappableEndPoint getDestination() {
		return destination;
	}

	/**
	 * Sets the destination for this UpcSwap.
	 *
	 * @param destination The destination for this UpcSwap.
	 */
	public void setDestination(SwappableEndPoint destination) {
		this.destination = destination;
	}

	/**
	 * Gets destination primary upc selected.
	 *
	 * @return the destination primary upc selected
	 */
	public Long getDestinationPrimaryUpcSelected() {
		return destinationPrimaryUpcSelected;
	}

	/**
	 * Sets destination primary upc selected.
	 *
	 * @param destinationPrimaryUpcSelected the destination primary upc selected
	 */
	public void setDestinationPrimaryUpcSelected(Long destinationPrimaryUpcSelected) {
		this.destinationPrimaryUpcSelected = destinationPrimaryUpcSelected;
	}

	/**
	 * Returns whether destination is alternate pack.
	 *
	 * @return The destinationAltPack for the UpcSwap.
	 */
	public boolean isDestinationAltPack() {
		return destinationAltPack;
	}

	/**
	 * Sets whether destination is alternate pack.
	 *
	 * @param destinationAltPack The destinationAltPack for the UpcSwap.
	 */
	public void setDestinationAltPack(boolean destinationAltPack) {
		this.destinationAltPack = destinationAltPack;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "UpcSwap{" +
				"sourceUpc=" + sourceUpc +
				", isSourcePrimaryUpc=" + isSourcePrimaryUpc +
				", selectSourcePrimaryUpc=" + selectSourcePrimaryUpc +
				", destinationPrimaryUpc=" + destinationPrimaryUpc +
				", makeDestinationPrimaryUpc=" + makeDestinationPrimaryUpc +
				", statusMessage='" + statusMessage + '\'' +
				", source=" + source +
				", destination=" + destination +
				'}';
	}

	/**
	 * The type Swappable end point.
	 */
	public class SwappableEndPoint implements Serializable {

		private static final long serialVersionUID = 1L;

		private Long productId;
		private Long itemCode;
		private Long upc;
		private Long primaryUpc;
		private Integer checkDigit;
		private String itemType;
		private String itemDescription;
		private String itemSize;
		private int balanceOnHand;
		private int balanceOnOrder;
		private Boolean onLiveOrPendingPog;
		private Long productRetailLink;
		@SuppressWarnings("rawtypes")
		private ArrayList purchaseOrders;
		private List<Long> associatedUpcList;
		private String errorMessage;
		private Date currentDate;

		// No PO found constant
		private static final String NO_PURCHASE_ORDER_TEXT = "No PO";
		// Unable to connect to TIM constant
		private static final String TIM_CONNECTION_ERROR = "Unable to connect to TIM";

		// Date formatter
		private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		/**
		 * Instantiates a new Swappable end point.
		 */
		public SwappableEndPoint(){
		}

		/**
		 * When processing add associates, this will have a value of either a UPC to add to or the UPC to add. This
		 * will not be set for the other operations.
		 *
		 * @return The UPC to add (when destination) or the UPC to add to (when source).
		 */
		public Long getUpc() {
			return upc;
		}

		/**
		 * Sets the UPC to add (when destination) or the UPC to add to (when source).
		 *
		 * @param upc The UPC to add (when destination) or the UPC to add to (when source).
		 */
		public void setUpc(Long upc) {
			this.upc = upc;
		}

		/**
		 * When adding an associate, they are required to enter a check digit to confirm the UPC they are entering
		 * is valid.
		 *
		 * @return The check digit for the UPC they are adding.
		 */
		public Integer getCheckDigit() {
			return checkDigit;
		}

		/**
		 * Sets The check digit for the UPC they are adding.
		 *
		 * @param checkDigit The check digit for the UPC they are adding.
		 */
		public void setCheckDigit(Integer checkDigit) {
			this.checkDigit = checkDigit;
		}

		/**
		 * Returns the primary UPC tied to the item code property.
		 *
		 * @return The primary UPC tied to the item code property.
		 */
		public Long getPrimaryUpc() {
			return primaryUpc;
		}

		/**
		 * Sets the primary UPC tied to the item code property.
		 *
		 * @param primaryUpc The primary UPC tied to the item code property.
		 */
		public void setPrimaryUpc(Long primaryUpc) {
			this.primaryUpc = primaryUpc;
		}

		/**
		 * Returns the product Id for the upc of the SwappableEndPoint.
		 *
		 * @return The productId for the SwappableEndPoint.
		 */
		public Long getProductId() {
			return productId;
		}

		/**
		 * Sets the product Id for the upc of the SwappableEndPoint.
		 *
		 * @param productId The productId for the SwappableEndPoint.
		 */
		public void setProductId(Long productId) {
			this.productId = productId;
		}

		/**
		 * Returns item code for the SwappableEndPoint.
		 *
		 * @return The itemCode for the SwappableEndPoint.
		 */
		public Long getItemCode() {
			return itemCode;
		}

		/**
		 * Sets the item code for the SwappableEndPoint.
		 *
		 * @param itemCode The itemCode for the SwappableEndPoint.
		 */
		public void setItemCode(Long itemCode) {
			this.itemCode = itemCode;
		}

		/**
		 * Returns the item type for the SwappableEndPoint.
		 *
		 * @return The item type for the SwappableEndPoint.
		 */
		public String getItemType() {
			return itemType;
		}

		/**
		 * Sets the item type for the SwappableEndPoint
		 *
		 * @param itemType The item type for the SwappableEndPoint
		 */
		public void setItemType(String itemType) {
			this.itemType = itemType;
		}

		/**
		 * Returns the item description for the SwappableEndPoint.
		 *
		 * @return The itemDescription for the SwappableEndPoint.
		 */
		public String getItemDescription() {
			return itemDescription;
		}

		/**
		 * Sets the item description for the SwappableEndPoint.
		 *
		 * @param itemDescription The itemDescription for the SwappableEndPoint.
		 */
		public void setItemDescription(String itemDescription) {
			this.itemDescription = itemDescription;
		}

		/**
		 * Returns the item's size.
		 *
		 * @return the item's size.
		 */
		public String getItemSize() {
			return itemSize;
		}

		/**
		 * Sets the item's size.
		 *
		 * @param itemSize the item's size.
		 */
		public void setItemSize(String itemSize) {
			this.itemSize = itemSize;
		}

		/**
		 * Returns BOH (Billing on order) inventory for the SwappableEndPoint.
		 *
		 * @return The balanceOnHand for the SwappableEndPoint.
		 */
		public int getBalanceOnHand() {
			return balanceOnHand;
		}

		/**
		 * Sets the billing on hand inventory for the SwappableEndPoint.
		 *
		 * @param balanceOnHand The balanceOnHand for the SwappableEndPoint.
		 */
		public void setBalanceOnHand(int balanceOnHand) {
			this.balanceOnHand = balanceOnHand;
		}

		/**
		 * Returns BOO (Billing on order) inventory for the SwappableEndPoint.
		 *
		 * @return The balanceOnOrder for the SwappableEndPoint.
		 */
		public int getBalanceOnOrder() {
			return balanceOnOrder;
		}

		/**
		 * Sets the billing on order inventory for the SwappableEndPoint.
		 *
		 * @param balanceOnOrder The balanceOnOrder for the SwappableEndPoint.
		 */
		public void setBalanceOnOrder(int balanceOnOrder) {
			this.balanceOnOrder = balanceOnOrder;
		}

		/**
		 * Returns whether item code is on live or pending POG for the SwappableEndPoint.
		 *
		 * @return The boolean onLiveOrPendingPog for the SwappableEndPoint.
		 */
		public Boolean isOnLiveOrPendingPog() {
			return onLiveOrPendingPog;
		}

		/**
		 * Sets whether item code is on live or pending POG for the SwappableEndPoint.
		 *
		 * @param isPOG The onLiveOrPendingPog for the SwappableEndPoint.
		 */
		public void setOnLiveOrPendingPog(Boolean isPOG) {
			this.onLiveOrPendingPog = isPOG;
		}

		/**
		 * Returns the product retail link for the SwappableEndPoint.
		 *
		 * @return The productRetailLink for the SwappableEndPoint.
		 */
		public Long getProductRetailLink() {
			return productRetailLink;
		}

		/**
		 * Sets product retail link for the SwappableEndPoint.
		 *
		 * @param productRetailLink The productRetailLink for the SwappableEndPoint.
		 */
		public void setProductRetailLink(Long productRetailLink) {
			this.productRetailLink = productRetailLink;
		}

		/**
		 * Sets purchase orders attached to the item.
		 *
		 * @param purchaseOrders The purchase orders for the SwappableEndPoint.
		 */
		@SuppressWarnings("rawtypes")
		public void setPurchaseOrders(ArrayList purchaseOrders) {
			this.purchaseOrders = purchaseOrders;
		}

		/**
		 * Returns the last date of the item code that has a purchase order within the next 7 days for the SwappableEndPoint.
		 *
		 * @return The last date of the purchase order within 7 days.
		 */
		public String getPurchaseOrderDisplayText() {
			Date currentDate;

			String returnString;
			if(this.purchaseOrders != null && this.getLastPurchaseOrderInWeek() != null) {

				try {
					currentDate = formatter.parse(this.getLastPurchaseOrderInWeek().getArrivalDate());
					returnString = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault()).toString();
				} catch (ParseException e) {
					e.printStackTrace();
					returnString = NO_PURCHASE_ORDER_TEXT;
				}
			} else if(this.purchaseOrders != null){
				returnString = NO_PURCHASE_ORDER_TEXT;
			} else {
				returnString = TIM_CONNECTION_ERROR;
			}
			return returnString;
		}

		/**
		 * Returns the latest purchase order that happened within a week.
		 *
		 * @return Purchase OrderVO with a purchase order arrival date within a week.
		 */
		public PurchaseOrderVO getLastPurchaseOrderInWeek() {
			LocalDateTime mostRecentDateTime = null;
			PurchaseOrderVO returnPurchaseOrderVO = null;
			LocalDateTime currentDateTime;

			if(this.purchaseOrders != null) {
				for (Object o : this.purchaseOrders) {
					PurchaseOrderVO purchaseOrderVO = (PurchaseOrderVO) o;
					if(purchaseOrderVO.getArrivalDate() == null){
						continue;
					}

					try {
						currentDate = formatter.parse(purchaseOrderVO.getArrivalDate());
					} catch (ParseException e) {
						e.printStackTrace();
						continue;
					}

					currentDateTime = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault());
					if (currentDateTime.isAfter(LocalDateTime.now()) &&
							currentDateTime.isBefore(LocalDateTime.now().plusDays(7))) {
						if (mostRecentDateTime == null || currentDateTime.isAfter(mostRecentDateTime)) {
							mostRecentDateTime = currentDateTime;
							returnPurchaseOrderVO = purchaseOrderVO;
						}
					}
				}
			}
			return returnPurchaseOrderVO;
		}

		/**
		 * Returns the list of associated upcs for the SwappableEndPoint.
		 *
		 * @return The list of associated upcs for the SwappableEndPoint.
		 */
		public List<Long> getAssociatedUpcList() {
			return associatedUpcList;
		}

		/**
		 * Sets the list of associated upcs for the SwappableEndPoint.
		 *
		 * @param associatedUpcList The list of associated upcs for the SwappableEndPoint.
		 */
		public void setAssociatedUpcList(List<Long> associatedUpcList) {
			this.associatedUpcList = associatedUpcList;
		}

		/**
		 * Returns the error message for the SwappableEndPoint.
		 *
		 * @return The errorMessage for the SwappableEndPoint.
		 */
		public String getErrorMessage() {
			return errorMessage;
		}

		/**
		 * Sets the message for the SwappableEndPoint.
		 *
		 * @param errorMessage The error message for the SwappableEndPoint.
		 */
		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		/**
		 * Returns a printable representation of the object.
		 *
		 * @return A printable representation of the object.
		 */
		@Override
		public String toString() {
			return "SwappableEndPoint{" +
					"productId=" + productId +
					", itemCode=" + itemCode +
					", upc=" + upc +
					", primaryUpc=" + primaryUpc +
					", checkDigit=" + checkDigit +
					", itemType='" + itemType + '\'' +
					", itemDescription='" + itemDescription + '\'' +
					", itemSize='" + itemSize + '\'' +
					", balanceOnHand=" + balanceOnHand +
					", balanceOnOrder=" + balanceOnOrder +
					", onLiveOrPendingPog=" + onLiveOrPendingPog +
					", productRetailLink=" + productRetailLink +
					", purchaseOrders=" + purchaseOrders +
					", errorMessage='" + errorMessage + '\'' +
					", currentDate=" + currentDate +
					'}';
		}
	}
}
