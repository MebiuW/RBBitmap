package mebiuw.rbb.fundation.bitstring.simple;

import mebiuw.rbb.fundation.bitstring.IConfiguration;
/**
 * 这个需要读取文件，分区方式以当前的0到最大值为全部数据域，以Phase为阶段进行判断
 * @author MebiuW
 *
 */

public class SimpleConfiguration implements IConfiguration{
	private String configurationFilePath;
	private int attrinum;
	private int[] lengthOfAttribute;
	private long[] maxOfEachAttribute;
	private long[] phaseOfEachAttribute;

	
	/**
	 * 提供文件地址后就可以得到
	 * @param configurationFilePath
	 */
	public SimpleConfiguration(String configurationFilePath) {
		super();
		this.configurationFilePath = configurationFilePath;
		//TODO 增加先关的读取代码
	}

	/**
	 * 根据属性值返回当前的对应属性的大小
	 * @param attributeIndex 第几个属性
	 * @param value 值
	 * @return 当前value在这个配置下的分区排序 -1代表无效的值
	 */
	@Override
	public long getAttributeIndex(int attributeIndex, double value) {
		if(value<0 || value>this.maxOfEachAttribute[attributeIndex])
			return -1;//invalid 
		return (long)value%this.phaseOfEachAttribute[attributeIndex];
	}

	@Override
	public int getAttributeNum() {
		return attrinum;
	}

	@Override
	public int getBitLengthOfAttribute(int index) {
		return lengthOfAttribute[index];
	}

}
