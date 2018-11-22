package dems.RecordApp;


/**
* RecordApp/RecordOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Record.idl
* Saturday, October 27, 2018 4:00:10 PM EDT
*/

public interface RecordApi 
{
  String createMRecord (String managerID, String firstName, String lastName, int employeeID, String mailID, dems.model.Project project, String location);
  String createERecord (String managerID, String firstName, String lastName, int employeeID, String mailID, String projectID);
  String getRecordCounts (String managerID);
  String editRecord (String managerID, String recordID, String fieldName, String newValue);
  String transferRecord (String managerID, String recordID, String remoteCenterServerName);
  String printData ();
} // interface RecordOperations
