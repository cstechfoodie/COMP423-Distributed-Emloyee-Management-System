package ca.dems;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ca.dems.api.recordController;
import ca.dems.repository.IRecordRepository;
import ca.dems.repository.RecordRepository;

public class CAServer {

	public static void main(String[] args) {
		IRecordRepository repo = new RecordRepository();
		recordController recordController = new recordController(repo);
		try{
			Registry registry = LocateRegistry.createRegistry(2964);
			registry.bind("recordApi", (Remote) recordController);
			System.out.println("Server is started");
		} catch(Exception e) {
			
		}

	}

}
