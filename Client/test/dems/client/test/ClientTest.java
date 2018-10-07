package dems.client.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dems.client.Client;


public class ClientTest {
	
	private Client[] clients;
	
	@Before
	public void setup() {
		clients = new Client[10];
		for(int i = 0; i < 10; i++) {
			clients[i] = new Client("CA"+i);
		}
	}

	@After
	public void cleanUp() throws InterruptedException {
		for(int i = 0; i < 10; i++) {
			clients[i].join();
		}
	}


	@Test
	public void ConcurrancyTest() {
		for(int i = 0; i < 10; i++) {
			clients[i].start();
		}
	}

}
