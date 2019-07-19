package com.heb.pm.productDetails.casePack;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Holds all business logic related to Morph
 *
 * @author vn55306
 * @since 2.12.0
 */
@Service
public class MorphDsdItemManagementService {
    private static final Logger logger = LoggerFactory.getLogger(MorphDsdItemManagementService.class);
    @Autowired
    private MorphDsdItemConverter morphDsdItemConverter;
    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Autowired
    private CandidateWorkRequestRepository candidateWorkRequestRepository;
    @Autowired
    private CandidateItemMasterRepository candidateItemMasterRepository;

    @CoreTransactional
    public Long copyItemDataToPsTable(List<BicepVendor> bicepVendors,UserInfo userInfo) throws MorphBusinessRuleException{
        logger.info("copyItemDataToPsTable copyItemDataToPsTable");
        Long psWorkId = null;
        Long productId = null;
        if(bicepVendors!=null && bicepVendors.size()>0){
            productId = bicepVendors.get(0).getProductId();
        }
        if(productId!=null && productId>0){
            // Get product information
            logger.info("copyItemDataToPsTable productId ="+productId);
            ProductMaster productMaster = this.productInfoRepository.findOne(productId);
            if(productMaster!=null){
                CandidateWorkRequest candidateWorkRequest = this.morphDsdItemConverter.convertProductDataToPsWorkRqst(productMaster,bicepVendors,userInfo);
                candidateWorkRequest = candidateWorkRequestRepository.save(candidateWorkRequest);
                psWorkId = candidateWorkRequest.getWorkRequestId();
                logger.info("psWorkId = "+psWorkId);
                if(psWorkId!=null && psWorkId>0) {
                    CandidateItemMaster psItemMaster = this.candidateItemMasterRepository.findFirstByCandidateWorkRequestIdAndCandidateVendorLocationItemsNewDataAndCandidateWarehouseLocationItemsNewData(psWorkId, true, true);
                    if (psItemMaster == null) {
                        throw new MorphBusinessRuleException("Data Invalid");
                    }
                }
            } else {
                throw new MorphBusinessRuleException("Product Not Exist");
            }

        }
        logger.info("psWorkId end = "+psWorkId);
        return psWorkId;
    }
}
