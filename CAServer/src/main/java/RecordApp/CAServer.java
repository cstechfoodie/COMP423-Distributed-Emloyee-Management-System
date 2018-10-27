package RecordApp;


import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import ca.dems.model.UDPServer;
import ca.dems.repository.IRecordRepository;
import ca.dems.repository.RecordRepository;

public class CAServer {
//
//	public static void main(String[] args) throws RemoteException {
//		
//		//initialize the database
//		IRecordRepository repo = new RecordRepository();
//		RecordApi con = new RecordController(repo);
//	
//		UDPServer udp = null;
//		try{
//			udp = new UDPServer(repo);
//			udp.start();
//
//			Registry registry = LocateRegistry.createRegistry(1099);
//			registry.bind("recordApi", con);
//			
//			System.out.println("CA Server is started");
//		} catch(Exception e) {
//			System.out.println(e);
//		}
//		finally {
//			//udp.stop();
//		}
//
//	}
	
	
	 public static void main(String args[]) {
		    try{
		      // create and initialize the ORB
		      ORB orb = ORB.init(args, null);
		      
		      //initialize the database
			  IRecordRepository repo = new RecordRepository();

		      // get reference to rootpoa & activate the POAManager
		      POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
		      rootpoa.the_POAManager().activate();

		      // create servant and register it with the ORB
		      RecordController recordController = new RecordController(repo);
		      recordController.setORB(orb); 

		      // get object reference from the servant
		      org.omg.CORBA.Object ref = rootpoa.servant_to_reference(recordController);
		      Record href = (Record) RecordHelper.narrow(ref);
		          
		      // get the root naming context
		      // NameService invokes the name service
		      org.omg.CORBA.Object objRef =
		          orb.resolve_initial_references("NameService");
		      // Use NamingContextExt which is part of the Interoperable
		      // Naming Service (INS) specification.
		      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

		      // bind the Object Reference in Naming
		      String name = "CARecord";
		      NameComponent path[] = ncRef.to_name( name );
		      ncRef.rebind(path, (Object) href);

		      System.out.println("CAServer ready and waiting ...");
		      
		      UDPServer udp = new UDPServer(repo);
			  udp.start();

		      // wait for invocations from clients
		      orb.run();
		      
		    } 
		        
		      catch (Exception e) {
		        System.err.println("ERROR: " + e);
		        e.printStackTrace(System.out);
		      }
		          
		      System.out.println("CAServer Exiting ...");
		        
		  }

}
