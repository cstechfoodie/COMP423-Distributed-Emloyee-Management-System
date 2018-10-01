package dems.client.api;

import dems.client.model.Project;

public interface RecordApi {
	
	//all return type should be string 
	//aemployeeID is Integer, there should be only one project ID in this create method
	public String createMRecord(String firstName, String lastName, Integer employeeID, String mailID, Project project, String location);
	
	public String createERecord(String firstName, String lastName, Integer employeeID, String mailID, String projectID);
	
	public String getRecordCounts();
	
	public String editRecord(String recordID, String fieldName, String newValue);
	
	public String printData(); //display the content of the map to the server side 
}
