package mebiuw.rbb.fundation.rowkey;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mebiuw.rbb.fundation.sql.Condition;


public interface IRowkeyable {

/**
 * 已有数据返回fals表示未插入 true表示新插入
 * @throws InterruptedException 
 */
/*
 * 基本操作策略
 */
	public boolean insertOrUpdate(long id, IDataItemable value);
	public boolean insertOrUpdate( IDataItemable value);
	public void delete(Condition con) ;
	public IDataItemable query(Condition con);
	public void delete(long id) ;
	public IDataItemable query(long id);
	
/*
 * 文件加载和释放
 */
	/**
	 * 设定文件地址，从那里读取信息到内存
	 * @param fileposition
	 * @return null代表事变
	 */
	public IRowkeyable init(String fileposition);
	public boolean save() throws IOException;
	public boolean release();
	public void queryAll(Condition con);
	
	

}
