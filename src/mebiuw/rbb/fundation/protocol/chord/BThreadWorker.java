package mebiuw.rbb.fundation.protocol.chord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mebiuw.rbb.exp.Logger;
import mebiuw.rbb.fundation.protocol.IProtocol;
import mebiuw.rbb.fundation.rowkey.IDataItemable;
import mebiuw.rbb.fundation.rowkey.IRowkeyable;
import mebiuw.rbb.fundation.rowkey.simplerowkey.SimpleDataItem;
import mebiuw.rbb.fundation.rowkey.simplerowkey.SimpleListRowkey;
import mebiuw.rbb.fundation.sql.Condition;
import mebiuw.rbb.fundation.sql.ConditionItem;
import mebiuw.rbb.fundation.sql.ConditionType;

import com.mebiuw.btree.BplusTree;
import com.mebiuw.btree.IBtree;
import com.mebiuw.btree.LinkedBplusTree;

public class BThreadWorker implements Runnable {
	/**
	 * 根据这两个参数，实现具体的线程的操作
	 */
	private LinkedBplusTree btree;
	private ChordMessage nextMessage;
	private IProtocol callbackProtocol;
	private String rowkeyPosition;
	private int dimension;

	public BThreadWorker(LinkedBplusTree btree, ChordMessage nextMessage,
			IProtocol callbackProtocol,String rowkeyPosition,int dimension) {
		super();
		Logger.Log("线程处理Function消息：Message ID :"+nextMessage.getMessageId());
		this.btree = btree;
		this.nextMessage = nextMessage;
		this.callbackProtocol = callbackProtocol;
		this.rowkeyPosition=rowkeyPosition;
		this.dimension=dimension;
	}

	@Override
	public void run() {
		this.processFunctionMessage();
	}

	/**
	 * 处理功能性消息
	 * 
	 * @param nextMessage
	 *            ，接触封装的消息
	 */

	private void processFunctionMessage() {

		if (nextMessage.getMessageType().equals("INSERT")) {
			try {
				this.processInsertFunction(nextMessage);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Logger.Count();

		} else if (nextMessage.getMessageType().equals("POINT")) {
			try {
				this.processPointFunction(nextMessage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
		else if (nextMessage.getMessageType().equals("RANGE")) {
			try {
				this.processRangeFunction(nextMessage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		

	}

	/**
	 * 处理插入的代码
	 * 
	 * @param messageEntry
	 */
	private void processPointFunction(ChordMessage message) {
		String messageEntry=message.getMessageEntry();
		String[] items = messageEntry.split("-");
		long regionId = Long.parseLong(items[0]);
		int index=1;
	
		//接下来转化条件
		ConditionItem[] cons=new ConditionItem[this.dimension];
		for(int i=0;i<this.dimension;i++){
			if(items[index++].equals(ConditionType.EQUAL)){
				cons[i]=new ConditionItem(ConditionType.EQUAL,Double.parseDouble(items[index++]));
			}
		}
		Condition con=new Condition(cons);
		this.btree.sleep();
		List<SimpleDataItem> t = this.btree.getLinkedList(Double.parseDouble(items[2]));
		if(t!=null){
		Iterator<SimpleDataItem> it = t.iterator();
		while(it.hasNext()){
			try {
				Thread.sleep(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(it.next().isSatisfy(con,con.getConditions().length)){
				System.out.println("Get it");
				this.callbackProtocol.callbackSupervisor(message);
				return ;
			}
			
		}
		}
		
		System.out.println("Get Faild");
		this.callbackProtocol.callbackSupervisor(message);
	}

	/**
	 * 处理插入的代码
	 * 
	 * @param messageEntry
	 * @throws InterruptedException 
	 */
	private void processInsertFunction(ChordMessage msg) throws InterruptedException {
		try{
		Thread.sleep(100);
		Logger.Log("线程准备处理InsertOrUpdate：Message ID :"+nextMessage.getMessageId());
		SimpleDataItem data=new SimpleDataItem(msg.getMessageEntry());
		synchronized(this){
		this.btree.insertOrUpdate(data.getFirstKey(), data);
		}
		Logger.Log("线程处理InsertOrUpdate处理成功消息：Message ID :"+nextMessage.getMessageId());
		//增加一个查询
		
		
		
		}
		finally{
			this.callbackProtocol.callbackSupervisor(msg);
		}
		
		}
		
		
	private void processRangeFunction(ChordMessage message) {
		String messageEntry = message.getMessageEntry();
		String[] items = messageEntry.split("[*]");
		int regionNum = Integer.parseInt(items[0]);
		List<Long> candidates = new ArrayList<Long>();
		int index = 1;
		for (int i = 0; i < regionNum; i++) {
			candidates.add(Long.parseLong(items[index++]));
		}

	
		// 接下来转化条件
		ConditionItem[] cons = new ConditionItem[this.dimension];
		for (int i = 0; i < this.dimension; i++) {
			if (items[index++].equals(ConditionType.LESSOREQUAL)) {
				cons[i] = new ConditionItem(ConditionType.LESSOREQUAL,
						Double.parseDouble(items[index++]));
			}
		}
		Condition con = new Condition(cons);
		Iterator<Object> it = this.btree.hashIndex.values().iterator();
		int count=0;
		while(it.hasNext()){
			//System.out.println("OOK");
	
			List<SimpleDataItem> next = (List<SimpleDataItem>)it.next();
			Iterator<SimpleDataItem> nit = next.iterator();
			
			while(nit.hasNext()){
				count++;
				nit.next().isSatisfy(con,con.getConditions().length);
			}
			
		}
System.out.println("Do over");
		this.callbackProtocol.callbackSupervisor(message);
		try {
			System.out.println(count);
			Thread.sleep((long) (Math.log(count)*50));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
		
	

}
