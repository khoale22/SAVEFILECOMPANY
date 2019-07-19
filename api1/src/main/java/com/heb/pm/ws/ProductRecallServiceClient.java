package com.heb.pm.ws;

import com.example.xmlns._1431050365119.ProductRecallServicePortType;
import com.example.xmlns._1431050365119.ProductRecallServiceServiceagent;
import com.heb.pm.entity.RecalledProduct;
import com.heb.util.ws.BaseWebServiceClient;
import com.heb.xmlns.ei.getrecallproduct_reply.GetRecallProductReply;
import com.heb.xmlns.ei.getrecallproduct_request.GetRecallProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Provides access to service endpoint for Product Recall Service.
 *
 * @author m594201
 * @since 2.13.0
 */
@Service
public class ProductRecallServiceClient extends BaseWebServiceClient
		<ProductRecallServiceServiceagent, ProductRecallServicePortType> {

	private static final Logger logger = LoggerFactory.getLogger(ProductRecallServiceClient.class);

	@Value("${productRecallService.uri}")
	private String uri;

	/**
	 * Gets product recall status.
	 *
	 * @param prodId the prod id
	 * @return the product recall status
	 * @throws CheckedSoapException the checked soap exception
	 */
	public RecalledProduct getProductRecallStatus(Long prodId) throws CheckedSoapException {
		GetRecallProductRequest request = new GetRecallProductRequest();
		request.setAuthentication(this.getAuthentication());
		request.setPRODID(prodId.toString());
		GetRecallProductReply reply = this.getProductRecallReply(request);

		RecalledProduct recalledProduct = new RecalledProduct();
		recalledProduct.setQaNumber(reply.getRecallProduct().get(0).getQAnumber());
		recalledProduct.setPosLock(reply.getRecallProduct().get(0).getPOSLock());
		recalledProduct.setClassification(reply.getRecallProduct().get(0).getClassification());
		recalledProduct.setCutOffDate(reply.getRecallProduct().get(0).getCutoffdate());
		recalledProduct.setIssueDate(reply.getRecallProduct().get(0).getIssueDate());
		recalledProduct.setDetails(reply.getRecallProduct().get(0).getDetails());

		return recalledProduct;
	}

	/**
	 * Gets product recall reply.
	 *
	 * @param request the request
	 * @return the product recall reply
	 * @throws CheckedSoapException the checked soap exception
	 */
	public GetRecallProductReply getProductRecallReply(GetRecallProductRequest request) throws CheckedSoapException {
		GetRecallProductReply reply;
		try{
			reply = this.getPort().getRecallProduct(request);
		} catch (com.example.xmlns._1431050365119.Fault fault) {
			fault.getFaultInfo().getProviderSOAPErrorMsg().get(0).getMessage();
			fault.getFaultInfo().getProviderSOAPErrorMsg().get(0).getMessageId();
			ProductRecallServiceClient.logger.error(fault.getMessage());
			if(fault.getFaultInfo().getFaultString().equals("No Records Found")){
				throw new CheckedSoapException(fault.getFaultInfo().getFaultString());
			}
			throw new CheckedSoapException(fault.getMessage());
		}


		catch (Exception e) {
			ProductRecallServiceClient.logger.error(e.getMessage());
			throw new CheckedSoapException(e.getMessage());
		}
		return reply;
	}

	@Override
	protected ProductRecallServiceServiceagent getServiceAgent() {
		try{
			URL url = new URL(this.getWebServiceUri());
			return new ProductRecallServiceServiceagent(url);
		} catch (MalformedURLException e) {
			ProductRecallServiceClient.logger.error(e.getMessage());
		}
		return new ProductRecallServiceServiceagent();
	}

	@Override
	protected ProductRecallServicePortType getServicePort(ProductRecallServiceServiceagent agent) {
		return agent.getProductRecallServicePortTypeEndpoint();
	}

	@Override
	protected String getWebServiceUri() {
		return this.uri;
	}
}
