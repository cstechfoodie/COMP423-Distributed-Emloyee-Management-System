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
		IRecordRepository repo = new RecordRepository();
		
		
		RecordController con = new RecordController(repo);
		
		Record r;
		for(int i = 0; i < 10; i++) {
			r = new ManagerRecord("Shunyu", "Wang", 12345 + i, "shunyu.wang@mail.concordia.ca", new Project("P00001", "client", "project"), "CA");
			//repo.createMRecord(r);
			con.createMRecord("Shunyu", "Wang", 12345 + i, "shunyu.wang@mail.concordia.ca", new Project("P00001", "client", "project"), "CA");
		}
		
		for(int i = 0; i < 10; i++) {
			r = new EmployeeRecord("Shunyu", "Zang", 67890 + i, "shunyu.wang@mail.concordia.ca", "P00001");
			//repo.createERecord(r);
			con.createERecord("Shunyu", "Zang", 67890 + i, "shunyu.wang@mail.concordia.ca", "P00001");
		}
		
		con.printData();
		String count = con.getecordCounts();
		System.out.println(count);
		
		
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
