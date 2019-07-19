package com.heb.pm.index;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.ItemClass;
import com.heb.pm.repository.ItemClassRepository;
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
 * Reads itemClasses from the database.
 *
 * @author m314029
 * @since 2.6.0
 */
public class ItemClassReader implements ItemReader<ItemClass>, StepExecutionListener {

	@Autowired
	private ItemClassRepository repository;

	private Iterator<ItemClass> data;
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
	 * Called by Spring Batch to return the next itemClass in the list.
	 *
	 * @return The next itemClass in the list. Null when there is no more data.
	 * @throws Exception
	 * @throws UnexpectedInputException
	 * @throws ParseException
	 * @throws NonTransientResourceException
	 */
	@Override
	@CoreTransactional
	public ItemClass read() throws Exception {

		// If there is still data, return it.
		if (this.data != null && this.data.hasNext()) {
			return this.data.next();
		}

		// If not, see if you can fetch another set.
		Page<ItemClass> page =
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
	 * Returns the repository used for testing.
	 *
	 * @param repository ItemClassRepository used in this class.
	 */
	public void setRepository(ItemClassRepository repository) {
		this.repository = repository;
	}
}
