package com.heb.pm.ws;

import com.example.xmlns._1427967512183.VertexServicePortType;
import com.example.xmlns._1427967512183.VertexServiceServiceagent;
import com.heb.pm.entity.VertexTaxCategory;
import com.heb.util.ws.BaseWebServiceClient;
import com.heb.xmlns.ei.getvertexcategories_reply.GetVertexCategoriesReply;
import com.heb.xmlns.ei.getvertexcategories_request.GetVertexCategoriesRequest;
import com.heb.xmlns.ei.vertex_categories.VERTEXCATEGORIES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides access to service endpoint for Vertex Service.
 *
 * @author m594201
 * @since 2.12.0
 */
@Service
public class VertexServiceClient extends BaseWebServiceClient
		<VertexServiceServiceagent, VertexServicePortType> {

	private static final Logger logger = LoggerFactory.getLogger(VertexServiceClient.class);

	private static final Boolean EXCLUDE_QUALIFYING_CONDITIONS = Boolean.FALSE;
	private static final Boolean ONLY_QUALIFYING_CONDITIONS = Boolean.TRUE;

	@Value("${vertexService.uri}")
	private String uri;


	/**
	 * Returns a list of all available Vertex tax categories.
	 *
	 * @return A list of all available Vertex tax categories.
	 * @throws CheckedSoapException
	 */
	public List<VertexTaxCategory> fetchAll() throws CheckedSoapException {

		GetVertexCategoriesRequest request = new GetVertexCategoriesRequest();
		request.setAuthentication(this.getAuthentication());

		// The service will include qualifying conditions if you don't exclude them.
		request.setQUALIFYINGCONDITION(VertexServiceClient.EXCLUDE_QUALIFYING_CONDITIONS);

		GetVertexCategoriesReply reply;

		try {
			reply = this.getVertexCategories(request);
		} catch (Exception e) {
			VertexServiceClient.logger.error(e.getMessage());
			throw new CheckedSoapException(e.getMessage());
		}

		List<VertexTaxCategory> vertexTaxCategories = new LinkedList<>();

		reply.getVERTEXCATEGORIES().forEach((v) -> vertexTaxCategories.add(this.mapTaxCategory(v)));

		return vertexTaxCategories;
	}

	/**
	 * Returns a list of all qualifying conditions.
	 *
	 * @return A list of all qualifying conditions.
	 * @throws CheckedSoapException
	 */
	public List<VertexTaxCategory> fetchAllQualifyingTaxConditions() throws CheckedSoapException {
		GetVertexCategoriesRequest request = new GetVertexCategoriesRequest();
		request.setAuthentication(this.getAuthentication());

		// The service will include qualifying conditions if you don't exclude them.
		request.setQUALIFYINGCONDITION(VertexServiceClient.ONLY_QUALIFYING_CONDITIONS);

		GetVertexCategoriesReply reply;

		try {
			reply = this.getVertexCategories(request);
		} catch (Exception e) {
			VertexServiceClient.logger.error(e.getMessage());
			throw new CheckedSoapException(e.getMessage());
		}

		List<VertexTaxCategory> vertexTaxCategories = new LinkedList<>();

		reply.getVERTEXCATEGORIES().forEach((v) -> vertexTaxCategories.add(this.mapTaxCategory(v)));

		return vertexTaxCategories;
	}

	/**
	 * Gets tax category.
	 *
	 * @param taxCode the tax code
	 * @return the tax category
	 * @throws CheckedSoapException the checked soap exception
	 */
	public VertexTaxCategory getTaxCategory(String taxCode) throws CheckedSoapException{
		GetVertexCategoriesRequest request = new GetVertexCategoriesRequest();
		request.setAuthentication(this.getAuthentication());
		request.getTXBLTYDVRCODE().add(String.valueOf(taxCode.trim()));
		GetVertexCategoriesReply reply = this.getVertexCategories(request);

		//Accessing first element since there can only ever be one tax category for a product.
		return this.mapTaxCategory(reply.getVERTEXCATEGORIES().get(0));
	}

	/**
	 * Maps a VERTEXCATEGORIES from the web service to a VertexTaxCategory.
	 *
	 * @param vertexCategory The VERTEXCATEGORIES to map.
	 * @return The mapped VertexTaxCategory.
	 */
	private VertexTaxCategory mapTaxCategory(VERTEXCATEGORIES vertexCategory) {

		VertexTaxCategory taxCategory = new VertexTaxCategory();

		taxCategory.setCategoryCode(vertexCategory.getTXBLTYCATCODE());
		taxCategory.setCategoryDescription(vertexCategory.getTXBLTYCATDESC());
		taxCategory.setCategoryMapId(vertexCategory.getTXBLTYCATID());
		taxCategory.setCategoryName(vertexCategory.getTXBLTYCATNAME());
		taxCategory.setDvrCode(StringUtils.trim(vertexCategory.getTXBLTYDVRCODE()));

		return taxCategory;
	}
	/**
	 * Gets vertex categories.
	 *
	 * @param request the request
	 * @return the vertex categories
	 * @throws CheckedSoapException the checked soap exception
	 */
	private GetVertexCategoriesReply getVertexCategories(GetVertexCategoriesRequest request) throws CheckedSoapException {
		GetVertexCategoriesReply reply;
		try{
			reply = this.getPort().getVertexCategories(request);
		} catch (com.example.xmlns._1427967512183.Fault fault) {
			fault.getFaultInfo().getProviderSOAPErrorMsg().get(0).getMessage();
			fault.getFaultInfo().getProviderSOAPErrorMsg().get(0).getMessageId();
			VertexServiceClient.logger.error(fault.getMessage());
			if(fault.getFaultInfo().getFaultString().equals("No Records Found")){
				throw new CheckedSoapException(fault.getFaultInfo().getFaultString());
			}
			throw new CheckedSoapException(fault.getMessage());
		}
		catch (Exception e) {
			VertexServiceClient.logger.error(e.getMessage());
			throw new CheckedSoapException(e.getMessage());
		}
		return reply;
	}

	/**
	 * Returns the service agent to connect to Vertex Service.
	 *
	 * @return The service agent to connect to Vertex Service.
	 */
	@Override
	protected VertexServiceServiceagent getServiceAgent() {
		try{
			URL url = new URL(this.getWebServiceUri());
			return new VertexServiceServiceagent(url);
		} catch (MalformedURLException e) {
			VertexServiceClient.logger.error(e.getMessage());
		}
		return new VertexServiceServiceagent();
	}

	/**
	 * Returns the port to call the Vertex Service.
	 *
	 * @param agent The agent to use to create the port.
	 * @return The port to call the Vertex Service.
	 */
	@Override
	protected VertexServicePortType getServicePort(VertexServiceServiceagent agent) {
		return agent.getVertexService();
	}

	/**
	 * Returns the URI to Vertex service.
	 *
	 * @return The URI to Vertex service.
	 */
	@Override
	protected String getWebServiceUri() {
		return this.uri;
	}
}