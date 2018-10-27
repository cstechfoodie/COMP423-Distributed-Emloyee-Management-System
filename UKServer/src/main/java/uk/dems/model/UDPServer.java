package uk.dems.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.fasterxml.jackson.databind.ObjectMapper;
import dems.api.EmployeeRecord;
import dems.api.ManagerRecord;
import dems.api.Record;
import uk.dems.repository.IRecordRepository;

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
			aSocket = new DatagramSocket(9002);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] buffer = new byte[1000];

		while (true) {
			DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			System.out.println("UK UDP Server Is Up!");
			try {
				aSocket.receive(request);
				String requestMsg = new String(request.getData()).trim();
				if(requestMsg.startsWith("1")) {
					int count = this.repo.getRecordCounts();
					String replyMessage = "UK " + count;
					DatagramPacket response = new DatagramPacket(replyMessage.getBytes(), replyMessage.getBytes().length,
							request.getAddress(), request.getPort());
					aSocket.send(response);
				}
				if(requestMsg.startsWith("2")) {
					String recordID = requestMsg.substring(1,8);
					boolean isExisted = this.repo.isExisted(recordID);
					String replyMessage;
					if(isExisted) {
						replyMessage = "true";
					}
					else {
						replyMessage = "false";
					}
					DatagramPacket response = new DatagramPacket(replyMessage.getBytes(), replyMessage.getBytes().length,
							request.getAddress(), request.getPort());
					aSocket.send(response);
				}
				if(requestMsg.startsWith("3")) {
					String record = requestMsg.substring(1);
//					ObjectMapper objectMapper = new ObjectMapper();
					Record obj = null;
					try {
						obj = (Record) BytesUtil.toObject(record.getBytes());
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(requestMsg.contains("MR")) {
						//ManagerRecord mr = objectMapper.readValue(recordJson, ManagerRecord.class);	
						ManagerRecord mr = (ManagerRecord) obj;
						this.repo.createMRecord(mr);
					} else {
						//EmployeeRecord er = objectMapper.readValue(recordJson, EmployeeRecord.class);
						EmployeeRecord er = (EmployeeRecord) obj;
						this.repo.createMRecord(er);
					}
					String replyMessage = "true";
					DatagramPacket response = new DatagramPacket(replyMessage.getBytes(), replyMessage.getBytes().length,
							request.getAddress(), request.getPort());
					aSocket.send(response);
				}
				if(requestMsg.startsWith("4")) {
					String recordID = requestMsg.substring(1);
					boolean isExisted = this.repo.isExisted(recordID);
					String replyMessage;
					if(isExisted) {
						replyMessage = this.repo.deleteRecord(recordID)? "true" : "false";
					}
					else {
						replyMessage = "true";
					}
					DatagramPacket response = new DatagramPacket(replyMessage.getBytes(), replyMessage.getBytes().length,
							request.getAddress(), request.getPort());
					aSocket.send(response);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Warning: UK UDP Server Problem!");
				e.printStackTrace();
			}
		}
	}
}
