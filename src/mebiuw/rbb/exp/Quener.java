package mebiuw.rbb.exp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import mebiuw.rbb.fundation.network.netty.NettyClient;
import mebiuw.rbb.fundation.protocol.AddressItem;
import mebiuw.rbb.fundation.protocol.ProtocolServer;
import mebiuw.rbb.fundation.protocol.chord.ChordMessage;
import mebiuw.rbb.fundation.storage.FileStorage;
/**
 * 专门用来测试的
 * @author MebiuW
 *
 */
public class Quener {
	 private  ReadWriteLock lock = new ReentrantReadWriteLock();
	 private int count=0,empty=0;
	private  HashMap<Long,Long> startTimeCounter=new HashMap<Long,Long>();
	private  List<Long> endTimeCounter=new ArrayList<Long>();
	public void newMsg(long msgId){
		this.startTimeCounter.put(msgId, System.currentTimeMillis());
		//Logger.Log("send"+System.currentTimeMillis());
	}
	public  synchronized void sendBack(String msgId){

		long nmsgId=System.currentTimeMillis()/1000;
		this.endTimeCounter.add(nmsgId);


	
		
	}
	public void computerss(){
		/*
		long time=0,count=0;
		Iterator<Long> it = this.startTimeCounter.keySet().iterator();
		while(it.hasNext()){
			long msgid=it.next();
			if(this.endTimeCounter.containsKey(msgid)){
				time+=this.endTimeCounter.get(msgid)-this.startTimeCounter.get(msgid);
				Logger.Log(""+(this.endTimeCounter.get(msgid)-this.startTimeCounter.get(msgid)));;
				count++;
			}
		}
		if(count==0)
			System.out.println("no sendback");
		else
			System.out.println(time+"  /  "+count+"  Speed"+(time/(0.0+count)));
			*/
	}
	
	public void computers(){
		long time=0,count=0,all=0;
		HashMap<Long,Long> counters=new HashMap<Long,Long>();
		Iterator<Long> it =this.endTimeCounter.iterator();
		while(it.hasNext()){
			time=it.next();
			if(counters.containsKey(time)){
				counters.put(time,counters.get(time)+1);
			}
			else{
				counters.put(time,(long)1);
			}
		}
		it = counters.keySet().iterator();

			it=counters.keySet().iterator();
			while(it.hasNext()){
				long key=it.next();
				all+=counters.get(key);
				count++;
				System.out.println(key+"   "+counters.get(key));
		
			}

		
		System.out.println("count  "+all+"   "+(all/(count+0.0)));
	}
	public void mains() throws Exception{


		
		System.out.println("Go");
		Random ran=new Random(System.currentTimeMillis());
		AddressItem item=new AddressItem("127.0.0.1","11001 11002 11003 11004 11005",-1);
		CountProtocolServer ps=new CountProtocolServer(item,this);
		ps.startListening();
		int step=100;

		Scanner it=new Scanner(System.in);
		
	
		//this.computers();
		System.out.println("OO");
		while(it.hasNext()){
			int id=it.nextInt();
			if(id==1){
				this.computers();
			}
		}
		
	}
	
	public static void main(String isfr[]) throws Exception{
		Quener quener=new Quener();
		quener.mains();
	}

}
