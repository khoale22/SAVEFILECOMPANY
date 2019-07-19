package com.heb.scaleMaintenance.job.reader;

import com.heb.pm.entity.ScaleUpc;
import com.heb.pm.repository.ScaleUpcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Iterator;

/**
 * Reader for a list of stores that returns one store.
 *
 * @author Arjun,S
 * @since 2.17.8
 */
public class ScaleScanUpcReader implements ItemReader<ScaleUpc>, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(ScaleScanUpcReader.class);

	@Value("#{jobParameters['departmentNbr']}")
	private String departmentNbr;

	@Autowired
	ScaleUpcRepository repository;
	private Iterator<ScaleUpc> scaleUpcs;
	private static int PAGE_SIZE = 500;
	private int currentPage = 0;

	@Override
	public ScaleUpc read() throws Exception {
		// If there is still data, return it.
		if (this.scaleUpcs != null && this.scaleUpcs.hasNext()) {
			return this.scaleUpcs.next();
		}

		Page<ScaleUpc> page = this.getData();

		// If there are results, return the next one.
		if (page.hasContent()) {
			this.scaleUpcs = page.iterator();
			return this.scaleUpcs.next();
		}

		// else we're at the end of the data.
		return null;
	}

	/**
	 * Gets data for the reader. If given a department number, find all Scale UPCs tied to that department. Else get
	 * all Scale UPCs.
	 *
	 * @return Page of Scale UPCs.
	 */
	private Page<ScaleUpc> getData() {

		if (this.departmentNbr == null) {
			return repository
					.findAll(new PageRequest(this.currentPage++, PAGE_SIZE));
		} else {
			return repository
					.findByAssociateUpcSellingUnitProductMasterSubDepartmentKeyDepartment(
							this.departmentNbr.trim(),
							new PageRequest(this.currentPage++, PAGE_SIZE));
		}
	}

	/**
	 * Sets up the data to be returned.
	 *
	 * @param stepExecution The environment this step is going to run in.
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {

		logger.info("Inside Before Step 2:  ScaleScanUpcReader");
	}

	/**
	 * Unimplemented.
	 *
	 * @param stepExecution Ignored.
	 * @return Always returns null.
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		logger.info("Inside After Step 2:  ScaleScanUpcReader");
		return null;
	}
}
