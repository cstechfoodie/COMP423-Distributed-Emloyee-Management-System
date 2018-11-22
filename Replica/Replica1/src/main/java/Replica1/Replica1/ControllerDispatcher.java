package Replica1.Replica1;

import dems.RecordApp.CARecordController;
import dems.RecordApp.RecordApi;
import dems.RecordApp.UKRecordController;
import dems.RecordApp.USRecordController;
import dems.model.CAUDPServer;
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

	public ControllerDispatcher recoverReplica() {
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
		this.uk = recordControllerUS;
		udpUK.setRepo(repoUS);
		System.out.println("USServer restarts ...");
		return this;
	}

}
