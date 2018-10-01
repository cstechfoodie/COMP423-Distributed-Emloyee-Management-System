package ca.dems.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Logger {

	private final String filePath = "CAServerLogs.txt";

	private File file;

	private PrintWriter printWriter;

	final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd:hh-mm-ss");

	public Logger() {
		try {
			file = new File(filePath);
			printWriter = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			System.out.println("Logger Error!");
			e.printStackTrace();
		}
	}

	public void logInfo(String message) {
		this.printWriter.println(formatter.format(new Date()) + ": " + message);
	}

	public void logSuccessfullyCreated(Record r) {
		this.printWriter.println(formatter.format(new Date()) + ": The following record has been succesffuly created!");
		this.printWriter.println("------>" + r.toString());
		closeWriter();
	}

	public void logUnsuccessfullyCreated(Record r) {
		this.printWriter
				.println(formatter.format(new Date()) + ": The following record has NOT been succesffuly created!");
		this.printWriter.println("------>" + r.toString());
		closeWriter();
	}

	public void logEdit(String recordID, String fieldName, String newValue) {
		this.printWriter.println(formatter.format(new Date()) + ": In Record " + recordID + ", fieldName {" + fieldName
				+ "} was changed to " + newValue + ".");
		closeWriter();
	}

	public void logMap(Map<String, List<Record>> map) {
		this.printWriter.println(formatter.format(new Date()) + ": Print all records associate with their keys");
		map.forEach((k, v) -> {
			this.printWriter.println("==============================Key: " + k + "==================================");
			for (int i = 0; i < v.size(); i++) {
				this.printWriter.println(i + ": " + v.get(i).toString());
			}
			this.printWriter.println("-=-=> Ttoal records associate with this key is " + v.size());
		});
		closeWriter();
	}

	private void closeWriter() {
		this.printWriter.close();
	}

}
