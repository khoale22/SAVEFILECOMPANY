/*
 * ProductHierarchyServiceClient
 *  *
 *  *
 *  *  Copyright (c) 2016 HEB
 *  *  All rights reserved.
 *  *
 *  *  This software is the confidential and proprietary information
 *  *  of HEB.
 *  *
 */
package com.heb.pm.ws;

import com.heb.pm.customHierarchy.HierarchyContextController;
import com.heb.util.ws.BaseWebServiceClient;
import com.heb.util.ws.SoapException;
import com.heb.xmlns.ei.ProductHierarchyPortType;
import com.heb.xmlns.ei.ProductHierarchyServiceServiceagent;
import com.heb.xmlns.ei.getpssubdeptbydeptsubdept_request.GetPSSubDeptByDeptSubDeptRequest;
import com.heb.xmlns.ei.getsalesrestrictionsbysubcommodity_reply.GetSalesRestrictionsBySubCommodityReply;
import com.heb.xmlns.ei.getsalesrestrictionsbysubcommodity_request.GetSalesRestrictionsBySubCommodityRequest;
import com.heb.xmlns.ei.getstatewarningbysubcommodity_reply.GetStateWarningBySubcommodityReply;
import com.heb.xmlns.ei.getstatewarningbysubcommodity_request.GetStateWarningBySubcommodityRequest;
import com.heb.xmlns.ei.producthierarchymanagement.update_product_hierarchy_request.UpdateProductHierarchyRequest;
import com.heb.xmlns.ei.productmanagement.update_producthierarchy_reply.UpdateProductHierarchyReplyMessage;
import com.heb.xmlns.ei.pssubdeptdetails.GetPSSubDeptByDeptSubDeptReply;
import com.heb.xmlns.ei.pssubdeptdetails.PSSubDeptDetails;
import com.heb.xmlns.ei.salesrestrictionsbysubcommodity.SalesRestrictionsBySubCommodity;
import com.heb.xmlns.ei.statewarningbysubcommodity.StateWarningBySubcommodity;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides access to service endpoint for product hierarchy service.
 *
 * @author vn70516
 * @since 2.8.0
 */
@Service
public class ProductHierarchyServiceClient extends BaseWebServiceClient<ProductHierarchyServiceServiceagent, ProductHierarchyPortType>{

	private static final Logger logger = LoggerFactory.getLogger(ProductHierarchyManagementServiceClient.class);

	@Value("${productHierarchyService.uri}")
	private String uri;

	/**
	 * Return the service agent for this client.
	 *
	 * @return ProductHierarchyServiceServiceagent associated with this client.
	 */
	@Override
	protected ProductHierarchyServiceServiceagent getServiceAgent() {
		try {
			URL url = new URL(this.getWebServiceUri());
			return new ProductHierarchyServiceServiceagent(url);
		} catch (MalformedURLException e) {
			ProductHierarchyServiceClient.logger.error(e.getMessage());
		}
		return new ProductHierarchyServiceServiceagent();
	}

	/**
	 * Return the port type for this client.
	 *
	 * @param agent The agent to use to create the port.
	 * @return PortType associated with this client.
	 */
	@Override
	protected ProductHierarchyPortType getServicePort(ProductHierarchyServiceServiceagent agent) {
		return agent.getProductHierarchyService();
	}

	/**
	 * Return the url for this client.
	 *
	 * @return String url for this client.
	 */
	@Override
	protected String getWebServiceUri() {
		return this.uri;
	}

