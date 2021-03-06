package mebiuw.rbb.fundation.protocol.chord;

import java.util.ArrayList;
import java.util.List;

import mebiuw.rbb.fundation.protocol.IMessage;
import mebiuw.rbb.fundation.protocol.IMultiMessage;
/**
 * #和%符号都是保留符号
 * @author 72770_000
 *
 */
public class ChordMessage implements IMessage,IMultiMessage {
	public long getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}
	public void setProcessTime(long processTime) {
		this.processTime = processTime;
	}
	public void setHops(int hops) {
		this.hops = hops;
	}


	private String entry,source,type,id,message;
	private long createdTime,endTime,processTime,chordid;
	int hops;
	
/**
 * 通过一个消息的字符串来生成一个消息，这个应该和getMessage得到的一起使用
 * @param msg
 */
	public ChordMessage(String msg){
		try{
		this.message=msg;
		List<String> itemList = this.getMessageItems();
		/**
		 * this.message=this.source+"#"+this.id+"#"+this.hops+"#"+this.processTime+"#"+this.createdTime+"#"+this.endTime+"#"+this.type+"#"+this.entry;
		
		 */
		this.source=itemList.get(0);
		this.id=itemList.get(1);
		this.hops=Integer.parseInt(itemList.get(2));
		this.processTime=Long.parseLong(itemList.get(3));
		this.createdTime=Long.parseLong(itemList.get(4));
		this.endTime=Long.parseLong(itemList.get(5));
		this.type=itemList.get(6);
		this.entry=itemList.get(7).replaceAll("#", "%");
		this.chordid=Long.parseLong(itemList.get(8));
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("   "   +msg);
		}
		
	}
	/**
	 * 首次创建的新消息
	 * @param entry 消息正文
	 * @param source 创建者ip
	 */
	public ChordMessage(String entry, String source,String type,String id,long chordid) {
		super();
		this.entry = entry.replaceAll("#", "%");
		this.source = source;
		this.createdTime=System.currentTimeMillis();
		this.endTime=System.currentTimeMillis();
		this.processTime=0;
		this.hops=1;
		this.type=type;
		this.id=id;
		this.chordid=chordid;
		this.refreshMessage();
	}
	/**
	 * 更新自己当前消息的传输所需奥的格式
	 */
	private void refreshMessage() {
		  this.message=this.source+"#"+this.id+"#"+this.hops+"#"+this.processTime+"#"+this.createdTime+"#"+this.endTime+"#"+this.type+"#"+this.entry+"#"+this.chordid;
		
	}

	/**
	 * 适用于第二次，或者是转发创建时使用，会自动记录最后时间
	 * @param message 消息正文
	 * @param source 源地址
	 * @param createdTime 创建时间
	 * @param processTime 处理时间
	 * @param hops 跳数
	 */

	public ChordMessage(String entry, String source, long createdTime,
			long processTime, int hops,String type,String id,long chordid) {
		super();
		this.entry = entry.replaceAll("#", "%");
		this.source = source;
		this.createdTime = createdTime;
		this.processTime = processTime;
		this.hops = hops;
		this.endTime=System.currentTimeMillis();
		this.type=type;
		this.id=id;
		this.chordid=chordid;
		this.refreshMessage();
	}



	@Override
	public String getMessageText() {
		return this.message;
	}

	@Override
	public long getCreateTime() {
		return this.createdTime;
	}

	@Override
	public long getEndTime() {
		return this.endTime;
	}

	@Override
	public String getSource() {
		return this.source;
	}

	@Override
	public int getHops() {
		return this.hops;
	}

	@Override
	public long getProcessTime() {
		return this.processTime;
	}

	@Override
	public void transmit(long usedTime) {
		this.processTime+=usedTime;
		this.hops++;
		this.refreshMessage();
		
	}

	@Override
	public String getMessageEntry() {

		return this.entry.replace("%", "#");
	}

	@Override
	public List<String> getMessageItems() {
		List<String> list = new ArrayList<String>();
		String[] tmp = this.message.split("#");
		for(int i=0;i<tmp.length;i++)
			list.add(tmp[i]);
		return list;
		}

	@Override
	public String getMessageType() {
		return this.type;
	}

	@Override
	public String getMessageId() {
		return this.id;
	}
	@Override
	public IMessage getNextMessage() {
		try{
			//保证消息一致
			ChordMessage msg = new ChordMessage(this.entry.replaceAll("%", "#"));
			msg.id=this.id;
			return msg;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public boolean hasNextMessage() {
		if(this.getNextMessage()!=null)
			return true;
		else return false;
	}
	
	
	/**
	 * 返回该有的Chord Id
	 * @return
	 */
	public long getChordId(){
		return this.chordid;
	}

}
