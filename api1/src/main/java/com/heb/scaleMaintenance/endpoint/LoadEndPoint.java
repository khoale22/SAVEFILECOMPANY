package com.heb.scaleMaintenance.endpoint;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ResourceConstants;
import com.heb.scaleMaintenance.ScaleMaintenanceConstants;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceTracking;
import com.heb.scaleMaintenance.model.ScaleMaintenanceLoadParameters;
import com.heb.scaleMaintenance.service.ScaleMaintenanceService;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Rest endpoint for scale maintenance loads.
 *
 * @author m314029
 * @since 2.17.8
 */
@RestController()
@RequestMapping(ScaleMaintenanceConstants.BASE_SCALE_MAINTENANCE_URL + LoadEndPoint.BASE_LOAD_URL)
@AuthorizedResource(ResourceConstants.SCALE_MAINTENANCE_SEND_LOAD)
public class LoadEndPoint{
	private static final Logger logger = LoggerFactory.getLogger(LoadEndPoint.class);

	// urls
	static final String BASE_LOAD_URL = "/load";
	private static final String AVAILABLE_LOAD_STORES_URL = "/availableStores";
	//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	private static final String AVAILABLE_LOAD_STORES_BY_DEPARTMENT_URL = "/availableStoresByDepartment";

	// log messages
	private static final String SEND_PLU_TO_STORE_LOG_MESSAGE =
			"User %s from IP %s requested to send PLUs: %s to Store numbers: %s.";
	// log messages
	private static final String SEND_PLU_TO_STORE_BY_DEPT_LOG_MESSAGE =
				"User %s from IP %s requested to send PLUs from Store numbers: %s and department Nbr: %s of effectiveDate: %s to target Store:%s. ";
   //End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani				
	private static final String GET_AVAILABLE_LOAD_STORES_LOG_MESSAGE =
			"User %s from IP %s requested all available stores to send a scale maintenance load to.";

	// error messages
	private static final String PLU_MESSAGE_KEY = "UpcPluMaintenanceController.missingPlus";
	private static final String DEFAULT_NO_PLU_MESSAGE = "Must search for at least one PLU.";
	private static final String NO_STORE_MESSAGE_KEY = "EPlumController.missingStoreNumber";
	private static final String DEFAULT_NO_STORE_MESSAGE = "Must have a store to send to.";
	private static final String NO_LOAD_MESSAGE_KEY = "EPlumController.missingLoad";
	private static final String DEFAULT_NO_LOAD_MESSAGE = "Must have a load to send to store.";

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private ScaleMaintenanceService service;

	/**
	 * Sends information to ePlum API to create a scale maintenance load.
	 *
	 * @param scaleMaintenanceLoadParameters Information surrounding the stores and PLU to send to ePlum.
	 * @param request The HTTP request that initiated this call.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST)
	public ScaleMaintenanceTracking submitLoadToStoresForPlus(
			@RequestBody ScaleMaintenanceLoadParameters scaleMaintenanceLoadParameters,
			HttpServletRequest request){
		//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani	
		if(scaleMaintenanceLoadParameters.getTargetStore() != null) {
			return this.submitLoadToStoresByDepartment(scaleMaintenanceLoadParameters, request);
		}else {		
			this.parameterValidator.validate(
					scaleMaintenanceLoadParameters,
					LoadEndPoint.DEFAULT_NO_LOAD_MESSAGE,
					LoadEndPoint.NO_LOAD_MESSAGE_KEY,
					request.getLocale());

			this.parameterValidator.validate(
					scaleMaintenanceLoadParameters.getStores(),
					LoadEndPoint.DEFAULT_NO_STORE_MESSAGE,
					LoadEndPoint.NO_STORE_MESSAGE_KEY,
					request.getLocale());

			this.parameterValidator.validate(
					scaleMaintenanceLoadParameters.getUpcs(),
					LoadEndPoint.DEFAULT_NO_PLU_MESSAGE,
					LoadEndPoint.PLU_MESSAGE_KEY,
					request.getLocale());
			this.logSubmitLoadToStoreForPlus(
					scaleMaintenanceLoadParameters.getStores(),
					scaleMaintenanceLoadParameters.getUpcs(),
					request.getRemoteAddr());
			return this.service.submitLoadToStoresForPlu(
					scaleMaintenanceLoadParameters,
					request.getRemoteAddr()); 
		}
		//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	}

	/**
	 * Gets available stores to send a scale maintenance load to.
	 *
	 * @param request The HTTP request that initiated this call.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value=AVAILABLE_LOAD_STORES_URL)
	public List<Integer> getAvailableLoadStores(HttpServletRequest request){
		this.logGetAvailableLoadStores(request.getRemoteAddr());
		return this.service.getAvailableLoadStores();
	}

	/**
	 * Log's a user's request to get available stores to send a load to.
	 *
	 * @param ip The IP address th user is logged in from.
	 */
	private void logGetAvailableLoadStores(String ip) {
		LoadEndPoint.logger.info(
				String.format(LoadEndPoint.GET_AVAILABLE_LOAD_STORES_LOG_MESSAGE,
						this.userInfo.getUserId(), ip));
	}

