package udp_bridge;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Process implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final InetAddress address;
	public final int port;
	
	public Process(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}
	
	public Process(String address, int port) throws UnknownHostException {
		this.address =  InetAddress.getByName(address);
		this.port = port;
	}

	public Process(int port) throws UnknownHostException {
		this("localhost", port);
	}
	
	@Override
	public boolean equals(Object ob1) {
		if(ob1 instanceof Process) {
			return ((Process)ob1).address.equals(address) && 
					((Process)ob1).port == port;
		}else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return address.hashCode() + port;
	}
}
