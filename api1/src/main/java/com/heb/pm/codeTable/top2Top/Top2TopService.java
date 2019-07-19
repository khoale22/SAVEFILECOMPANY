/*
 *  Top2TopService.java
 *  
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.top2Top;

import com.heb.pm.entity.CostOwner;
import com.heb.pm.entity.TopToTop;
import com.heb.pm.repository.CostOwnerRepository;
import com.heb.pm.repository.TopToTopRepository;
import com.heb.pm.repository.TopToTopRepositoryWithoutCount;
import com.heb.util.jpa.PageableResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related to code table TopToTop information.
 *
 * @author vn73545
 * @since 2.41.0
 */
@Service
public class Top2TopService {

	private static final String UNABLE_TO_DELETE_ERROR_MESSAGE = "Unable to delete - In relationship code table.";
	private static final String ALREADY_EXISTS_ERROR_MESSAGE = "%s already exists.";

	@Autowired
	private TopToTopRepository topToTopRepository;

	@Autowired
	private TopToTopRepositoryWithoutCount topToTopRepositoryWithoutCount;

	@Autowired
	private CostOwnerRepository costOwnerRepository;
	
    /**
     * Find all TopToTops with pagination.
     *
     * @param page     	   the page number.
     * @param pageSize 	   the page size.
     * @param topToTopId   the TopToTop id to search.
     * @param topToTopName the TopToTop name to search.
     * @param includeCount the flag that check include count or not.
     * @return the page of TopToTops.
     */
    public PageableResult<TopToTop> findAllTop2Tops(int page, int pageSize,
    		String topToTopId, String topToTopName, boolean includeCount) {
    	Pageable pageRequest = new PageRequest(page, pageSize, TopToTop.getDefaultSort());
    	PageableResult<TopToTop> results;
    	if (includeCount) {
    		Page<TopToTop> topToTops = this.findAllTopToTopsWithCount(topToTopId, topToTopName, pageRequest);
    		results = new PageableResult<>(pageRequest.getPageNumber(), topToTops.getTotalPages(),
    				topToTops.getTotalElements(), topToTops.getContent());
    	} else {
    		List<TopToTop> topToTops = this.findAllTopToTopsWithoutCount(topToTopId, topToTopName, pageRequest);
    		results = new PageableResult<>(pageRequest.getPageNumber(), topToTops);
    	}
    	return results;
    }
    
    /**
     * Get all TopToTops with pagination.
     *
     * @param topToTopId   the TopToTop id to search.
     * @param topToTopName the TopToTop name to search.
     * @param pageRequest  the page request for pagination.
     * @return the page of TopToTops.
     */
    private Page<TopToTop> findAllTopToTopsWithCount(String topToTopId, String topToTopName, Pageable pageRequest) {
        Page<TopToTop> topToTops;
        if (StringUtils.isEmpty(topToTopId) && StringUtils.isEmpty(topToTopName)) {
            // get all TopToTops.
            topToTops = this.topToTopRepository.findAll(pageRequest);
        } else if (!StringUtils.isEmpty(topToTopId) && StringUtils.isEmpty(topToTopName)) {
            // get all TopToTops filter by TopToTop id.
            topToTops = this.topToTopRepository.findById(topToTopId, pageRequest);
        } else if (StringUtils.isEmpty(topToTopId) && !StringUtils.isEmpty(topToTopName)) {
            // get all TopToTops filter by TopToTop name.
            topToTops = this.topToTopRepository.findByTopToTopNameIgnoreCaseContaining(topToTopName, pageRequest);
        } else {
            // get all TopToTops filter by TopToTop id and TopToTop name.
            topToTops = this.topToTopRepository.findByIdAndName(
            		topToTopId, topToTopName, pageRequest);
        }
        return topToTops;
    }

