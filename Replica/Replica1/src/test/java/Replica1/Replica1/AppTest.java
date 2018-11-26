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
	
//	@BeforeClass
//	public static void setup() {
//		 try {
//			u = new Reliable(new Unicast(7021));
//		} catch (SocketException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	
	
	
	@Test
	public void listenAndHandleRequestTest() throws SocketException, UnknownHostException {
		u = new Reliable(new Unicast(7021));
		String method1 = "1;CA1001;Shunyu;Wang;44944;a@google.com;P234;Joshua;LearnJava;CA;";
		String method2 = "2;CA1001;Jack;Smith;88787;jack@google.com;P234;";
		String method3 = "3;CA1001;";
		String method4 = "4;CA1001;ER30000;mailID;new@concordia.ca";
		String method5a = "5;CA1001;ER30000;UK";
		String method5b = "5;CA1001;MR30000;UK";
		String method3a = "3;UK1001;";
		String method3b = "3;US1001;";
		
		String method1a = "1;CA1001;aaa;ddd;12345;aaa@google.com;Pjkl;Joshua;LearnC++;US;";
		String method2a = "2;CA1001;ccc;zzz;67890;ccc@google.com;Pjkl;";
		String method5c = "5;CA1001;ER30001;US";
		String method5d = "5;CA1001;MR30001;US";
		
		String method5e = "5;UK1001;ER30000;CA";
		String method5f = "5;US1001;MR30001;CA";
		
		
		try {
			u.send(method1);
			Thread.sleep(500);
			u.send(method2);
			Thread.sleep(500);
			u.send(method3);
			Thread.sleep(500); // CA 2 US 0 UK 0
			u.send(method4);
			Thread.sleep(500);
			u.send(method5a);
			Thread.sleep(1000);
			u.send(method5b);
			Thread.sleep(1000);
			u.send(method3);
			Thread.sleep(500);
			u.send(method3a);
			Thread.sleep(500);// UK 2 US 0 CA 1
			
			u.send(method1a);
			Thread.sleep(500);
			u.send(method2a);
			Thread.sleep(500);
			u.send(method3);
			Thread.sleep(500); //CA 2 US 0 UK 2
			u.send(method5c);
			Thread.sleep(1000);
			u.send(method5d);
			Thread.sleep(1000);
			u.send(method3b);
			Thread.sleep(500);//US 2 UK 2 CA 0
			
			u.send(method5e);
			Thread.sleep(1000);
			u.send(method5f);
			Thread.sleep(1000);
			u.send(method3);
			Thread.sleep(500);//CA 2 US 1 UK 1
			
		} catch (IOException e) {
			System.out.println("Exception occurred during test");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void replicaRecoveryTest() throws SocketException, UnknownHostException {
		u = new Reliable(new Unicast(7021));
		String method1 = "1;CA1001;Shunyu;Wang;44944;a@google.com;P234;Joshua;LearnJava;CA;";
		String method2 = "2;CA1001;Jack;Smith;88787;jack@google.com;P234;";
		String method3 = "3;CA1001;";
		String method4 = "4;CA1001;ER30000;mailID;new@concordia.ca";
		String method5a = "5;CA1001;ER30000;UK";
		String method5b = "5;CA1001;MR30000;UK";
		String method3a = "3;UK1001;";
		String method3b = "3;US1001;";
		
		String method1a = "1;CA1001;aaa;ddd;12345;aaa@google.com;Pjkl;Joshua;LearnC++;US;";
		String method2a = "2;CA1001;ccc;zzz;67890;ccc@google.com;Pjkl;";
		String method5c = "5;CA1001;ER30001;US";
		String method5d = "5;CA1001;MR30001;US";
		
		String method5e = "5;UK1001;ER30000;CA";
		String method5f = "5;US1001;MR30001;CA";
		
		
		try {
			u.send(method3);
			Thread.sleep(500); // CA 2 US 0 UK 0

			u.send(method3a);
			Thread.sleep(500);// UK 2 US 0 CA 1
			
			u.send(method3b);
			Thread.sleep(500);//US 2 UK 2 CA 0

			
		} catch (IOException e) {
			System.out.println("Exception occurred during test");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
