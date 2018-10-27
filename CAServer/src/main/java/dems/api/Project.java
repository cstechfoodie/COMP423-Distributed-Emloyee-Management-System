package dems.api;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Project implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty()
	private String projectID;
	
	@JsonProperty()
	private String clientName;
	
	@JsonProperty()
	private String projectName;
	
//	public Project() {
//		projectID = "ID_DEFAULT";
//		clientName = "CLIENT_NAME";
//		projectName = "PROJECT_NAME_DEFAULT";
//	}
	
	@JsonCreator
	public Project(@JsonProperty("projectID")String projectID, @JsonProperty("clientName")String clientName, @JsonProperty("projectName")String projectName) {
		this.projectID = projectID;
		this.clientName = clientName;
		this.projectName = projectName;
	}
	
	
	@Override
	public String toString() {
		return "{ProjectID is " + projectID + ", client name is " + this.clientName + " project name is " + this.projectName + "}";
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


	/**
	 * @return the clientName
	 */
	public String getClientName() {
		return clientName;
	}


	/**
	 * @param clientName the clientName to set
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}


	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}


	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}
