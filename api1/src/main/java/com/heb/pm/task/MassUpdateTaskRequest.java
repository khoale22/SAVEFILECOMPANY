/*
 * MassUpdateTaskRequest
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.AlertStaging;
import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.entity.FulfillmentChannel;
import com.heb.pm.entity.PDPTemplate;
import com.heb.pm.entity.SalesChannel;
import com.heb.pm.productSearch.ProductSearchCriteria;
import com.heb.pm.productSearch.RawSearchCriteria;

import java.util.List;

/**
 * MassUpdateTaskRequest use to send mass fill data to server. Handle case select all and paging server
 *
 * @author vn87351
 * @since 2.17.0
 */
public class MassUpdateTaskRequest {
    public static final int ACTION_TYPE_SAVE = 1;
    public static final int ACTION_TYPE_ASSIGN_BDM = 2;
    public static final int ACTION_TYPE_ASSIGN_eBM = 3;
    public static final int ACTION_TYPE_PUBLISH_PRODUCT = 4;
    public static final int ACTION_TYPE_DELETE_PRODUCT = 5;

    private boolean isSelectAll;
    private List<Long> excludedAlerts;
    private List<CandidateWorkRequest> selectedCandidateWorkRequests;
    private List<AlertStaging> selectedAlertStaging;
    private SalesChannel salesChannel;
    private List<FulfillmentChannel> lstFulfillmentChannel;
    private String isShowOnSite;
    private String showOnSiteFilter;
    private String effectiveDate;
    private String expirationDate;
    private PDPTemplate pdpTemplate;
    private Long trackingId;
    private String assigneeId;
    private String description;

    private String alertType;
    private List<Long> attributes;
    private String salesChannelsFilter;
    private String userId;
    private Integer alertId;

    private int actionType;

    private RawSearchCriteria searchCriteria;
	private ProductSearchCriteria productSearchCriteria;

    /**
     * Get ProductSearchCriteria with setter for Session id field. It used to caching data to load Hits
     *
     * @return
     */
    public ProductSearchCriteria getProductSearchCriteria() {
		if (searchCriteria != null && this.productSearchCriteria == null) {
			this.productSearchCriteria = searchCriteria.toProductSearchCriteria();
            productSearchCriteria.setSessionId(userId);
        }
		return this.productSearchCriteria;
    }

    /**
     * Returns the Product Search Criteria.
     *
     * @return the Product Search Criteria.
     */
    public RawSearchCriteria getSearchCriteria() {
        return searchCriteria;
    }

