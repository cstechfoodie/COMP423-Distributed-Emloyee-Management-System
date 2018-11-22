package dems.replicamanager;

public class RMLauncher {

	public static void main(String[] args) {
		ReplicaManager rm = new ReplicaManager();
		
		boolean msgSent = false;
		while(true) {
			msgSent = rm.trySendRecoverRequest();
			if(msgSent) {
				System.out.println("'recover' msg sent to replica1");
			} else {
				System.out.println("Not trigger recover msg");
			}
		}
	}

}
