package server;

import java.net.SocketException;

public class SequencerTest {

	public static void main(String[] args) {

		Sequencer seq;
		try {
			seq = new Sequencer(1050,1099,1098,1097);
			seq.start();
			System.out.println("sequencer started on: 1050");
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
