package mebiuw.rbb.fundation.network.netty;
/**
 * 
 * @author MebiuW
 * 通过这个通知收到的消息
 */
public interface Subscriberable {
	/**
	 * 通知接受到的消息
	 * @param msg
	 */
	public void update(String msg);

}
