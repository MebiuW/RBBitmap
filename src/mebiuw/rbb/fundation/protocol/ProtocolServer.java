package mebiuw.rbb.fundation.protocol;

import mebiuw.rbb.fundation.network.netty.NettyServer;

public class ProtocolServer {
	private AddressItem serverState;
	private IProtocol hoster;
	private NettyServer[] serverList;
	private int portSize;

	/**
	 * 
	 * @param serverState
	 *            当前服务器的地址
	 * @param hoster
	 *            消息需要相应的地址，需要提供一个处理消息的协议
	 */
	public ProtocolServer(AddressItem serverState, IProtocol hoster) {
		super();
		// 初始化
		this.serverState = serverState;
		this.hoster = hoster;
		this.portSize = this.serverState.getPorts().size();
		this.serverList = new NettyServer[this.portSize];
		// 初始化
		for (int i = 0; i < this.portSize; i++) {
			this.serverList[i] = new NettyServer(this.serverState.getPorts()
					.get(i), hoster);
		}
	}

	public void startListening() {
		// 开始坚挺
		for (int i = 0; i < this.portSize; i++) {
			Thread thread=new Thread(this.serverList[i],"server-port:"+this.serverState.getPorts()
					.get(i));
			thread.start();
		}
	}

}
