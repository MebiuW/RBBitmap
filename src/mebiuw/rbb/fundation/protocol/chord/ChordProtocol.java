package mebiuw.rbb.fundation.protocol.chord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mebiuw.btree.BplusTree;

import mebiuw.rbb.fundation.protocol.AddressItem;
import mebiuw.rbb.fundation.protocol.IMessage;
import mebiuw.rbb.fundation.protocol.IProtocol;
import mebiuw.rbb.fundation.protocol.IRouter;
import mebiuw.rbb.fundation.rowkey.simplerowkey.SimpleListRowkey;
import mebiuw.rbb.fundation.storage.FileStorage;

public class ChordProtocol implements IProtocol,IRouter{
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
	private String position,bposition,rposition;
	private int networkSize,routeTableSize,netid,blevel;
	private long df;
	private AddressItem myAddressItem,supervisor;
	private List<AddressItem> routeTable;
	
	public ChordProtocol(String configurationPosition) throws Exception{
		/**
		 * 读取配置文件
		 */
		this.position=configurationPosition;
		FileStorage file=new FileStorage(this.position);
		/**
		 * 参数按行
		 * 网络节点数量
		 * 该路由表的数量
		 * 当前节点ID
		 * =======
		 * 第N个IP
		 * 第N个的Port
		 * =========
		 * 监视节点的信息
		 * B+树存放位置
		 * 依次将这些信息读入
		 */
		List<String> params = file.readAllLines();
		int index=0;
		this.networkSize=Integer.parseInt(params.get(index++));
		this.routeTableSize=Integer.parseInt(params.get(index++));
		this.netid=Integer.parseInt(params.get(index++));
		this.routeTable=new ArrayList<AddressItem>();
		String tmpIp,tmpPorts;
		for(int i=0;i<this.routeTableSize;i++){
			tmpIp=params.get(index++);
			tmpPorts=params.get(index++);
			this.routeTable.add(new AddressItem(tmpIp,tmpPorts,netid));
		}
		this.myAddressItem=this.routeTable.get(this.netid);
		this.supervisor=new AddressItem(params.get(index++),params.get(index++),-1);
		this.bposition=params.get(index++);
		/**
		 * 接下来将初始化B+数
		 */
		FileStorage Bfile=new FileStorage(this.bposition);
		List<String> bparams = Bfile.readAllLines();
		/**
		 * b+的几个叶
		 * rowkey位置
		 * 有几个rowkey
		 * 有的序列
		 */
		int bindex=0;
		this.blevel=Integer.parseInt(bparams.get(bindex++));
		this.rposition=bparams.get(bindex++);
		this.btree=new BplusTree(this.blevel);
		int rowkeys=Integer.parseInt(bparams.get(bindex++));
		int rowid;
		for(int i=0;i<rowkeys;i++){
			rowid=Integer.parseInt(bparams.get(bindex++));
			SimpleListRowkey slr=new SimpleListRowkey(this.rposition+rowid+".txt");
			this.btree.insertOrUpdate(rowid,slr);
		}
		/**
		 * 初始化Netty模块
		 */
		
		
	}

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
