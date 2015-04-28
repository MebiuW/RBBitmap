package mebiuw.rbb.fundation.protocol.chord;

import mebiuw.rbb.fundation.protocol.IProtocol;
import mebiuw.rbb.fundation.rowkey.IRowkeyable;
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
			this.processInsertFunction(nextMessage.getMessageEntry());

		} else if (nextMessage.getMessageType().equals("POINT")) {

		}
		this.processPointFunction(nextMessage.getMessageEntry());

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
	private void processInsertFunction(String messageEntry) {
		String[] items = messageEntry.split("#");
		long regionId = Long.parseLong(items[0]);
		IRowkeyable rowkey = (IRowkeyable) this.btree.get(regionId);
		if (rowkey == null) {
			rowkey=new SimpleListRowkey(this.dimension,this.rowkeyPosition);
			this.btree.insertOrUpdate(regionId, rowkey);
		}
		rowkey.insertOrUpdate(null);
		this.callbackProtocol.callbackSupervisor(null);
	}

}
