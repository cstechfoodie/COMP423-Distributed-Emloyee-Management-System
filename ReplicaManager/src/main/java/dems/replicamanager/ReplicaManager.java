package dems.replicamanager;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import udp_bridge.Reliable;
import udp_bridge.*;
import udp_bridge.Process;

public class ReplicaManager {
	
	
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
	
	
	
	
	private String REPLICA_NAME;
	private int FAILURE_COUNTER = 0;
	private Reliable forward;
	private Reliable request;
	private Process nextRM;
	private Process prevRM;
	private Process replica;
	
	
	public ReplicaManager(String name, int local1, int local2, Process replica, Process nextRM, Process prevRM) throws SocketException {
		this.REPLICA_NAME = name;
		this.nextRM = nextRM;
		this.prevRM = prevRM;
		this.replica = replica;
		
		this.forward = new Reliable(new Unicast(local1, replica));
		this.request = new Reliable(new Unicast(local2, nextRM));
		
	}
	
	
	/**
	 * listen on local port for receive the message from FE
	 * 
	 */
	private String listen() throws IOException {
		String msg = this.forward.listen();
		System.out.println(msg);
		return msg;
	}
	
	
	private void parseMsg(String msg) throws IOException {
		if(msg.contains(REPLICA_NAME) && msg.contains("failed")) {
			FAILURE_COUNTER++;
			System.out.println("failure report received");
		}
		else if(msg.contains("crashed")) {
			FAILURE_COUNTER++;
			System.out.println("skeptical crash report received");
		}
		else if(msg.contains("requestmap")) {
			System.out.println("Getting map info from my own replica and forward it to requesting RM");
			forwardMapInfoToRM(requestMapInfoFromReplica());
		}/* else {
			testingMethod(msg);
		}*/
	}
	
	public boolean handleUDPRequests() {
		boolean recovered = false;
		try {
			parseMsg(listen());
			if(FAILURE_COUNTER >= 3) {
				forward.changeRemote(replica);
				forward.send("recover");
				System.out.println("Receiving map info from other RM and forward it to my own replica");
				//forwardMapInfoToReplica(requestMapInfoFromRM());
				forwardMapInfoToReplica(requestMapInfoFromRM());
				recovered = true;
			}
			if(recovered) {
				FAILURE_COUNTER = 0;
				System.out.println("Failure conter reset to 0");
			}
			
			return recovered;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return recovered;
		}
	}
	
	public void forwardMapInfoToReplica(String map) throws IOException {
		this.forward.changeRemote(replica);
		this.forward.send(map);
	}
	
	public void forwardMapInfoToRM(String map) throws IOException {
		this.request.changeRemote(prevRM);
		
		this.request.send(map);
	}
	
	public String requestMapInfoFromRM() {
		try {
			this.request.changeRemote(nextRM);
			this.request.send("requestmap");
			System.out.println("sending requestmap to other RM");
			String map = request.listen(); // waiting for reply
			return map;
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
		return " ! ! ";
	}
	
	public String requestMapInfoFromReplica() {
		try {
			forward.changeRemote(replica);
			forward.send("requestmap");
			System.out.println("sending requestmap to replica");
			String map = forward.listen();
			return map;
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			System.out.println("--------------1-----------------");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("--------------2-----------------");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("--------------3-----------------");
			e.printStackTrace();
		}
		return " ! ! ";
	}
	
	
	//for unit test purpose only 
	/*private void testingMethod(String request) {
		if(request.contains("requestMapInfoFromReplica")) {
			System.out.println(requestMapInfoFromReplica());
		}
		if(request.contains("forwardMapInfoToReplica")) {
			String test1 = "MR1124;Shunyu;Wang;35345;shunyu@gmail.com;P2354;Jack;killJava;CA,ER5555;Jill;Dong;234234;jill2yahoo.com;P9935! ! ";
			String test2 = "ER30000;Jack;Smith;88787;new@concordia.ca;P234,MR30001;aaa;ddd;12345;aaa@google.com;Pjkl;Joshua;LearnC++;US,!ER30000;Jack;Smith;88787;new@concordia.ca;P234,MR30001;aaa;ddd;12345;aaa@google.com;Pjkl;Joshua;LearnC++;US,!ER30000;Jack;Smith;88787;new@concordia.ca;P234,MR30001;aaa;ddd;12345;aaa@google.com;Pjkl;Joshua;LearnC++;US,";
			String test3 = "ER30000;Jack;Smith;88787;new@concordia.ca;P234,MR30001;aaa;ddd;12345;aaa@google.com;Pjkl;Joshua;LearnC++;US,!MR30000;Shunyu;Wang;44944;a@google.com;P234;Joshua;LearnJava;CA,!ER30001;ccc;zzz;67890;ccc@google.com;Pjkl,";
			try {
				udp.send("recover");
				System.out.println("TEST, RM sends 'recover' to replica");
				forwardMapInfoToReplica(test3);
				System.out.println("replica is recoverd");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(request.contains("initializeReplica")) {
			try {
				udp.send("recover");
				System.out.println("TEST, RM sends 'revover' to replica with empty maps");
				forwardMapInfoToReplica(" ! ! ");
				System.out.println("replica is initialized");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/
}
