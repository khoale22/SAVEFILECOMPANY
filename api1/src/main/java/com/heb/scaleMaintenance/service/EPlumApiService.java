package com.heb.scaleMaintenance.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.heb.pm.ws.EmailServiceClient;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceTracking;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceTransmit;
import com.heb.scaleMaintenance.entity.Status;
import com.heb.scaleMaintenance.invatron.EPlumLogin;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceTrackingRepository;
import com.invatron.invatronlib.api.INVATRON_EXCEPTION;
import com.invatron.invatronlib.api.MESSAGE_DATA;
import com.invatron.invatronlib.api.MESSAGE_TYPE;
import com.invatron.invatronlib.api.MQ_CLIENT;
import com.invatron.invatronlib.api.TRANSACTION;

/**
 * Holds the business logic for ePlum Api.
 *
 * @author m314029
 * @since 2.17.8
 */
@Service
public class EPlumApiService {
	private static final Logger logger = LoggerFactory.getLogger(EPlumApiService.class);

	// transaction types
	private static final String LOGIN_TRANSACTION_TYPE = "LOGIN";
	private static final String LOGOUT_TRANSACTION_TYPE = "LOGOUT";
	private static final String BACK_TRANSACTION_TYPE = "BACK";

	// message types
	private static final String SESSION_MESSAGE_TYPE = "SESN";
	private static final String BATCH_MESSAGE_TYPE = "BTCH";

	// batch commands
	private static final String BATCH_IMPORT_COMMAND = "CCOIMPORT";
	private static final String SESSION_COMMAND = "SSN";
	private static final String BATCH_LOGOUT_COMMAND = "CCOLOGOUT";

	// message keys
	private static final String KEY_MESSAGE_KEY = "KEY";

	// time
	private static final int DEFAULT_TIMEOUT = 1000;

	// message constants
	private static final String EPLUM_MESSAGE_CHUNK_DELIMITER = ";";
	private static final String EPLUM_MESSAGE_DELIMITER = ",";

	// log messages
	private static final String SEND_EPLUM_MESSAGE = "Sending message to ePlum: %s.";
	private static final String RETRY_EPLUM_ALTERNATE_IP_MESSAGE = "Connection to first message broker ip failed; " +
			"retrying connection with alternate ip.";

	// error messages
	private static final String ACCESS_DENIED_ERROR_MESSAGE = "Access Denied.";
	private static final String MQ_CLIENT_CREATE_ERROR_MESSAGE = "Create MQ_CLIENT FAILED.";
	private static final String LOGIN_FAILED_ERROR_MESSAGE = "Unable to login to ePlum servers.";

	@Value("${ePlum.ip}")
	private String ip;

	@Value("${ePlum.backUpIp}")
	private String backUpIp;

	@Value("${ePlum.port}")
	private int port;

	@Value("${ePlum.username}")
	private String userName;

	@Value("${ePlum.password}")
	private String password;
	//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	@Value("${email.toAddressForLoadByDept}")
	private String toAddressForLoadByDept;
	
	@Value("${email.fromAddressForLoadByDept}")
	private String fromAddressForLoadByDept;

	@Value("${email.copyAddressForLoadByDept}")
	private String copyAddressForLoadByDept;
	
	private String emailMessage = "Hi All  ,   \r\n\r\n\t A Store %s / Dept %s Load is Send to Destination Store %s. Tracking Id %s.";
	@Value("${email.emailSubject}")
	private String emailSubject;
	@Autowired
    private EmailServiceClient emailService;
	
	@Autowired
	private ScaleMaintenanceTrackingRepository repository;
	//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

