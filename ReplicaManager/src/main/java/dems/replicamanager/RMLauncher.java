package dems.replicamanager;
import java.net.SocketException;
import java.net.UnknownHostException;

import udp_bridge.*;
import udp_bridge.Process;

public class RMLauncher {

	public static void main(String[] args) {
		
		
		/**
		 * 		
				Process replica3 = new Process(port of your replica that listens to RM);
				Process replica2 = new Process(port of your replica that listens to RM);
				Process replica1 = new Process(port of your replica that listens to RM);
				Process rm1 = new Process(3010); //listening for FE and previous RM
				Process rm11 = new Process(3011);//listening for the requested map (next rm replies to that)
				Process rm2 = new Process(4010);
				Process rm22 = new Process(4011);
				Process rm3 = new Process(5010);
				Process rm33 = new Process(5011);
				
				Process rmX = new Process(address, port);
				Process rmXX = new Process(address, port);
				
				
	 			ReplicaManager rmanagerX = new ReplicaManager("ReplicaX", rmX.port,rmXX.port, replicaX, rm(X+1), rm(X - 1));
				ReplicaManager rmanager3 = new ReplicaManager("Replica3", rm3.port,rm33.port, replica3, rm1, rm22);
				ReplicaManager rmanager2 = new ReplicaManager("Replica2", rm2.port,rm22.port, replica2, rm3, rm11);
				ReplicaManager rmanager1 = new ReplicaManager("Replica1", rm1.port,rm11.port, replica1, rm2, rm33);
				
		 */
		try {
			Process replica1 = new Process(7021);
			Process rm1 = new Process(3010); //listening for FE and previous RM
			Process rm11 = new Process(3011);//listening for the requested map (next rm replies to that)
			Process rm2 = new Process(4010);
			Process rm22 = new Process(4011);
			Process rm3 = new Process(5010);
			Process rm33 = new Process(5011);
			ReplicaManager rmanager1 = new ReplicaManager("Replica1", rm1.port,rm11.port, replica1, rm2, rm33);
			boolean msgSent = false;
			while(true) {
				msgSent = rmanager1.handleUDPRequests();
				if(msgSent) {
					System.out.println("'recover' msg sent to replica1");
				} else {
					System.out.println("Not trigger recover msg");
				}
			}
		} catch (UnknownHostException | SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
