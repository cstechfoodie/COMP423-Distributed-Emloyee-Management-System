package Replica1.Replica1;

import Replica1.Replica1.Interface.IRecordRepository;
import Replica1.Replica1.Interface.RecordApi;

/**
 * Hello world!
 *
 */
public class ReplicaLauncher 
{
    public static void main( String[] args )
    {
	    try{
	      // create and initialize the ORB
	      
	      //initialize the database, server, udp server 
	      IRecordRepository repoCA = (IRecordRepository) new ca.dems.repository.RecordRepository();
	      RecordApi recordControllerCA = (RecordApi) new ca.dems.RecordApp.RecordController((ca.dems.repository.IRecordRepository) repoCA);
	      System.out.println("CAServer ready and waiting ...");
	      ca.dems.model.UDPServer udpCA = new ca.dems.model.UDPServer((ca.dems.repository.IRecordRepository) repoCA);
	      udpCA.start();	      
	      
	      IRecordRepository repoUK = (IRecordRepository) new uk.dems.repository.RecordRepository();
	      RecordApi recordControllerUK = (RecordApi) new uk.dems.RecordApp.RecordController((uk.dems.repository.IRecordRepository) repoUK);
	      System.out.println("UKServer ready and waiting ...");
	      uk.dems.model.UDPServer udpUK = new uk.dems.model.UDPServer((uk.dems.repository.IRecordRepository) repoUK);
	      udpUK.start();	      
	      
	      IRecordRepository repoUS = (IRecordRepository) new us.dems.repository.RecordRepository();
	      RecordApi recordControllerUS = (RecordApi) new us.dems.RecordApp.RecordController((us.dems.repository.IRecordRepository) repoUS);
	      System.out.println("USServer ready and waiting ...");
	      us.dems.model.UDPServer udpUS = new us.dems.model.UDPServer((us.dems.repository.IRecordRepository) repoUS);
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
