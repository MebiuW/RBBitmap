package mebiuw.rbb.fundation.bitstring.simple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import mebiuw.rbb.fundation.sql.Condition;
import mebiuw.rbb.fundation.storage.FileStorage;

public class RangeCreater {

	public static void main(String[] args) throws Exception {
		getMessage();

	}
	
	public static String getMessage() throws Exception{
		Random ran=new Random(System.currentTimeMillis());
		FileStorage db=new FileStorage("/Users/MebiuW/Documents/TMP/RBB/rdb.txt");
		List<String> datas=new ArrayList<String>();
		SimpleConfiguration sc = new SimpleConfiguration(
				"/Users/MebiuW/Documents/TMP/RBB/bitConfig.txt");
		for(int i=0;i<20000;i++){
		Condition con = sc.getRandomRange((int) (ran.nextDouble()*2000+2000));
		StringBuilder sb=new StringBuilder();
		SimpleBitString sbs=new SimpleBitString(sc);
		List<Long> rs = sbs.getRegions(con);
		sb.append(rs.size());
		Iterator<Long> it = rs.iterator();
		while(it.hasNext()){
			sb.append("*"+it.next());
		}
		sb.append("*"+con.toMessageEntry());
		datas.add(sb.toString());
		}
		db.writeToFile(datas);
		return "" ;
		
	}

}
