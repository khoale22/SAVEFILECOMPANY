package com.heb.pm.massUpdate;
import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.FulfillmentChannel;
import com.heb.pm.entity.PDPTemplate;
import com.heb.pm.entity.SalesChannel;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Access;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * REST controller to support the mass update functions.
 *
 * @author d116773
 * @since 2.12.0
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL)
@AuthorizedResource(ResourceConstants.MASS_UPDATE)
public class MassUpdateController {

	private static final Logger logger = LoggerFactory.getLogger(MassUpdateController.class);

	private static final String SUCCESS_MESSAGE = "Mass update submitted with tracking ID %d";
	private static final String MASS_UPDATE_LOG_MESSAGE =
			"User %s from IP %s requested a mass update with the following parameters: %s to this product criteria %s";
	private static final String GET_SALES_CHANNELS =
			"User %s from IP %s, has requested all the sales channels for custom hierarchy";
	private static final String GET_PDP_TEMPLATES =
			"User %s from IP %s, has requested all the PDP templates for custom hierarchy";
	private static final String GET_FULFILLMENT_PROGRAMS =
			"User %s from IP %s, has requested all the fulfillment programs for custom hierarchy";
	private static final String NO_PERMISSION_UPDATE_SHOW_ON_SITE_ERROR_MESSAGE = "You do not have permission to update show on site.";

	/**
	 * Response class to send back to the front end once the job is started.
	 */
	public class MassUpdateResponse {
		private String message;
		private Long transactionId;

		/**
		 * Constructs a new MassUpdateResponse.
		 *
		 * @param transactionId The transaction ID for the job.
		 * @param message       Any message for the user.
		 */
		public MassUpdateResponse(Long transactionId, String message) {
			this.transactionId = transactionId;
			this.message = message;
		}

		/**
		 * Returns any messages for the user.
		 *
		 * @return Messages for the user.
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * Returns the transaction ID for the user's mass update job. This will be
		 *
		 * @return The transaction ID for the user's mass update job.
		 */
		public Long getTransactionId() {
			return transactionId;
		}

		/**
		 * Returns a string representation of this object.
		 *
		 * @return A string representation of this object.
		 */
		@Override
		public String toString() {
			return "MassUpdateResponse{" +
					"message='" + message + '\'' +
					", transactionId=" + transactionId +
					'}';
		}
	}

	@Autowired
	private MassUpdateService massUpdateService;

	@Autowired
	private UserInfo userInfo;

	/**
	 * Submits a mass update job.
	 *
	 * @param request The parameters for the user's request for a mass update.
	 * @return A response with the transaction ID and a message.
	 */
	@ViewPermission()
	@RequestMapping(method = RequestMethod.POST, value = "/massUpdate")
	public MassUpdateResponse submitMassUpdate(@RequestBody MassUpdateRequest massUpdateRequest,
											   HttpServletRequest request) {

		this.logMassUpdateRequest(request.getRemoteAddr(), massUpdateRequest);

		// Check that the user has access.
		this.validatePermission(massUpdateRequest);

		// Pull the logged in user out of the session.
		massUpdateRequest.getParameters().setUserId(this.userInfo.getUserId());
		Long transactionId = this.massUpdateService.submitMassUpdate(massUpdateRequest);

		MassUpdateResponse response = new MassUpdateResponse(transactionId,
				String.format(MassUpdateController.SUCCESS_MESSAGE, transactionId));

		MassUpdateController.logger.info(response.toString());

		return response;
	}

