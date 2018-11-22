package dems.model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class Logger {
	
	private String userID ="";

	/**
	 * @param userID the userID to set
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	private final static String filePath = "USServerLogs.txt";

	private static FileOutputStream file;

	private PrintWriter printWriter;

	final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd:hh-mm-ss");

	public Logger() {
		try {
			file = new FileOutputStream(filePath, true);
			printWriter = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			System.out.println("Logger Error!");
			e.printStackTrace();
		}
	}

	public synchronized void logInfo(String message) {
		this.printWriter.println(userID + "---" + formatter.format(new Date()) + ": " + message);
		flush();
	}

	public synchronized void logSuccessfullyCreated(Record r) {
		this.printWriter.println(userID + "---" + formatter.format(new Date()) + ": The following record has been successfully created!");
		this.printWriter.println("------>" + r.toString());
		flush();
	}

	public synchronized void logUnsuccessfullyCreated(Record r) {
		this.printWriter
				.println(userID + "---" + formatter.format(new Date()) + ": The following record has NOT been successfully created!");
		this.printWriter.println("------>" + r.toString());
		flush();
	}

	public synchronized void logEdit(String recordID, String fieldName, String newValue) {
		this.printWriter.println(userID + "---" + formatter.format(new Date()) + ": In Record " + recordID + ", fieldName {" + fieldName
				+ "} was changed to " + newValue + ".");
		flush();
	}

	public synchronized void logMap(Map<String, List<Record>> map) {
		this.printWriter.println(userID + "---" + formatter.format(new Date()) + ": Print all records associate with their keys");
		if(map.size() > 0) {
			map.forEach((k, v) -> {
				this.printWriter.println("==============================Key: " + k + "==================================");
				for (int i = 0; i < v.size(); i++) {
					this.printWriter.println(i + ": " + v.get(i).toString());
				}
				this.printWriter.println("-=-=> Total records associate with this key is " + v.size());
			});
		}
		flush();
	}

	private void flush() {
		this.printWriter.flush();
	}

}
