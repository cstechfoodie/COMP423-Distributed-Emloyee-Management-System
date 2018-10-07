package dems.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RecordApi extends Remote {
	
	//all return type should be string 
	//aemployeeID is Integer, there should be only one project ID in this create method
	public String createMRecord(String firstName, String lastName, Integer employeeID, String mailID, Project project, String location) throws RemoteException;
	
	public String createERecord(String firstName, String lastName, Integer employeeID, String mailID, String projectID) throws RemoteException;;
	
	public String getRecordCounts() throws RemoteException;;
	
	public String editRecord(String recordID, String fieldName, String newValue) throws RemoteException;;
	
	public String printData() throws RemoteException;; //display the content of the map to the server side 
}
