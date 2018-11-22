package Replica1.Replica1;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import RecordApp.RecordPackage.Project;
import Replica1.Replica1.Interface.RecordApi;
import udp_bridge.Reliable;
import udp_bridge.UDP;

public class UDPRequestHandler {
	
	private ControllerDispatcher cd;
	
	private UDP udp;
	
	private boolean needsRecover = false;
	
	/**
	 * @return the cd
	 */
	public ControllerDispatcher getCd() {
		return cd;
	}

	/**
	 * @param cd the cd to set
	 */
	public void setCd(ControllerDispatcher cd) {
		this.cd = cd;
	}

	/**
	 * @return the udp
	 */
	public UDP getUdp() {
		return udp;
	}

	/**
	 * @param udp the udp to set
	 */
	public void setUdp(UDP udp) {
		this.udp = udp;
	}

	/**
	 * @return the needsRecover
	 */
	public boolean isNeedsRecover() {
		return needsRecover;
	}

	/**
	 * @param needsRecover the needsRecover to set
	 */
	public void setNeedsRecover(boolean needsRecover) {
		this.needsRecover = needsRecover;
	}

	public UDPRequestHandler(ControllerDispatcher cd) {
		this.cd = cd;
		try {
			udp = new Reliable(7001, 10001);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public UDPRequestHandler(ControllerDispatcher cd, int localport, int feport) {
		this.cd = cd;
		try {
			udp = new Reliable(localport, feport);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String listen() throws IOException {
		String msg = udp.listen();
		System.out.println(msg);
		return msg;
	}
	
	
	public void listenAndHandleRequest() throws IOException {
		String msg = listen();
		String reply = "";
		RecordApi api = null;
		String managerID = "CA100001"; // get managerID first from UDP request message
		if(msg.contains("recover")) {
			needsRecover = true;
			cd = cd.recoverReplica();
			needsRecover = false;
		}
		if(msg.contains("CA")) {
			api = cd.findServer("CA");
			reply = processRequest(managerID, msg, api);
		}
		if(msg.contains("UK")) {
			api = cd.findServer("UK");
			reply = processRequest(managerID, msg, api);
		}
		if(msg.contains("US")) {
			api = cd.findServer("US");
			reply = processRequest(managerID, msg, api);
		}
		udp.send(reply);
	}
	
	
	private String processRequest(String managerID, String msg, RecordApi api) {
		String reply = "";
		if(msg.contains("createMRecord")) {
			api = cd.findServer("CA");
			//String recordId = "";
			String firstName = "";
			String lastName = "";
			Integer employeeID = 0;
			String mailID = "";
			Project project = new Project();
			String location = "";
			reply = api.createMRecord(managerID, firstName, lastName, employeeID, mailID, project, location);
		}
		if(msg.contains("createERecord")) {
			api = cd.findServer("CA");
			//String recordId = "";
			String firstName = "";
			String lastName = "";
			Integer employeeID = 0;
			String mailID = "";
			String projectID = "";
			String location = "";
			reply = api.createERecord(managerID, firstName, lastName, employeeID, mailID, projectID);
		}
		if(msg.contains("getRecordCounts")) {
			reply = api.getRecordCounts(managerID);
		}
		if(msg.contains("getRecordCounts")) {
			String recordID = "";
			String fieldName = "";
			String newValue = "";
			reply = api.editRecord(managerID, recordID, fieldName, newValue);
		}
		return reply;
	}
	

}
