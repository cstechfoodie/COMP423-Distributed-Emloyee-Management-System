package ca.dems.api;

import java.util.List;

import ca.dems.model.Location;
import ca.dems.model.Project;

public interface recordApi {
	
	public void createMRecord(String firstName, String lastName, String employeeID, String mailID, List<Project> projects, Location location);
	
	public void createERecord(String firstName, String lastName, String employeeID, String mailID, String projectID);
	
	public int getecordCounts();
	
	public void editRecord(String recordID, String fieldName, Object newValue);
}
