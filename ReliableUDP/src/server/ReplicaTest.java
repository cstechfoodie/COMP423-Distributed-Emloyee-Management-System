package server;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import udp_bridge.Reliable;

public class ReplicaTest {

	public static void main(String[] args) {
		try {
			int port = 1099;
			Reliable udp = new Reliable(port,3000);
			System.out.println("Server listening on: " + port);
			while(true) {
				System.out.println(udp.listen());
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
