package mebiuw.rbb.exp;

import mebiuw.rbb.fundation.protocol.chord.ChordProtocol;

public class CopyOfCopyOfDataNodeLoader {

	public static void main(String[] args) throws Exception {
		Thread tw=new Thread(new ThreadWorker("G:\\Datas\\RBB\\config33.txt"),"MainWorker-Thread  3");
		//Thread tw=new Thread(new ThreadWorker(args[0]),"MainWorker-Thread");
		tw.start();
	//	Thread tw2=new Thread(new ThreadWorker("G:\\Datas\\RBB\\config2.txt"),"MainWorker-Thread  2");
		//Thread tw=new Thread(new ThreadWorker(args[0]),"MainWorker-Thread");
	//	tw2.start();
		
		
	}
    
}
