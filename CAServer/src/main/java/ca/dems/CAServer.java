package ca.dems;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ca.dems.api.recordController;
import ca.dems.model.EmployeeRecord;
import ca.dems.model.ManagerRecord;
import ca.dems.model.Project;
import ca.dems.model.Record;
import ca.dems.repository.IRecordRepository;
import ca.dems.repository.RecordRepository;

public class CAServer {

	public static void main(String[] args) {
		IRecordRepository repo = new RecordRepository();
		
		Record r;
		for(int i = 0; i < 10; i++) {
			r = new ManagerRecord("Shunyu", "Wang", 12345 + i, "shunyu.wang@mail.concordia.ca", new Project("P00001", "client", "project"), "CA");
			repo.createMRecord(r);
		}
		
		for(int i = 0; i < 10; i++) {
			r = new EmployeeRecord("Shunyu", "Wang", 67890 + i, "shunyu.wang@mail.concordia.ca", "P00001");
			repo.createERecord(r);
		}
		
		
		recordController recordController = new recordController(repo);
		try{
			Registry registry = LocateRegistry.createRegistry(2964);
			registry.bind("recordApi", (Remote) recordController);
			System.out.println("Server is started");
		} catch(Exception e) {
			
		}

	}

}
