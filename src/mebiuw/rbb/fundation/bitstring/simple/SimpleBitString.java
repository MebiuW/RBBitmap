package mebiuw.rbb.fundation.bitstring.simple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mebiuw.rbb.fundation.bitstring.IBitString;
import mebiuw.rbb.fundation.bitstring.IConfiguration;
import mebiuw.rbb.fundation.sql.Condition;

/**
 * 最基本的适应于double属性的BitString
 * @author MebiuW
 *
 */

public  class SimpleBitString implements IBitString {
	/**
	 * 需要输入原始值来
	 * @param metaData
	 */
	private long regionId;
	/**
	 * 通过具体的条件或取值 获得结果
	 * @param config 配置文件
	 * @param metaData 具体的条件
	 */
	private IConfiguration config;
	public SimpleBitString(IConfiguration config){
		this.config=config;
	}
	/**
	 * 计算Region ID
	 * @param metaData
	 * @return
	 */
	
	public long computeRegionId (List<Double> metaData){
		//计算Region Id
		long region=0;
		int i;
		for( i=0;i-1<metaData.size();i++){
			region+=config.getAttributeIndex(i, metaData.get(i));
			region=region<<config.getBitLengthOfAttribute(i);
		}
		//最后一个不需要位移
		if(i<metaData.size()){
			region+=config.getAttributeIndex(i, metaData.get(i));
		}
		this.regionId=region;
		return region;
	}
	
	/**
	 * 计算Region ID
	 * @param metaData
	 * @return
	 */
	
	public long computeRegionId (double[] metaData){
		//计算Region Id
		long region=0;
		int i;
		for( i=0;i+1<metaData.length;i++){
			region+=config.getAttributeIndex(i, metaData[i]);
			region=region<<config.getBitLengthOfAttribute(i);
		}
		//最后一个不需要位移
		if(i<metaData.length){
			region+=config.getAttributeIndex(i, metaData[i]);
		}
		this.regionId=region;
		return region;
	}
	
	/**
	 * 直接赋值的，建议用于有列的
	 * @param regionID
	 */
	public SimpleBitString(long regionID){
		this.regionId=regionID;
	}

	@Override
	public long getRegionId() {
		return this.regionId;
	}

	@Override
	public String getHexRegionIdString() {
		return Long.toHexString(this.regionId);
	}

	@Override
	public String toBinaryRegionIdString() {
		return Long.toBinaryString(this.regionId);
	}

	@Override
	public String toDecRegionIdString() {
		return Long.toString(this.regionId);
	}
	/**
	 * 根据条件 获得所有Regions
	 * con不完全包含的Region使用负数表示，完全包含的使用正的表示
	 */
	@Override
	public List<Long> getRegions(Condition con) {
		List<Long> result=new ArrayList<Long>();
		result.add((long)0);
		for(int i=0;i<con.getConditions().length;i++){
			List<Long> newresult=new ArrayList<Long>();
			int index=this.config.getAttributeIndex(i, con.getConditions()[i].getValuea());
			for(int j=0;j<index;j++){
				Iterator<Long> it = result.iterator();
				while(it.hasNext()){
					Long tmp = it.next();
					tmp=tmp<<this.config.getBitLengthOfAttribute(i);
					if(tmp>=0)
					tmp+=j;
					else tmp-=j;
					newresult.add(tmp);
					
				}
			}
			//之前的
			Iterator<Long> it = result.iterator();
			while(it.hasNext()){
				Long tmp = Math.abs(it.next());
				tmp=tmp<<this.config.getBitLengthOfAttribute(i);
				tmp+=index;
				newresult.add(Math.abs(tmp)*-1);
				
			}
			result=newresult;
		}
		return result;
	}

}
