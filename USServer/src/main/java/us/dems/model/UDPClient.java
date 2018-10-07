package us.dems.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient {
	
	public static String getRecordCounts(String address, int port) {
		String msg = "get record counts";
		String replyMessage = null;
		
		DatagramSocket aSocket = null;
		byte[] requestMessage = msg.getBytes();
		
		try {
			aSocket = new DatagramSocket();
			InetAddress host = InetAddress.getByName(address);
			
			DatagramPacket request = new DatagramPacket(requestMessage, requestMessage.length, host, port);
			aSocket.send(request);
			
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			replyMessage = new String(reply.getData());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			aSocket.close();			
		}
		return replyMessage;
	}

}
