package com.heb.scaleMaintenance.invatron;

import com.invatron.invatronlib.api.MESSAGE_RECORD;

/**
 * This is the login object used by Invatron. Added getters/ setters.
 *
 * @author m314029
 * @since 2.17.8
 */
public class EPlumLogin {
	private String type = "LOGIN";
	private String username;
	private String password;
	private String storeNumber = "";
	private String storeKey = "";

	public EPlumLogin(String Username, String Password, String StoreNo) {
		this.username = Username;
		this.password = Password;
		this.storeNumber = StoreNo;
	}

	public void Update(MESSAGE_RECORD Record) {
		Record.SetField("CCO", this.type);
		Record.SetField("USR", "," + this.username);
		Record.SetFieldEncrypted("PWD", this.password);
		if (this.storeKey != null && !this.storeKey.isEmpty())
			Record.SetField("STR", this.storeKey);
		if (this.storeNumber != null && !this.storeNumber.isEmpty())
			Record.SetField("SNO", this.storeNumber);
	}

	/**
	 * Returns Type.
	 *
	 * @return The Type.
	 **/
	public String getType() {
		return type;
	}

	/**
	 * Sets the Type.
	 *
	 * @param type The Type.
	 **/
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Returns Username.
	 *
	 * @return The Username.
	 **/
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the Username.
	 *
	 * @param username The Username.
	 **/
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Returns Password.
	 *
	 * @return The Password.
	 **/
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the Password.
	 *
	 * @param password The Password.
	 **/
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returns StoreNumber.
	 *
	 * @return The StoreNumber.
	 **/
	public String getStoreNumber() {
		return storeNumber;
	}

	/**
	 * Sets the StoreNumber.
	 *
	 * @param storeNumber The StoreNumber.
	 **/
	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}

	/**
	 * Returns StoreKey.
	 *
	 * @return The StoreKey.
	 **/
	public String getStoreKey() {
		return storeKey;
	}

	/**
	 * Sets the StoreKey.
	 *
	 * @param storeKey The StoreKey.
	 **/
	public void setStoreKey(String storeKey) {
		this.storeKey = storeKey;
	}
}
