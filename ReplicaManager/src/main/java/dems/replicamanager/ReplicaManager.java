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
	
	private int forwardPort = 40011;  // port used to established reliable connection with next before
	
	private int rm2listeningPort = 7012; //request map info from next RM
	
	private int rm3listeningPort = 7013; //return map info to RM before
	
	private int DES_PORT = 7021;
	
	private UDP udp;
	
	private UDP requestMapFromRM;
	
	private UDP fowardMapToRM;
	
	public ReplicaManager() {
		try {
			this.udp = new Reliable(LIS_PORT, DES_PORT); //listen 7011 replica1 listen on 7021
			this.requestMapFromRM = new Reliable(requestPort, rm2listeningPort);
			this.fowardMapToRM = new Reliable(forwardPort, rm3listeningPort);
			System.out.println("RM1 starts listening pn port " + LIS_PORT + "; ready to send msg to replica1 on " + DES_PORT);
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ReplicaManager(int LIS_PORT, int DES_PORT) {
		try {
			this.LIS_PORT = LIS_PORT;
			this.DES_PORT = DES_PORT;
			this.udp = new Reliable(LIS_PORT, DES_PORT); //listen 6001 replica1 listen on 7001
			this.requestMapFromRM = new Reliable(requestPort, rm2listeningPort);
			this.fowardMapToRM = new Reliable(forwardPort, rm3listeningPort);
			System.out.println("RM1 starts listening pn port " + LIS_PORT + "; ready to send msg to replica1 on " + DES_PORT);
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
		}
	}
	
	public boolean handleUDPRequests() {
		boolean recovered = false;
		try {
			parseMsg(listen());
			if(FAILURE_COUNTER >= 3) {
				udp.send("recover");
				System.out.println("Recieving map info from other RM and forward it to my own replica");
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
		return " @ @ ";
	}
	
	public String requestMapInfoFromReplica() {
		try {
			udp.send("requestmap");
			String map = udp.listen();
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
		return " @ @ ";
	}
}
