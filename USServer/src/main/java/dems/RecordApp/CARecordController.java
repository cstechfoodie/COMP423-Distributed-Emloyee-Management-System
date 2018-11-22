package dems.RecordApp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import dems.api.EmployeeRecord;
import dems.api.ManagerRecord;
import dems.api.Record;
import dems.model.Logger;
import dems.model.UDPClient;
import dems.repository.IRecordRepository;

public class CARecordController implements RecordApi {

	private IRecordRepository repo;

	private Logger logger;
	
	private HashMap<String, Integer> serverPortRegistry;

	public CARecordController(IRecordRepository repo){
		super();
		this.repo = repo;
		logger = new Logger();
		serverPortRegistry = new HashMap<>();
		serverPortRegistry.put("CA", 9001);
		serverPortRegistry.put("UK", 9002);
		serverPortRegistry.put("US", 9003);
	}

//	public String createMRecord(String managerID, String firstName, String lastName, Integer employeeID, String mailID, RecordApp.RecordPackage.Project project,
//			String location) {
//		logger.setUserID(managerID);
//		ManagerRecord m = new ManagerRecord(firstName, lastName, employeeID, mailID, project, location);
//		boolean isSuccessful = repo.createMRecord(m);
//		if (isSuccessful) {
//			logger.logSuccessfullyCreated(m);
//			return "ManagerRecord has been successfully created!";
//		} else {
//			logger.logUnsuccessfullyCreated(m);
//			return "Failed to Create ManagerRecord!";
//		}
//	}

	@Override
	public String createERecord(String managerID, String firstName, String lastName, int employeeID, String mailID,
			String projectID) {
		logger.setUserID(managerID);
		EmployeeRecord e = new EmployeeRecord(firstName, lastName, employeeID, mailID, projectID);
		boolean isSuccessful = repo.createMRecord(e);
		if (isSuccessful) {
			logger.logSuccessfullyCreated(e);
			return "EmployeeRecord has been successfully created!";
		} else {
			logger.logUnsuccessfullyCreated(e);
			return "Failed to Create EmployeeRecord!";
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
		String result = "";
		String countsUS = UDPClient.getRecordCounts("localhost", 9003);
		String countsUK = UDPClient.getRecordCounts("localhost", 9002);
		result = "CA " + localServerCount + " " + countsUS + " " + countsUK;			

		//return result;
		logger.logInfo("Check the counts of record in each server. The total number is: " + result);
		return "Check the counts of record in each server. The total number is: " + result;
	}

	@Override
	public String editRecord(String managerID, String recordID, String fieldName, String newValue) {
		logger.setUserID(managerID);
		boolean isSuccessful = this.repo.editRecord(recordID, fieldName, newValue);
		if (isSuccessful) {
			logger.logEdit(recordID, fieldName, newValue);
			return "Edited Successfully!";
		} else {
			return "Failed to Edit!";
		}
	}

	@Override
	public String printData() {
		Map<String, List<Record>> map = this.repo.getDataMap();
		if(map.size() > 0) {
			map.forEach((k, v) -> {
				System.out.println("==============================Key: " + k + "==================================");
				for (int i = 0; i < v.size(); i++) {
					System.out.println(i + ": " + v.get(i).toString());
				}
				System.out.println("-=-=> Total records associate with this key is " + v.size());
			});
		}
		return "Print to Console Successfully!";
	}

	@Override
	public String transferRecord(String managerID, String recordID, String remoteCenterServerName){
		logger.setUserID(managerID);
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
							return "The following record is sucessfully transfer to " + remoteCenterServerName + " server. " + r.toString();
						} else {
							//rollback
							String rollback = UDPClient.rollbackTransfer("localhost", serverPortRegistry.get(remoteCenterServerName), recordID);
							if(rollback.equals("true")) {
								logger.logInfo("Transfer failed due to internal local server error. Rollback remote successfully");
								return "Transfer failed due to internal local server error. Rollback remote successfully";								
							} else {
								logger.logInfo("Transfer failed due to internal local server error. Rollback remote failed");
								return "Transfer failed due to internal local server error. Rollback remote failed";
							}
						}
					} else {
						logger.logInfo("Transfer failed due to " + remoteCenterServerName + " server error.");
						return "Transfer failed due to " + remoteCenterServerName + " server error.";
					}
				} catch (Exception e) {
					logger.logInfo("Transfer failed due to Exception");
					return "Transfer failed due to Exception";
				}
			} else {
				logger.logInfo("Transfer failed due to conflicted recordID in "+ remoteCenterServerName + " server.");
				return "Transfer failed due to conflicted recordID in "+ remoteCenterServerName + " server.";
			}
		} else {
			logger.logInfo("Transfer failed due to recordID not existing in local server.");
			return "Transfer failed due to recordID not existing in local server.";
		}
	}

	@Override
	public String createMRecord(String managerID, String firstName, String lastName, int employeeID, String mailID,
			RecordApp.RecordPackage.Project project, String location) {
		logger.setUserID(managerID);
		ManagerRecord m = new ManagerRecord(firstName, lastName, employeeID, mailID, project, location);
		boolean isSuccessful = repo.createMRecord(m);
		if (isSuccessful) {
			logger.logSuccessfullyCreated(m);
			return "ManagerRecord has been successfully created!";
		} else {
			logger.logUnsuccessfullyCreated(m);
			return "Failed to Create ManagerRecord!";
		}
	}
}
