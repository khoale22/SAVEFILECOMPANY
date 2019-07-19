package com.heb.pm.batchUpload.earley;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.batchUpload.BatchUploadResponse;
import com.heb.sadc.utils.AntiVirusException;
import com.heb.util.controller.UserInfo;
import com.heb.util.sercurity.AntiVirusUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * REST endpoint for uploading Earley attribute files.
 *
 * @author d116773
 * @since 2.15.0
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + "/earley/attribute")
@AuthorizedResource(ResourceConstants.BATCH_UPLOAD_EARLEY_ATTRIBUTES)
public class EarleyAttributeUploadController {

	private static final Logger logger = LoggerFactory.getLogger(EarleyAttributeUploadController.class);

	private static final String FILE_UPLOAD_MESSAGE =
			"User %s from IP %s has requested to upload file %s as an Earley attribute upload.";

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private AntiVirusUtil antiVirusUtil;

	@Autowired
	private EarleyUploadService earleyUploadService;

	/**
	 * Processes a user's upload of an Earley attribute file.
	 *
	 * @param file The file the user uploaded.
	 * @param description A description the user gave for the file.
	 * @param request The HTTP Request that initiated the call.
	 * @return A response with the transaction ID of the user's uplaod.
	 * @throws IOException
	 * @throws AntiVirusException
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST)
	public BatchUploadResponse uploadAttributeFile(@RequestParam("file") MultipartFile file,
												 @RequestParam("description") String description,
												 HttpServletRequest request) throws IOException, AntiVirusException, FileProcessingException {

		this.logFileUpload(request.getRemoteAddr(), file.getName());

		// Scan the file for viruses.
		this.antiVirusUtil.virusCheck(file.getBytes());

		EarleyAttributeUploadController.logger.info("File scanned for viruses");

		// Submit the job to process the upload.
		long transactionId = this.earleyUploadService.submitAttributeUpload(this.userInfo.getUserId(),
				file, description);

		return new BatchUploadResponse(transactionId, "Success");
	}

	/**
	 * Logs a user's request to upload an Earley file.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param fileName The name of the file the user uploaded.
	 */
	private void logFileUpload(String ip, String fileName) {

		EarleyAttributeUploadController.logger.info(String.format(EarleyAttributeUploadController.FILE_UPLOAD_MESSAGE,
				this.userInfo.getUserId(), ip, fileName));
	}
}
