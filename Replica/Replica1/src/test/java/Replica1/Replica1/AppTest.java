package Replica1.Replica1;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import udp_bridge.Reliable;
import udp_bridge.UDP;
import udp_bridge.Unicast;

/**
 * Unit test for simple App.
 */
public class AppTest {
	
	private static UDP u;
	
	@BeforeClass
	public static void setup() {
		 try {
			u = new Reliable(new Unicast(8888, 7021));
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	
	@Test
	public void listenAndHandleRequestTest() {
		String method1 = "1;CA1001;Shunyu;Wang;44944;a@google.com;P234;Joshua;LearnJava;CA;";
		String method2 = "2;CA1001;Jack;Smith;88787;jack@google.com;P234;";
		String method3 = "3;CA1001;";
		String method4 = "4;CA1001;ER30000;mailID;new@concordia.ca";
		String method5 = "5;CA1001;ER30000;US";
		
		try {
			u.send(method1);
			Thread.sleep(1000);
			u.send(method2);
			Thread.sleep(1000);
			u.send(method3);
			Thread.sleep(1000);
			u.send(method4);
			Thread.sleep(1000);
			u.send(method5);
			Thread.sleep(1000);
		} catch (IOException e) {
			System.out.println("Exception occurred during test");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
