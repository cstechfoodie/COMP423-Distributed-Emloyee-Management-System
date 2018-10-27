package dems.client.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import RecordApp.Client;


public class ClientTest {
	
	private Client[] clients;
	

	public void setup() {
		clients = new Client[20];
		for(int i = 0; i < 20; i++) {
			clients[i] = new Client("UK"+i);				
		}
	}


	public void cleanUp() throws InterruptedException {
		for(int i = 0; i < 20; i++) {
			clients[i].join();
		}
	}


	@Test
	public void ConcurrancyTest() throws InterruptedException {
		setup();
		for(int i = 0; i < 20; i++) {
			clients[i].start();
		}
		cleanUp();
	}

}
