package mebiuw.rbb.fundation.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 用于管理文件，做到缓存的作用 FIFO的管理
 * 
 * @author MebiuW
 *
 */
public class FileCachePool {
	private HashMap<Integer, FileStorage> mapper;
	/**
	 * 修改位
	 */
	private HashMap<Integer, Boolean> using;
	private int max, empty, used;
	private List<Integer> fifoList;

	/**
	 * 设定最大容量，然后初始化
	 * 
	 * @param max
	 *            最大容量
	 */
	public FileCachePool(int max) {
		this.max = max;
		this.used = 0;
		this.empty = max;
		this.mapper = new HashMap<Integer, FileStorage>();
		this.fifoList = new ArrayList<Integer>();
		this.using = new HashMap<Integer, Boolean>();
	}

	/**
	 * 获得文件对象
	 * 
	 * @param rowid
	 * @return
	 */
	public synchronized FileStorage getFileByRowId(int rowid) {
		FileStorage result = null;
		this.using.put(rowid, true);
		if (this.mapper.containsKey(rowid))
			result = mapper.get(rowid);
		else {
			if (empty == 0) {
				//看有没有不是未在使用中的
				int index=0;
				while(index<this.fifoList.size() && this.using.get(this.fifoList.get(index)))
					index++;
				//无法释放和分配
				if(index>=this.fifoList.size())
					return null;
				
				empty--;
				used++;
				this.mapper.get(this.fifoList.get(0)).close();
				this.mapper.remove(this.fifoList.get(0));
				this.using.remove(this.fifoList.get(0));
				this.fifoList.remove(0);
				}
			}
			FileStorage fs = new FileStorage("url");
			this.mapper.put(rowid, fs);
			this.fifoList.add(rowid);
			result = fs;

		
		return result;

	}
/**
 * 告知已经不适用了
 * @param rowid
 */
		public synchronized void setUnuseNow(int rowid) {
			
			this.using.put(rowid, false);

	}

}
