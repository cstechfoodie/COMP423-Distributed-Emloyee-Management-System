package dems.RecordApp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dems.model.EmployeeRecord;
import dems.model.Logger;
import dems.model.ManagerRecord;
import dems.model.Project;
import dems.model.Record;
import dems.model.UDPClient;
import dems.repository.IRecordRepository;

public class USRecordController implements RecordApi {

	private IRecordRepository repo;

	private Logger logger;
	
	private HashMap<String, Integer> serverPortRegistry;

	public USRecordController(IRecordRepository repo) {
		super();
		this.repo = repo;
		logger = new Logger();
		serverPortRegistry = new HashMap<>();
		serverPortRegistry.put("CA", 9001);
		serverPortRegistry.put("UK", 9002);
		serverPortRegistry.put("US", 9003);
	}

	@Override
	public String createERecord(String managerID, String firstName, String lastName, int employeeID, String mailID,
			String projectID) {
		logger.setUserID(managerID);
		EmployeeRecord e = new EmployeeRecord(firstName, lastName, employeeID, mailID, projectID);
		boolean isSuccessful = repo.createMRecord(e);
		if (isSuccessful) {
			logger.logSuccessfullyCreated(e);
			printData();
			return "Success";
		} else {
			logger.logUnsuccessfullyCreated(e);
			printData();
			return "Fail";
		}
	}

	@Override
	public String getRecordCounts(String managerID) {
		logger.setUserID(managerID);
		int localServerCount = this.repo.getRecordCounts();
		if (localServerCount >= 0) {
			logger.logInfo("Check the count of the local server. The total number is: " + localServerCount);
		}

		// UDP
		String countsUK = UDPClient.getRecordCounts("localhost", 9002);
		String countsCA = UDPClient.getRecordCounts("localhost", 9001);
		
		String result = "US " + localServerCount + " " + countsUK + " " + countsCA;

		//return result;
		System.out.println("Check the counts of record in each server. The total number is: " + result);
		printData();
		logger.logInfo("Check the counts of record in each server. The total number is: " + result);
		return result.trim();
	}

	@Override
	public String editRecord(String managerID, String recordID, String fieldName, String newValue) {
		logger.setUserID(managerID);
		boolean isSuccessful = this.repo.editRecord(recordID, fieldName, newValue);
		if (isSuccessful) {
			logger.logEdit(recordID, fieldName, newValue);
			printData();
			return "Success";
		} else {
			printData();
			return "Fail";
		}
	}

	private void printData() {
		Map<String, List<Record>> map = this.repo.getDataMap();
		System.out.println("==============================New Log from US==================================");
		if(map.size() > 0) {
			map.forEach((k, v) -> {
				System.out.println("==============================Key: " + k + "==================================");
				for (int i = 0; i < v.size(); i++) {
					System.out.println(i + ": " + v.get(i).toString());
				}
				System.out.println("-=-=> Total records associate with this key is " + v.size());
			});
		}
	}

	@Override
	public String transferRecord(String managerID, String recordID, String remoteCenterServerName){
		logger.setUserID(managerID);
		if(!remoteCenterServerName.equals("US")) {
			boolean isExisted;
			if((isExisted = repo.isExisted(recordID))) {
				String isExistedInRemote = UDPClient.checkRecordInRemoteServer("localhost", serverPortRegistry.get(remoteCenterServerName), recordID);
				if(isExistedInRemote.equals("false")) {
					try {
						Record r = this.repo.getRecord(recordID);
						String isTransferSuccessfully = UDPClient.transeferRecord("localhost", serverPortRegistry.get(remoteCenterServerName), r);
						if(isTransferSuccessfully.equals("true")) {
							boolean isDeleted = this.repo.deleteRecord(recordID);
							if(isDeleted) {
								logger.logInfo("The following record is sucessfully transfer to " + remoteCenterServerName + " server. " + r.toString());
								printData();
								return "Success";
							} else {
								//rollback
								String rollback = UDPClient.rollbackTransfer("localhost", serverPortRegistry.get(remoteCenterServerName), recordID);
								if(rollback.equals("true")) {
									logger.logInfo("Transfer failed due to internal local server error. Rollback remote successfully");
									return "Fail";							
								} else {
									logger.logInfo("Transfer failed due to internal local server error. Rollback remote failed");
									return "Fail";
								}
							}
						} else {
							logger.logInfo("Transfer failed due to " + remoteCenterServerName + " server error.");
							return "Fail";
						}
					} catch (Exception e) {
						logger.logInfo("Transfer failed due to Exception");
						return "Fail";
					}
				} else {
					logger.logInfo("Transfer failed due to conflicted recordID in "+ remoteCenterServerName + " server.");
					return "Fail";
				}
			} else {
				logger.logInfo("Transfer failed due to recordID not existing in local server.");
				return "Fail";
			}
		} else {
			logger.logInfo("Transfer failed due to transfet to itself.");
			return "Fail";
		}
	}

	@Override
	public String createMRecord(String managerID, String firstName, String lastName, int employeeID, String mailID,
			Project project, String location) {
		logger.setUserID(managerID);
		ManagerRecord m = new ManagerRecord(firstName, lastName, employeeID, mailID, project, location);
		boolean isSuccessful = repo.createMRecord(m);
		if (isSuccessful) {
			logger.logSuccessfullyCreated(m);
			printData();
			return "Success";
		} else {
			logger.logUnsuccessfullyCreated(m);
			printData();
			return "Fail";
		}
	}

}
