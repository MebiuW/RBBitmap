package mebiuw.rbb.fundation.protocol;

public interface IProtocol {
	/**
	 * 必须要做的如何处理消息
	 * @param msg 消息
	 */
	public void processMessage(IMessage msg);
	/**
	 * 通知监视服务器
	 * @param msg
	 */
	public void callbackSupervisor(IMessage msg);
	/**
	 * 获得当前节点的Ip地址
	 * @return
	 */
	public String getIPAddress();

}
