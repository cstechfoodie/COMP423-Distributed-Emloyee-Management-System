package ca.dems.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import dems.api.Record;

public class UDPClient {
	
	public synchronized static String getRecordCounts(String address, int port) {
		String msg = "1";
		String replyMessage = null;
		
		DatagramSocket aSocket = null;
		byte[] requestMessage = msg.getBytes();
		
		try {
			aSocket = new DatagramSocket();
			InetAddress host = InetAddress.getByName(address);
			
			DatagramPacket request = new DatagramPacket(requestMessage, requestMessage.length, host, port);
			aSocket.send(request);
			
			byte[] buffer = new byte[100];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			replyMessage = new String(reply.getData()).trim();
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
	
	public synchronized static String checkRecordInRemoteServer(String address, int port, String recordID) {
		String msg = "2" + recordID;
		String replyMessage = null;
		
		DatagramSocket aSocket = null;
		byte[] requestMessage = msg.getBytes();
		
		try {
			aSocket = new DatagramSocket();
			InetAddress host = InetAddress.getByName(address);
			
			DatagramPacket request = new DatagramPacket(requestMessage, requestMessage.length, host, port);
			aSocket.send(request);
			
			byte[] buffer = new byte[100];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			replyMessage = new String(reply.getData()).trim();
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
	
	public synchronized static String transeferRecord(String address, int port, Record record) throws JsonProcessingException {
//		ObjectMapper objectMapper = new ObjectMapper();
//		String recordJson = objectMapper.writeValueAsString(record);		
		String msg = "3";
		try {
			msg = "3" + new String(BytesUtil.toByteArray(record));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String replyMessage = null;
		
		DatagramSocket aSocket = null;
		byte[] requestMessage = msg.getBytes();
		
		try {
			aSocket = new DatagramSocket();
			InetAddress host = InetAddress.getByName(address);
			
			DatagramPacket request = new DatagramPacket(requestMessage, requestMessage.length, host, port);
			aSocket.send(request);
			
			byte[] buffer = new byte[100];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			replyMessage = new String(reply.getData()).trim();
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
	
	public synchronized static String rollbackTransfer(String address, int port, String recordID) {
		String msg = "4" + recordID;
		String replyMessage = null;
		
		DatagramSocket aSocket = null;
		byte[] requestMessage = msg.getBytes();
		
		try {
			aSocket = new DatagramSocket();
			InetAddress host = InetAddress.getByName(address);
			
			DatagramPacket request = new DatagramPacket(requestMessage, requestMessage.length, host, port);
			aSocket.send(request);
			
			byte[] buffer = new byte[100];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			replyMessage = new String(reply.getData()).trim();
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
