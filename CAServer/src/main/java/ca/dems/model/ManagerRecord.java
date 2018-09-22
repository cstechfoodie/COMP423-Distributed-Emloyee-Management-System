package ca.dems.model;

import java.util.List;
import java.util.UUID;

public class ManagerRecord extends Record{

	
	private List<Project> projects;
	private Enum<Location> location;
	
	public ManagerRecord(String firstName, String lastName, String employeeID, String mailID, List<Project> projects, Location location) {
		super(firstName, lastName, employeeID, mailID);
		
		this.projects = projects;
		this.location = location;
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
