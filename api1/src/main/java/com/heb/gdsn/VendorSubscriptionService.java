package com.heb.gdsn;

import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;


/**
 * Service class for managing vendor subscriptions to GDSN data.
 *
 * @author d116773
 * @since 2.3.0
 */
@Service
public class VendorSubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(VendorSubscriptionService.class);

    private static final int MAX_GLN_LENGTH = 13;

    private static final String VENDOR_NAME_REGULAR_EXPRESSION = "%%%s%%";

    private static final String DEFAULT_MISSING_GLN_MESSAGE = "GLN is required.";
    private static final String MISSING_GLN_KEY = "VendorSubscriptionService.glnRequired";
    private static final String DEFAULT_NUMERIC_ONLY_GLN_MESSAGE = "GLNs must be numeric.";
    private static final String BAD_GLN_MESSAGE_KEY = "VendorSubscriptionService.glnNumbersOnly";
    private static final String DEFAULT_GLN_SIZE_MESSAGE = "GLNs must be less than 13 digits long.";
    private static final String GNL_SIZE_KEY = "VendorSubscriptionService.gnl13";
    private static final String DEFAULT_MISSING_VENDOR_NAME = "Vendor name is required.";
    private static final String MISSING_VENDOR_NAME_KEY = "VendorSubscriptionService.vendorNameRequired";
    private static final String DEFAULT_BAD_STATUS = "Subscription status must be either ADD, DELETE, or MODIFY.";
    private static final String BAD_STATUS_KEY = "VendorSubscriptionService.badSubscriptionStatus";

    private static final String SUBSCRIPTION_STATUS_ADD = "ADD";
    private static final String SUBSCRIPTION_STATUS_DELETE= "DELETE";
    private static final String SUBSCRIPTION_STATUS_MODIFY= "MODIFY";

    private static final String MESSAGE_STATUS_PENDING = "PEND";
    private static final String MESSAGE_STATUS_COMPLETE = "COMP";

    @Autowired
    private VendorSubscriptionRepository vendorSubscriptionRepository;

    @Autowired
    private VendorSubscriptionRepositoryWithCount vendorSubscriptionRepositoryWithCount;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserInfo userInfo;

    /**
     * Returns a list of all vendor subscription records.
     *
     * @param page The page being looked for.
     * @param pageSize The number of records to return.
     * @return A pageable list of all vendor subscription records.
     */
    public PageableResult<VendorSubscription> findAll(int page, int pageSize) {

        PageRequest pageRequest = new PageRequest(page, pageSize, VendorSubscription.getDefaultSort());
        Page<VendorSubscription> results = this.vendorSubscriptionRepository.findAll(pageRequest);
        return new PageableResult<>(page, results.getTotalPages(),results.getTotalElements(),
                results.getContent());
    }

    /**
     * Returns a list of all vendor subscription records filtering by GLN.
     *
     * @param page The page being looked for.
     * @param pageSize The number of records to return.
     * @param includeCounts Whether or not to inlcude record counts.
     * @param vendorGln The GLN to filter by.
     * @return  A pageable list of all vendor subscription records.
     */
    public PageableResult<VendorSubscription> findByGln(int page, int pageSize, boolean includeCounts,
                                                        String vendorGln) {

        PageRequest pageRequest = new PageRequest(page, pageSize, VendorSubscription.getDefaultSort());

        if (includeCounts) {
            Page<VendorSubscription> results =
                    this.vendorSubscriptionRepositoryWithCount.findByTrimmedVendorGln(vendorGln, pageRequest);
            return new PageableResult<>(page, results.getTotalPages(), results.getTotalElements(),
                    results.getContent());
        } else {
            List<VendorSubscription> results =
                    this.vendorSubscriptionRepository.findByTrimmedVendorGln(vendorGln, pageRequest);
            return new PageableResult<>(page, results);
        }
    }

    /**
     * Returns a list of all vendor subscription records filtering by vendor name.
     *
     * @param page The page being looked for.
     * @param pageSize The number of records to return.
     * @param includeCounts Whether or not to inlcude record counts.
     * @param vendorName The vendor name to filter by.
     * @return  A pageable list of all vendor subscription records.
     */
    public PageableResult<VendorSubscription> findByVendorName(int page, int pageSize, boolean includeCounts,
                                                        String vendorName) {

        PageRequest pageRequest = new PageRequest(page, pageSize, VendorSubscription.getDefaultSort());
        String vendorNameSearch =
                String.format(VendorSubscriptionService.VENDOR_NAME_REGULAR_EXPRESSION, vendorName);

        if (includeCounts) {
            Page<VendorSubscription> results =
                    this.vendorSubscriptionRepositoryWithCount.findByVendorName(vendorNameSearch, pageRequest);
            return new PageableResult<>(page, results.getTotalPages(), results.getTotalElements(),
                    results.getContent());
        } else {
            List<VendorSubscription> results =
                    this.vendorSubscriptionRepository.findByVendorrName(vendorNameSearch, pageRequest);
            return new PageableResult<>(page, results);
        }
    }

    /**
     * Saves a vendor subscription to the database.
     *
     * @param vendorSubscription The vendor subscription to save.
     * @param locale The locale of the HTTP request.
     * @return The saved vendor subscription as returned from the database.
     */
    public VendorSubscription addSubscription(VendorSubscription vendorSubscription, Locale locale) {

        // Check to make sure the subscription has valid values.
        this.validateVendorSubscription(vendorSubscription, locale);

        // Set the sequence number.
        int maxSequenceNumber = this.vendorSubscriptionRepository.findMaxSubscriptionNumber();
        maxSequenceNumber++;
        vendorSubscription.setSequenceNumber(maxSequenceNumber);

        LocalDateTime now = LocalDateTime.now();
        // If this is a modify, set the status to complete and the process time.
        if (VendorSubscriptionService.SUBSCRIPTION_STATUS_MODIFY.equals(vendorSubscription.getSubscriptionStatus())) {
            vendorSubscription.setMessageStatus(VendorSubscriptionService.MESSAGE_STATUS_COMPLETE);
            vendorSubscription.setProcessTime(now);
        }
        // Otherwise, set it to PEND so the integration will pick it up.
        else {
            vendorSubscription.setMessageStatus(VendorSubscriptionService.MESSAGE_STATUS_PENDING);
        }

        // Set the create time to now.
        vendorSubscription.setCreateTime(now);

        // Set the user to the currently logged in user
        vendorSubscription.setCreateUserId(this.userInfo.getUserId());

        // Message ID and document ID are null
        return this.vendorSubscriptionRepository.save(vendorSubscription);
    }

    /**
     * Validates that the vendor subscription record is valid. Will throw an exception if validation does not pass.
     *
     * @param vendorSubscription The VendorSubscription to validate.
     * @param locale The locale of the HTTP request.
     */
    private void validateVendorSubscription(VendorSubscription vendorSubscription, Locale locale) {

        StringBuilder errorMessageBuilder = new StringBuilder();

        // Check to make sure the GLN is not empty.
        if (vendorSubscription.getVendorGln() == null || vendorSubscription.getVendorGln().isEmpty()) {
            errorMessageBuilder.append(this.messageSource.getMessage(VendorSubscriptionService.MISSING_GLN_KEY,
                    null, VendorSubscriptionService.DEFAULT_MISSING_GLN_MESSAGE, locale));
        }

        // Check to make sure the GLN is all digits.
        for (char c : vendorSubscription.getVendorGln().toCharArray()) {
            if (!Character.isDigit(c)) {
                if (errorMessageBuilder.length() > 0){
                    errorMessageBuilder.append('\n');
                }
                errorMessageBuilder.append(this.messageSource.getMessage(VendorSubscriptionService.BAD_GLN_MESSAGE_KEY,
                        null, VendorSubscriptionService.DEFAULT_NUMERIC_ONLY_GLN_MESSAGE, locale));
                break;
            }
        }

        // Check to make sure the GLN is less than 13 digits long
        if (vendorSubscription.getVendorGln().length() > VendorSubscriptionService.MAX_GLN_LENGTH) {
            if (errorMessageBuilder.length() > 0){
                errorMessageBuilder.append('\n');
            }
            errorMessageBuilder.append(this.messageSource.getMessage(VendorSubscriptionService.GNL_SIZE_KEY,
                    null, VendorSubscriptionService.DEFAULT_GLN_SIZE_MESSAGE, locale));
        }
        // Check to make sure the vendor name is not empty.
        if (vendorSubscription.getVendorName() == null || vendorSubscription.getVendorName().isEmpty()) {
            if (errorMessageBuilder.length() > 0){
                errorMessageBuilder.append('\n');
            }
            errorMessageBuilder.append(this.messageSource.getMessage(VendorSubscriptionService.MISSING_VENDOR_NAME_KEY,
                    null, VendorSubscriptionService.DEFAULT_MISSING_VENDOR_NAME, locale));
        }

        // Check to make sure the subscription status is ADD, DELETE, or MODIFY
        if (!(VendorSubscriptionService.SUBSCRIPTION_STATUS_ADD.equals(vendorSubscription.getSubscriptionStatus()) ||
                        VendorSubscriptionService.SUBSCRIPTION_STATUS_DELETE.equals(vendorSubscription.getSubscriptionStatus()) ||
                        VendorSubscriptionService.SUBSCRIPTION_STATUS_MODIFY.equals(vendorSubscription.getSubscriptionStatus()))) {
            if (errorMessageBuilder.length() > 0){
                errorMessageBuilder.append('\n');
            }
            errorMessageBuilder.append(this.messageSource.getMessage(VendorSubscriptionService.BAD_STATUS_KEY,
                    null, VendorSubscriptionService.DEFAULT_BAD_STATUS, locale));
        }

        if (errorMessageBuilder.length() > 0) {
            throw new IllegalArgumentException(errorMessageBuilder.toString());
        }
    }

    /**
     * Sets the VendorSubscriptionRepository for this class to use.
     *
     * @param vendorSubscriptionRepository The VendorSubscriptionRepository
     */
    public void setVendorSubscriptionRepository(VendorSubscriptionRepository vendorSubscriptionRepository) {
        this.vendorSubscriptionRepository = vendorSubscriptionRepository;
    }
}
