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
public class CopyOfQuener {
	 private  ReadWriteLock lock = new ReentrantReadWriteLock();
	 private int count=0,empty=0;
	private  HashMap<Long,Long> startTimeCounter=new HashMap<Long,Long>();
	private  HashMap<Long,Long> endTimeCounter=new HashMap<Long,Long>();
	public void newMsg(long msgId){
		this.startTimeCounter.put(msgId, System.currentTimeMillis());
		//Logger.Log("send"+System.currentTimeMillis());
	}
	public synchronized void sendBack(long msgId){
		//Logger.Log("back"+System.currentTimeMillis());
		this.endTimeCounter.put(msgId, System.currentTimeMillis());
		count++;
		empty++;
	}
	public void computer(){
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
	}
	
	public void computers(){
		long time=0,count=0;
		HashMap<Long,Long> counters=new HashMap<Long,Long>();
		Iterator<Long> it = this.endTimeCounter.values().iterator();
		while(it.hasNext()){
			long vv=it.next();
			long msgid=vv/1000;
			count++;
			if(counters.containsKey(msgid))
				counters.put(msgid, counters.get(msgid)+1);
			else 
				counters.put(msgid, (long) 1);
			
		}
		
		if(count==0)
			System.out.println("no sendback");
		else{
			it=counters.keySet().iterator();
			while(it.hasNext()){
				long key=it.next();
				System.out.println(key+"   "+counters.get(key));
			}
			//System.out.println(time+"  /  "+count+"  Speed"+(time/(0.0+count)));
		}
	}
	public void mains() throws Exception{
		FileStorage db=new FileStorage("/Users/MebiuW/Documents/TMP/RBB/db.txt");
		List<String> all = db.readAllLines();
		Queue<String> str=new LinkedList<String>();
		Queue<Long> msgids=new LinkedList<Long>();
		for(int i=0;i<20000;i++){
			long chordid=Long.parseLong(all.get(i).substring(0,all.get(i).indexOf(",")))%2;
			ChordMessage icm=new ChordMessage(all.get(i),"lc","INSERT",""+i,chordid);
			ChordMessage cm=new ChordMessage(icm.getMessageText(),"lc","LOCATE",""+i,chordid);
			str.add(cm.getMessageText());
			msgids.add(Long.parseLong(cm.getMessageId()));
			//System.out.println(cm.getMessageText());
		}
		List<NettyClient> netlist=new ArrayList<NettyClient>();
		for(int i=0;i<3;i++){
		//	netlist.add(new NettyClient("127.0.0.1",10001+i));
			netlist.add(new NettyClient("192.168.31.160",10001+i));
			netlist.add(new NettyClient("192.168.31.161",10001+i));
		}
		Thread t0=null;
	
		
		System.out.println("Go");
		Random ran=new Random(System.currentTimeMillis());

		AddressItem item=new AddressItem("127.0.0.1","20005 20006 20001 20002 20003 20004 20007",-1);
		CountProtocolServer ps=new CountProtocolServer(item,null);
		ps.startListening();
		Thread.sleep(31000);
		int step=100;
		for(int i=0;i<step*netlist.size()+10;i++){
			this.newMsg(msgids.poll());
			netlist.get(ran.nextInt(netlist.size())).sendMsg(str.poll());
		}
		Scanner it=new Scanner(System.in);
		
		while(count<15000){
			if(empty<0)
				Thread.yield();
			if(empty>=0){
			//	this.lock.writeLock().lock();
				empty-=step;
				//this.lock.writeLock().unlock();
				for(int i=0;i<step;i++){
					this.newMsg(msgids.poll());
					netlist.get(ran.nextInt(netlist.size())).sendMsg(str.poll());
				}
				
			}
			
		}
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
		CopyOfQuener quener=new CopyOfQuener();
		quener.mains();
	}

}
