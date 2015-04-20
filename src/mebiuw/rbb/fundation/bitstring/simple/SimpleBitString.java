package mebiuw.rbb.fundation.bitstring.simple;

import java.util.List;

import mebiuw.rbb.fundation.bitstring.IBitString;
import mebiuw.rbb.fundation.bitstring.IConfiguration;

/**
 * 最基本的适应于double属性的BitString
 * @author MebiuW
 *
 */

public abstract class SimpleBitString implements IBitString {
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
	public SimpleBitString(IConfiguration config,List<Double> metaData){
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

}
