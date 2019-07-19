/*
 *  CheckDigitCalculatorService
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.utilities.checkDigitCalculator;

import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.ItemMasterKey;
import com.heb.pm.repository.ItemMasterRepository;
import com.heb.pm.repository.PrimaryUpcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds all business logic related to check digit calculator.
 *
 * @author vn75469
 * @since 2.16.0
 */
@Service
public class CheckDigitCalculatorService {

    @Autowired
    private ItemMasterRepository itemMasterRepository;
    @Autowired
    private PrimaryUpcRepository primaryUpcRepository;
    /**
     * Call repository to get all items which specific upc code.
     *
     * @param upc code.
     * @return ItemMaster object.
     */
    public ItemMaster getItems(Long upc) {
        ItemMaster itemMaster = null;
        List<Long> itemCodes = this.primaryUpcRepository.findByUpcCode(upc);
        if (!itemCodes.isEmpty()){
            Long itemCode = itemCodes.get(0);
            ItemMasterKey key = new ItemMasterKey();
            key.setItemCode(itemCode);
            key.setItemType(ItemMasterKey.WAREHOUSE);
            itemMaster = this.itemMasterRepository.findOne(key);
        }
        return itemMaster;
    }
}