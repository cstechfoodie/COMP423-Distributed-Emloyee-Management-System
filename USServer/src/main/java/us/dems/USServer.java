package us.dems;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import dems.api.Project;
import dems.api.RecordApi;
import dems.api.RecordController;
import us.dems.model.Record;
import us.dems.model.UDPServer;
import us.dems.repository.IRecordRepository;
import us.dems.repository.RecordRepository;

public class USServer {

	public static void main(String[] args) throws RemoteException {
		
		//initialize the database
		IRecordRepository repo = new RecordRepository();
		RecordApi con = new RecordController(repo);

		UDPServer udp = null;
		try{
			udp = new UDPServer(repo);
			udp.start();

			Registry registry = LocateRegistry.createRegistry(2966);
			registry.bind("recordApi", con);
			
			System.out.println("US Server is started");
		} catch(Exception e) {
			System.out.println(e);
		}
		finally {
			//udp.stop();
		}

	}

}
