package Replica1.Replica1;

import dems.RecordApp.CARecordController;
import dems.RecordApp.RecordApi;
import dems.RecordApp.UKRecordController;
import dems.RecordApp.USRecordController;
import dems.model.CAUDPServer;
import dems.model.UKUDPServer;
import dems.model.USUDPServer;
import dems.repository.IRecordRepository;
import dems.repository.RecordRepository;

public class ReplicaLauncher 
{
    public static void main( String[] args )
    {
	    try{
	      // create and initialize the ORB
	      
	      //initialize the database, server, udp server 
	      IRecordRepository repoCA =  new RecordRepository();
	      RecordApi recordControllerCA = (RecordApi) new CARecordController(repoCA);
	      System.out.println("CAServer ready and waiting ...");
	      CAUDPServer udpCA = new CAUDPServer(repoCA);
	      udpCA.start();	      
	      
	      IRecordRepository repoUK = new RecordRepository();
	      RecordApi recordControllerUK = (RecordApi) new UKRecordController(repoUK);
	      System.out.println("UKServer ready and waiting ...");
	      UKUDPServer udpUK = new UKUDPServer(repoUK);
	      udpUK.start();	      
	      
	      IRecordRepository repoUS = new RecordRepository();
	      RecordApi recordControllerUS = (RecordApi) new USRecordController(repoUS);
	      System.out.println("USServer ready and waiting ...");
	      USUDPServer udpUS = new USUDPServer(repoUS);
	      udpUS.start();	
	      
	      ControllerDispatcher cd = new ControllerDispatcher(recordControllerCA, recordControllerUK, recordControllerUS, udpCA, udpUK, udpUS);

	      UDPRequestHandler requestHandler = new UDPRequestHandler(cd);

	      
	      while(true) {
	    	  requestHandler.listenAndHandleRequest();
	      }
	      
	    } 
	        
	      catch (Exception e) {
	        System.err.println("ERROR: " + e);
	        e.printStackTrace(System.out);
	      }
	          
	   
    }
}
