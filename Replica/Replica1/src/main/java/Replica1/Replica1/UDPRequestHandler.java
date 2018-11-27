package Replica1.Replica1;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dems.RecordApp.CARecordController;
import dems.RecordApp.RecordApi;
import dems.RecordApp.UKRecordController;
import dems.RecordApp.USRecordController;
import dems.model.EmployeeRecord;
import dems.model.ManagerRecord;
import dems.model.Project;
import dems.model.Record;
import dems.repository.IRecordRepository;
import dems.repository.RecordRepository;
import udp_bridge.Message;
import udp_bridge.Process;
import udp_bridge.Reliable;
import udp_bridge.UDP;
import udp_bridge.Unicast;

public class UDPRequestHandler {
	
	private static String REPLICA_NAME = "Replica1";
	
	private ControllerDispatcher cd;
	
	private UDP udp;
	
	private UDP replyToReplica;
	
	private boolean needsRecover = false;
	
	private boolean rebindFrontEnd = false;

	public UDPRequestHandler(ControllerDispatcher cd) {
		this.cd = cd;
		try {
			udp = new Reliable(new Unicast(7021, "localhost", 10001)); // set it lenyen to 7021, and bind the with front end
			replyToReplica = new Reliable(new Unicast(8021, "localhost", 7011)); //used to reply map to its replica
			System.out.print("The replica is listening to resquest on port 7021.");
			System.out.println(" The replica will respond to FE on 10001 and RM1 on 7011");
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Used when there is a local feport or the front end will be changed later upon first frond end call
	 * @param cd ControllerDispatcher
	 * @param localport udp local port listening udp request from front end
	 * @param feport FE port
	 */
	public UDPRequestHandler(ControllerDispatcher cd, int localport, int feport) {
		this.cd = cd;
		try {
			udp = new Reliable(new Unicast(localport, "localhost", feport));
			replyToReplica = new Reliable(new Unicast(8021, "localhost", 7011)); //used to reply map to its replica
			System.out.print("The replica is listening to resquest on " + localport);
			System.out.println(" The replica will respond to FE on " + feport + " and RM1 on 7011");
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Used when initialized with appropriate 
	 * @param cd
	 * @param localport
	 * @param address
	 * @param feport
	 */
	
	public UDPRequestHandler(ControllerDispatcher cd, int localport, String address, int feport) {
		this.cd = cd;
		try {
			udp = new Reliable(new Unicast(localport, address, feport));
			rebindFrontEnd = true;
			replyToReplica = new Reliable(new Unicast(8021, "localhost", 7011)); //used to reply map to its replica
			System.out.print("The replica is listening to resquest on " + localport);
			System.out.println(" The replica will respond to FE on " + feport + " and RM1 on 7011");
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String listen() throws IOException {
		byte[] reception = udp.receive();
		String msg = new String(reception);
//		String msg = null;
//		Message data = null;
//		try {
//			data = Message.fromBytes(reception);
//			msg = new String(data.message);
//		} catch (Exception e){
//			if(msg == null) {
//				msg = new String(reception);
//			} else {
//				Process p = data.process;
//				int port = p.port;
//				InetAddress addr = p.address;
//				if(!rebindFrontEnd) {
//					udp.changeRemote(new Process(addr, port));
//					rebindFrontEnd = true;
//				}
//			}
//		}
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
		
		if(msg.contains("recover")) {
			needsRecover = true;
			msg = listen();
			while(!msg.contains("!")) {
				msg = listen();
			}
			restartWithMap(msg);
			needsRecover = false;
		}
		else if(msg.contains("requestmap")) {
			replyToReplica.send(retrieveMap());
		} else {
			String[] args = msg.split(";");
			for(int i = 0; i < args.length; i++) {
				args[i] = args[i].trim();
			}
			
			String managerID = args[1]; // get managerID first from UDP request message
			if(needsRecover) {
				reply = "Server recovery being processed";
			} else {
				if(managerID.contains("CA")) {
					api = cd.findServer("CA");
					reply = processRequest(managerID, args, api);
				}
				if(managerID.contains("UK")) {
					api = cd.findServer("UK");
					reply = processRequest(managerID, args, api);
				}
				if(managerID.contains("US")) {
					api = cd.findServer("US");
					reply = processRequest(managerID, args, api);
				}
			}
			processReply(managerID, reply);
		}
		
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
	 * format replay message and send it through reliable UDP
	 * @param managerID
	 * @param reply
	 */
	private void processReply(String managerID, String reply) {
		String freply = managerID + ";" + reply + ";" + REPLICA_NAME;
		try {
			udp.send(freply);
		} catch (IOException e) {
			System.out.println("error happens during reply");
			e.printStackTrace();
		}
	}
	
	
	private void restartWithMap(String map) {
		IRecordRepository repoCA = new RecordRepository();
		IRecordRepository repoUK = new RecordRepository();
		IRecordRepository repoUS = new RecordRepository();
		String[] maps = map.split("!");
		String[] records = null;
		String[] fields = null;
		if(maps.length == 0) {
			cd.initiateReplica();
		} else {
			for(int i = 0; i < maps.length; i++) {
				if(i == 0) {
					if(!maps[i].equals(" ")) {
						if(maps[i].contains(",")) {
							records = maps[i].split(",");
							for(int j = 0; j < records.length; j++) {
								fields = records[j].split(";");
								if(fields.length == 9) {
									Project p = new Project();
									p.setProjectID(fields[5]);
									p.setClientName(fields[6]);
									p.setProjectName(fields[7]);
									Record mr = new ManagerRecord(fields[1],fields[2],Integer.parseInt(fields[3]), fields[4], p ,fields[8]);
									mr.setRecordID(fields[0]);
									repoCA.createMRecord(mr);
								} else {
									Record er = new EmployeeRecord(fields[1],fields[2],Integer.parseInt(fields[3]), fields[4],fields[5]);
									er.setRecordID(fields[0]);
									repoCA.createERecord(er);
								}
							}
						}
					}
				} else if(i == 1) {
					if(!maps[i].equals(" ")) {
						if(maps[i].contains(",")) {
							records = maps[i].split(",");
							for(int j = 0; j < records.length; j++) {
								fields = records[j].split(";");
								if(fields.length == 9) {
									Project p = new Project();
									p.setProjectID(fields[5]);
									p.setClientName(fields[6]);
									p.setProjectName(fields[7]);
									Record mr = new ManagerRecord(fields[1],fields[2],Integer.parseInt(fields[3]), fields[4], p ,fields[8]);
									mr.setRecordID(fields[0]);
									repoUK.createMRecord(mr);
								} else {
									Record er = new EmployeeRecord(fields[1],fields[2],Integer.parseInt(fields[3]), fields[4],fields[5]);
									er.setRecordID(fields[0]);
									repoUK.createERecord(er);
								}
							}
						}
					}
				} else {
					if(!maps[i].equals(" ")) {
						if(maps[i].contains(",")) {
							records = maps[i].split(",");
							for(int j = 0; j < records.length; j++) {
								fields = records[j].split(";");
								if(fields.length == 9) {
									Project p = new Project();
									p.setProjectID(fields[5]);
									p.setClientName(fields[6]);
									p.setProjectName(fields[7]);
									Record mr = new ManagerRecord(fields[1],fields[2],Integer.parseInt(fields[3]), fields[4], p ,fields[8]);
									mr.setRecordID(fields[0]);
									repoUS.createMRecord(mr);
								} else {
									Record er = new EmployeeRecord(fields[1],fields[2],Integer.parseInt(fields[3]), fields[4],fields[5]);
									er.setRecordID(fields[0]);
									repoUS.createERecord(er);
								}
							}
						}
					}
				}
			}
			cd.recoverReplica(repoCA, repoUK, repoUS);
		}
	}
	
	private String retrieveMap() {
		CARecordController ca = (CARecordController) cd.findServer("CA");
		Map<String, List<Record>> mapCA = ca.getRepo().getDataMap();
		UKRecordController uk = (UKRecordController) cd.findServer("UK");
		Map<String, List<Record>> mapUK = uk.getRepo().getDataMap();
		USRecordController us = (USRecordController) cd.findServer("US");
		Map<String, List<Record>> mapUS = us.getRepo().getDataMap();
		String record = "";
		StringBuilder caBuilder = new StringBuilder();
		StringBuilder ukBuilder = new StringBuilder();
		StringBuilder usBuilder = new StringBuilder();
		
		Collection<List<Record>> keys = mapCA.values();
		Iterator<List<Record>> it = keys.iterator();
		while(it.hasNext()) {
			List<Record> lst = it.next();
			Iterator<Record> records = lst.iterator();
			while(records.hasNext()) {
				Record r = records.next();
				if(r instanceof ManagerRecord) {
					ManagerRecord mr = (ManagerRecord) r;
					record = "" + mr.getRecordID() + ";" + mr.getFirstName() + ";" +  mr.getLastName() + ";" +  mr.getEmployeeID() + ";" +  mr.getMailID() + ";" +  mr.getProject().getProjectID() + ";" +  mr.getProject().getClientName() + ";" +  mr.getProject().getProjectName()+ ";" + mr.getLocation();
				} else {
					EmployeeRecord er = (EmployeeRecord) r;
					record = "" + er.getRecordID() + ";" + er.getFirstName() + ";" + er.getLastName() + ";" +  er.getEmployeeID() + ";" +  er.getMailID() + ";" +  er.getProjectID();
				}
				caBuilder.append(record + ",");
			}
		}
		
		keys = mapUK.values();
		it = keys.iterator();
		while(it.hasNext()) {
			List<Record> lst = it.next();
			Iterator<Record> records = lst.iterator();
			while(records.hasNext()) {
				Record r = records.next();
				if(r instanceof ManagerRecord) {
					ManagerRecord mr = (ManagerRecord) r;
					record = "" + mr.getRecordID() + ";" + mr.getFirstName() + ";" +  mr.getLastName() + ";" +  mr.getEmployeeID() + ";" +  mr.getMailID() + ";" +  mr.getProject().getProjectID() + ";" +  mr.getProject().getClientName() + ";" +  mr.getProject().getProjectName()+ ";" + mr.getLocation();
				} else {
					EmployeeRecord er = (EmployeeRecord) r;
					record = "" + er.getRecordID() + ";" + er.getFirstName() + ";" + er.getLastName() + ";" +  er.getEmployeeID() + ";" +  er.getMailID() + ";" +  er.getProjectID();
				}
				ukBuilder.append(record + ",");
			}
		}
		
		keys = mapUS.values();
		it = keys.iterator();
		while(it.hasNext()) {
			List<Record> lst = it.next();
			Iterator<Record> records = lst.iterator();
			while(records.hasNext()) {
				Record r = records.next();
				if(r instanceof ManagerRecord) {
					ManagerRecord mr = (ManagerRecord) r;
					record = "" + mr.getRecordID() + ";" + mr.getFirstName() + ";" +  mr.getLastName() + ";" +  mr.getEmployeeID() + ";" +  mr.getMailID() + ";" +  mr.getProject().getProjectID() + ";" +  mr.getProject().getClientName() + ";" +  mr.getProject().getProjectName()+ ";" + mr.getLocation();
				} else {
					EmployeeRecord er = (EmployeeRecord) r;
					record = "" + er.getRecordID() + ";" + er.getFirstName() + ";" + er.getLastName() + ";" +  er.getEmployeeID() + ";" +  er.getMailID() + ";" +  er.getProjectID();
				}
				usBuilder.append(record + ",");
			}
		}
		
		String castr = " ";
		if(caBuilder.length() > 0) {
			castr = caBuilder.toString();
		}
		
		String ukstr = " ";
		if(caBuilder.length() > 0) {
			ukstr = ukBuilder.toString();
		}
		
		String usstr = " ";
		if(caBuilder.length() > 0) {
			usstr = usBuilder.toString();
		}

		return castr + "!" + ukstr + "!" + usstr;
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