	/**
	 * Sends messages to ePlum Api.
	 *  @param scaleMaintenanceTransmits List of scale maintenance transmits that contain the messages.
	 * @param userIp User connecting/ logging into ePLum API.
	 */
	public void sendMessagesToEPlum(List<ScaleMaintenanceTransmit> scaleMaintenanceTransmits, String userIp) {

		if(scaleMaintenanceTransmits == null || scaleMaintenanceTransmits.isEmpty()){
			return;
		}
		MESSAGE_DATA loginResponse = null;
		try {
			loginResponse = this.connectAndLoginToEPlumApi(ip, userIp);
			TRANSACTION ePlumTransaction;
			MESSAGE_DATA ePlumMessage;
			for (ScaleMaintenanceTransmit scaleMaintenanceTransmit : scaleMaintenanceTransmits) {
				EPlumApiService.logger.debug(
						String.format(
								EPlumApiService.SEND_EPLUM_MESSAGE,
								scaleMaintenanceTransmit.getePlumMessage().toString()));
				if(scaleMaintenanceTransmit.getePlumMessage().getHeaderData() != null){
					ePlumTransaction = new TRANSACTION(BACK_TRANSACTION_TYPE);
					String currentMessage = BATCH_IMPORT_COMMAND
							.concat(EPLUM_MESSAGE_DELIMITER)
							.concat(SESSION_COMMAND)
							.concat(loginResponse.GetField(0, KEY_MESSAGE_KEY))
							.concat(EPLUM_MESSAGE_DELIMITER)
							.concat(scaleMaintenanceTransmit.getePlumMessage().getHeaderData())
							.concat(EPLUM_MESSAGE_CHUNK_DELIMITER)
							.concat(scaleMaintenanceTransmit.getePlumMessage().getPluData())
							.concat(EPLUM_MESSAGE_CHUNK_DELIMITER)
							.concat(scaleMaintenanceTransmit.getePlumMessage().getIngredientData())
							.concat(EPLUM_MESSAGE_CHUNK_DELIMITER)
							.concat(scaleMaintenanceTransmit.getePlumMessage().getNutrientData());
							//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
							currentMessage = currentMessage.replaceAll(";;",";");
						    //End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	
					ePlumMessage = new MESSAGE_DATA(currentMessage);
	
					ePlumTransaction.SendRequest(new MESSAGE_TYPE(BATCH_MESSAGE_TYPE), ePlumMessage);
					scaleMaintenanceTransmit.setePlumTransmissionId(String.valueOf(ePlumTransaction.GetId()));
					scaleMaintenanceTransmit.setStatusCode(Status.Code.TRANSMITTED.getId());
					//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
					ScaleMaintenanceTracking scaleMaintenanceTracking =  repository.findOne(scaleMaintenanceTransmit.getePlumBatchId());
					
					if(scaleMaintenanceTracking.getScaleTransactionTypeCode() == 2) {
						logger.debug("Sending Mail to: "+toAddressForLoadByDept + "From: "+fromAddressForLoadByDept + "With Copy Address"
								+copyAddressForLoadByDept );
						emailService.sendMail(fromAddressForLoadByDept, toAddressForLoadByDept, copyAddressForLoadByDept, emailSubject, 
								String.format(emailMessage,
										scaleMaintenanceTracking.getLoadParametersAsJson().getStores(),
										scaleMaintenanceTracking.getLoadParametersAsJson().getDepartmentNbr(),
										scaleMaintenanceTracking.getLoadParametersAsJson().getTargetStore(),
										scaleMaintenanceTracking.getTransactionId()));
					}
				}
				//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
			}
		} catch (IllegalArgumentException e) {
			// if there was an issue connecting with ePlum for any reason, set the status on the transmits to ERROR
			for (ScaleMaintenanceTransmit scaleMaintenanceTransmit : scaleMaintenanceTransmits) {
				scaleMaintenanceTransmit.setResponseMessage(e.getLocalizedMessage());
				scaleMaintenanceTransmit.setStatusCode(Status.Code.ERROR.getId());
			}
		} finally {
			// always log out from ePlum API
			if(loginResponse != null){
				// log out
				TRANSACTION ePlumTransaction = new TRANSACTION(LOGOUT_TRANSACTION_TYPE);
				MESSAGE_DATA logoutMessage = new MESSAGE_DATA(BATCH_LOGOUT_COMMAND);
				logoutMessage.SetField(0, KEY_MESSAGE_KEY, loginResponse.GetField(0, KEY_MESSAGE_KEY));
				ePlumTransaction.SendRequest(new MESSAGE_TYPE(SESSION_MESSAGE_TYPE), logoutMessage);
			}
		}
	}

