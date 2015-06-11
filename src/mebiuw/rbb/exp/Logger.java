package mebiuw.rbb.exp;

import java.util.HashMap;
import java.util.Iterator;

public class Logger {
	static HashMap<Long,Long> map=new HashMap<Long,Long>();
	public static void Log(String str){
		System.out.println(str);
	}
	public static void Count(){
		long time=System.currentTimeMillis()%1000;
		if(map.containsKey(time))
			map.put(time, map.get(time)+1);
			else map.put(time, (long) 0);
		Iterator<Long> it = map.keySet().iterator();
		while(it.hasNext()){
			long key=it.next();
			//System.out.println(key+"  "+map.get(key));
		}
		System.out.println("##");
	}

}
