package mebiuw.rbb.fundation.protocol.chord;

import java.util.ArrayList;
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
			IProtocol callbackProtocol, String rowkeyPosition, int dimension) {
		super();
		Logger.Log("线程处理Function消息：Message ID :" + nextMessage.getMessageId());
		this.btree = btree;
		this.nextMessage = nextMessage;
		this.callbackProtocol = callbackProtocol;
		this.rowkeyPosition = rowkeyPosition;
		this.dimension = dimension;
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
				System.out.println(nextMessage.getMessageText());
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
		String messageEntry = message.getMessageEntry();
		String[] items = messageEntry.split(",");
		long regionId = Long.parseLong(items[0]);
		
		try {
			Thread.sleep((long) (Math.log(((BplusTree) this.btree).hashIndex
					.size()) / 2));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 接下来转化条件
		ConditionItem[] cons = new ConditionItem[this.dimension];
		int index = 1;
		System.out.println(index);
		for (int i = 0; i < this.dimension; i++) {
			System.out.println(index);
			if (items[index++].equals(ConditionType.EQUAL)) {
				cons[i] = new ConditionItem(ConditionType.EQUAL,
						Double.parseDouble(items[index++]));
			}
		}

		Condition con = new Condition(cons);
		IRowkeyable rowkey = (IRowkeyable) this.btree.get(regionId);
		if (rowkey == null) {
			// TODO
			this.callbackProtocol.callbackSupervisor(message);
		}
		rowkey.query(con);
		this.callbackProtocol.callbackSupervisor(message);

	}

	/**
	 * 处理插入的代码
	 * 
	 * @param messageEntry
	 * @throws InterruptedException
	 */
	private void processInsertFunction(ChordMessage msg)
			throws InterruptedException {
		try {
			Thread.sleep(20);
			Logger.Log("线程准备处理InsertOrUpdate：Message ID :"
					+ nextMessage.getMessageId());
			String[] items = msg.getMessageEntry().split(",");
			long regionId = Long.parseLong(items[0]);

			IRowkeyable rowkey = null;
			synchronized (this) {
				rowkey = (IRowkeyable) this.btree.get(regionId);
			}
			if (rowkey == null) {
				rowkey = new SimpleListRowkey(this.dimension,
						this.rowkeyPosition + "\\" + regionId + ".txt");
				synchronized (this) {
					this.btree.insertOrUpdate(regionId, rowkey);
				}
			}
			IDataItemable data = new SimpleDataItem(msg.getMessageEntry());
			rowkey.insertOrUpdate(data);
			Logger.Log("线程处理InsertOrUpdate处理成功消息：Message ID :"
					+ nextMessage.getMessageId());
			// 增加一个查询

		} finally {
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
		for (int i = 0; i < regionNum; i++) {
			
			long regionId = candidates.get(i);
			if(regionId>=0)
				continue;
			else regionId*=-1;
	
			IRowkeyable rowkey = (IRowkeyable) this.btree.get(regionId);
			if (rowkey != null) {
				rowkey.queryAll(con);
			}
		
		}

		this.callbackProtocol.callbackSupervisor(message);

	}

}
