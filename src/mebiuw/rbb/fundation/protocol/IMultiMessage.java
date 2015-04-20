package mebiuw.rbb.fundation.protocol;


/**
 * 能够实现多层消息的，也就是一个消息包含多个
 * @author 72770_000
 *
 */

public interface IMultiMessage {
	/**
	 * 获得嵌套的下一个消息
	 * @return 下一个消息
	 */
	public IMessage getNextMessage();
	/**
	 * 是否具有下一个消息
	 * @return
	 */
	public boolean hasNextMessage();

}