	/**
	 * Create a connection and log in to ePlum API.
	 *
	 * @param clientIp IP of the client to connect/ log in from.
	 * @param userIp User connecting/ logging into ePLum API.
	 * @return Login response from ePlum API.
	 */
	private MESSAGE_DATA connectAndLoginToEPlumApi(String clientIp, String userIp) throws IllegalArgumentException {
		try {
			this.connectToEPlumApi(clientIp, userIp);
			return this.loginToEPlumApi(clientIp, userIp);
		} catch (IllegalArgumentException e) {
			return handleErrorResponse(clientIp, userIp, LOGIN_FAILED_ERROR_MESSAGE);
		}
	}

	/**
	 * Handles error response from ePlum. If the client ip does not equal the back-up ip, retry connect and login
	 * with back-up ip. Else throw the error message.
	 *
	 * @param clientIp Client ip to attempt connection with ePlum.
	 * @param userIp User attempting to connect.
	 * @param errorMessage Error message to throw.
	 * @return Login response from ePlum API.
	 */
	private MESSAGE_DATA handleErrorResponse(String clientIp, String userIp, String errorMessage) throws IllegalArgumentException {
		if(!backUpIp.equals(clientIp)){
			logger.info(RETRY_EPLUM_ALTERNATE_IP_MESSAGE);
			return this.connectAndLoginToEPlumApi(backUpIp, userIp);
		} else {
			logger.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}
	}

	/**
	 * Logs in to the ePlum API with the supplied username and password. If the user is unable to, or if any other
	 * exception occurs, this method throws an Illegal Argument exception.
	 *
	 * @param clientIp IP of the client trying to log in.
	 * @param userIp User trying to log in.
	 * @return Response of logging in to the ePlum API.
	 */
	private MESSAGE_DATA loginToEPlumApi(String clientIp, String userIp) throws IllegalArgumentException {
		// Login and authenticate
		TRANSACTION loginTransObj = new TRANSACTION(LOGIN_TRANSACTION_TYPE);
		MESSAGE_DATA loginMsgData = new MESSAGE_DATA();
		EPlumLogin loginObj = new EPlumLogin(userName, password, null);
		loginObj.Update(loginMsgData.At(0));
		try {
			loginTransObj.SendRequest(new MESSAGE_TYPE(SESSION_MESSAGE_TYPE), loginMsgData);
			MESSAGE_DATA loginResponse = loginTransObj.GetResponse(DEFAULT_TIMEOUT);
			if (loginResponse.GetField(0, KEY_MESSAGE_KEY, null) == null) {
				return handleErrorResponse(clientIp, userIp, ACCESS_DENIED_ERROR_MESSAGE);
			} else {
				return loginResponse;
			}
		} catch (INVATRON_EXCEPTION Ex) {
			logger.error(Ex.getMessage());
			throw new IllegalArgumentException(Ex.getMessage());
		}
	}

	/**
	 * Creates a connection to the ePlum API.
	 *
	 * @param clientIp Client ip to connect to.
	 * @param userIp User ip connecting from.
	 */
	private void connectToEPlumApi(String clientIp, String userIp) throws IllegalArgumentException {
		try {
			new MQ_CLIENT(clientIp, port, userIp);
		} catch (INVATRON_EXCEPTION exception){
			logger.error(MQ_CLIENT_CREATE_ERROR_MESSAGE);
			throw new IllegalArgumentException(MQ_CLIENT_CREATE_ERROR_MESSAGE);
		}
	}
}
