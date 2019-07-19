package com.heb.scaleMaintenance.job.processor;

import com.heb.pm.entity.PriceDetail;
import com.heb.pm.productDetails.product.ProductInformationService;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetail;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceTracking;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceTrackingRepository;
import com.heb.scaleMaintenance.service.ScaleAuthorizationService;
import com.heb.scaleMaintenance.utils.EPlumApiUtils;
import com.heb.util.controller.LongListFromStringFormatter;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Locale;

/**
 * Processor for converting a long into ScaleMaintenanceAuthorizeRetails.
 *
 * @author m314029
 * @since 2.17.8
 */
public class ScaleMaintenanceAuthorizeRetailProcessor implements ItemProcessor<Integer, List<ScaleMaintenanceAuthorizeRetail>>, StepExecutionListener {

	@Value("#{jobParameters['transactionId']}")
	private Long transactionId;

	private List<Long> upcList;

	// errors
	private final static String RETAIL_ERROR = "Problems acquiring retail for UPC: %d ," +
			"store: %d.";
	private final static String AUTHORIZE_ERROR = "UPC: %d is not authorized for " +
			"store: %d. Retail will not be looked up.";

	private LongListFromStringFormatter longListFromStringFormatter = new LongListFromStringFormatter();

	@Autowired
	private ScaleAuthorizationService scaleAuthorizationService;

	@Autowired
	private ScaleMaintenanceTrackingRepository repository;

	@Autowired
	private ProductInformationService productInformationService;

	@Override
	public List<ScaleMaintenanceAuthorizeRetail> process(Integer store) throws Exception {

		List<ScaleMaintenanceAuthorizeRetail> toReturn = this.scaleAuthorizationService
				.getAuthorizedForUpcsByStore(transactionId, store, this.upcList);

		// set retails for authorized UPCs
		this.setRetail(toReturn);
		return toReturn;
	}

	/**
	 * Sets retail for a UPC/ store. Currently only handles fetching retails for lab stores. Else sets error message.
	 *
	 * @param authorizationRetails Scale maintenance auth retail with authorized data pre-populated.
	 */
	private void setRetail(List<ScaleMaintenanceAuthorizeRetail> authorizationRetails) {

		Integer store;
		for (ScaleMaintenanceAuthorizeRetail authorizationRetail : authorizationRetails) {
			store = authorizationRetail.getKey().getStore();
			// UPC is authorized for store
			if (authorizationRetail.getAuthorized()) {
				// if store is lab store
				if (EPlumApiUtils.LAB_STORES.contains(store)) {
					PriceDetail currentPriceDetail = this.productInformationService
							.getPriceInformation(authorizationRetail.getKey().getUpc());
					authorizationRetail
							.setRetail(currentPriceDetail.getRetailPrice())
							.setByCountQuantity(currentPriceDetail.getxFor())
							.setWeighed(currentPriceDetail.getWeight());
				} else {
					// else store is not lab store
					authorizationRetail.setMessage(String.format(
							RETAIL_ERROR, authorizationRetail.getKey().getUpc(), store));
				}
			} else {
				// UPC is NOT authorized for store
				authorizationRetail.setMessage(String.format(
						AUTHORIZE_ERROR, authorizationRetail.getKey().getUpc(), store));
			}
		}
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ScaleMaintenanceTracking tracking = repository.findOne(transactionId);
		this.upcList = this.longListFromStringFormatter.parse(tracking.getLoadParametersAsJson().getUpcs(),Locale.US);
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
