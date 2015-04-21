package mebiuw.rbb.fundation.protocol.chord;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mebiuw.btree.BplusTree;

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
	/**
	 * B-Tree
	 */
	private BplusTree btree;
	

	/**
	 * 启动新的线程去执行相关操作
	 */
	@Override
	public void processMessage(IMessage msg) {
		ChordMessage chordMsg = (ChordMessage)msg;
		if(msg.getMessageType().equals("LOCATE")){
			AddressItem nextHop=this.toNextHop(chordMsg.getChordId());
			if(nextHop==this.myAddressItem)
			{
				/**
				 * 启动一个新线程去执行
				 */
				ThreadWorker th=new ThreadWorker(null,chordMsg,this);
				Thread thread=new Thread(th,"b+process:"+chordMsg.getMessageId());
				thread.start();
			}
			else {
				this.sendToDestination((ChordMessage) msg);
			}
		}
		else{
			/**
			 * 启动一个新线程去执行
			 */
			ThreadWorker th=new ThreadWorker(null,chordMsg,this);
			Thread thread=new Thread(th,"b+process:"+chordMsg.getMessageId());
			thread.start();
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
	
	
	public void initManagement(){
		this.btree=new BplusTree(4);
	}


}
