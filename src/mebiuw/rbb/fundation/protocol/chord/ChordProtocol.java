package mebiuw.rbb.fundation.protocol.chord;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import mebiuw.rbb.fundation.protocol.AddressItem;
import mebiuw.rbb.fundation.protocol.IMessage;
import mebiuw.rbb.fundation.protocol.IProtocol;
import mebiuw.rbb.fundation.protocol.IRouter;

public class ChordProtocol implements IProtocol,IRouter{
	private AddressItem myAddressItem,supervisor;
	/**
	 * Chord ID ：路由信息
	 */
	private Map<Long,AddressItem> routerList;
	/**
	 * 用来保存当前维护的chordid
	 */
	private List<Long> chordIDofRouters;
	private String myip;
	

	@Override
	public void processMessage(IMessage msg) {
		ChordMessage chordMsg = (ChordMessage)msg;
		if(msg.getMessageType().equals("LOCATE")){
			AddressItem nextHop=this.toNextHop(chordMsg.getChordId());
			if(nextHop==this.myAddressItem)
			{
				this.processFunctionMessage((ChordMessage)chordMsg.getNextMessage());
			}
			else {
				this.sendToDestination((ChordMessage) msg);
			}
		}
		else{
			this.processFunctionMessage((ChordMessage)msg);
		}
		
	}
	/**
	 * 送去Chord的其他节点之上 这里需要实现路由算法
	 * @param msg
	 */
	private void sendToDestination(ChordMessage msg) {
		long destChord=msg.getChordId();
		//获得ID
		AddressItem item=this.toNextHop(destChord);
		//
		
		
	}
	/**
	 * 处理功能性消息
	 * @param nextMessage，接触封装的消息
	 */

	private void processFunctionMessage(ChordMessage nextMessage) {
		if(nextMessage.getMessageType().equals("INSERT")){
			this.processInsertFunction(nextMessage.getMessageEntry());
		}
		else if(nextMessage.getMessageType().equals("POINT")){
			
		}this.processPointFunction(nextMessage.getMessageEntry());
		
	}
	/**
	 * 处理插入的代码
	 * @param messageEntry
	 */
	private void processPointFunction(String messageEntry) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 处理插入的代码
	 * @param messageEntry
	 */
	private void processInsertFunction(String messageEntry) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void callbackSupervisor(IMessage msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getIPAddress() {
		// TODO Auto-generated method stub
		return this.myip;
	}

	@Override
	public void addAddressItem(AddressItem item) {
		/**
		 * 最后一定要排序
		 */
		Collections.sort(this.chordIDofRouters);
		
	}
/**
 * 根据吓一跳的chord id得到应该路由的位置
 */
	@Override
	public AddressItem toNextHop(long chordid) {
		int nextHop=0;
		while(nextHop<this.chordIDofRouters.size() && this.chordIDofRouters.get(nextHop)<chordid){
			nextHop++;
		}
		nextHop%=this.chordIDofRouters.size();
		return this.routerList.get(nextHop);
	}

	@Override
	public void removeAddressItem(String ip) {
		// TODO Auto-generated method stub
		
	}

}
