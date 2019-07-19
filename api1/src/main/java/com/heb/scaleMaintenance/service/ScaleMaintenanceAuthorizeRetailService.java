package com.heb.scaleMaintenance.service;

import com.heb.scaleMaintenance.CreateCheckStatusRetailCsv;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetail;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceAuthorizeRetailRepository;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceAuthorizeRetailRepositoryWithCount;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.jpa.PageableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Holds the business logic for scale maintenance authorization and retail.
 *
 * @author m314029
 * @since 2.17.8
 */
@Service
public class ScaleMaintenanceAuthorizeRetailService {

	// Defaults
	private static final int DEFAULT_PAGE_SIZE = 20;

	@Autowired
	private ScaleMaintenanceAuthorizeRetailRepository repository;

	@Autowired
	private ScaleMaintenanceAuthorizeRetailRepositoryWithCount repositoryWithCount;

	@Autowired
	private CreateCheckStatusRetailCsv createCheckStatusRetailCsv;

	public Set<Integer> getAuthorizedStoresByTransactionId(Long transactionId) {
		return this.repository.getUniqueStoresByTransactionIdAndAuthorized(transactionId);
	}

	public List<ScaleMaintenanceAuthorizeRetail> getAuthorizedByTransactionIdAndStoreAndNoMessage(Long transactionId, Integer store) {
		return this.repository.getUniqueByKeyTransactionIdAndKeyStoreAndAuthorizedAndMessageIsNull(transactionId, store, Boolean.TRUE);
	}

	public List<ScaleMaintenanceAuthorizeRetail> findByTransactionIdAndStore(Long transactionId, Integer store) {
		return this.repository.findByKeyTransactionIdAndKeyStore(transactionId, store);
	}

	/**
	 * This method finds all scale maintenance retail information by transaction id and by store.
	 *
	 * @param page The page requested.
	 * @param pageSize The page size requested.
	 * @param includeCount Whether to include count.
	 * @param transactionId Transaction id to search for
	 * @param store Store number to search for.
	 * @return Page of scale maintenance retails.
	 */
	public PageableResult<ScaleMaintenanceAuthorizeRetail> findAllByTransactionIdAndStore(
			Integer page, Integer pageSize, Boolean includeCount, Long transactionId, Integer store){

		PageRequest request = new PageRequest(page, pageSize);
		return includeCount ?
				this.findAllByTransactionIdAndByStoreWithCount(transactionId, store, request) :
				this.findAllByTransactionIdAndByStoreWithOutCount(transactionId, store, request);
	}

	/**
	 * Finds all scale maintenance retails sorted including count query.
	 *
	 * @param transactionId Transaction id for this scale maintenance.
	 * @param store Store number for this scale maintenance.
	 * @param request Request containing search criteria (page, pageSize, sort).
	 * @return Page of data.
	 */
	private PageableResult<ScaleMaintenanceAuthorizeRetail> findAllByTransactionIdAndByStoreWithCount(
			Long transactionId, Integer store, PageRequest request) {
		Page<ScaleMaintenanceAuthorizeRetail> data = this.repositoryWithCount
				.findByKeyTransactionIdAndKeyStore(transactionId, store, request);
		return new PageableResult<>(request.getPageNumber(), data.getTotalPages(),
				data.getTotalElements(), data.getContent());
	}

	/**
	 * Finds all scale maintenance retails without including count query.
	 *
	 * @param transactionId Transaction id for this scale maintenance.
	 * @param store Store number for this scale maintenance.
	 * @param request Request containing search criteria (page, pageSize, sort).
	 * @return Page of data.
	 */
	private PageableResult<ScaleMaintenanceAuthorizeRetail> findAllByTransactionIdAndByStoreWithOutCount(
			Long transactionId, Integer store, PageRequest request) {
		List<ScaleMaintenanceAuthorizeRetail> data = this.repository
				.findByKeyTransactionIdAndKeyStore(transactionId, store, request);
		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Exports the store scale retail information to a csv.
	 *
	 * @param outputStream the HTTP response output stream.
	 * @param store the store.
	 * @param transactionId the transaction id.
	 * @param totalRecordCount the total record count.
	 */
	public void exportCheckStatusRetailCsv(ServletOutputStream outputStream, Integer store, Long transactionId, int totalRecordCount) {
		try {
			outputStream.println(this.createCheckStatusRetailCsv.getHeading());
			double pageCount = Math.ceil((float)totalRecordCount/DEFAULT_PAGE_SIZE);
			PageableResult<ScaleMaintenanceAuthorizeRetail> retails;
			for(int x=0; x<pageCount; x++) {
				retails = this.findAllByTransactionIdAndStore(x, DEFAULT_PAGE_SIZE,false,transactionId, store);
				for(ScaleMaintenanceAuthorizeRetail retail : retails.getData()) {
					outputStream.print(this.createCheckStatusRetailCsv.createCsv(retail));
				}
			}

		} catch (IOException e) {
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}
}
