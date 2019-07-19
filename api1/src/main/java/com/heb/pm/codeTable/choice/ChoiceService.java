/*
 *  ChoiceService
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.choice;

import com.heb.pm.entity.ChoiceOption;
import com.heb.pm.entity.ChoiceType;
import com.heb.pm.entity.CustomerProductChoice;
import com.heb.pm.entity.ImageMetaData;
import com.heb.pm.repository.ChoiceOptionRepository;
import com.heb.pm.repository.ChoiceTypeRepository;
import com.heb.pm.repository.CustomerProductChoiceRepository;
import com.heb.pm.repository.ImageMetaDataRepository;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import com.heb.pm.ws.ContentManagementServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related to code table choice information.
 *
 * @author vn70516
 * @since 2.12.0
 */
@Service
public class ChoiceService {

    @Autowired
    private ChoiceTypeRepository choiceTypeRepository;

    @Autowired
    private ChoiceOptionRepository choiceOptionRepository;

    @Autowired
    private ImageMetaDataRepository imageMetaDataRepository;

    @Autowired
    private CustomerProductChoiceRepository customerProductChoiceRepository;

    @Autowired
    private CodeTableManagementServiceClient serviceClient;

    @Autowired
    ContentManagementServiceClient contentManagementServiceClient;

    private static final String IMAGE_CHOICE_OPTION_SUBJECT_TYPE = "CHCOP";
    private static final boolean CHOICE_OPTION_IMAGE_ACTIVATED = true;
    private static final boolean CHOICE_OPTION_IMAGE_ACTIVATED_ONLINE = true;
    private static final String IMAGE_CHOICE_PRIORITY_TYPE = "P    ";

    /**
     * Returns a list of all choice type default order by choice type description.
     *
     * @return A List of all choice type ordered by choice type description.
     */
    public List<ChoiceType> findAllChoiceType() {
        return this.choiceTypeRepository.findAll(ChoiceType.getDefaultSort());
    }

    /**
     * Returns a list of all choice option default order by choice option code.
     *
     * @return A List of all choice option ordered by choice option code.
     */
    public List<ChoiceOption> findAllChoiceOption() {
        return this.choiceOptionRepository.findAll(ChoiceOption.getDefaultSort());
    }

    /**
     * Returns a list of all image for all list choice option.
     *
     * @return A List of all image for list of choice option.
     */
    public List<ImageMetaData> findAllChoiceOptionImage() {
        return this.imageMetaDataRepository.findAllChoiceOptionImages
                (IMAGE_CHOICE_OPTION_SUBJECT_TYPE, CHOICE_OPTION_IMAGE_ACTIVATED,
                                CHOICE_OPTION_IMAGE_ACTIVATED_ONLINE, IMAGE_CHOICE_PRIORITY_TYPE);
    }

    /**
     * Delete the list of choice type.
     *
     * @param choiceTypes The list of choice type to delete.
     */
    public void deleteChoiceTypes(List<ChoiceType> choiceTypes) {
        this.serviceClient.updateChoiceType(choiceTypes, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_DELETE.getValue());
    }

    /**
     * Update information for the list of choice type.
     *
     * @param choiceTypes The list of choice type to edit.
     */
    public void updateChoiceTypes(List<ChoiceType> choiceTypes) {
        this.serviceClient.updateChoiceType(choiceTypes,  CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE.getValue());
    }

    /**
     * Add new the list of choice type.
     *
     * @param choiceTypes The list of choice type to add new.
     */
    public void addChoiceTypes(List<ChoiceType> choiceTypes) {
        this.serviceClient.updateChoiceType(choiceTypes,  CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue());
    }

    /**
     * Delete the list of choice option.
     *
     * @param choiceOptions The list of choice option to delete.
     */
    public void deleteChoiceOptions(List<ChoiceOption> choiceOptions) {
        this.serviceClient.updateChoiceOptions(choiceOptions, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_DELETE.getValue());
    }

    /**
     * Update information for the list of choice option.
     *
     * @param choiceOptions The list of choice option to edit.
     */
    public void updateChoiceOptions(List<ChoiceOption> choiceOptions) {
        this.serviceClient.updateChoiceOptions(choiceOptions, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE.getValue());
    }

    /**
     * Add new the list of choice option.
     *
     * @param choiceOptions The list of choice option to add new.
     */
    public void addChoiceOptions(List<ChoiceOption> choiceOptions) {
        this.serviceClient.updateChoiceOptions(choiceOptions, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue());
    }

    /**
     * This method contacts a client and gets the  image data from it
     * @param imageUri unique image identifier
     * @return
     */
    public byte[] findChoiceOptionImageByUri(String imageUri){
        return this.contentManagementServiceClient.getImage(imageUri);
    }

    /**
     * Finds a list of customer product choices by their product id.
     * @param productId
     * @return list of customer product choices.
     */
    public List<CustomerProductChoice> retrieveCustomerProductChoiceList(Long productId) {
        return this.customerProductChoiceRepository.findByKeyProductId(productId);
    }
}