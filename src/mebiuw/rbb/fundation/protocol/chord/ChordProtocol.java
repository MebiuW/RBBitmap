package mebiuw.rbb.fundation.protocol.chord;

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
				this.sendToDestination(msg);
			}
		}
		else{
			this.processFunctionMessage((ChordMessage)msg);
		}
		
	}
	/**
	 * 送去Chord的其他节点之上
	 * @param msg
	 */
	private void sendToDestination(IMessage msg) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 处理功能性消息
	 * @param nextMessage，接触封装的消息
	 */

	private void processFunctionMessage(ChordMessage nextMessage) {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public AddressItem toNextHop(long chordid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeAddressItem(String ip) {
		// TODO Auto-generated method stub
		
	}

}
