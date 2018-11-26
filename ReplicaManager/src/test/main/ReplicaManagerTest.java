import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import dems.replicamanager.ReplicaManager;
import udp_bridge.Reliable;
import udp_bridge.UDP;
import udp_bridge.Unicast;

public class ReplicaManagerTest {

//	public  static ReplicaManager rm;
//	
//	@BeforeClass
//	public static void setup() {
//		rm = new ReplicaManager();
//		rm.handleUDPRequests();
//	}
	
	@Test
	public void requestMapInfoFromReplicaTest() {
		UDP udp;
		try {
			udp = new Reliable(10002, 7011);
			udp.send("requestMapInfoFromReplica");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void initializeReplicaTest() {
		UDP udp;
		try {
			udp = new Reliable(new Unicast(7011));
			udp.send("initializeReplica");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void forwardMapInfoToReplicaTest() {
		try {
			UDP udp = new Reliable(new Unicast(7011));
			udp.send("forwardMapInfoToReplica");
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
