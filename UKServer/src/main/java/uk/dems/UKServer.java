package uk.dems;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import dems.api.Project;
import dems.api.RecordApi;
import dems.api.RecordController;
import uk.dems.model.Record;
import uk.dems.model.UDPServer;
import uk.dems.repository.IRecordRepository;
import uk.dems.repository.RecordRepository;

public class UKServer {

	public static void main(String[] args) throws RemoteException {
		
		//initialize the database
		IRecordRepository repo = new RecordRepository();
		RecordApi con = new RecordController(repo);
		
		UDPServer udp = null;
		try{
			udp = new UDPServer(repo);
			udp.start();

			Registry registry = LocateRegistry.createRegistry(2965);
			registry.bind("recordApi", con);
			
			System.out.println("UK Server is started");
		} catch(Exception e) {
			System.out.println(e);
		}
		finally {
			//udp.stop();
		}

	}

}
