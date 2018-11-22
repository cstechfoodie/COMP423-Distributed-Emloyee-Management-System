package Replica1.Replica1;

import Replica1.Replica1.Interface.IRecordRepository;
import Replica1.Replica1.Interface.RecordApi;

public class ControllerDispatcher {
	
	private RecordApi ca;
	
	private RecordApi uk;
	
	private RecordApi us;
	
	public ControllerDispatcher(RecordApi ca, RecordApi uk, RecordApi us) {
		this.ca = ca;
		
		this.uk = uk;
		
		this.us = us;
	}
	
	
	public RecordApi findServer(String managerId) {
		if(managerId.contains("CA")) {
			return ca;
		}
		if(managerId.contains("UK")) {
			return uk;
		}
		if(managerId.contains("US")) {
			return us;
		}
		return null;
	}
	
	public ControllerDispatcher recoverReplica() {
		  IRecordRepository repoCA = (IRecordRepository) new ca.dems.repository.RecordRepository();
	      RecordApi recordControllerCA = (RecordApi) new ca.dems.RecordApp.RecordController((ca.dems.repository.IRecordRepository) repoCA);
	      this.ca = recordControllerCA;
	      System.out.println("CAServer restarts ...");
      	      
	      IRecordRepository repoUK = (IRecordRepository) new uk.dems.repository.RecordRepository();
	      RecordApi recordControllerUK = (RecordApi) new uk.dems.RecordApp.RecordController((uk.dems.repository.IRecordRepository) repoUK);
	      this.uk = recordControllerUK;
	      System.out.println("UKServer restarts ...");	      
	      
	      IRecordRepository repoUS = (IRecordRepository) new us.dems.repository.RecordRepository();
	      RecordApi recordControllerUS = (RecordApi) new us.dems.RecordApp.RecordController((us.dems.repository.IRecordRepository) repoUS);
	      this.us = recordControllerUS;
	      System.out.println("USServer restarts ...");	      
	      return this;
	}

}
