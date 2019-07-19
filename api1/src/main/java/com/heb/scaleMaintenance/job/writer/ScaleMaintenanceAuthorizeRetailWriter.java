package com.heb.scaleMaintenance.job.writer;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetail;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceAuthorizeRetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Writer for a scale maintenance authorizationAndRetail and retail.
 *
 * @author m314029
 * @since 2.17.8
 */
public class ScaleMaintenanceAuthorizeRetailWriter implements ItemWriter<List<ScaleMaintenanceAuthorizeRetail>> {
	private static final Logger logger = LoggerFactory.getLogger(ScaleMaintenanceAuthorizeRetailWriter.class);

	// error messages
	private static final String EMPTY_LIST_LOGGER_MESSAGE = "Called writer with null or empty list.";

	@Autowired
	private ScaleMaintenanceAuthorizeRetailRepository repository;

	@Override
	public void write(List<? extends List<ScaleMaintenanceAuthorizeRetail>> items) throws Exception {
		if (items == null || items.isEmpty()) {
			ScaleMaintenanceAuthorizeRetailWriter.logger.info(ScaleMaintenanceAuthorizeRetailWriter.EMPTY_LIST_LOGGER_MESSAGE);
			return;
		}
		for(List<ScaleMaintenanceAuthorizeRetail> authorizeRetails : items){
			if (authorizeRetails == null || authorizeRetails.isEmpty()) {
				ScaleMaintenanceAuthorizeRetailWriter.logger.info(ScaleMaintenanceAuthorizeRetailWriter.EMPTY_LIST_LOGGER_MESSAGE);
			} else {
				this.repository.save(authorizeRetails);
			}
		}
	}
}
