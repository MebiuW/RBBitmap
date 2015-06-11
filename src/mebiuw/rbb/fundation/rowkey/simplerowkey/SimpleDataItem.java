package mebiuw.rbb.fundation.rowkey.simplerowkey;

import java.util.ArrayList;
import java.util.List;

import mebiuw.rbb.fundation.rowkey.IDataItemable;
import mebiuw.rbb.fundation.sql.Condition;
import mebiuw.rbb.fundation.sql.ConditionItem;
import mebiuw.rbb.fundation.sql.ConditionType;


public class SimpleDataItem implements IDataItemable {
	
	private double[] data;
	private long regionid;
	private double firstkey;
	private String storeRecords;

	
	public SimpleDataItem(double[] data,long regionid){
		this.data=data;
		this.regionid=regionid;
		if(data.length==0)
			return ;
		StringBuilder sb=new StringBuilder(regionid+"");
		for(int i=0;i<data.length;i++)
			sb.append(","+data[i]);
		this.storeRecords=sb.toString();
	}
	
	/**
	 * 从文件中读取出来的一行"regionid,A1,A2,A3...."
	 * @param inputLine
	 */
	public SimpleDataItem(String inputLine){
	     String[] array = inputLine.split(",");
	     data = new double[array.length];
	     this.regionid=Long.parseLong(array[0]);
	     for(int i=1;i<array.length;i++){
	    	 data[i]=(Double.parseDouble(array[i]));
	     }
	     this.storeRecords=inputLine;
	}
	@Override
	public boolean isSatisfy(Condition con,int dimension) {
		// TODO Auto-generated method stub
		ConditionItem[] conditions = con.getConditions();
		for(int i=0;i<conditions.length;i++){
			try {
				//Thread.sleep(0,dimension);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(conditions[i].getType()== ConditionType.NULL)
				continue;
			if(conditions[i].getType()== ConditionType.EQUAL)
				if(this.data[i+1]!=conditions[i].getValuea())
				   return false;
			if(conditions[i].getType()== ConditionType.BETWEEN)
				if(this.data[i+1]<conditions[i].getValuea() || this.data[i+1]>conditions[i].getValueb())
				   return false;
			if(conditions[i].getType()== ConditionType.LESSOREQUAL)
				if(this.data[i+1]>conditions[i].getValuea() )
				   return false;
			if(conditions[i].getType()== ConditionType.MOREOREQUAL)
				if(this.data[i]>conditions[i].getValuea() )
				   return false;
		}
		
		return true;
	}

	@Override
	public double getFirstKey() {
		// TODO Auto-generated method stub
		return this.data[1];
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

	@Override
	public long getRegionId() {
		// TODO Auto-generated method stub
		return this.regionid;
	}

	@Override
	public String toStringLine() {
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<this.data.length;i++)
			sb.append(data[i]+",");
		return sb.toString();
	}


}
