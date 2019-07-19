package com.heb.pm.productDetails.product.SpecialAttributes;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.TobaccoProduct;
import com.heb.pm.entity.TobaccoProductType;
import com.heb.util.audit.AuditRecord;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * This is the controller for pharmacy
 *
 * @author s753601
 * @since 2.13.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + TobaccoController.SPECIAL_ATTRIBUTES)
@AuthorizedResource(ResourceConstants.SPECIAL_ATTRIBUTES_TOBACCO)
public class TobaccoController {

	private static final Logger logger = LoggerFactory.getLogger(TobaccoController.class);

	private static final String GET_TOBACCO_AUDITS = "/getTobaccoAudits";
	protected static final String SPECIAL_ATTRIBUTES = "/specialAttributes";
	private static final String GET_TOBACCO_TYPES = "/getTobaccoTypes";
	private static final String GET_TOBACCO_PRODUCT = "/getTobaccoProduct";
	private static final String SAVE_TOBACCO_PRODUCT="/saveTobaccoProduct";

	private static final String TOBACCO_TYPE_LOG = "User %s from IP %s requested for the Tobacco Product Types";
	private static final String TOBACCO_PRODUCT_LOG = "User %s from IP %s requested for the Tobacco Product with id %s";
	private static final String SAVE_TOBACCO_PRODUCT_LOG = "User %s from IP %s requested to update Tobacco Product information for Product Master with id %s";
	private static final String UPDATE_SUCCESS_MESSAGE = "Tobacco prouduct [%d] has been successfully updated.";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE = "TobaccoProductController.saveTobaccoProductChanges";
	private static final String LOG_TOBACCO_AUDIT_BY_PRODUCT_ID = "User %s from IP %s has requested special attributes tobacco audit information for prod ID: %s";

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private TobaccoService service;

	@Autowired
	private MessageSource messageSource;

	/**
	 * Returns a list of description types to verify the length of all descriptions for front end validation.
	 * @param request the http request.
	 * @return list of description types.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_TOBACCO_TYPES)
	public List<TobaccoProductType> getTobaccoProductType(HttpServletRequest request) {
		TobaccoController.logger.info(String.format(TOBACCO_TYPE_LOG, this.userInfo.getUserId(), request.getRemoteAddr()));
		List<TobaccoProductType> result=this.service.getTobaccoProductTypes();
		return result;
	}

	/**
	 * Returns a products tobacco product infromation
	 * @param request the http request.
	 * @return list of description types.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_TOBACCO_PRODUCT)
	public TobaccoProduct getTobaccoProduct(@RequestParam long prodId, HttpServletRequest request) {
		TobaccoController.logger.info(String.format(TOBACCO_PRODUCT_LOG, this.userInfo.getUserId(), request.getRemoteAddr(), prodId));
		TobaccoProduct result=this.service.getTobaccoProduct(prodId);
		return result;
	}

	/**
	 * This updates a tobacco product where changes have been made from the code date screen.
	 * @param productMaster
	 * @param request
	 * @return
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = SAVE_TOBACCO_PRODUCT)
	public ModifiedEntity<ProductMaster> saveTobaccoChanges(@RequestBody ProductMaster productMaster, HttpServletRequest request) {
		TobaccoController.logger.info(String.format(SAVE_TOBACCO_PRODUCT_LOG, this.userInfo.getUserId(), request.getRemoteAddr(), productMaster.getProdId()));
		ProductMaster updatedProductMaster = this.service.saveTobaccoChanges(productMaster);

		String updateMessage = this.messageSource.getMessage(
				TobaccoController.DEFAULT_UPDATE_SUCCESS_MESSAGE,
				new Object[]{productMaster.getProdId()}, String.format(TobaccoController.UPDATE_SUCCESS_MESSAGE,
						productMaster.getProdId()), request.getLocale());
		return new ModifiedEntity<>(updatedProductMaster, updateMessage);
	}

	/**
	 * Retrieves Tobacco audit information.
	 * @param prodId The Product ID that the audit is being searched on.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of Tobacco audits attached to given product ID.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = TobaccoController.GET_TOBACCO_AUDITS)
	public List<AuditRecord> getTobaccoAuditInfo(@RequestParam(value="prodId") Long prodId, HttpServletRequest request) {
		this.logGetTobaccoAuditInformation(request.getRemoteAddr(), prodId);
		List<AuditRecord> tobaccoAuditRecords = this.service.getTobaccoAuditInformation(prodId);
		return tobaccoAuditRecords;
	}

	/**
	 * Logs get tobacco audit information by prodId.
	 *
	 * @param ip The user's ip.
	 * @param prodId The prodId being searched on.
	 */
	private void logGetTobaccoAuditInformation(String ip, Long prodId) {
		TobaccoController.logger.info(
				String.format(TobaccoController.LOG_TOBACCO_AUDIT_BY_PRODUCT_ID, this.userInfo.getUserId(), ip, prodId)
		);
	}
}
