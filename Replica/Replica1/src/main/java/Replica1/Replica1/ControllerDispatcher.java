package Replica1.Replica1;

import Replica1.Replica1.Interface.IRecordRepository;
import Replica1.Replica1.Interface.RecordApi;

public class ControllerDispatcher {

	private RecordApi ca;
	private ca.dems.model.UDPServer udpCA;

	private RecordApi uk;
	private uk.dems.model.UDPServer udpUK;

	private RecordApi us;
	private us.dems.model.UDPServer udpUS;

	public ControllerDispatcher(RecordApi ca, RecordApi uk, RecordApi us, ca.dems.model.UDPServer udpCA,
			uk.dems.model.UDPServer udpUK, us.dems.model.UDPServer udpUS) {
		this.ca = ca;
		this.udpCA = udpCA;

		this.uk = uk;
		this.udpUK = udpUK;

		this.us = us;
		this.udpUS = udpUS;
	}

	public RecordApi findServer(String managerId) {
		if (managerId.contains("CA")) {
			return ca;
		}
		if (managerId.contains("UK")) {
			return uk;
		}
		if (managerId.contains("US")) {
			return us;
		}
		return null;
	}

	public ControllerDispatcher recoverReplica() {
		//takes a map
		IRecordRepository repoCA = (IRecordRepository) new ca.dems.repository.RecordRepository();
		RecordApi recordControllerCA = (RecordApi) new ca.dems.RecordApp.RecordController(
				(ca.dems.repository.IRecordRepository) repoCA);
		this.ca = recordControllerCA;
		udpCA.setRepo((ca.dems.repository.IRecordRepository) repoCA);
		System.out.println("CAServer restarts ...");

		IRecordRepository repoUK = (IRecordRepository) new uk.dems.repository.RecordRepository();
		RecordApi recordControllerUK = (RecordApi) new uk.dems.RecordApp.RecordController(
				(uk.dems.repository.IRecordRepository) repoUK);
		this.uk = recordControllerUK;
		udpUK.setRepo((uk.dems.repository.IRecordRepository) repoUK);
		System.out.println("UKServer restarts ...");

		IRecordRepository repoUS = (IRecordRepository) new us.dems.repository.RecordRepository();
		RecordApi recordControllerUS = (RecordApi) new us.dems.RecordApp.RecordController(
				(us.dems.repository.IRecordRepository) repoUS);
		this.us = recordControllerUS;
		udpUS.setRepo((us.dems.repository.IRecordRepository) repoUS);
		System.out.println("USServer restarts ...");
		return this;
	}

}
