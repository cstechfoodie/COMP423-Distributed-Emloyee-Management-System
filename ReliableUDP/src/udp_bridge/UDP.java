package udp_bridge;

import java.io.IOException;

public interface UDP {
	
	public void send(String message) throws IOException;
	
	public void send(byte[] message) throws IOException;
	
	public String listen() throws IOException;
	
	public byte[] receive() throws IOException;

}
