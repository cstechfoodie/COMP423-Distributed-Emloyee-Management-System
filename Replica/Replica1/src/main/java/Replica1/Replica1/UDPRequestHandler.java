package Replica1.Replica1;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import dems.RecordApp.RecordApi;
import dems.model.Project;
import udp_bridge.Reliable;
import udp_bridge.UDP;

public class UDPRequestHandler {
	
	private ControllerDispatcher cd;
	
	private UDP udp;
	
	private boolean needsRecover = false;

	public UDPRequestHandler(ControllerDispatcher cd) {
		this.cd = cd;
		try {
			udp = new Reliable(7021, 10001);
			System.out.print("The replica is listening to resquest on port 7001.");
			System.out.println(" The replica will respond to FE on 10001");
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param cd ControllerDispatcher
	 * @param localport udp local port listening udp request from front end
	 * @param feport FE port
	 */
	public UDPRequestHandler(ControllerDispatcher cd, int localport, int feport) {
		this.cd = cd;
		try {
			udp = new Reliable(localport, feport);
			System.out.print("The replica is listening to resquest on " + localport);
			System.out.println(" The replica will respond to FE on " + feport);
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
	
	/**
	 * take each request from udp and parse it, find appropriate server by managerID and pass the udp msg to it
	 * @throws IOException
	 */
	public void listenAndHandleRequest() throws IOException {
		String msg = listen();
		String reply = "";
		RecordApi api = null;
		
		String[] args = msg.split(";");
		for(int i = 0; i < args.length; i++) {
			args[i] = args[i].trim();
		}
		
		String managerID = args[1]; // get managerID first from UDP request message
		if(msg.contains("recover")) {
			needsRecover = true;
			cd = cd.initiateReplica();
			needsRecover = false;
		}
		if(needsRecover) {
			reply = "Server recovery being processed";
		} else {
			if(msg.contains("CA")) {
				api = cd.findServer("CA");
				reply = processRequest(managerID, args, api);
			}
			if(msg.contains("UK")) {
				api = cd.findServer("UK");
				reply = processRequest(managerID, args, api);
			}
			if(msg.contains("US")) {
				api = cd.findServer("US");
				reply = processRequest(managerID, args, api);
			}
		}
		udp.send(reply);
	}
	
	/**
	 * process each request with appropriate proxy
	 * @param managerID used as first argument in each operation
	 * @param msg raw udp message parsed to deprive information
	 * @param api represent the proxy on which each opeation call
	 * @return reply message from operations
	 */
	private String processRequest(String managerID, String[] args, RecordApi api) {
		String reply = "";
		if(args[0].equals("1")) {
			String firstName = args[2];
			String lastName = args[3];
			Integer employeeID = Integer.parseInt(args[4]);
			String mailID = args[5];
			Project project = new Project();
			project.setProjectID(args[6]);
			project.setClientName(args[7]);
			project.setProjectName(args[8]);
			String location = args[9];
			reply = api.createMRecord(managerID, firstName, lastName, employeeID, mailID, project, location);
		}
		else if(args[0].equals("2")) {
			String firstName = args[2];
			String lastName = args[3];
			Integer employeeID = Integer.parseInt(args[4]);
			String mailID = args[5];
			String projectID = args[6];
			reply = api.createERecord(managerID, firstName, lastName, employeeID, mailID, projectID);
		}
		else if(args[0].equals("3")) {
			reply = api.getRecordCounts(managerID);
		}
		else if(args[0].equals("4")) {
			String recordID = args[2];
			String fieldName = args[3];
			String newValue = args[4];
			reply = api.editRecord(managerID, recordID, fieldName, newValue);
		}
		else if(args[0].equals("5")) {
			String recordID = args[2];
			String remoteCenterServerName = args[3];
			reply = api.transferRecord(managerID, recordID, remoteCenterServerName);
		}
		return reply;
	}
	
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
	

}
