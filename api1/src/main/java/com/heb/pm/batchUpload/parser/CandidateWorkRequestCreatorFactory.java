package com.heb.pm.batchUpload.parser;

import com.heb.pm.batchUpload.BatchUploadType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
/**
 * This is CandidateWorkRequestCreatorFactory class.
 *
 * @author vn55306
 * @since 2.8.0
 */
@Service
public class CandidateWorkRequestCreatorFactory {

	@Autowired
	private CandidateWorkRequestCreator assortmentCandidateWorkRequestCreator;
	@Autowired
	private CandidateWorkRequestCreator wineCandidateWorkRequestCreator;
	@Autowired
	private CandidateWorkRequestCreator primoPickCandidateWorkRequestCreator;
	@Autowired
	private CandidateWorkRequestCreator nutritionCandidateWorkRequestCreator;
	@Autowired
	private CandidateWorkRequestCreator ecommerceAttributeCandidateWorkRequestCreator;
	@Autowired
	private CandidateWorkRequestCreator serviceCaseSignCandidateWorkRequestCreator;
	@Autowired
	private CandidateWorkRequestCreator relatedProductsCandidateWorkRequestCreator;
	@Autowired
	private CandidateWorkRequestCreator ebmBdaCandidateWorkRequestCreator;

	public CandidateWorkRequestCreator getCreator(BatchUploadType type) {

		switch (type) {
			case ASSORTMENT:
				return this.assortmentCandidateWorkRequestCreator;
		    case WINE:
				return this.wineCandidateWorkRequestCreator;
			case PRIMO_PICK:
				return this.primoPickCandidateWorkRequestCreator;
			case NUTRITION:
				return this.nutritionCandidateWorkRequestCreator;
			case BIG_5:
				return this.ecommerceAttributeCandidateWorkRequestCreator;
			case SERVICE_CASE_SIGNS:
				return this.serviceCaseSignCandidateWorkRequestCreator;
			case RELATED_PRODUCTS:
				return this.relatedProductsCandidateWorkRequestCreator;
			case EBM_BDA:
				return this.ebmBdaCandidateWorkRequestCreator;
			default:
				throw new IllegalArgumentException("parser not defined");
		}

	}

}
