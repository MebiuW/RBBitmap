package mebiuw.rbb.fundation.protocol.chord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mebiuw.btree.BplusTree;

import mebiuw.rbb.exp.Logger;
import mebiuw.rbb.fundation.network.netty.NettyClient;
import mebiuw.rbb.fundation.protocol.AddressItem;
import mebiuw.rbb.fundation.protocol.IMessage;
import mebiuw.rbb.fundation.protocol.IProtocol;
import mebiuw.rbb.fundation.protocol.IRouter;
import mebiuw.rbb.fundation.protocol.ProtocolServer;
import mebiuw.rbb.fundation.rowkey.simplerowkey.SimpleListRowkey;
import mebiuw.rbb.fundation.storage.FileStorage;

public class ChordProtocol implements IProtocol,IRouter{
	/**
	 * Chord ID ：路由信息
	 */
	private Map<Long,AddressItem> routerList;
	/**
	 * 用于路由随机访问的
	 */
	private List<List<NettyClient>> nettyClientList;
	private List<NettyClient> supervisorServer;
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
	private int dimension;
	private AddressItem myAddressItem,supervisor;
	private List<AddressItem> routeTable;
	
	public ChordProtocol(String configurationPosition) throws Exception{
		/**
		 * 读取配置文件
		 */
		this.position=configurationPosition;
		this.nettyClientList=new ArrayList<List<NettyClient>>();
		FileStorage file=new FileStorage(this.position);
		/**
		 * 参数按行
		 * 网络节点数量
		 * 该路由表的数量
		 * 当前节点ID
		 * =======
		 * 第N个Chord ID
		 * 第N个IP
		 * 第N个的Port
		 * =========
		 * 监视节点的信息
		 * B+树存放位置
		 * 依次将这些信息读入
		 */
		Logger.Log("开始装载配置文件");
		List<String> params = file.readAllLines();
		int index=0;
		this.networkSize=Integer.parseInt(params.get(index++));
		this.routeTableSize=Integer.parseInt(params.get(index++));
		this.netid=Integer.parseInt(params.get(index++));
		this.routeTable=new ArrayList<AddressItem>();
		this.chordIDofRouters=new ArrayList<Long>();
		String tmpIp,tmpPorts;
		for(int i=0;i<this.routeTableSize;i++){
			long nodeChordId=Long.parseLong(params.get(index++));
			this.chordIDofRouters.add(nodeChordId);
			tmpIp=params.get(index++);
			tmpPorts=params.get(index++);
			AddressItem item = new AddressItem(tmpIp,tmpPorts,netid);
			this.routeTable.add(item);
			/**
			 * 并且初始化Netty,因为使用的时ArrayList所以就算id是自己也要加入一个空的进去
			 * 这样就能实现随机访问了
			 */
			
			List<NettyClient> currentNettyClient=new ArrayList<NettyClient>();
			if(nodeChordId!=this.netid)
			for(int j=0;j<item.getPorts().size();j++){
				NettyClient client =new NettyClient(tmpIp,item.getPorts().get(j));
				currentNettyClient.add(client);
			}
			else{
				this.myAddressItem=item;
			}
			this.nettyClientList.add(currentNettyClient);
		}
		
		this.supervisor=new AddressItem(params.get(index++),params.get(index++),-1);
		this.supervisorServer=new ArrayList<NettyClient>();
		for(int i=0;i<this.supervisor.getPorts().size();i++){
			NettyClient nc=new NettyClient(this.supervisor.getIp(),this.supervisor.getPorts().get(i));
			this.supervisorServer.add(nc);
			
		}
		this.bposition=params.get(index++);
		Logger.Log("完成装载配置文件 装载B+树");
		/**
		 * 接下来将初始化B+数
		 */
		FileStorage Bfile=new FileStorage(this.bposition);
		List<String> bparams = Bfile.readAllLines();
		/**
		 * b+的几个叶
		 * rowkey位置
		 * 有几个rowkey
		 * 具体数据的维度
		 * 有的序列
		 */
		int bindex=0;
		this.blevel=Integer.parseInt(bparams.get(bindex++));
		this.rposition=bparams.get(bindex++);
		this.btree=new BplusTree(this.blevel);
		int rowkeys=Integer.parseInt(bparams.get(bindex++));
		this.dimension=Integer.parseInt(bparams.get(bindex++));
		int rowid;
		for(int i=0;i<rowkeys;i++){
			rowid=Integer.parseInt(bparams.get(bindex++));
			SimpleListRowkey slr=new SimpleListRowkey(this.rposition+rowid+".txt");
			this.btree.insertOrUpdate(rowid,slr);
		}
		/**
		 * 初始化Netty模块 并开始监听
		 */
		Logger.Log("初始化监听模块 本机ip："+this.myAddressItem.getIp());
		ProtocolServer ps=new ProtocolServer(this.myAddressItem,this);
		ps.startListening();
		
		
	}

	/**
	 * 启动新的线程去执行相关操作
	 */
	@Override
	public void processMessage(IMessage msg) {
	
		ChordMessage chordMsg = (ChordMessage)msg;
		Logger.Log("【"+this.netid+"】 收到消息  to "+chordMsg.getChordId());
		if(msg.getMessageType().equals("LOCATE")){
			Logger.Log("收到LOCATE消息：Message ID :"+chordMsg.getMessageId());
			int nextHop=this.toNextHop(chordMsg.getChordId());
			if(nextHop==this.netid)
			{
				/**
				 * 启动一个新线程去执行
				 */
				ThreadWorker th=new ThreadWorker(this.btree,(ChordMessage)chordMsg.getNextMessage(),this,this.rposition,this.dimension);
				Thread thread=new Thread(th,"b+process:"+chordMsg.getMessageId());
				thread.start();
			}
			else {
				this.sendToDestination(nextHop,(ChordMessage) msg);
			}
		}
		else{
			/**
			 * 启动一个新线程去执行
			 */
			ThreadWorker th=new ThreadWorker(this.btree,chordMsg,this,this.rposition,this.dimension);
			Thread thread=new Thread(th,"b+process:"+chordMsg.getMessageId());
			thread.start();
		}
		
	}
	/**
	 * 送去Chord的其他节点之上 这里需要实现路由算法
	 * @param msg
	 */
	private void sendToDestination(int nextid,ChordMessage msg) {
		Logger.Log("【"+this.netid+"】路由消息"+msg.getMessageId()+"/c:"+ msg.getChordId()+"   to"+nextid);
		List<NettyClient> senders = this.nettyClientList.get(nextid);
		int nextPort=msg.getMessageId().hashCode()%senders.size();
		senders.get(nextPort).sendMsg(msg.getMessageText());
		
		
	}

	// 返回通知消息
	@Override
	public void callbackSupervisor(IMessage msg) {
		List<NettyClient> senders = this.supervisorServer;
		int nextPort=msg.getMessageId().hashCode()%senders.size();
		//只需要返回id就可以了
		senders.get(nextPort).sendMsg(msg.getMessageId());
		
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
	public int toNextHop(long chordid) {
		int nextHop=0;
		while(nextHop<this.chordIDofRouters.size() && this.chordIDofRouters.get(nextHop)<chordid){
			nextHop++;
		}
		nextHop%=this.chordIDofRouters.size();
		return nextHop;
	}

	@Override
	public void removeAddressItem(String ip) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void initManagement(){
		this.btree=new BplusTree(4);
	}


}
