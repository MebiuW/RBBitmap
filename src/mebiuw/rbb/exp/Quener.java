package mebiuw.rbb.exp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import mebiuw.rbb.fundation.network.netty.NettyClient;
import mebiuw.rbb.fundation.protocol.chord.ChordMessage;
import mebiuw.rbb.fundation.storage.FileStorage;

public class Quener {
	public static void main(String args[]) throws Exception{
		FileStorage db=new FileStorage("/Users/MebiuW/Documents/TMP/RBB/db.txt");
		List<String> all = db.readAllLines();
		Queue<String> str=new LinkedList<String>();
		for(int i=0;i<20000;i++){
			long chordid=Long.parseLong(all.get(i).substring(0,all.get(i).indexOf(",")))%2;
			ChordMessage icm=new ChordMessage(all.get(i),"lc","INSERT",""+i,chordid);
			ChordMessage cm=new ChordMessage(icm.getMessageText(),"lc","LOCATE",""+i,chordid);
			str.add(cm.getMessageText());
			//System.out.println(cm.getMessageText());
		}
		List<NettyClient> netlist=new ArrayList<NettyClient>();
		for(int i=0;i<5;i++){
			netlist.add(new NettyClient("192.168.31.161",10001+i));
			
			netlist.add(new NettyClient("192.168.31.160",10001+i));
		}
		Thread t0=null;
		for(int i=0;i<netlist.size();i++){
			Thread t=new Thread(netlist.get(i));
			t.start();
			t0=t;
		}
		Thread.sleep(31);
		System.out.println("Go");
		Random ran=new Random(System.currentTimeMillis());
		for(int i=0;i<5000;i++){
			netlist.get(ran.nextInt(netlist.size())).sendMsg(str.poll());
		}
		while(t0.isAlive()){
			Thread.yield();
		}
		
	}

}
