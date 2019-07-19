package com.heb.pm.productDetails.product.SpecialAttributes;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.DrugScheduleType;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.productDetails.product.ShelfAttributesController;
import com.heb.util.audit.AuditRecord;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + PharmacyController.SPECIAL_ATTRIBUTES)
@AuthorizedResource(ResourceConstants.SPECIAL_ATTRIBUTES_PHARMACY)
public class PharmacyController {

	private static final Logger logger = LoggerFactory.getLogger(ShelfAttributesController.class);

	protected static final String SPECIAL_ATTRIBUTES = "/specialAttributes";
	private static final String GET_DRUG_SCHEDULE="/getDrugSchedule";
	private static final String SAVE_PHARMACY_CHANGES = "/savePharmacyChanges";
	private static final String GET_PHARMACY_AUDITS = "/getPharmacyAudits";

	private static final String LOG_PHARMACY_AUDIT_BY_PRODUCT_ID = "User %s from IP %s has requested special attributes pharmacy audit information for prod ID: %s";

	private static final String LOG_DRUG_SCHEDULE =
			"User %s from IP %s has requested the drug schedule code table";

	private static final String PHARMACY_SAVE_LOG =
			"User %s from IP %s requested to update the pharmacy item [%s].";
	private static final String PHARMACY_SAVE_LOG_COMPLETED =
			"Pharmacy item [%s] has completed updating.";

	private static final String UPDATE_SUCCESS_MESSAGE =
			"SpecialAttributesPharmacyController.updateSuccessful";

	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE = "Pharmacy product: %d updated successfully.";

	@Autowired
	private PharmacyService service;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	/**
	 * Returns a list of description types to verify the length of all descriptions for front end validation.
	 * @param request the http request.
	 * @return list of description types.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_DRUG_SCHEDULE)
	public List<DrugScheduleType> getDrugScheduleType(HttpServletRequest request) {
		this.logGetDrugSchedule(request.getRemoteAddr());
		List<DrugScheduleType> result=this.service.getDrugSchedulerTypes();
		return result;
	}

	/**
	 * Saves any pharmacy changes.
	 * @param productMaster
	 * @param request
	 * @return an update product Master
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = SAVE_PHARMACY_CHANGES)
	public ModifiedEntity<ProductMaster> savePharmacyChanges(@RequestBody ProductMaster productMaster, HttpServletRequest request) {
		this.logSavePharmacyChanges(request.getRemoteAddr(), productMaster.getProdId());

		ProductMaster updatedProductMaster = this.service.savePharmacyChanges(productMaster);
		this.logSavePharmacyChangesCompleted(productMaster.getProdId());

		String updateMessage = this.messageSource.getMessage(
				PharmacyController.UPDATE_SUCCESS_MESSAGE,
				new Object[]{productMaster.getProdId()}, String.format(PharmacyController.DEFAULT_UPDATE_SUCCESS_MESSAGE, productMaster.getProdId()), request.getLocale());

		return new ModifiedEntity<>(updatedProductMaster, updateMessage);
	}

	/**
	 * Logs the saving of a pharmacy change completion.
	 * @param prodId
	 */
	private void logSavePharmacyChangesCompleted(Long prodId) {
		PharmacyController.logger.info(
				String.format(PharmacyController.PHARMACY_SAVE_LOG_COMPLETED, prodId)
		);
	}

	/**
	 *
	 * @param remoteAddr
	 * @param prodId
	 */
	private void logSavePharmacyChanges(String remoteAddr, Long prodId) {
		PharmacyController.logger.info(
				String.format(PharmacyController.PHARMACY_SAVE_LOG, this.userInfo.getUserId(),
						remoteAddr, prodId)
		);
	}

	/**
	 * Sets the UserInfo for this class to use. This is primarily for testing.
	 *
	 * @param userInfo The UserInfo for this class to use.
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * Log's a user's request to get all records for a prodId.
	 *
	 * @param ip The IP address th user is logged in from.
	 */
	private void logGetDrugSchedule(String ip){
		PharmacyController.logger.info(String.format(LOG_DRUG_SCHEDULE, this.userInfo.getUserId(), ip));
	}

	/**
	 * Retrieves Pharmacy audit information.
	 * @param prodId The Product ID that the audit is being searched on.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of Pharmacy audits attached to given product ID.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = PharmacyController.GET_PHARMACY_AUDITS)
	public List<AuditRecord> getPharmacyAuditInfo(@RequestParam(value="prodId") Long prodId, HttpServletRequest request) {
		this.logGetPharmacyAuditInformation(request.getRemoteAddr(), prodId);
		List<AuditRecord> pharmacyAuditRecords = this.service.getPharmacyAuditInformation(prodId);
		return pharmacyAuditRecords;
	}

	/**
	 * Logs get pharmacy audit information by prodId.
	 *
	 * @param ip The user's ip.
	 * @param prodId The prodId being searched on.
	 */
	private void logGetPharmacyAuditInformation(String ip, Long prodId) {
		PharmacyController.logger.info(
				String.format(PharmacyController.LOG_PHARMACY_AUDIT_BY_PRODUCT_ID, this.userInfo.getUserId(), ip, prodId)
		);
	}
}
