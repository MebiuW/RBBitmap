package mebiuw.rbb.fundation.sql;

import java.util.List;

/**
 * 分析SQL语句
 * @author SixingWu
 * SELECT * WHERE XXXX
 */

public class SQLAnalysis {
	public List<String> parameter;
	public void preProcess(String str){
		//分析是那种类型的数据
		if(str.indexOf(SQLDictionary.INSERT)==0){
			this.processInsert(str);
		}
		else if(str.indexOf(SQLDictionary.UPDATE)==0){
			this.processInsert(str);
		}
		else if(str.indexOf(SQLDictionary.SELECT)==0){
			this.processSelect(str);
		}

}
	private void processSelect(String str) {
		// TODO Auto-generated method stub
		
	}
	private void processInsert(String str) {
		// TODO Auto-generated method stub
		
	}
}
