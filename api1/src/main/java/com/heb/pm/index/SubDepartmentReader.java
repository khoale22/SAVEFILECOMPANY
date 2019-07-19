package com.heb.pm.index;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.SubDepartment;
import com.heb.pm.repository.SubDepartmentRepository;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Iterator;

/**
 * Reads subDepartments from the database.
 *
 * @author m314029
 * @since 2.6.0
 */
public class SubDepartmentReader implements ItemReader<SubDepartment>, StepExecutionListener {

	@Autowired
	private SubDepartmentRepository repository;

	private Iterator<SubDepartment> data;
	private int pageSize = 100;
	private int currentPage = 0;

	/**
	 * Sets up the data to be returned.
	 *
	 * @param stepExecution The environment this step is going to run in.
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.currentPage = 0;
	}

	/**
	 * Unimplemented.
	 *
	 * @param stepExecution Ignored.
	 * @return Always reutrns null.
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}

	/**
	 * Called by Spring Batch to return the next subDepartment in the list.
	 *
	 * @return The next subDepartment in the list. Null when there is no more data.
	 * @throws Exception
	 * @throws UnexpectedInputException
	 * @throws ParseException
	 * @throws NonTransientResourceException
	 */
	@Override
	@CoreTransactional
	public SubDepartment read() throws Exception {

		// If there is still data, return it.
		if (this.data != null && this.data.hasNext()) {
			return this.data.next();
		}

		// If not, see if you can fetch another set.
		Page<SubDepartment> page =
				this.repository.findAll(new PageRequest(this.currentPage++, this.pageSize));
		// If there was results, return the next one.
		if (page.hasContent()) {
			this.data = page.iterator();
			return data.next();
		}
		// If not, we're at the end of the data.
		return null;
	}

	/**
	 * Sets the SubDepartmentRepository to read data from. This is used for testing.
	 *
	 * @param repository The SubDepartmentRepository to read data from.
	 */
	public void setRepository(SubDepartmentRepository repository) {
		this.repository = repository;
	}

	/**
	 * Sets the size of the page to data in.
	 *
	 * @param pageSize The size of the page to read data in.
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
