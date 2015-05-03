package mebiuw.rbb.fundation.bitstring.simple;

import java.util.List;
import java.util.Random;

import mebiuw.rbb.fundation.bitstring.IConfiguration;
import mebiuw.rbb.fundation.rowkey.IDataItemable;
import mebiuw.rbb.fundation.rowkey.simplerowkey.SimpleDataItem;
import mebiuw.rbb.fundation.storage.FileStorage;
/**
 * 这个需要读取文件，分区方式以当前的0到最大值为全部数据域，以Phase为阶段进行判断
 * @author MebiuW
 *
 */

public class SimpleConfiguration implements IConfiguration{
	private String configurationFilePath;
	private int attrinum;
	/**
	 * 占用的Bit位 
	 */
	private int[] lengthOfAttribute;
	private long[] maxOfEachAttribute;
	private long[] phaseOfEachAttribute;
	private SimpleBitString sbs;
	
	/**
	 * 提供文件地址后就可以得到
	 * @param configurationFilePath
	 * @throws Exception 
	 */
	public SimpleConfiguration(String configurationFilePath) throws Exception {
		super();
		this.configurationFilePath = configurationFilePath;
		/**
		 * 先说属性数量，然后按照属性的顺序
		 * （第i个的最大值，每个分区多少）
		 */
		FileStorage file=new FileStorage(this.configurationFilePath);
		List<String> params = file.readAllLines();
		this.attrinum=Integer.parseInt(params.get(0));
		this.phaseOfEachAttribute=new long[this.attrinum];
		this.maxOfEachAttribute=new long[this.attrinum];
		this.lengthOfAttribute=new int[this.attrinum];
		for(int i=0;i<this.attrinum;i++){
			String attribute=params.get(i+1);
			this.phaseOfEachAttribute[i]=Long.parseLong(attribute.substring(attribute.indexOf(' ')+1));
			attribute=attribute.substring(0,attribute.indexOf(' '));
			this.maxOfEachAttribute[i]=Long.parseLong(attribute);
			this.lengthOfAttribute[i]=(int) Math.round(0.5+Math.log(this.maxOfEachAttribute[i]/this.phaseOfEachAttribute[i]+2)/Math.log(2));
		}
		/**
		 * 配置BitString的生成；
		 */
		this.sbs=new SimpleBitString(this);
		
	}

	/**
	 * 根据属性值返回当前的对应属性的大小
	 * @param attributeIndex 第几个属性
	 * @param value 值
	 * @return 当前value在这个配置下的分区排序 -1代表无效的值
	 */
	@Override
	public int getAttributeIndex(int attributeIndex, double value) {
		if(value<0 || value>this.maxOfEachAttribute[attributeIndex])
			return -1;//invalid 
		return (int) (value/this.phaseOfEachAttribute[attributeIndex]);
	}

	@Override
	public int getAttributeNum() {
		return attrinum;
	}

	@Override
	public int getBitLengthOfAttribute(int index) {
		return lengthOfAttribute[index];
	}

	@Override
	public double getRandomValue(int index) {
		Random ran=new Random(System.nanoTime());
		return ran.nextDouble()*this.maxOfEachAttribute[index];
	}

	@Override
	public IDataItemable getRandomDataItem() {
		Random ran=new Random(System.nanoTime());
		double result[]=new double[this.attrinum];
		for(int i=0;i<this.attrinum;i++){
			result[i]=ran.nextDouble()*this.maxOfEachAttribute[i];
		}
	
		IDataItemable item=new SimpleDataItem(result,sbs.computeRegionId(result));
		return item;
	}

}
