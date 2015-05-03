package mebiuw.rbb.exp;

import mebiuw.rbb.fundation.protocol.chord.ChordProtocol;

public class DataNodeLoader {

	public static void main(String[] args) throws Exception {
		Thread tw=new Thread(new ThreadWorker(args[0]),"MainWorker-Thread");
		tw.start();
		
		
	}
    
}
class ThreadWorker extends Thread{
	String position;

	public ThreadWorker(String position) {
		super();
		this.position = position;
	}

	@Override
	public void run() {
		try {
			ChordProtocol chord=new ChordProtocol(position);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.run();
	}
	
}

