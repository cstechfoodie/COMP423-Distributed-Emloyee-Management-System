package dems.client.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import dems.api.Project;

public class Logger {

	private String filePath;

	private FileOutputStream file;

	private PrintWriter printWriter;

	final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd:hh-mm-ss");

	public Logger(String managerID) {
		try {
			filePath = managerID + ".txt";
			file = new FileOutputStream(filePath, true);
			printWriter = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			System.out.println("Logger Error!");
			e.printStackTrace();
		}
	}

	public synchronized void logInfo(String message) {
		this.printWriter.println(formatter.format(new Date()) + ": " + message);
		flush();
	}

	public synchronized void logCreateMRecord(String firstName, String lastName, Integer employeeID, String mailID, Project project,
			String location) {
		this.printWriter.println(formatter.format(new Date()) + ": Trying to create the following record: ");
		String str = "EmployeeId: "+ employeeID + ". First name: " + firstName + ", Last Name: " + lastName
				+ " Located at: " + location + ", Mail is " + mailID + ". The project info is: "
				+ project.toString();
		this.printWriter.println("------>" + str);
		flush();
	}
	
	public synchronized void logCreateERecord(String firstName, String lastName, Integer employeeID, String mailID,
			String projectID) {
		this.printWriter.println(formatter.format(new Date()) + ": Trying to create the following record: ");
		String str = "EmployeeId: "+ employeeID + ". First name: " + firstName + ", Last Name: " + lastName + ", Mail is " + mailID +". The project Id is: "
				+ projectID;
		this.printWriter.println("------>" + str);
		flush();
	}


	public synchronized void logEdit(String recordID, String fieldName, String newValue) {
		this.printWriter.println(formatter.format(new Date()) + ": Trying to modify Record " + recordID + ", fieldName {" + fieldName
				+ "} will be changed to " + newValue + ".");
		flush();
	}


	private void flush() {
		this.printWriter.flush();
	}
	
	
	public void close() {
		this.printWriter.close();
	}
}
