package Replica1.Replica1;

import java.util.List;
import java.util.Map;

import dems.RecordApp.CARecordController;
import dems.RecordApp.RecordApi;
import dems.RecordApp.UKRecordController;
import dems.RecordApp.USRecordController;
import dems.model.CAUDPServer;
import dems.model.Record;
import dems.model.UKUDPServer;
import dems.model.USUDPServer;
import dems.repository.IRecordRepository;
import dems.repository.RecordRepository;

public class ControllerDispatcher {

	private RecordApi ca;
	private CAUDPServer udpCA;

	private RecordApi uk;
	private UKUDPServer udpUK;

	private RecordApi us;
	private USUDPServer udpUS;

	/**
	 * register three implementations of servers and udp servers
	 * @param ca ca implementation
	 * @param uk uk implementation
	 * @param us us implementation
	 * @param udpCA ca daemon udp process
	 * @param udpUK uk daemon udp process
	 * @param udpUS us daemon udp process
	 */
	public ControllerDispatcher(RecordApi ca, RecordApi uk, RecordApi us, CAUDPServer udpCA,
			UKUDPServer udpUK, USUDPServer udpUS) {
		this.ca = ca;
		this.udpCA = udpCA;

		this.uk = uk;
		this.udpUK = udpUK;

		this.us = us;
		this.udpUS = udpUS;
	}

	public RecordApi findServer(String managerID) {
		if (managerID.contains("CA")) {
			return ca;
		}
		if (managerID.contains("UK")) {
			return uk;
		}
		if (managerID.contains("US")) {
			return us;
		}
		return null;
	}

	/**
	 * initialte replica meaning the map is the newly created
	 * @return ControllerDispatcher
	 */
	public ControllerDispatcher initiateReplica() {
		//takes a map
		IRecordRepository repoCA = new RecordRepository();
		RecordApi recordControllerCA = new CARecordController(repoCA);
		this.ca = recordControllerCA;
		udpCA.setRepo(repoCA);
		System.out.println("CAServer restarts ...");

		IRecordRepository repoUK = new RecordRepository();
		RecordApi recordControllerUK = new UKRecordController(repoUK);
		this.uk = recordControllerUK;
		udpUK.setRepo(repoUK);
		System.out.println("UKServer restarts ...");

		IRecordRepository repoUS = new RecordRepository();
		RecordApi recordControllerUS = new USRecordController(repoUS);
		this.us = recordControllerUS;
		udpUS.setRepo(repoUS);
		System.out.println("USServer restarts ...");
		return this;
	}
	
	/**
	 * restart replica with map information
	 * @return ControllerDispatcher
	 */
	public ControllerDispatcher recoverReplica(Map<String, List<Record>> mapCA, Map<String, List<Record>> mapUK, Map<String, List<Record>> mapUS) {
		//takes a map
		IRecordRepository repoCA = new RecordRepository(mapCA);
		RecordApi recordControllerCA = new CARecordController(repoCA);
		this.ca = recordControllerCA;
		udpCA.setRepo(repoCA);
		System.out.println("CAServer restarts ...");

		IRecordRepository repoUK = new RecordRepository(mapUK);
		RecordApi recordControllerUK = new UKRecordController(repoUK);
		this.uk = recordControllerUK;
		udpUK.setRepo(repoUK);
		System.out.println("UKServer restarts ...");

		IRecordRepository repoUS = new RecordRepository(mapUS);
		RecordApi recordControllerUS = new USRecordController(repoUS);
		this.us = recordControllerUS;
		udpUS.setRepo(repoUS);
		System.out.println("USServer restarts ...");
		return this;
	}
	
	/**
	 * restart replica with record repository object which includes the map of records
	 * @see the other method
	 * @return ControllerDispatcher
	 */
	public ControllerDispatcher recoverReplica(IRecordRepository repoCA, IRecordRepository repoUK, IRecordRepository repoUS) {
		RecordApi recordControllerCA = new CARecordController(repoCA);
		this.ca = recordControllerCA;
		udpCA.setRepo(repoCA);
		System.out.println("CAServer restarts with restored map...");

		RecordApi recordControllerUK = new UKRecordController(repoUK);
		this.uk = recordControllerUK;
		udpUK.setRepo(repoUK);
		System.out.println("UKServer restarts with restored map...");

		RecordApi recordControllerUS = new USRecordController(repoUS);
		this.us = recordControllerUS;
		udpUS.setRepo(repoUS);
		System.out.println("USServer restarts with restored map...");
		return this;
	}

}
