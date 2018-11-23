package dems.model;

import java.io.Serializable;

/**
* RecordApp/RecordPackage/Project.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Record.idl
* Saturday, October 27, 2018 4:00:10 PM EDT
*/

public final class Project implements Serializable
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public String projectID = null;
  public String clientName = null;
  public String projectName = null;

  public Project ()
  {
  } // ctor

  public Project (String _projectID, String _clientName, String _projectName)
  {
    projectID = _projectID;
    clientName = _clientName;
    projectName = _projectName;
  } // ctor

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

public String toString() {
	return "Project{" + projectID + " " + clientName + " " +  projectName + "}";
}

} // class Project
