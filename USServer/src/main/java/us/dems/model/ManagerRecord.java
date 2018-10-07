package us.dems.model;

import dems.api.Project;

public class ManagerRecord extends Record {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Project project;
	private String location;
	
	private static int counter = 10000;
	private static String recordPrefix = "MR";
	
	private String recordID;

	public ManagerRecord(String firstName, String lastName, Integer employeeID, String mailID, Project project,
			String location) {
		super(firstName, lastName, employeeID, mailID);
		this.recordID = recordPrefix + counter;
		counter++;
		this.project = project;
		this.location = location;
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param project
	 *            the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
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
	
	
	public String toString() {
		String str = "This is a Manager Record with ID: " + this.getRecordID().toString() + " EmployeeId: "
				+ this.getEmployeeID() + ". First name: " + this.getFirstName() + ", Last Name: " + this.getLastName()
				+ " Located at: " + this.getLocation() + ", Mail is " + this.getMailID() + ". The project info is: "
				+ this.project.toString();
		return str;
	}


}
