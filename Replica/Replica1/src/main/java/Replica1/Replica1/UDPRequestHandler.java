package Replica1.Replica1;

import java.net.SocketException;
import java.net.UnknownHostException;

import udp_bridge.Reliable;
import udp_bridge.UDP;

public class UDPRequestHandler {
	
	private ControllerDispatcher cd;
	
	private UDP reliable;
	
	public UDPRequestHandler(ControllerDispatcher cd) {
		this.cd = cd;
		try {
			reliable = new Reliable(7001, 10001);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
