package dems.replicamanager;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import udp_bridge.Reliable;
import udp_bridge.UDP;
import udp_bridge.Unicast;

public class ReplicaManager {
	
	private static int FAILURE_COUNTER = 0;
	
	private int LIS_PORT = 6001;
	
	private int DES_PORT = 7001;
	
	private UDP udp;
	
	public ReplicaManager() {
		try {
			this.udp = new Reliable(new Unicast(LIS_PORT, DES_PORT)); //listen 6001 replica1 listen on 7001
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
	
	
	private void parseMsg(String msg) {
		if(msg.contains("1") && msg.contains("failed")) {
			FAILURE_COUNTER++;
			System.out.println("failure report recived");
		}
		if(msg.contains("crashed")) {
			FAILURE_COUNTER = 4;
		}
	}
	
	public boolean trySendRecoverRequest() {
		boolean msgSent = false;
		try {
			parseMsg(listen());
			if(FAILURE_COUNTER >= 3) {
				udp.send("recover");
				msgSent = true;
			}
			if(msgSent) {
				FAILURE_COUNTER = 0;
				System.out.println("Failure conter reset to 0");
			}
			
			return msgSent;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return msgSent;
		}
	}
}
