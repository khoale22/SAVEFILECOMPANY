/*
 * TimRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.heb.pm.entity.Store;
import com.heb.pm.inventory.TimException;
import com.heb.tim.services.dao.UnitsOnHandJDBCDAO;
import com.heb.tim.services.dao.UnitsOnOrderJDBCDAO;
import com.heb.tim.services.exception.TIMSQLException;
import com.heb.tim.services.vo.StoreInventoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Repository with functions to return inventory data from Tim.
 * Since these methods all require connection to TIM, it will not have any unit tests. I attempted to remove the need
 * for this class, but, once I give a connection to the TIM DAO, I cannot get it back separate from the function
 * where I created it, and, therefore, cannot close it.
 *
 * @author d116773
 * @since 2.0.1
 */
@Service
@SuppressWarnings("rawtypes")
public class TimRepository  {

	private static final Logger logger = LoggerFactory.getLogger(TimRepository.class);

	private static final String TIM_CONNECT_ERROR_MESSAGE = "Unable to connect to TIM.";

	private static final String CONNECT_ATTEMPT_LOGGER_MESSAGE = "Fetching connection to TIM.";

	@Autowired
	@Qualifier("timDataSource")
	private DataSource timDataSource;


	/**
	 * Returns a map of stores and their inventory objects.
	 *
	 * @param productId The product ID to look for inventory for.
	 * @return A Map where the key is a string of the store number and the value is a StoreInventoryVo with
	 * inventory data for the given store.
	 */
	public ArrayList getStoreInventory(long productId) {

		Connection connection = null;
		Connection connectionWrap = null;

		try {
			TimRepository.logger.debug(TimRepository.CONNECT_ATTEMPT_LOGGER_MESSAGE);

			connection = this.timDataSource.getConnection();
			connectionWrap = connection.unwrap(Connection.class);
			// You have to call unwrap to get an actual Oracle connection object in order to call
			// the stored procedures.
			UnitsOnHandJDBCDAO unitsOnHandJDBCDAO = new UnitsOnHandJDBCDAO(connectionWrap);

			return unitsOnHandJDBCDAO.getAllInvenForSingleProduct(Long.toString(productId));
		} catch (TIMSQLException | SQLException e) {
			throw new TimException(TimRepository.TIM_CONNECT_ERROR_MESSAGE, e.getCause());
		} finally {
			// This doesn't actually close the connection, but it releases it back to the pool.
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					TimRepository.logger.error(e.getMessage());
				}
			}
		}

	}

	/**
	 * Returns the PO.
	 *
	 * @param itemCode The item code to look for purchase orders for.
	 */
	public ArrayList getPurchaseOrders(long itemCode) {

		Connection connection = null;
		Connection connectionWrap = null;

		try {
			TimRepository.logger.debug(TimRepository.CONNECT_ATTEMPT_LOGGER_MESSAGE);

			connection = this.timDataSource.getConnection();
			connectionWrap = connection.unwrap(Connection.class);
			// You have to call unwrap to get an actual Oracle connection object in order to call
			// the stored procedures.
			UnitsOnOrderJDBCDAO unitsOnOrderJDBCDAO = new UnitsOnOrderJDBCDAO(connectionWrap);

			return unitsOnOrderJDBCDAO.getPurchaseOrderDetailsByItem(Long.toString(itemCode));
		} catch (TIMSQLException | SQLException e) {
			throw new TimException(TimRepository.TIM_CONNECT_ERROR_MESSAGE, e.getCause());
		} finally {
			// This doesn't actually close the connection, but it releases it back to the pool.
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					TimRepository.logger.error(e.getMessage());
				}
			}
		}
	}

	/**
	 * The TIM API is based around Strings. This is a utility method that will take a list of Store objects and create
	 * an array of Strings with the store numbers in it.
	 *
	 * @param stores The list of stores to convert to a String array.
	 * @return An array containing the corporate numbers from the list passed in converted to Strings.
	 */
	private static String[] storeListToStringArray(List<Store> stores) {
		String[] storeArray = new String[stores.size()];
		int i = 0;
		for (Store store : stores) {
			storeArray[i] = Long.toString(store.getStoreNumber());
			i++;
		}

		return storeArray;
	}


	/**
	 * Returns total vendor units on hand for a product.
	 *
	 * @param productId The product ID to search for information about.
	 * @return The map with vendor info the product.
	 */
	public Map getVendorOnHandQuantity(int productId) {

		Connection connection = null;
		Connection connectionWrap = null;

		try {
			TimRepository.logger.debug(TimRepository.CONNECT_ATTEMPT_LOGGER_MESSAGE);

			connection = this.timDataSource.getConnection();
			connectionWrap = connection.unwrap(Connection.class);

			// You have to call unwrap to get an actual Oracle connection object in order to call
			// the stored procedures.
			UnitsOnHandJDBCDAO unitsOnHandJDBCDAO = new UnitsOnHandJDBCDAO(connectionWrap);

			return unitsOnHandJDBCDAO.getVendOnHandQty(productId);
		} catch (TIMSQLException | SQLException e) {
			throw new TimException(TimRepository.TIM_CONNECT_ERROR_MESSAGE, e.getCause());
		} finally {
			// This doesn't actually close the connection, but it releases it back to the pool.
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					TimRepository.logger.error(e.getMessage());
				}
			}
		}
	}

}
