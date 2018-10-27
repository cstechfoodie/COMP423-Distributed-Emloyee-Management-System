package dems.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeRecord extends Record{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String projectID;
	
	private static int counter = 10000;
	private static String recordPrefix = "ER";
	
	private String recordID;
	
	public EmployeeRecord() {super();};
	

	public EmployeeRecord(String firstName, String lastName, Integer employeeID, String mailID, String projectID) {
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
		String str = "This is a Employee Record with ID: " + this.getRecordID().toString() + " EmployeeId: "
				+ this.getEmployeeID() + ". First name: " + this.getFirstName() + ", Last Name: " + this.getLastName()
				+ ", Mail is " + this.getMailID() + ". The project Id is: "
				+ this.getProjectID();
		return str;
	}
}