	/**
	 * Sets the URI to access product hierarchy service. This is primarily used for testing.
	 *
	 * @param uri The URI to access product hierarchy service.
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * Returns the list of pss department by department id and sub department.
	 * Call ProductHierarchyService.getPSSubDeptDetails() method.
	 * Input: departmentId and subDepartment Id.
	 * Output: Return list of pssDepartment Code basing to department and subdepartment. Value of pss department code
	 * must be greater than zero. Webservice return max 15 value.
	 *
	 * @param department The department
	 * @param subDepartment The sub department
	 * @return the list of pss department
	 */
	public List<Integer> findPssDepartmentsByDepartmentAndSubDepartment(String department, String subDepartment) throws
			CheckedSoapException{
		List<Integer> pssDepartmentCodes = new ArrayList<>();
		GetPSSubDeptByDeptSubDeptRequest request = new GetPSSubDeptByDeptSubDeptRequest();
		request.setAuthentication(this.getAuthentication());
		request.setDEPTID(department);
		request.setSUBDEPTID(subDepartment);
		try{
			GetPSSubDeptByDeptSubDeptReply reply = this.getPort().getPSSubDeptByDeptSubDept(request);
			List<PSSubDeptDetails> psSubDeptDetails = reply.getPSSubDeptDetails();
			if(psSubDeptDetails != null && !psSubDeptDetails.isEmpty()){
				for(PSSubDeptDetails psSubDeptDetail : psSubDeptDetails){
					if(StringUtils.isNotBlank(psSubDeptDetail.getPSSDEPARTMENT1()) && Integer
							.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT1())) > 0){
						pssDepartmentCodes.add(Integer.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT1())));
					}
					if(StringUtils.isNotBlank(psSubDeptDetail.getPSSDEPARTMENT2()) && Integer
							.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT2())) > 0){
						pssDepartmentCodes.add(Integer.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT2())));
					}
					if(StringUtils.isNotBlank(psSubDeptDetail.getPSSDEPARTMENT3()) && Integer
							.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT3())) > 0){
						pssDepartmentCodes.add(Integer.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT3())));
					}
					if(StringUtils.isNotBlank(psSubDeptDetail.getPSSDEPARTMENT4()) && Integer
							.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT4())) > 0){
						pssDepartmentCodes.add(Integer.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT4())));
					}
					if(StringUtils.isNotBlank(psSubDeptDetail.getPSSDEPARTMENT5()) && Integer
							.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT5())) > 0){
						pssDepartmentCodes.add(Integer.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT5())));
					}
					if(StringUtils.isNotBlank(psSubDeptDetail.getPSSDEPARTMENT6()) && Integer
							.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT6())) > 0){
						pssDepartmentCodes.add(Integer.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT6())));
					}
					if(StringUtils.isNotBlank(psSubDeptDetail.getPSSDEPARTMENT7()) && Integer
							.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT7())) > 0){
						pssDepartmentCodes.add(Integer.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT7())));
					}
					if(StringUtils.isNotEmpty(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT8())) && Integer
							.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT8())) > 0){
						pssDepartmentCodes.add(Integer.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT8())));
					}
					if(StringUtils.isNotBlank(psSubDeptDetail.getPSSDEPARTMENT9()) && Integer
							.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT9())) > 0){
						pssDepartmentCodes.add(Integer.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT9())));
					}
					if(StringUtils.isNotBlank(psSubDeptDetail.getPSSDEPARTMENT10()) && Integer
							.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT10())) > 0){
						pssDepartmentCodes.add(Integer.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT10())));
					}
					if(StringUtils.isNotBlank(psSubDeptDetail.getPSSDEPARTMENT11()) && Integer
							.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT11())) > 0){
						pssDepartmentCodes.add(Integer.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT11())));
					}
					if(StringUtils.isNotBlank(psSubDeptDetail.getPSSDEPARTMENT12()) && Integer
							.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT12())) > 0){
						pssDepartmentCodes.add(Integer.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT12())));
					}
					if(StringUtils.isNotBlank(psSubDeptDetail.getPSSDEPARTMENT13()) && Integer
							.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT13())) > 0){
						pssDepartmentCodes.add(Integer.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT13())));
					}
					if(StringUtils.isNotBlank(psSubDeptDetail.getPSSDEPARTMENT14()) && Integer
							.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT14())) > 0){
						pssDepartmentCodes.add(Integer.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT14())));
					}
					if(StringUtils.isNotBlank(psSubDeptDetail.getPSSDEPARTMENT15()) && Integer
							.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT15())) > 0){
						pssDepartmentCodes.add(Integer.valueOf(StringUtils.trim(psSubDeptDetail.getPSSDEPARTMENT15())));
					}
				}
			}
		} catch (com.heb.xmlns.ei.Fault fault) {
			ProductHierarchyServiceClient.logger.error(fault.getMessage());
			throw new CheckedSoapException(fault.getMessage());
		}
		catch (Exception e) {
			ProductHierarchyServiceClient.logger.error(e.getMessage());
			throw new CheckedSoapException(e.getCause());
		}
		return  pssDepartmentCodes;
	}

	/**
	 * Get list of sales restrictions by sub commodity.
	 *
	 * @param subcomcd The sub commodity code.
	 * @return the list of sales restrictions.
	 */
	public List<String> getSalesRestrictionsBySubCommodity(String subcomcd) throws
	CheckedSoapException{
		List<String> listReturn = new ArrayList<>();
		GetSalesRestrictionsBySubCommodityRequest request = new GetSalesRestrictionsBySubCommodityRequest();
		request.setAuthentication(this.getAuthentication());
		request.getPDOMISUBCOMCD().add(subcomcd);
		try{
			GetSalesRestrictionsBySubCommodityReply reply = this.getPort().getSalesRestrictionsBySubCommodity(request);
			List<SalesRestrictionsBySubCommodity> listReply = reply.getSalesRestrictionsBySubCommodity();
			if(listReply != null && !listReply.isEmpty()){
				for(SalesRestrictionsBySubCommodity item : listReply){
					listReturn.add(StringUtils.trim(item.getSALSRSTRCD()));
				}
			}
		} catch (com.heb.xmlns.ei.Fault fault) {
			ProductHierarchyServiceClient.logger.error(fault.getMessage());
			throw new CheckedSoapException(fault.getMessage());
		}
		catch (Exception e) {
			ProductHierarchyServiceClient.logger.error(e.getMessage());
			throw new CheckedSoapException(e.getCause());
		}
		return  listReturn;
	}

	/**
	 * Get list of state warning by sub commodity.
	 *
	 * @param subcomcd The sub commodity code.
	 * @return the list of state warning.
	 */
	public List<String> getStateWarningBySubcommodity(String subcomcd) throws
			CheckedSoapException{
		List<String> listReturn = new ArrayList<>();
		GetStateWarningBySubcommodityRequest request = new GetStateWarningBySubcommodityRequest();
		request.setAuthentication(this.getAuthentication());
		request.getPDOMISUBCOMCD().add(subcomcd);
		try{
			GetStateWarningBySubcommodityReply reply = this.getPort().getStateWarningBySubcommodity(request);
			List<StateWarningBySubcommodity> listReply = reply.getStateWarningBySubcommodity();
			if(listReply != null && !listReply.isEmpty()){
				for(StateWarningBySubcommodity item : listReply){
					listReturn.add(StringUtils.trim(item.getSTPRODWARNCD()));
				}
			}
		} catch (com.heb.xmlns.ei.Fault fault) {
			ProductHierarchyServiceClient.logger.error(fault.getMessage());
			throw new CheckedSoapException(fault.getMessage());
		}
		catch (Exception e) {
			ProductHierarchyServiceClient.logger.error(e.getMessage());
			throw new CheckedSoapException(e.getCause());
		}
		return  listReturn;
	}
}
