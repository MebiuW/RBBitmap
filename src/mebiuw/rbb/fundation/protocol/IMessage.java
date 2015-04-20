package mebiuw.rbb.fundation.protocol;

import java.util.List;

/**
 * 网络中传输的消息
 * @author 72770_000
 *
 */

public interface IMessage {
	/**
	 * 
	 * @return 返回文本消息体
	 */
	public String getMessageText();
	/**
	 * 
	 * @return 消息生成时间
	 */
	public long getCreateTime();
	/**
	 * 
	 * @return 消息最后记录到的时间
	 */
	public long getEndTime();
	/**
	 * 
	 * @return 得到源地址
	 */
	public String getSource();
	/**
	 * 
	 * @return 得到原跳数
	 */
	public int getHops();
	/**
	 * 
	 * @return 返回处理这个消息所占用了的时间
	 */
	public long getProcessTime();
	/**
	 * 简单的处理一次转发，需要给出本次处理的时间
	 * @param usedTime
	 */
	public void transmit(long usedTime);
	/**
	 * 获得消息的实际承载的内容
	 * @return 消息内容
	 */
	public String getMessageEntry();
	/**
	 * 获得分离后的消息元素
	 * @return 消息内容
	 */
	public List<String> getMessageItems();
	/**
	 * 获得消息的种类
	 * @return 消息的种类
	 */
	public String getMessageType();
	
	/**
	 * 消息的标识符，这个一般应该由消息生成的一方提供
	 * @return
	 */
	public String getMessageId();

}
