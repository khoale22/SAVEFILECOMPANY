package com.heb.scaleMaintenance.service;

import com.heb.pm.entity.PriceDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Holds the business logic for retail.
 *
 * @author m314029
 * @since 2.39.0
 */
@Service
public class RetailService {

	public RetailService(@Qualifier("dataSourceOracle") DataSource dataSource) throws SQLException {
		this.dataSource = dataSource;
		this.initJDBCConnection();
	}

	private RetailService() {
		// intentionally left empty so constructor with dataSource must be called
	}

	private static final String AD_EXCEPTION_PRICE_STATEMENT = "select pd_retail_prc, pd_xfor_qty, pd_weight_sw " +
			"from emd.PD_AD_EXCP_PRICE " +
			"where pd_upc_no = ? and FC_STORE_NO = ? and pd_price_eff_dt <= sysdate and PD_AD_EXCP_END_DT > sysdate";
	private static final String AD_PRICE_STATEMENT = "select pd_retail_prc, pd_xfor_qty, pd_weight_sw " +
			"from emd.PD_AD_PRICE " +
			"where pd_upc_no = ? and pd_zone_no = ? and pd_price_eff_dt <= sysdate and PD_AD_END_DT > sysdate ";
	private static final String REG_EXCEPTION_PRICE_STATEMENT = "select pd_retail_prc, pd_xfor_qty, pd_weight_sw " +
			"from emd.PD_EXCEPTION_PRICE " +
			"where pd_upc_no = ? and FC_STORE_NO = ? and pd_price_eff_dt <= sysdate and PD_EXCP_END_DT > sysdate ";
	private static final String REG_PRICE_STATEMENT = "select pd_retail_prc, pd_xfor_qty, pd_weight_sw from (" +
			"select pd_upc_no, pd_zone_no, pd_price_eff_dt, pd_retail_prc, pd_xfor_qty, pd_weight_sw, max(" +
			"pd_price_eff_dt) over (partition by pd_upc_no, pd_zone_no) med " +
			"from emd.pd_regular_price " +
			"where pd_upc_no = ? and pd_zone_no = ? and pd_price_eff_dt <= sysdate ) x " +
			"where x.pd_price_eff_dt = med";

	private static final String RETAIL_PRICE_COLUMN = "pd_retail_prc";
	private static final String RETAIL_XFOR_COLUMN = "pd_xfor_qty";
	private static final String RETAIL_WEIGHT_COLUMN = "pd_weight_sw";

	private DataSource dataSource;

	private Connection connection;
	private PreparedStatement adExceptionPriceQuery;
	private PreparedStatement adPriceQuery;
	private PreparedStatement regExceptionPriceQuery;
	private PreparedStatement regPriceQuery;

	/**
	 * Initializes this service's jdbc connections/ prepared statements related to the connection. Callers will have to
	 * handle the SQLException.
	 *
	 * @throws SQLException
	 */
	private void initJDBCConnection() throws SQLException {
		this.connection = this.dataSource.getConnection();
		this.adExceptionPriceQuery = this.connection.prepareStatement(AD_EXCEPTION_PRICE_STATEMENT);
		this.adPriceQuery = this.connection.prepareStatement(AD_PRICE_STATEMENT);
		this.regExceptionPriceQuery = this.connection.prepareStatement(REG_EXCEPTION_PRICE_STATEMENT);
		this.regPriceQuery = this.connection.prepareStatement(REG_PRICE_STATEMENT);
	}

	/**
	 * Tears down this service's jdbc connections/ prepared statements related to the connection. Callers will have to
	 * handle the SQLException.
	 *
	 * @throws SQLException
	 */
	public void tearDownJDBCConnection() throws SQLException {
		if (this.adExceptionPriceQuery != null) {
			this.adExceptionPriceQuery.close();
		}
		if (this.adPriceQuery != null) {
			this.adPriceQuery.close();
		}
		if (this.regExceptionPriceQuery != null) {
			this.regExceptionPriceQuery.close();
		}
		if (this.regPriceQuery != null) {
			this.regPriceQuery.close();
		}
		if (this.connection != null) {
			this.connection.close();
		}
	}

	/**
	 * Gets price information for a given store/ primary UPC by cycling through the retail tables. There are four cases:
	 * 1. If there is a record in the ad exception table (upc/ store), return it
	 * 2. If there is a record in the ad table (upc/ zone), return it
	 * 3. If there is a record in the regular exception table (upc/ store), return it
	 * 4. Return the record in regular table (upc, zone).
	 *
	 * @param primaryUpc Primary UPC to find current retail for.
	 * @param store Store to find current retail for.
	 * @param zoneId Zone the store is tied to.
	 * @return Retail for the given UPC/ store.
	 */
	public PriceDetail getPriceInformation(long primaryUpc, int store, int zoneId) {
		PriceDetail toReturn;

		// check if there is an ad exception price
		toReturn = this.getPriceInformation(primaryUpc, store, zoneId, this.adExceptionPriceQuery);
		if (toReturn != null) {
			return toReturn;
		}

		// check if there is an ad price
		toReturn = this.getPriceInformation(primaryUpc, zoneId, zoneId, this.adPriceQuery);
		if (toReturn != null) {
			return toReturn;
		}

		// check if there is an exception price
		toReturn = this.getPriceInformation(primaryUpc, store, zoneId, this.regExceptionPriceQuery);
		if (toReturn != null) {
			return toReturn;
		}

		// else return the regular price
		return this.getPriceInformation(primaryUpc, zoneId, zoneId, this.regPriceQuery);
	}

	/**
	 * Gets price information given a UPC, store/zone, and query to run.
	 *
	 * @param primaryUpc Primary UPC to look up retail for.
	 * @param storeOrZone Store/ zone to include in query.
	 * @param zoneId Zone id linked to the store.
	 * @param query Query to look up retail.
	 * @return Retail for the given UPC/ store, or null if not found.
	 */
	private PriceDetail getPriceInformation(long primaryUpc, int storeOrZone, int zoneId, PreparedStatement query) {

		if (query == null) {
			return null;
		}

		try {
			query.setLong(1, primaryUpc);
			query.setInt(2, storeOrZone);
			return this.convertObjectToPriceDetail(query.executeQuery(), zoneId);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Converts the result set of a price query into a PriceDetail.
	 *
	 * @param priceDetailResultSet Result set from a JDBC query.
	 * @param zoneId Zone id to set on Price Detail.
	 * @return Price Detail mapping of result set.
	 * @throws SQLException
	 */
	private PriceDetail convertObjectToPriceDetail(ResultSet priceDetailResultSet, int zoneId) throws SQLException {
		PriceDetail toReturn = null;
		boolean setPriceDetail = false;
		while (priceDetailResultSet.next()) {
			if (!setPriceDetail) {
				toReturn = new PriceDetail();
				toReturn.setRetailPrice(priceDetailResultSet.getDouble(RETAIL_PRICE_COLUMN));
				toReturn.setxFor(priceDetailResultSet.getInt(RETAIL_XFOR_COLUMN));
				toReturn.setWeight(priceDetailResultSet.getBoolean(RETAIL_WEIGHT_COLUMN));
				toReturn.setZone(zoneId);
				setPriceDetail = true;
			}
		}

		return toReturn;
	}
}
