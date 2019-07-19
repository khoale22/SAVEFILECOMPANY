package com.heb.pm.batchUpload.earley;

/**
 * Provides a base for the entities created by these jobs and the ability to track status and an update messge.
 *
 * @author d116773
 * @since 2.16.0
 */
public class WrappedEarlyEntity<T> {

	private T entity;
	private String message;
	private Boolean success;

	/**
	 * Create a new WrappedEarleyEntity. This will save the entity and assume that it is bing processed. It will set
	 * the message accordingly.
	 *
	 * @param entity The entity to save.
	 */
	public WrappedEarlyEntity(T entity) {
		this.entity = entity;
		this.message = "PENDING";
	}

	/**
	 * Creates a new WrappedEarleyEntity. Since there is no entity, the assumption is that there is something wrong
	 * and sets this to failed. It will store the message for an error.
	 *
	 * @param message The error message.
	 */
	public WrappedEarlyEntity(String message) {
		this.success = false;
		this.message = message;
	}

	/**
	 * Returns the saved entity.
	 *
	 * @return The saved entity.
	 */
	public T getEntity() {
		return entity;
	}

	/**
	 * Returns the current message for the wrapped entity.
	 *
	 * @return The current message for the wrapped entity.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the entity as successfully processed and updates the message accordingly.
	 */
	public void succeed() {
		this.success = true;
		this.message = "COMPLETE";
	}

	/**
	 * Sets the entity as not processed successfully.
	 *
	 * @param message An error message for the operation.
	 */
	public void fail(String message) {
		this.success = false;
		this.message = message;
	}

	/**
	 * Returns whether or not the entity has been processed successfully. May be null if the entity is in progress.
	 *
	 * @return True if the entity has been processed successfully. False if it has failed. Null if in progress.
	 */
	public Boolean getSuccess() {
		return success;
	}

	/**
	 * Returns whether or not an entity has been saved.
	 *
	 * @return True if an entity has been saved and false otherwise.
	 */
	public boolean hasEntity() {
		return this.entity != null;
	}
}