	/**
	 * This method returns all of the possible sales channels used for creating product fulfillment programs
	 *
	 * @param request simple http request
	 * @return
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "getAllSalesChannels")
	public List<SalesChannel> getAllSalesChannels(HttpServletRequest request) {
		MassUpdateController.logger.info(String.format(GET_SALES_CHANNELS, this.userInfo.getUserId(), request.getRemoteAddr()));
		return this.massUpdateService.getAllSalesChannels();
	}

	/**
	 * This method returns all of the possible pdp templates that can be tied to a product as a dynamic attribute
	 *
	 * @param request simple http request
	 * @return
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "getAllPDPTemplates")
	public List<PDPTemplate> getAllPDPTemplates(HttpServletRequest request) {
		MassUpdateController.logger.info(String.format(GET_PDP_TEMPLATES, this.userInfo.getUserId(), request.getRemoteAddr()));
		return this.massUpdateService.getAllPDPTemplates();
	}

	/**
	 * This method returns all of the possible fulfillment programs possible to create a product fulfillment program
	 *
	 * @param request simple http request
	 * @return
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "getAllFulfillmentPrograms")
	public List<FulfillmentChannel> getAllFulfillmentPrograms(HttpServletRequest request) {
		MassUpdateController.logger.info(String.format(GET_FULFILLMENT_PROGRAMS, this.userInfo.getUserId(), request.getRemoteAddr()));
		return this.massUpdateService.getAllFulfillmentPrograms();
	}


	/**
	 * Logs a user's request to mass update products.
	 *
	 * @param ipAddress The IP address the user is logged in from.
	 * @param request   The user's request parameters.
	 */
	private void logMassUpdateRequest(String ipAddress, MassUpdateRequest request) {

		MassUpdateController.logger.info(String.format(MassUpdateController.MASS_UPDATE_LOG_MESSAGE, "",
				ipAddress, request.getParameters(), request.getProductSearchCriteria()));
	}

