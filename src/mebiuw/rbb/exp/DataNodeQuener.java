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

import mebiuw.rbb.fundation.bitstring.simple.RangeCreater;
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
public class DataNodeQuener {
	 private  ReadWriteLock lock = new ReentrantReadWriteLock();
	private List<String> all;
	private Queue<String> str;
	private Queue<Long> msgids;
	List<List<NettyClient>> netlist;
	private int count,nextsize;
	Random ran;
	/**
	 * 触发发送请求的
	 * @param step
	 */
	public  void triger(int step){
		
		synchronized(this){
			count+=step;
			if(count<5000/this.nextsize)
		for(int i=0;i<step;i++){
			//System.out.println(netlist.size());
			List<NettyClient> node = netlist.get(ran.nextInt(netlist.size()));
			//System.out.println(node.size());
			node.get(ran.nextInt(node.size())).sendMsg(str.poll());
		}
		}
	}
	
	public DataNodeQuener(String dbposition,List<List<NettyClient>> nettyClient,int netsize) throws Exception{
		/**
		 * 装载字典
		 */
		count=0;
		FileStorage db=new FileStorage("/Users/MebiuW/Documents/TMP/RBB/rdb.txt");
		 all = db.readAllLines();
		 str=new LinkedList<String>();
		 msgids=new LinkedList<Long>();
		 for(int i=0;i<20000;i++){
				long chordid=0;//Long.parseLong(all.get(i).substring(0,all.get(i).indexOf(",")))%netsize;
				String msg=all.get(i);
				msg=msg.replace(",", "-EUQAL-");
				ChordMessage icm=new ChordMessage(all.get(i),"lc","RANGE",""+i,chordid);
				ChordMessage cm=new ChordMessage(icm.getMessageText(),"lc","LOCATE",""+i,chordid);
				str.add(cm.getMessageText());
				msgids.add((long) i);
				//System.out.println(cm.getMessageText());
			}
		 netlist=nettyClient;
		 ran =new Random(System.currentTimeMillis());
		 this.nextsize=netsize;
		 //初始发送
		 Thread.sleep(100);
		 this.triger(50);
	}


}
