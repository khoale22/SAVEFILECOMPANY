package com.heb.scaleMaintenance.job.processor;

import com.heb.pm.entity.PriceDetail;
import com.heb.pm.entity.RetailLocation;
import com.heb.pm.entity.RetailLocationKey;
import com.heb.pm.entity.ScaleUpc;
import com.heb.pm.repository.RetailLocationRepository;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetail;
import com.heb.scaleMaintenance.service.ScaleAuthorizationService;
import com.heb.scaleMaintenance.service.RetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Date;

/**
 * Processor for converting list of longs into ScaleMaintenanceUpcs.
 *
 * @author Arjun,S
 * @since 2.17.8
 */
public class ScaleScanUpcProcessor implements ItemProcessor<ScaleUpc,  ScaleMaintenanceAuthorizeRetail>, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(ScaleScanUpcProcessor.class);
	private static final String NO_ZONE_LOG = "Could not find a zone for %s.";
	private static final String FOUND_ZONE_LOG = "Found a zone: %d for store: %d.";
	private static final String NO_RETAIL_MESSAGE = "Could not find a retail for UPC: %d, store: %d.";
	private static final String STORE_LOCATION_TYPE_CODE = "S";

	@Autowired
	private ScaleAuthorizationService scaleAuthorizationService;

	@Autowired
	@Qualifier("dataSourceOracle")
	private DataSource dataSource;

	@Autowired
	private RetailLocationRepository retailLocationRepository;

	@Value("#{jobParameters['transactionId']}")
	private Long transactionId;

	@Value("#{jobParameters['sourceStoreNo']}")
	private int sourceStoreNo;

	@Value("#{jobParameters['departmentNbr']}")
	private String departmentNbr;

	@Value("#{jobParameters['targetStoreNbr']}")
	private int targetStoreNbr;

	@Value("#{jobParameters['effectiveDate']}")
	private Date effectiveDate;
	private Integer zoneId;
	private RetailService retailService;

	@Override
	public ScaleMaintenanceAuthorizeRetail process(ScaleUpc scaleUpc) throws Exception {
		if (this.zoneId == null) {
			return null;
		}
		ScaleMaintenanceAuthorizeRetail toReturn = this.scaleAuthorizationService
				.getAuthorizationDetailByDepartment(
						transactionId, scaleUpc, sourceStoreNo, departmentNbr);
		// if UPC is authorized, get retail
		if (toReturn != null && toReturn.getAuthorized()) {
			PriceDetail currentPriceDetail = this.retailService.getPriceInformation(
					scaleUpc.getAssociateUpc().getPdUpcNo(), this.sourceStoreNo, this.zoneId);
			// if retail is found, set retail
			if (currentPriceDetail != null) {
				logger.debug("UPC:"+scaleUpc.getUpc() +" retail price :"+currentPriceDetail.getRetailPrice());
				toReturn
						.setRetail(currentPriceDetail.getRetailPrice())
						.setByCountQuantity(currentPriceDetail.getxFor())
						.setWeighed(currentPriceDetail.getWeight());
			} else {
				// else set error message
				toReturn.setMessage(String.format(
						NO_RETAIL_MESSAGE, toReturn.getKey().getUpc(), toReturn.getKey().getStore()));
			}
		}
		return toReturn;
	}

	/**
	 * Sets up the data to be returned.
	 *
	 * @param stepExecution The environment this step is going to run in.
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		try {
			this.retailService = new RetailService(dataSource);
			this.zoneId = this.getZoneForStore(sourceStoreNo);
			if (this.zoneId == null) {
				logger.error(String.format(NO_ZONE_LOG, this.sourceStoreNo));
			} else {
				logger.info(String.format(FOUND_ZONE_LOG, this.zoneId, this.sourceStoreNo));
			}
		} catch (SQLException exception) {
			logger.error("Unable to build prepared statements. Stopping job.");
			throw new IllegalArgumentException(exception.getLocalizedMessage());
		}
		logger.info("Inside Before Step 2:  ScaleScanUpcProcessor");
	}

	/**
	 * Unimplemented.
	 *
	 * @param stepExecution Ignored.
	 * @return Always returns null.
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		logger.info("Inside After Step 2:  ScaleScanUpcProcessor");
		try {
			this.retailService.tearDownJDBCConnection();
		} catch (SQLException exception) {
			logger.error("Unable to close connections and prepared statements.");
		}
		return null;
	}

	/**
	 * Gets a zone id of store number.
	 *
	 * @param storeNumber Store number.
	 * @return Zone id of given store number.
	 */
	private Integer getZoneForStore(Integer storeNumber) {
		if (storeNumber == null) {
			return null;
		}
		RetailLocationKey currentKey;
		currentKey = new RetailLocationKey();
		currentKey.setLocationNumber(storeNumber);
		currentKey.setLocationTypeCode(STORE_LOCATION_TYPE_CODE);

		RetailLocation retailLocation = this.retailLocationRepository.findOne(currentKey);
		if (retailLocation != null) {
			return retailLocation.getZoneId();
		} else {
			return null;
		}
	}
}
