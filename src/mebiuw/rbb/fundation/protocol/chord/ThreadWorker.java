package mebiuw.rbb.fundation.protocol.chord;

import mebiuw.rbb.exp.Logger;
import mebiuw.rbb.fundation.protocol.IProtocol;
import mebiuw.rbb.fundation.rowkey.IDataItemable;
import mebiuw.rbb.fundation.rowkey.IRowkeyable;
import mebiuw.rbb.fundation.rowkey.simplerowkey.SimpleDataItem;
import mebiuw.rbb.fundation.rowkey.simplerowkey.SimpleListRowkey;

import com.mebiuw.btree.IBtree;

public class ThreadWorker implements Runnable {
	/**
	 * 根据这两个参数，实现具体的线程的操作
	 */
	private IBtree btree;
	private ChordMessage nextMessage;
	private IProtocol callbackProtocol;
	private String rowkeyPosition;
	private int dimension;

	public ThreadWorker(IBtree btree, ChordMessage nextMessage,
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
			this.processInsertFunction(nextMessage);
			Logger.Count();

		} else if (nextMessage.getMessageType().equals("POINT")) {

		}
		

	}

	/**
	 * 处理插入的代码
	 * 
	 * @param messageEntry
	 */
	private void processPointFunction(String messageEntry) {
		String[] items = messageEntry.split("#");
		long regionId = Long.parseLong(items[0]);
		IRowkeyable rowkey = (IRowkeyable) this.btree.get(regionId);
		if (rowkey == null) {
			// TODO
			this.callbackProtocol.callbackSupervisor(null);
		}
		rowkey.query(null);
		this.callbackProtocol.callbackSupervisor(null);

	}

	/**
	 * 处理插入的代码
	 * 
	 * @param messageEntry
	 */
	private void processInsertFunction(ChordMessage msg) {
		try{
		
		Logger.Log("线程准备处理InsertOrUpdate：Message ID :"+nextMessage.getMessageId());
		String[] items = msg.getMessageEntry().split(",");
		long regionId = Long.parseLong(items[0]);
		
		IRowkeyable rowkey =null;
		synchronized(this){
		rowkey=(IRowkeyable) this.btree.get(regionId);
		}
		if (rowkey == null) {
			rowkey=new SimpleListRowkey(this.dimension,this.rowkeyPosition+"\\"+regionId+".txt");
			synchronized(this){
			this.btree.insertOrUpdate(regionId, rowkey);
			}
		}
		IDataItemable data=new SimpleDataItem(msg.getMessageEntry());
		rowkey.insertOrUpdate(data);
		Logger.Log("线程处理InsertOrUpdate处理成功消息：Message ID :"+nextMessage.getMessageId());
		//增加一个查询
		
		
		
		}
		finally{
			this.callbackProtocol.callbackSupervisor(msg);
		}
		
		}
		
		
		
	

}
