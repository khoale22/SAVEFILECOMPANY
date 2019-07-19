package com.heb.scaleMaintenance.job.writer;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetail;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * Writer for a scale maintenance authorizationAndRetail and retail.
 *
 * @author Arjun,S
 * @since 2.17.8
 */
public class ScaleMaintenanceByDeptAuthorizeRetailWriter implements ItemWriter<ScaleMaintenanceAuthorizeRetail>, StepExecutionListener {
	private static final Logger logger = LoggerFactory.getLogger(ScaleMaintenanceByDeptAuthorizeRetailWriter.class);

	// log messages
	private static final String DATABASE_SAVED_LOG_MESSAGE = "%d scale maintenance auth retails inserted.";
	private static final String DATABASE_SAVED_ERROR_LOG_MESSAGE = "%d scale maintenance auth retails inserted " +
			"with errors.";

	// error messages
	private static final String EMPTY_LIST_LOGGER_MESSAGE = "Called writer with null or empty list.";
	private static final String INSERT_WRITE_ERROR = "Error inserting UPC: '%d', object: %s.";

	private String addSql = "insert into eplum.SCL_MAINT_AUTH_RETL " +
			"(CRE8_TS, AUTHD_SW, MSG_TXT, RETL_PRC_AMT, STR_NBR, TRX_ID, UPC_NBR, WT_SW, X4_QTY) " +
			"VALUES (SYSDATE, ?, ?, ?, ?, ?, ?, ?, ?)";

	private String addErrorSql = "insert into eplum.SCL_MAINT_AUTH_RETL " +
			"(CRE8_TS, AUTHD_SW, MSG_TXT, STR_NBR, TRX_ID, UPC_NBR) " +
			"VALUES (SYSDATE, ?, ?, ?, ?, ?)";

	@Autowired
	@Qualifier("ePlumDataSource")
	private DataSource ePlumDataSource;

	private PreparedStatement addStatement;

	private PreparedStatement addErrorStatement;

	private Connection connection;

	@Override
	public void write(List<? extends ScaleMaintenanceAuthorizeRetail> authorizeRetails) throws Exception {
		if (authorizeRetails == null || authorizeRetails.isEmpty()) {
			ScaleMaintenanceByDeptAuthorizeRetailWriter.logger.info(ScaleMaintenanceByDeptAuthorizeRetailWriter.EMPTY_LIST_LOGGER_MESSAGE);
			return;
		}

		if (this.addStatement == null) {
			this.setupAddStatement();
		}

		for(ScaleMaintenanceAuthorizeRetail authorizeRetail : authorizeRetails){

			if (StringUtils.isBlank(authorizeRetail.getMessage())) {
				this.setAddStatement(authorizeRetail);
				this.addStatement.addBatch();
			} else {
				if (this.addErrorStatement == null) {
					this.setupAddErrorStatement();
				}
				this.setAddErrorStatement(authorizeRetail);
				this.addErrorStatement.addBatch();
			}
		}

		int[] count = this.addStatement.executeBatch();
		logger.info(String.format(DATABASE_SAVED_LOG_MESSAGE, count.length));

		if (this.addErrorStatement != null) {
			count = this.addErrorStatement.executeBatch();
			logger.info(String.format(DATABASE_SAVED_ERROR_LOG_MESSAGE, count.length));
		}

	}

	/**
	 * Sets up the data to be returned.
	 *
	 * @param stepExecution The environment this step is going to run in.
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {

		logger.info("Inside Before Step 2:  ScaleMaintenanceByDeptAuthorizeRetailWriter");
	}

	/**
	 * Unimplemented.
	 *
	 * @param stepExecution Ignored.
	 * @return Always returns null.
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		logger.info("Inside After Step 2:  ScaleMaintenanceByDeptAuthorizeRetailWriter");
		try {
			if (this.addStatement != null) {
				this.addStatement.close();
			}
			if (this.addErrorStatement != null) {
				this.addErrorStatement.close();
			}
			if (this.connection != null) {
				this.connection.close();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	private void setAddStatement(ScaleMaintenanceAuthorizeRetail authorizeRetail) {
		try {
			this.addStatement.setString(1, authorizeRetail.getAuthorized().equals(Boolean.TRUE) ? "Y" : "N");
			this.addStatement.setString(2, authorizeRetail.getMessage());
			this.addStatement.setDouble(3, authorizeRetail.getRetail());
			this.addStatement.setInt(4, authorizeRetail.getKey().getStore());
			this.addStatement.setLong(5, authorizeRetail.getKey().getTransactionId());
			this.addStatement.setLong(6, authorizeRetail.getKey().getUpc());
			this.addStatement.setString(7, authorizeRetail.getWeighed().equals(Boolean.TRUE) ? "Y" : "N");
			this.addStatement.setInt(8, authorizeRetail.getByCountQuantity());
		} catch (SQLException e) {
			logger.error(String.format(INSERT_WRITE_ERROR, authorizeRetail.getKey().getUpc(), authorizeRetail.toString()));
		}
	}

	private void setAddErrorStatement(ScaleMaintenanceAuthorizeRetail authorizeRetail) {
		try {
			this.addErrorStatement.setString(1, authorizeRetail.getAuthorized().equals(Boolean.TRUE) ? "Y" : "N");
			this.addErrorStatement.setString(2, authorizeRetail.getMessage());
			this.addErrorStatement.setInt(3, authorizeRetail.getKey().getStore());
			this.addErrorStatement.setLong(4, authorizeRetail.getKey().getTransactionId());
			this.addErrorStatement.setLong(5, authorizeRetail.getKey().getUpc());
		} catch (SQLException e) {
			logger.error(String.format(INSERT_WRITE_ERROR, authorizeRetail.getKey().getUpc(), authorizeRetail.toString()));
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

	private void setupAddErrorStatement() {
		try {
			this.addErrorStatement = this.getConnection().prepareStatement(addErrorSql);
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
}
