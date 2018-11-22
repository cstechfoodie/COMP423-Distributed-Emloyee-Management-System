package client;

import java.net.SocketException;

import server.Sequencer;
import udp_bridge.Reliable;
import udp_bridge.Unreliable;

public class ClientSequencerTest {

	public static void main(String[] args) {
		try {
			
			Reliable c1 = new Reliable(new Unreliable(50,0,3011, 1050));
			Reliable c2 = new Reliable(new Unreliable(50,0,3012, 1050));
			Reliable c3 = new Reliable(new Unreliable(50,0,3013, 1050));
			
			
			c1.send("HI from c1".getBytes());
			c2.send("Hello from c2".getBytes());
			c3.send("hi from c3".getBytes());
			
			System.exit(0);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
