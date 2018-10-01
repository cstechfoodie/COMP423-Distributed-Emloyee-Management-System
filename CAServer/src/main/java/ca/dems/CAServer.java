package ca.dems;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ca.dems.api.RecordController;
import ca.dems.model.EmployeeRecord;
import ca.dems.model.ManagerRecord;
import ca.dems.model.Project;
import ca.dems.model.Record;
import ca.dems.repository.IRecordRepository;
import ca.dems.repository.RecordRepository;

public class CAServer {

	public static void main(String[] args) {
		
		//initialize the database
		IRecordRepository repo = new RecordRepository();
		RecordController con = new RecordController(repo);
		
		Record r;
		for(int i = 0; i < 10; i++) {
			con.createMRecord("Shunyu", "Wang", 12345 + i, "shunyu.wang@mail.concordia.ca", new Project("P00001", "client", "project"), "CA");
		}
		
		for(int i = 0; i < 10; i++) {
			con.createERecord("Shunyu", "Zang", 67890 + i, "shunyu.wang@mail.concordia.ca", "P00001");
		}
		
		for(int i = 0; i < 5; i++) {
			con.createERecord("Martin", "Smith", 20100 + i, "aaa.bbb@mail.concordia.ca", "P00001");
		}
		
		con.printData();
		String count = con.getecordCounts();
		System.out.println(count);
		con.editRecord("ER10001", "projectID", "P00200");
		con.editRecord("MR10003", "location", "US");
		con.editRecord("MR10005", "mailID", "john@mail.concordia.ca");
		con.printData();
		
		
//		recordController recordController = new recordController(repo);
//		try{
//			Registry registry = LocateRegistry.createRegistry(2964);
//			registry.bind("recordApi", (Remote) recordController);
//			System.out.println("Server is started");
//		} catch(Exception e) {
//			
//		}

	}

}
