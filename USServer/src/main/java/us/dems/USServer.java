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
		//String count = con.getRecordCounts();
		//System.out.println(count);
		con.editRecord("ER10001", "projectID", "P00200");
		con.editRecord("MR10003", "location", "US");
		con.editRecord("MR10005", "mailID", "john@mail.concordia.ca");
		con.printData();
		
		UDPServer udp = null;
		try{
			udp = new UDPServer(repo);
			udp.start();

			Registry registry = LocateRegistry.createRegistry(2966);
			registry.bind("recordApi", con);
			
			System.out.println("Server is started");
		} catch(Exception e) {
			System.out.println(e);
		}
		finally {
			//udp.stop();
		}

	}

}