	/**
	 * Log's a user's request to send PLU information to stores.
	 *
	 * @param ip The IP address th user is logged in from.
	 * @param stores The store numbers to send data to.
	 * @param plus The PLUs the information requested pertains to.
	 */
	private void logSubmitLoadToStoreForPlus(String stores, String plus, String ip) {
		LoadEndPoint.logger.info(
				String.format(LoadEndPoint.SEND_PLU_TO_STORE_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, plus, stores));
	}
	
	//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	/**
	 * Log's a user's request to send PLU information to stores.
	 *
	 * @param ip The IP address th user is logged in from.
	 * @param stores The store numbers to send data to.
	 * @param plus The PLUs the information requested pertains to.
	 */
	private void logSubmitLoadToStoreByDepartment(String stores, String departmentNbr, String targetStoreNo, Date effectiveDate, String ip) {
		LoadEndPoint.logger.info(
				String.format(LoadEndPoint.SEND_PLU_TO_STORE_BY_DEPT_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, stores, departmentNbr,effectiveDate,targetStoreNo));
	}
	
	
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value=AVAILABLE_LOAD_STORES_BY_DEPARTMENT_URL)
	public List<Integer> getAvailableLoadByDepartmentStores(HttpServletRequest request){
		this.logGetAvailableLoadStores(request.getRemoteAddr());
		return this.service.getAvailableLoadStores();
	}
	
	/**
	 * Sends information to ePlum API to create a scale maintenance load by Department.
	 *
	 * @param scaleMaintenanceLoadParameters Information surrounding the stores and PLU to send to ePlum.
	 * @param request The HTTP request that initiated this call.
	 */
	
	public ScaleMaintenanceTracking submitLoadToStoresByDepartment(
			@RequestBody ScaleMaintenanceLoadParameters scaleMaintenanceLoadParameters,
			HttpServletRequest request){
		scaleMaintenanceLoadParameters.setDepartmentNbr(populateField(scaleMaintenanceLoadParameters.getDepartmentNbr(),5));
		this.logSubmitLoadToStoreByDepartment(
				scaleMaintenanceLoadParameters.getStores(),
				scaleMaintenanceLoadParameters.getDepartmentNbr(),
				scaleMaintenanceLoadParameters.getTargetStore(),
				scaleMaintenanceLoadParameters.getEffectiveDate(),
				request.getRemoteAddr());

		return this.service.submitLoadToStoresByDepartmentNbr(
				scaleMaintenanceLoadParameters,
				request.getRemoteAddr());
	} 
	
	/**
	 * populating the field for the prefix and suffix
	 * @param field
	 * @param fieldLength
	 * @return string
	 */
	public static String populateField(Object field, int fieldLength){
		boolean primitive = field instanceof Integer || field instanceof Long ;
		String columnField = (field != null ? field.toString():"");
		StringBuffer resultColumn = null;
		int filedLength = columnField.length();
		int diff = fieldLength  - filedLength;
		if(diff >0) {
			resultColumn = new StringBuffer();
			if(!primitive)
				resultColumn.append(columnField);
			for(int i = 0; i< diff; i++){
				resultColumn.append(primitive ? "0" : " ");
			}
			if(primitive)
				resultColumn.append(columnField);
			
			columnField = resultColumn.toString();
		}
		if(columnField.length() > fieldLength){
			columnField = columnField.substring(0,fieldLength);
		}
		
		return columnField;
	}
	
	
	//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
}
