package mebiuw.rbb.fundation.bitstring;

import mebiuw.rbb.fundation.rowkey.IDataItemable;
import mebiuw.rbb.fundation.sql.Condition;

/**
 * 如果需要配置一个参数，那么所需要实现的东西
 * @author MebiuW
 *
 */

public interface IConfiguration {
	/**
	 * 根据属性值返回当前的对应属性的大小
	 * @param attributeIndex 第几个属性
	 * @param value 值
	 * @return 当前value在这个配置下的分区排序
	 */
	public int getAttributeIndex(int attributeIndex,double value);
	/**
	 * 返回当前这个配置下有多少个属性数目
	 * @return 属性数目
	 */
	public int getAttributeNum();
	/**
	 * 当前index所需要占用的bitstring大小
	 * @param index 第几个
	 * @return 占用大小
	 */
	public int getBitLengthOfAttribute(int index);
	/**
	 * 根据当前时间获得一个指定属性上的随机值
	 * @param index
	 * @return
	 */
	public double getRandomValue(int index);
	/**
	 * 获得一个随机的值
	 * @return
	 */
	public IDataItemable getRandomDataItem();
	/**
	 * 随机获得一个封闭的等区间范围
	 * @param subrange 每个属性设计到的范围
	 * @return
	 */
	public Condition getRandomRange(int subrange);
	
	

}
