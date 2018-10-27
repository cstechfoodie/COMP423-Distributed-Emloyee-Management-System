package dems.api;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Record implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty()
	private String firstName;
	@JsonProperty()
	private String lastName;
	@JsonProperty()
	private int employeeID;
	@JsonProperty()
	private String MailID;
	
	@JsonCreator
	public Record(@JsonProperty("firstName")String firstName, @JsonProperty("lastName")String lastName, @JsonProperty("employeeID")Integer employeeID, @JsonProperty("mailID")String mailID) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.employeeID = employeeID;
		this.MailID = mailID;
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
	public int getEmployeeID() {
		return employeeID;
	}

	/**
	 * @param employeeID the employeeID to set
	 */
	public void setEmployeeID(Integer employeeID) {
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
	
	public abstract String getRecordID();
	public abstract void setRecordID(String recordID);
}
