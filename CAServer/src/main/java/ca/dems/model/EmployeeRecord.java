package ca.dems.model;

public class EmployeeRecord extends Record{

	private String projectID;
	
	public EmployeeRecord(String firstName, String lastName, String employeeID, String mailID, String projectID) {
		super(firstName, lastName, employeeID, mailID);
		this.projectID = projectID;
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
}
