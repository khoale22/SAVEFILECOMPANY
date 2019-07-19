package com.heb.pm.ws;

import com.example.xmlns._1423201195856.ProductAssortmentServicePortType;
import com.example.xmlns._1423201195856.ProductAssortmentServiceServiceagent;
import com.heb.pm.entity.ProductAssortment;
import com.heb.util.ws.BaseWebServiceClient;
import com.heb.util.ws.SoapException;
import com.heb.xmlns.ei.getassortmentbyproductid_reply.GetAssortmentByProductIdReply;
import com.heb.xmlns.ei.getassortmentbyproductid_request.GetAssortmentByProductIdRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Provides access to service endpoint for Product assortment Service.
 *
 * @author m594201
 * @since 2.12.0
 */
@Service
public class ProductAssortmentServiceClient extends BaseWebServiceClient<ProductAssortmentServiceServiceagent, ProductAssortmentServicePortType>{

	private static final Logger logger = LoggerFactory.getLogger(ProductAssortmentServiceClient.class);

	@Value("${productAssortmentService.uri}")
	private String uri;

	/**
	 * Gets assortment by product id.
	 *
	 * @param productId the product id
	 * @return the assortment by product id
	 * @throws CheckedSoapException the checked soap exception
	 */
	public ProductAssortment getAssortmentByProductId(String productId) throws CheckedSoapException{

		GetAssortmentByProductIdRequest request = new GetAssortmentByProductIdRequest();
		StringBuilder errorMessage = new StringBuilder(StringUtils.EMPTY);
		ProductAssortment parameters;
		request.setAuthentication(this.getAuthentication());
		request.setPRODID(productId);
		try {
			GetAssortmentByProductIdReply reply = this.getAssortmentByProductIdReply(request);

			if(reply.getTOTALCOUNT() == null){
				throw new SoapException(errorMessage.toString());
			}

			parameters = new ProductAssortment();

			// if there were any error messages, throw exception
			if(!errorMessage.toString().equals(StringUtils.EMPTY)){
				throw new SoapException(errorMessage.toString());
			}

			parameters.setStoreCount(reply.getTOTALCOUNT().longValue());

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CheckedSoapException(e.getCause());
		}

		return parameters;
	}

	/**
	 * Gets assortment by product id reply.
	 *
	 * @param request the request
	 * @return the assortment by product id reply
	 * @throws CheckedSoapException the checked soap exception
	 */
	public GetAssortmentByProductIdReply getAssortmentByProductIdReply(GetAssortmentByProductIdRequest request) throws CheckedSoapException {
		GetAssortmentByProductIdReply reply = new GetAssortmentByProductIdReply();
		try{
			reply = this.getPort().getAssortmentByProductId(request);
		} catch (Exception e) {
			ProductAssortmentServiceClient.logger.error(e.getMessage());
		}

		return reply;
	}

	@Override
	protected ProductAssortmentServiceServiceagent getServiceAgent() {
		try{
			URL url = new URL(this.getWebServiceUri());
			return new ProductAssortmentServiceServiceagent();
		} catch (MalformedURLException e){
			ProductAssortmentServiceClient.logger.error(e.getMessage());
		}
		return new ProductAssortmentServiceServiceagent();
	}

	@Override
	protected ProductAssortmentServicePortType getServicePort(ProductAssortmentServiceServiceagent agent) {
		return agent.getProductAssortmentService();
	}

	@Override
	protected String getWebServiceUri() {
		return this.uri;
	}
}
