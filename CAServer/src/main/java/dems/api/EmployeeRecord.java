package dems.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeRecord extends Record{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty()
	private String projectID;
	
	private static int counter = 10000;
	private static String recordPrefix = "ER";
	@JsonProperty()
	private String recordID;
	
	@JsonCreator
	public EmployeeRecord(@JsonProperty("firstName")String firstName, @JsonProperty("lastName")String lastName, @JsonProperty("employeeID")Integer employeeID, @JsonProperty("mailID")String mailID, @JsonProperty("projectID")String projectID) {
		super(firstName, lastName, employeeID, mailID);
		this.recordID = recordPrefix + counter;
		counter++;
		this.projectID = projectID;
	}

	/**
	 * @return the recordID
	 */
	public String getRecordID() {
		return recordID;
	}
	
	/**
	 * @param recordID the recordID to set
	 */
	public void setRecordID(String recordID) {
		this.recordID = recordID;
	}
	/**
	 * @return the projectID
	 */
	public String getProjectID() {
		return projectID;
	}

	/**
	 * @param projectID the projectID to set
	 */
	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}
	
	public String toString() {
		String str = "This is a Emloyee Record with ID: " + this.getRecordID().toString() + " EmployeeId: "
				+ this.getEmployeeID() + ". First name: " + this.getFirstName() + ", Last Name: " + this.getLastName()
				+ ", Mail is " + this.getMailID() + ". The project Id is: "
				+ this.getProjectID();
		return str;
	}
}