	/**
	 * Validates the user has the permissions to mass update the attribute they requested. This will throw an
	 * exception if they do not have permissions.
	 *
	 * @param massUpdateRequest The request the user sent to do the mass update.
	 */
	private void validatePermission(MassUpdateRequest massUpdateRequest) {

		if (massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.THIRD_PARTY_SELLABLE)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.MASS_UPDATE_THIRD_PARTY_SELLABLE)) {
				throw new AccessDeniedException("You do not have permission to perform a mass update on the third party sellable attribute.");
			}
		}

		if (massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.PRODUCE_ORGANIC)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.MASS_UPDATE_PRODUCE_ORGANIC)) {
				throw new AccessDeniedException("You do not have permission to perform a mass update on the produce organic attribute.");
			}
		}

		if (massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.GO_LOCAL)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.MASS_UPDATE_GO_LOCAL)) {
				throw new AccessDeniedException("You do not have permission to perform a mass update on the go local attribute.");
			}
		}

		if (massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.TOTALLY_TEXAS)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.MASS_UPDATE_TOTALLY_TEXAS)) {
				throw new AccessDeniedException("You do not have permission to perform a mass update on the totally Texas attribute.");
			}
		}

		if (massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.SELECT_INGREDIENTS)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.MASS_UPDATE_SELECT_INGREDIENTS)) {
				throw new AccessDeniedException("You do not have permission to perform a mass update on the select ingredients attribute.");
			}
		}

		if (massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.FOOD_STAMP)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.MASS_UPDATE_FOOD_STAMP)) {
				throw new AccessDeniedException("You do not have permission to perform a mass update on the food stamp attribute.");
			}
		}

		if (massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.FSA)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.MASS_UPDATE_FSA)) {
				throw new AccessDeniedException("You do not have permission to perform a mass update on the FSA attribute.");
			}
		}

		if (massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.TAX_FLAG)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.MASS_UPDATE_TAX_FLAG)) {
				throw new AccessDeniedException("You do not have permission to perform a mass update on the tax flag attribute.");
			}
		}

		if (massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.SELF_MANUFACTURED)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.MASS_UPDATE_SELF_MANUFACTURED)) {
				throw new AccessDeniedException("You do not have permission to perform a mass update on the self manufactured attribute.");
			}
		}

		if (massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.TAX_CATEGORY)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.MASS_UPDATE_TAX_CATEOGRY)) {
				throw new AccessDeniedException("You do not have permission to perform a mass update on the tax category attribute.");
			}
		}

		if (massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.UNASSIGN_PRODUCTS)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.MASS_UPDATE_UNASSIGN_PRODUCTS)) {
				throw new AccessDeniedException("You do not have permission to unassign these products.");
			}
		}

		if (massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.ADD_HIERARCHY_PRODUCT)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.CUSTOM_HIERARCHY_EDIT)) {
				throw new AccessDeniedException("You do not have permission to Add these products.");
			}
		}

		if (massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.MOVE_HIERARCHY_PRODUCT)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.CUSTOM_HIERARCHY_EDIT)) {
				throw new AccessDeniedException("You do not have permission to Move these products.");
			}
		}

		if (massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.REMOVE_HIERARCHY_PRODUCT)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.CUSTOM_HIERARCHY_EDIT)) {
				throw new AccessDeniedException("You do not have permission to Remove these products.");
			}
		}

		if (massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.PDP_TEMPLATE)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.CUSTOM_HIERARCHY_EDIT)) {
				throw new AccessDeniedException("You do not have permission to update the PDP Template.");
			}
		}

		if (massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.FULFILLMENT_CHANNEL)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.CUSTOM_HIERARCHY_EDIT)) {
				throw new AccessDeniedException("You do not have permission to update the Fulfillment Program.");
			}
		}

		if(massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.PRIMARY_PATH)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.CUSTOM_HIERARCHY_EDIT)) {
				throw new AccessDeniedException("You do not have permission to change the primary path.");
			}
		}

		if(massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.ADD_HIERARCHY_PRODUCT_GROUP)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.CUSTOM_HIERARCHY_EDIT)) {
				throw new AccessDeniedException("You do not have permission to add this product group.");
			}
		}

		if(massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.REMOVE_HIERARCHY_PRODUCT_GROUP)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.CUSTOM_HIERARCHY_EDIT)) {
				throw new AccessDeniedException("You do not have permission to remove this product group.");
			}
		}

		// With primo-pick, need to go a level further down.
		if (massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.PRIMO_PICK)) {
			// Everything else, you need to be an approver
			if (massUpdateRequest.getParameters().getPrimoPickFunction().equals(MassUpdateParameters.PrimoPickFunction.APPROVE_PRIMO_PICK)) {
				if (!this.userInfo.canUserEditResource(ResourceConstants.MASS_UPDATE_PRIMO_PICK_APPROVER)) {
					throw new AccessDeniedException("You do not have permission to perform a mass update to approve primo picks.");
				}
			}

			if (massUpdateRequest.getParameters().getPrimoPickFunction().equals(MassUpdateParameters.PrimoPickFunction.TURN_OFF_PRIMO_PICK)) {
				if (!this.userInfo.canUserEditResource(ResourceConstants.MASS_UPDATE_PRIMO_PICK_APPROVER)) {
					throw new AccessDeniedException("You do not have permission to perform a mass update to end a primo picks.");
				}
			}

			if (massUpdateRequest.getParameters().getPrimoPickFunction().equals(MassUpdateParameters.PrimoPickFunction.TURN_ON_PRIMO_PICK)) {
				if (!this.userInfo.canUserEditResource(ResourceConstants.MASS_UPDATE_PRIMO_PICK_APPROVER)) {
					throw new AccessDeniedException("You do not have permission to perform a mass update to make something a primo picks.");
				}
			}

			if (massUpdateRequest.getParameters().getPrimoPickFunction().equals(MassUpdateParameters.PrimoPickFunction.REJECT_PRIMO_PICK)) {
				if (!this.userInfo.canUserEditResource(ResourceConstants.MASS_UPDATE_PRIMO_PICK_APPROVER)) {
					throw new AccessDeniedException("You do not have permission to perform a mass update to reject a primo picks.");
				}
			}

			if (massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.TAG_TYPE)) {
				if (!this.userInfo.canUserEditResource(ResourceConstants.MASS_UPDATE_TAG_TYPE)) {
					throw new AccessDeniedException("You do not have permission to update tag type.");
				}
			}
		}
		// Check EBM permission
		if (massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.SHOW_ON_SITE)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.SHOW_ON_SITE)) {
				throw new AccessDeniedException(NO_PERMISSION_UPDATE_SHOW_ON_SITE_ERROR_MESSAGE);
			}
		}

		//Check Distinctive permission
		if (massUpdateRequest.getParameters().getAttribute().equals(MassUpdateParameters.Attribute.DISTINCTIVE)) {
			if (!this.userInfo.canUserEditResource(ResourceConstants.SHELF_ATTRIBUTES_DISTINCTIVE)) {
				throw new AccessDeniedException("You do not have permission to perform a mass update to modify the distinctive attribute.");
			}
		}
	}
}