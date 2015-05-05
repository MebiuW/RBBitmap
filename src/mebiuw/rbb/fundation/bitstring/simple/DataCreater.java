package mebiuw.rbb.fundation.bitstring.simple;

import java.util.ArrayList;
import java.util.List;

import mebiuw.rbb.fundation.rowkey.IDataItemable;
import mebiuw.rbb.fundation.rowkey.simplerowkey.SimpleDataItem;
import mebiuw.rbb.fundation.storage.FileStorage;

public class DataCreater {
	public static void main(String args[]) throws Exception {
		SimpleConfiguration sc = new SimpleConfiguration(
				"G:\\Datas\\RBB\\BT\\bitConfig.txt");
		System.out.println(sc.getRandomDataItem().getStoreRecords());
		// 分别生成5000*12=30000个数据后以此保存
		List<List<IDataItemable>> array = new ArrayList<List<IDataItemable>>();
		for (int i = 0; i < 12; i++)
			array.add(new ArrayList<IDataItemable>());
		for (int i = 0; i < 12; i++) {
			IDataItemable tmp;
			while (array.get(i).size() < 5000) {
				tmp = sc.getRandomDataItem();
				if (tmp.getRegionId()%12 == i) {
					array.get(i).add(tmp);
				}
			}
			System.out.println("完成一个");
		}
		FileStorage db=new FileStorage("G:\\Datas\\RBB\\BT\\db3.txt");
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<5000;i++){
			for(int j=0;j<12;j++){
				sb.append(array.get(j).get(i).getStoreRecords()+"\n");
			}
		}
		db.writeToFile(sb.toString());

	}

}
