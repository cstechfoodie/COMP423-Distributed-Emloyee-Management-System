package server;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;

import udp_bridge.Message;
import udp_bridge.Multicast;
import udp_bridge.Reliable;
import udp_bridge.UDP;
import udp_bridge.Unicast;
import udp_bridge.Unreliable;
import udp_bridge.Process;


public class TestServer {

	public static void main(String[] args) throws IOException {
		try {
			Process[] p = {
					new Process("localhost", 1050),
					new Process("localhost", 1050),
					new Process("localhost", 1050)};
			UDP p1 = new Reliable(new Multicast(1099, p[1],p[2]));
			UDP p2 = new Reliable(1098,1050);
			UDP p3 = new Reliable(1097,1050,1050);
			
			new Thread() {
				public void run() {
					try {
						while(true) {
							System.out.println("P2:"+p2.listen());	
						}
						
						//System.exit(0);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
			new Thread() {
				public void run() {
					try {
						while(true) {
							System.out.println("P3:"+p3.listen());	
						}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
			
			
			
			
			//p1.setRemote(Message.fromBytes(p).process);
		
			p1.send("hi");
			p1.send("hello");
			p2.send("p2");
			System.out.println("p1:" + p1.listen());
			//p2.listen();
			
		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
