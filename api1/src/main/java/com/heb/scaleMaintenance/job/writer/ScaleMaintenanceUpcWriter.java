package com.heb.scaleMaintenance.job.writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceUpc;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * Writer for a scale maintenance upc.
 *
 * @author m314029
 * @since 2.17.8
 */
public class ScaleMaintenanceUpcWriter implements ItemWriter<List<ScaleMaintenanceUpc>> , StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(ScaleMaintenanceUpcWriter.class);

	// log messages
	private static final String DATABASE_SAVED_LOG_MESSAGE = "%d scale maintenance upc inserted.";

	// error messages
	private static final String EMPTY_LIST_LOGGER_MESSAGE = "Called writer with null or empty list.";
	private static final String INSERT_WRITE_ERROR = "Error inserting UPC: '%d', object: %s.";

	private String addSql = "insert into eplum.SCL_MAINT_UPC " +
			"(CRE8_TS, JSON_VER_TXT, MSG_TXT, SCL_PROD_JSON, TRX_ID, UPC_NBR) " +
			"VALUES (SYSDATE, ?, ?, ?, ?, ?)";

	@Autowired
	@Qualifier("ePlumDataSource")
	private DataSource ePlumDataSource;

	private PreparedStatement addStatement;

	private Connection connection;

	@Override
	public void write(List<? extends List<ScaleMaintenanceUpc>> items) throws Exception {

		if (CollectionUtils.isEmpty(items)) {
			ScaleMaintenanceUpcWriter.logger.info(ScaleMaintenanceUpcWriter.EMPTY_LIST_LOGGER_MESSAGE);
			return;
		}

		if (this.addStatement == null) {
			this.setupAddStatement();
		}
		for(List<ScaleMaintenanceUpc> scaleMaintenanceUpcs : items){
			if (CollectionUtils.isEmpty(scaleMaintenanceUpcs)) {
				ScaleMaintenanceUpcWriter.logger.info(ScaleMaintenanceUpcWriter.EMPTY_LIST_LOGGER_MESSAGE);
			} else {
				for (ScaleMaintenanceUpc upc : scaleMaintenanceUpcs) {
					this.setAddStatement(upc);
					this.addStatement.addBatch();
				}
			}
		}

		int[] count = this.addStatement.executeBatch();
		logger.info(String.format(DATABASE_SAVED_LOG_MESSAGE, count.length));
	}

	private void setAddStatement(ScaleMaintenanceUpc scaleMaintenanceUpc) {
		try {
			Clob clob = this.connection.createClob();
			clob.setString(1, objectMapper().writeValueAsString(scaleMaintenanceUpc.getScaleProductAsJson()));
			this.addStatement.setString(1, scaleMaintenanceUpc.getJsonVersion());
			this.addStatement.setString(2, scaleMaintenanceUpc.getMessage());
			this.addStatement.setClob(3, clob);
			this.addStatement.setLong(4, scaleMaintenanceUpc.getKey().getTransactionId());
			this.addStatement.setLong(5, scaleMaintenanceUpc.getKey().getUpc());
		} catch (SQLException e) {
			logger.error(String.format(INSERT_WRITE_ERROR, scaleMaintenanceUpc.getKey().getUpc(), scaleMaintenanceUpc.toString()));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private void setupAddStatement() {
		try {
			this.addStatement = this.getConnection().prepareStatement(addSql);
		} catch (SQLException e) {
			logger.error(e.getLocalizedMessage());
			throw new RuntimeException(e.getCause());
		}
	}

	private Connection getConnection() throws SQLException {
		if (Objects.isNull(this.connection)) {
			this.connection = this.ePlumDataSource.getConnection();
		}
		return this.connection;
	}

	private static ObjectMapper objectMapper() {
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return om;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		try {
			if (this.addStatement != null) {
				this.addStatement.close();
			}
			if (this.connection != null) {
				this.connection.close();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
}
