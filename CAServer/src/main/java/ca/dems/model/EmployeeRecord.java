package ca.dems.model;

public class EmployeeRecord extends Record{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String projectID;
	
	public EmployeeRecord(String firstName, String lastName, Integer employeeID, String mailID, String projectID) {
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
	
	public String toString() {
		String str = "This is a Manager Record with ID: " + this.getRecordID().toString() + "EmployeeId: "
				+ this.getEmployeeID() + ". First name: " + this.getFirstName() + ", Last Name: " + this.getLastName()
				+ ", Mail is " + this.getMailID() + ". The project Id is: "
				+ this.getProjectID();
		return str;
	}
}
