package ca.dems;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ca.dems.model.UDPServer;
import ca.dems.repository.IRecordRepository;
import ca.dems.repository.RecordRepository;
import dems.api.Project;
import dems.api.Record;
import dems.api.RecordApi;
import dems.api.RecordController;

public class CAServer {

	public static void main(String[] args) throws RemoteException {
		
		//initialize the database
		IRecordRepository repo = new RecordRepository();
		RecordApi con = new RecordController(repo);
	
		UDPServer udp = null;
		try{
			udp = new UDPServer(repo);
			udp.start();

			Registry registry = LocateRegistry.createRegistry(1099);
			registry.bind("recordApi", con);
			
			System.out.println("CA Server is started");
		} catch(Exception e) {
			System.out.println(e);
		}
		finally {
			//udp.stop();
		}

	}

}