    /**
     * Set the ProductSearchCriteria
     *
     * @param searchCriteria
     */
    public void setSearchCriteria(RawSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    /**
     * Returns the action type.
     *
     * @return the action type.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * Set the action type
     *
     * @param actionType
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * Returns the user id.
     *
     * @return the user id.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Set the user Id.
     *
     * @param userId the user Id.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Returns the show On Site Filter.
     *
     * @return the show On Site Filter.
     */
    public String getShowOnSiteFilter() {
        return showOnSiteFilter;
    }

    /**
     * Set the show On Site Filter.
     *
     * @param showOnSiteFilter the show On Site Filter.
     */
    public void setShowOnSiteFilter(String showOnSiteFilter) {
        this.showOnSiteFilter = showOnSiteFilter;
    }

    /**
     * Returns the alert Type.
     *
     * @return the alert Type.
     */
    public String getAlertType() {
        return alertType;
    }

    /**
     * Set the alert Type.
     *
     * @param alertType the alert Type.
     */
    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    /**
     * Returns the attributes.
     *
     * @return the attributes.
     */
    public List<Long> getAttributes() {
        return attributes;
    }

    /**
     * Set the attributes.
     *
     * @param attributes the attributes.
     */
    public void setAttributes(List<Long> attributes) {
        this.attributes = attributes;
    }

    /**
     * Returns the selected Alert Staging.
     *
     * @return the selected Alert Staging.
     */
    public List<AlertStaging> getSelectedAlertStaging() {
        return selectedAlertStaging;
    }

    /**
     * Set the selected Alert Staging.
     *
     * @param selectedAlertStaging the selected Alert Staging.
     */
    public void setSelectedAlertStaging(List<AlertStaging> selectedAlertStaging) {
        this.selectedAlertStaging = selectedAlertStaging;
    }

    /**
     * Returns the check all status.
     *
     * @return the check all status.
     */
    public boolean isSelectAll() {
        return isSelectAll;
    }

    /**
     * Set the check all status.
     *
     * @param selectAll the check all status.
     */
    public void setIsSelectAll(boolean selectAll) {
        isSelectAll = selectAll;
    }

    /**
     * Returns the SalesChannel.
     *
     * @return the SalesChannel.
     */
    public SalesChannel getSalesChannel() {
        return salesChannel;
    }

    /**
     * Sets the SalesChannel.
     *
     * @param salesChannel the SalesChannel.
     */
    public void setSalesChannel(SalesChannel salesChannel) {
        this.salesChannel = salesChannel;
    }

    /**
     * Returns the list of FulfillmentChannels.
     *
     * @return the list of FulfillmentChannels.
     */
    public List<FulfillmentChannel> getLstFulfillmentChannel() {
        return lstFulfillmentChannel;
    }

    /**
     * Sets the list of FulfillmentChannels.
     *
     * @param lstFulfillmentChannel the list of FulfillmentChannels.
     */
    public void setLstFulfillmentChannel(List<FulfillmentChannel> lstFulfillmentChannel) {
        this.lstFulfillmentChannel = lstFulfillmentChannel;
    }

    /**
     * Return the ShowOnSite.
     *
     * @return the ShowOnSite.
     */
    public String isShowOnSite() {
        return isShowOnSite;
    }

    /**
     * Set the ShowOnSite.
     *
     * @param showOnSite the ShowOnSite.
     */
    public void setIsShowOnSite(String showOnSite) {
        isShowOnSite = showOnSite;
    }

    /**
     * Return the EffectiveDate.
     *
     * @return the EffectiveDate.
     */
    public String getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * Set the EffectiveDate.
     *
     * @param effectiveDate the EffectiveDate.
     */
    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /**
     * Return the ExpirationDate.
     *
     * @return the ExpirationDate.
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * Set the ExpirationDate.
     *
     * @param expirationDate the ExpirationDate.
     */
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Return the TrackingId.
     *
     * @return the TrackingId.
     */
    public Long getTrackingId() {
        return trackingId;
    }

    /**
     * Set the TrackingId.
     *
     * @param trackingId the TrackingId.
     */
    public void setTrackingId(Long trackingId) {
        this.trackingId = trackingId;
    }

    /**
     * Return the AssigneeId.
     *
     * @return the AssigneeId
     */
    public String getAssigneeId() {
        return assigneeId;
    }

    /**
     * Set the AssigneeId.
     *
     * @param assigneeId the AssigneeId.
     */
    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    /**
     * Return the Description.
     *
     * @return the Description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the Description.
     *
     * @param description the Description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the list of SelectedCandidateWorkRequests.
     *
     * @return the list of SelectedCandidateWorkRequests
     */
    public List<CandidateWorkRequest> getSelectedCandidateWorkRequests() {
        return selectedCandidateWorkRequests;
    }

    /**
     * Set the list of SelectedCandidateWorkRequests
     *
     * @param selectedCandidateWorkRequests the list of SelectedCandidateWorkRequests
     */
    public void setSelectedCandidateWorkRequests(List<CandidateWorkRequest> selectedCandidateWorkRequests) {
        this.selectedCandidateWorkRequests = selectedCandidateWorkRequests;
    }

    /**
     * Return the PdpTemplate.
     *
     * @return the PdpTemplate.
     */
    public PDPTemplate getPdpTemplate() {
        return pdpTemplate;
    }

    /**
     * Set the PdpTemplate.
     *
     * @param pdpTemplate
     */
    public void setPdpTemplate(PDPTemplate pdpTemplate) {
        this.pdpTemplate = pdpTemplate;
    }

    /**
     * Returns the list of ExcludedAlerts.
     *
     * @return the list of ExcludedAlerts.
     */
    public List<Long> getExcludedAlerts() {
        return excludedAlerts;
    }

    /**
     * Returns  the list of ExcludedAlerts.
     *
     * @param excludedAlerts the list of excludedAlerts.
     */
    public void setExcludedAlerts(List<Long> excludedAlerts) {
        this.excludedAlerts = excludedAlerts;
    }

    public Integer getAlertId() {
        return alertId;
    }

    public void setAlertId(Integer alertId) {
        this.alertId = alertId;
    }

    /**
     * Returns  the list of SalesChannels filter.
     *
     * @return salesChannelsFilter
     */
    public String getSalesChannelsFilter() {
        return salesChannelsFilter;
    }

    /**
     * Set the salesChannelsFilter.
     *
     * @param salesChannelsFilter
     */
    public void setSalesChannelsFilter(String salesChannelsFilter) {
        this.salesChannelsFilter = salesChannelsFilter;
    }

    @Override
    public String toString() {
        return "MassUpdateTaskRequest{" +
                "isSelectAll=" + isSelectAll +
                ", excludedAlerts=" + excludedAlerts +
                ", selectedCandidateWorkRequests=" + selectedCandidateWorkRequests +
                ", selectedAlertStaging=" + selectedAlertStaging +
                ", salesChannel=" + salesChannel +
                ", lstFulfillmentChannel=" + lstFulfillmentChannel +
                ", isShowOnSite='" + isShowOnSite + '\'' +
                ", showOnSiteFilter='" + showOnSiteFilter + '\'' +
                ", salesChannelsFilter='" + salesChannelsFilter + '\'' +
                ", effectiveDate='" + effectiveDate + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", pdpTemplate=" + pdpTemplate +
                ", trackingId=" + trackingId +
                ", assigneeId='" + assigneeId + '\'' +
                ", description='" + description + '\'' +
                ", alertType='" + alertType + '\'' +
                ", attributes=" + attributes +
                ", userId='" + userId + '\'' +
                '}';
    }
}
