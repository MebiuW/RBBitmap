package mebiuw.rbb.exp;

import mebiuw.rbb.fundation.protocol.chord.ChordProtocol;

public class DataNodeLoader {

	public static void main(String[] args) throws Exception {
		Thread tw=new Thread(new ThreadWorker("G:\\Datas\\RBB\\config1.txt"),"MainWorker-Thread  1");
		//Thread tw=new Thread(new ThreadWorker(args[0]),"MainWorker-Thread");
		//Thread tw=new Thread(new ThreadWorker("/Users/MebiuW/Documents/TMP/RBB/config.txt"),"MainWorker-Thread");
		tw.start();
		//Thread tw2=new Thread(new ThreadWorker("G:\\Datas\\RBB\\config2.txt"),"MainWorker-Thread  2");
		//Thread tw=new Thread(new ThreadWorker(args[0]),"MainWorker-Thread");
	//	tw2.start();
		
		
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

