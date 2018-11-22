package server;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import udp_bridge.Reliable;
import udp_bridge.UDP;

public class RemoteListen {

	public static void main(String[] args) throws SocketException, UnknownHostException {
		
		UDP p = new Reliable(1090, 0);
		
		new Thread() {
			public void run() {
				try {
					while(true) {
						System.out.println("P:"+p.listen());	
					}
					
					//System.exit(0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

	}

}
