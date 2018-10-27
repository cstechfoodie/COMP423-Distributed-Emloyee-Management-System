package RecordApp;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import us.dems.model.UDPServer;
import us.dems.repository.IRecordRepository;
import us.dems.repository.RecordRepository;

public class USServer {


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
		      String name = "USRecord";
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
