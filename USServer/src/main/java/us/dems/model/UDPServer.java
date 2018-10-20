package us.dems.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import dems.api.RecordController;
import us.dems.repository.IRecordRepository;

public class UDPServer extends Thread {

	private IRecordRepository repo;

	public UDPServer(IRecordRepository repo) {
		super();
		this.repo = repo;
	}

	@SuppressWarnings({ "resource" })
	@Override
	public void run() {
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(9003);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] buffer = new byte[100];

		while (true) {
			DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			System.out.println("US UDP Server Is Up!");
			try {
				aSocket.receive(request);
				if(request.getLength() >0) {
					int count = this.repo.getRecordCounts();
					String replyMessage = "US " + count;
					DatagramPacket response = new DatagramPacket(replyMessage.getBytes(), replyMessage.getBytes().length,
							request.getAddress(), request.getPort());
					aSocket.send(response);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Warning: US UDP Server Problem!");
				e.printStackTrace();
			}
		}
	}
}
