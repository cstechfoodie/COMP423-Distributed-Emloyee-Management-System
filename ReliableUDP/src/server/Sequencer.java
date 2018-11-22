package server;

import java.io.IOException;
import java.net.SocketException;
import java.util.LinkedList;

import udp_bridge.Reliable;


public class Sequencer{
	

	private Reliable udp;
	private LinkedList<byte[]> holdback = new LinkedList<byte[]>();
	
	private Receiver receiver = new Receiver();
	private Broadcaster broadcaster = new Broadcaster();
	
	
	public Sequencer(int localport, int...remoteports) throws SocketException {
		this.udp = new Reliable(localport, remoteports);
	}
	
	public void start() {
		receiver.start();
		broadcaster.start();
		
	}
	
	public void stop() {
		receiver.kill();
		broadcaster.kill();
		
	}
	
	
	
	
	private class Receiver extends Thread{
		private boolean running = true;
		@Override
		public void run() {
			while(running) {
				try {
					byte[] m = udp.receive();
					synchronized(holdback) {						
						holdback.add(m);
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		public void kill() {
			this.running = false;
		}
		
	}
	
	
	private class Broadcaster extends Thread{
		private boolean running = true;
		@Override
		public void run() {
			while(running) {
				if(holdback.size() > 0) {
					try {
						synchronized(holdback) {							
							udp.send(holdback.removeFirst());
						}
					} catch (IOException e) {

						e.printStackTrace();
					}
				}
			}
		}
		
		public void kill() {
			this.running = false;
		}
	}
	
	
}
