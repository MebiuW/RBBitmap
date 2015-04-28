package mebiuw.rbb.exp;

import mebiuw.rbb.fundation.protocol.chord.ChordProtocol;

public class DataNodeLoader {

	public static void main(String[] args) throws Exception {
		Thread tw=new Thread(new ThreadWorker(),"MainWorker-Thread");
		tw.start();
		while(1==1){
			
		}
		
	}
    
}
class ThreadWorker extends Thread{

	@Override
	public void run() {
		try {
			ChordProtocol chord=new ChordProtocol("G:\\Datas\\RBB\\config.txt");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.run();
	}
	
}

