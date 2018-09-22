package ca.dems.model;

import java.util.List;
import java.util.UUID;

public class ManagerRecord {
	private UUID recordID;
	
	private String firstName;
	private String lastName;
	private String employeeID;
	private String MailID;
	
	private List<Project> projects;
	private Enum<Location> location;
	
	public ManagerRecord(String firstName, String lastName, String employeeID, String mailID, List<Project> projects, Location location) {
		recordID = UUID.randomUUID();
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.employeeID = employeeID;
		this.MailID = mailID;
		
		this.projects = projects;
		this.location = location;
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

	/**
	 * @return the projects
	 */
	public List<Project> getProjects() {
		return projects;
	}

	/**
	 * @param projects the projects to set
	 */
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	/**
	 * @return the location
	 */
	public Enum<Location> getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Enum<Location> location) {
		this.location = location;
	}
}
