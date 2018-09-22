package ca.dems.model;

import java.util.UUID;

public class Record {
	private UUID recordID;
	
	private String firstName;
	private String lastName;
	private String employeeID;
	private String MailID;
	
	public Record(String firstName, String lastName, String employeeID, String mailID) {
		recordID = UUID.randomUUID();
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.employeeID = employeeID;
		this.MailID = mailID;
	}

	/**
	 * @return the recordID
	 */
	public UUID getRecordID() {
		return recordID;
	}

	/**
	 * @param recordID the recordID to set
	 */
	public void setRecordID(UUID recordID) {
		this.recordID = recordID;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the employeeID
	 */
	public String getEmployeeID() {
		return employeeID;
	}

	/**
	 * @param employeeID the employeeID to set
	 */
	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	/**
	 * @return the mailID
	 */
	public String getMailID() {
		return MailID;
	}

	/**
	 * @param mailID the mailID to set
	 */
	public void setMailID(String mailID) {
		MailID = mailID;
	}
}
