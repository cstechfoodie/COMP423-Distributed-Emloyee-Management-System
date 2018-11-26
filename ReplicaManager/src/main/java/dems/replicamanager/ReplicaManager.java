package dems.replicamanager;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import udp_bridge.Reliable;
import udp_bridge.UDP;
import udp_bridge.Unicast;

public class ReplicaManager {
	
	private static int FAILURE_COUNTER = 0;
	
	private int LIS_PORT = 7011; //the only port listening to message
	
	private int requestPort = 30011; // port used to established reliable connection with next RM
										//useful to listen to the reply from RM only(NECESSARY)
	
	private int forwardPort = 40011;  // port used to established reliable connection with next before(NOT NECESSARY)
	
	private int rm2listeningPort = 7012; //request map info from next RM
	
	private int rm3listeningPort = 7013; //return map info to RM before
	
	private int REPLICA_PORT = 7021;
	
	private UDP udp;
	
	private UDP requestMapFromRM;
	
	private UDP fowardMapToRM;
	
	public ReplicaManager() {
		try {
			this.udp = new Reliable(LIS_PORT, REPLICA_PORT); //listen 7011 replica1 listen on 7021
			this.requestMapFromRM = new Reliable(requestPort, rm2listeningPort);
			this.fowardMapToRM = new Reliable(forwardPort, rm3listeningPort);
			System.out.println("RM1 starts listening pn port " + LIS_PORT + "; ready to send msg to replica1 on " + REPLICA_PORT);
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ReplicaManager(int LIS_PORT, int REPLICA_PORT, int rm2listeningPort, int rm3listeningPort) {
		try {
			this.LIS_PORT = LIS_PORT;
			this.REPLICA_PORT = REPLICA_PORT;
			this.rm2listeningPort = rm2listeningPort;
			this.rm3listeningPort = rm3listeningPort;
			this.udp = new Reliable(LIS_PORT, REPLICA_PORT); //listen 6001 replica1 listen on 7001
			this.requestMapFromRM = new Reliable(requestPort, rm2listeningPort);
			this.fowardMapToRM = new Reliable(forwardPort, rm3listeningPort);
			System.out.println("RM1 starts listening pn port " + LIS_PORT + "; ready to send msg to replica1 on " + REPLICA_PORT);
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * listen on local port for receive the message from FE
	 * 
	 */
	private String listen() throws IOException {
		String msg = udp.listen();
		System.out.println(msg);
		return msg;
	}
	
	
	private void parseMsg(String msg) throws IOException {
		if(msg.contains("1") && msg.contains("failed")) {
			FAILURE_COUNTER++;
			System.out.println("failure report recived");
		}
		if(msg.contains("crashed")) {
			FAILURE_COUNTER = 4;
		}
		if(msg.contains("requestmap")) {
			System.out.println("Getting map info from my own replica and forward it to requesting RM");
			forwardMapInfoToRM(requestMapInfoFromReplica());
		} else {
			testingMethod(msg);
		}
	}
	
	public boolean handleUDPRequests() {
		boolean recovered = false;
		try {
			parseMsg(listen());
			if(FAILURE_COUNTER >= 3) {
				udp.send("recover");
				System.out.println("Recieving map info from other RM and forward it to my own replica");
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
		udp.send(map);
	}
	
	public void forwardMapInfoToRM(String map) throws IOException {
		this.fowardMapToRM.send(map);
	}
	
	public String requestMapInfoFromRM() {
		try {
			this.requestMapFromRM.send("requestmap");
			System.out.println("sending requestmap to other RM");
			String map = requestMapFromRM.listen(); // waiting for reply
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
			udp.send("requestmap");
			System.out.println("sending requestmap to replica");
			String map = udp.listen();
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
	private void testingMethod(String request) {
		if(request.contains("requestMapInfoFromReplica")) {
			System.out.println(requestMapInfoFromReplica());
		}
		if(request.contains("forwardMapInfoToReplica")) {
			String test1 = "MR1124;Shunyu;Wang;35345;shunyu@gmail.com;P2354;Jack;killJava;CA,ER5555;Jill;Dong;234234;jill2yahoo.com;P9935! ! ";
			String test2 = "ER30000;Jack;Smith;88787;new@concordia.ca;P234,MR30001;aaa;ddd;12345;aaa@google.com;Pjkl;Joshua;LearnC++;US,!ER30000;Jack;Smith;88787;new@concordia.ca;P234,MR30001;aaa;ddd;12345;aaa@google.com;Pjkl;Joshua;LearnC++;US,!ER30000;Jack;Smith;88787;new@concordia.ca;P234,MR30001;aaa;ddd;12345;aaa@google.com;Pjkl;Joshua;LearnC++;US,";
			String test3 = "ER30000;Jack;Smith;88787;new@concordia.ca;P234,MR30001;aaa;ddd;12345;aaa@google.com;Pjkl;Joshua;LearnC++;US,!MR30000;Shunyu;Wang;44944;a@google.com;P234;Joshua;LearnJava;CA,!ER30001;ccc;zzz;67890;ccc@google.com;Pjkl,";
			try {
				udp.send("recover");
				System.out.println("TEST, RM sends 'revover' to replica");
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
	}
}
