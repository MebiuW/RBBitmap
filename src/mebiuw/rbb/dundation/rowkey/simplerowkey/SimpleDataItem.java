package mebiuw.rbb.dundation.rowkey.simplerowkey;

import java.util.ArrayList;
import java.util.List;

import mebiuw.rbb.dundation.rowkey.IDataItemable;
import mebiuw.rbb.fundation.sql.Condition;
import mebiuw.rbb.fundation.sql.Condition.ConditionItem;
import mebiuw.rbb.fundation.sql.ConditionStates;


public class SimpleDataItem implements IDataItemable {
	
	private double[] data;
	private long regionid;
	private double firstkey;
	private String storeRecords;

	
	public SimpleDataItem(double[] data){
		this.data=data;
		if(data.length==0)
			return ;
		StringBuilder sb=new StringBuilder(data[0]+"");
		for(int i=1;i<data.length;i++)
			sb.append(","+data[i]);
		this.storeRecords=sb.toString();
	}
	
	/**
	 * 从文件中读取出来的一行"A1,A2,A3...."
	 * @param inputLine
	 */
	public SimpleDataItem(String inputLine){
	     String[] array = inputLine.split(",");
	     data = new double[array.length];
	     for(int i=0;i<array.length;i++){
	    	 data[i]=(Double.parseDouble(array[i]));
	     }
	     this.storeRecords=inputLine;
	}
	@Override
	public boolean isSatisfy(Condition con) {
		// TODO Auto-generated method stub
		ConditionItem[] conditions = con.getConditions();
		for(int i=0;i<conditions.length;i++){
			if(conditions[i].getType()== ConditionStates.NULL)
				continue;
			if(conditions[i].getType()== ConditionStates.EQUAL)
				if(this.data[i]!=conditions[i].getValuea())
				   return false;
			if(conditions[i].getType()== ConditionStates.BETWEEN)
				if(this.data[i]<conditions[i].getValuea() || this.data[i]>conditions[i].getValueb())
				   return false;
			if(conditions[i].getType()== ConditionStates.LESSOREQUAL)
				if(this.data[i]<conditions[i].getValuea() )
				   return false;
			if(conditions[i].getType()== ConditionStates.MOREOREQUAL)
				if(this.data[i]>conditions[i].getValuea() )
				   return false;
		}
		
		return true;
	}

	@Override
	public double getFirstKey() {
		// TODO Auto-generated method stub
		return this.firstkey;
	}
	@Override
	public String getStoreRecords() {
		// TODO Auto-generated method stub
		return this.storeRecords;
	}

	@Override
	public double[] getData() {
		return this.data;
	}


}
