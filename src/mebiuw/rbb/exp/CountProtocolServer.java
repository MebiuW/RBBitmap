package mebiuw.rbb.exp;

import mebiuw.rbb.exp.Logger;
import mebiuw.rbb.fundation.network.netty.NettyServer;
import mebiuw.rbb.fundation.protocol.AddressItem;
import mebiuw.rbb.fundation.protocol.IProtocol;

public class CountProtocolServer {
	private AddressItem serverState;
	private Quener hoster;
	private CountServer[] serverList;
	private int portSize;

	/**
	 * 
	 * @param serverState
	 *            当前服务器的地址
	 * @param hoster
	 *            消息需要相应的地址，需要提供一个处理消息的协议
	 */
	public CountProtocolServer(AddressItem serverState, Quener hoster) {
		super();
		// 初始化
		Logger.Log("初始化本机监听端口");
		this.serverState = serverState;
		this.hoster = hoster;
		this.portSize = this.serverState.getPorts().size();
		this.serverList = new CountServer[this.portSize];
		// 初始化
		for (int i = 0; i < this.portSize; i++) {
			this.serverList[i] = new CountServer(this.serverState.getPorts()
					.get(i), hoster);
		}
		Logger.Log("本机监听端口配置完成");
	}

	public void startListening() {
		// 开始坚挺
		Logger.Log("本机监听端口监听准备开始");
		for (int i = 0; i < this.portSize; i++) {
			Thread thread=new Thread(this.serverList[i],"server-port:"+this.serverState.getPorts()
					.get(i));
			thread.start();
		}
		Logger.Log("本机监听端口初始化完成");
	}

}