    /**
     * Get all TopToTops with pagination.
     *
     * @param topToTopId   the TopToTop id to search.
     * @param topToTopName the TopToTop name to search.
     * @param pageRequest  the page request for pagination.
     * @return the list of TopToTops.
     */
    private List<TopToTop> findAllTopToTopsWithoutCount(String topToTopId, String topToTopName, Pageable pageRequest) {
    	List<TopToTop> topToTops;
    	if (StringUtils.isEmpty(topToTopId) && StringUtils.isEmpty(topToTopName)) {
    		// get all TopToTops.
    		topToTops = this.topToTopRepositoryWithoutCount.findAll(pageRequest).getContent();
    	} else if (!StringUtils.isEmpty(topToTopId) && StringUtils.isEmpty(topToTopName)) {
    		// get all TopToTops filter by TopToTop id.
    		topToTops = this.topToTopRepositoryWithoutCount.findById(topToTopId, pageRequest);
    	} else if (StringUtils.isEmpty(topToTopId) && !StringUtils.isEmpty(topToTopName)) {
    		// get all TopToTops filter by TopToTop name.
    		topToTops = this.topToTopRepositoryWithoutCount.findByTopToTopNameIgnoreCaseContaining(topToTopName, pageRequest);
    	} else {
    		// get all TopToTops filter by TopToTop id and TopToTop name.
    		topToTops = this.topToTopRepositoryWithoutCount.findByIdAndName(
    				topToTopId, topToTopName, pageRequest);
    	}
    	return topToTops;
    }
    
    /**
     * Add new TopToTops.
     *
     * @param topToTops the list of added TopToTops.
     * @return the list of newly added TopToTops.
     */
    public List<TopToTop> addTopToTops(List<TopToTop> topToTops) {
    	List<TopToTop> newTopToTops = new ArrayList<>();
    	if(CollectionUtils.isEmpty(topToTops)){
    		return newTopToTops;
    	}
    	Integer maxId = this.topToTopRepository.findFirstByOrderByTopToTopIdDesc().getTopToTopId();
    	for(TopToTop topToTop : topToTops) {
    		topToTop.setTopToTopId(++maxId);
    		newTopToTops.add(topToTop);
    	}
    	return this.topToTopRepository.save(newTopToTops);
    }

    /**
     * Validate TopToTop is existed or not.
     *
     * @param topToTops the list of added TopToTops.
     * @return the list of existed TopToTops.
     */
    public List<TopToTop> validateTopToTops(List<TopToTop> topToTops) {
    	List<TopToTop> newTopToTops = new ArrayList<>();
    	if(CollectionUtils.isNotEmpty(topToTops)){
    		for(TopToTop topToTop : topToTops) {
    			List<TopToTop> topToTopList = 
    					this.topToTopRepositoryWithoutCount.findByName(topToTop.getTopToTopName());
    			if(CollectionUtils.isNotEmpty(topToTopList)){
    				newTopToTops.add(topToTop);
    			}
    		}
    	}
    	return newTopToTops;
    }

    /**
     * Update a TopToTop's description. Throws error if description is already in use.
     *
     * @param topToTop The TopToTop to be updated.
     * @return The updated TopToTop.
     */
    public TopToTop updateTopToTop(TopToTop topToTop) {
		List<TopToTop> topToTops = 
    			this.topToTopRepositoryWithoutCount.findByName(topToTop.getTopToTopName());
    	if(CollectionUtils.isNotEmpty(topToTops)){
    		for (TopToTop duplicateTopToTop : topToTops) {
    			if (!duplicateTopToTop.getTopToTopId().equals(topToTop.getTopToTopId())) {
    				throw new IllegalArgumentException(String.format(ALREADY_EXISTS_ERROR_MESSAGE, topToTop.getTopToTopName()));
    			}
			}
    	}
    	return this.topToTopRepository.save(topToTop);
    }
    
    /**
     * Delete a TopToTop. Throws exception if the TopToTop is tied to a product.
     *
     * @param topToTopId The TopToTop's id to be deleted.
     */
    public void deleteTopToTop(Integer topToTopId) {
    	CostOwner costOwner = this.costOwnerRepository.findFirstByTopToTopId(topToTopId);
    	if(costOwner != null){
    		throw new IllegalArgumentException(UNABLE_TO_DELETE_ERROR_MESSAGE);
    	}
    	TopToTop topToTop = this.topToTopRepository.findOne(topToTopId);
    	if(topToTop != null) {
    		this.topToTopRepository.delete(topToTop);
    	}
    }
}
