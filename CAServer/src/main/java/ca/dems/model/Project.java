package ca.dems.model;

import java.io.Serializable;

public class Project implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String projectID;
	
	public String clientName;
	
	public String projectName;
	
	public String toString() {
		return "{ProjectID is " + projectID + ", client name is " + this.clientName + " project name is" + this.projectName + "}";
	}
}
