package ca.dems.api;

import ca.dems.model.EmployeeRecord;
import ca.dems.model.ManagerRecord;
import ca.dems.model.Project;
import ca.dems.repository.IRecordRepository;

public class recordController implements recordApi {
	
	private IRecordRepository repo;
	
	public recordController(IRecordRepository repo) {
		this.repo = repo;
	}

	@Override
	public String createMRecord(String firstName, String lastName, Integer employeeID, String mailID, Project project, String location) {
		ManagerRecord m = new ManagerRecord(firstName, lastName, employeeID, mailID, project, location);
		boolean isSuccessful = repo.createMRecord(m);
		if(isSuccessful) {
			return "ManagerRecord has been successfully created!";
		} else {
			return "Failed to Create ManagerRecord!";
		}
	}

	@Override
	public String createERecord(String firstName, String lastName, Integer employeeID, String mailID,
			String projectID) {
		EmployeeRecord e = new EmployeeRecord(firstName, lastName, employeeID, mailID, projectID);
		boolean isSuccessful = repo.createMRecord(e);
		if(isSuccessful) {
			return "EmployeeRecord has been successfully created!";
		} else {
			return "Failed to Create EmployeeRecord!";
		}
	}

	@Override
	public String getecordCounts() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String editRecord(String recordID, String fieldName, String newValue) {
		boolean isSuccessful =  this.repo.editRecord(recordID, fieldName, newValue);
		if(isSuccessful) {
			return "Edited Successfully!";
		} else {
			return "Failed to Edit!";
		}
	}

	@Override
	public String printData() {
		// TODO Auto-generated method stub
		return "";
	}

}
