package dems.client.model;

import java.io.Serializable;

public class Project implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String projectID;
	
	public String clientName;
	
	public String projectName;
	
	public Project() {
		projectID = "ID_DEFAULT";
		clientName = "CLIENT_NAME";
		projectName = "PROJECT_NAME_DEFAULT";
	}
	
	public Project(String projectID, String clientName, String projectName) {
		this.projectID = projectID;
		this.clientName = clientName;
		this.projectName = projectName;
	}
	
	
	
	public String toString() {
		return "{ProjectID is " + projectID + ", client name is " + this.clientName + " project name is " + this.projectName + "}";
	}
}
