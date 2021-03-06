package mebiuw.rbb.fundation.rowkey;

import mebiuw.rbb.fundation.sql.Condition;
import mebiuw.rbb.fundation.sql.IConditionable;

/**
 * 最终存储的数据
 * @author SixingWu
 *
 */
public abstract interface IDataItemable extends IConditionable {
	/**
	 * 这个元素是否满足输入的条件
	 * @param con
	 * @return
	 */
	

	
	public double getFirstKey();
	public long getRegionId();
	
	/**
	 * 如果需要保存的话，通过调用这个方法获得需要保存的内容，限制为一行
	 * @return
	 */
	public String getStoreRecords();
	
    public double[] getData();
    public String toStringLine();


}
