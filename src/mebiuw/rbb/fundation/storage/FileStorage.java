package mebiuw.rbb.fundation.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 改进：http://www.cnblogs.com/yezhenhan/archive/2012/09/10/2678690.html
 * 
 * @author MebiuW
 *
 */
public class FileStorage {
	private File file;

	/**
	 * 构造函数时必须要指明文件地址，兵器验证
	 * 
	 * @param pathname
	 */
	public FileStorage(String pathname) {
		file = new File(pathname);
		if (!file.exists()) {
			try {
				File dic=new File(file.getParent());
				if (!dic.isDirectory() && !dic.exists()) {
					System.out.println("//不存在");
					dic.mkdir();
				} 
					file.createNewFile();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public List<String> readAllLines() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));
		List<String> tmps = new ArrayList<String>();
		String data;
		while ((data = br.readLine()) != null)
			tmps.add(data);
		br.close();
		return tmps;
	}

	public List<String> readAllwords() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));
		List<String> tmps = new ArrayList<String>();
		String data;
		while ((data = br.readLine()) != null)
			tmps.add(data);
		br.close();
		return tmps;
	}

	/**
	 * 
	 * @param str
	 *            完整的写一个文件
	 * @return
	 * @throws IOException
	 */
	public boolean writeToFile(String str) throws IOException {
		FileOutputStream out = new FileOutputStream(this.file);
		out.write(str.getBytes());
		out.close();
		return true;
	}

	/**
	 * 按行写入
	 * 
	 * @param strs
	 * @return
	 * @throws IOException
	 */
	public boolean writeToFile(List<String> strs) throws IOException {
		FileOutputStream out = new FileOutputStream(this.file);
		Iterator<String> it = strs.iterator();
		while (it.hasNext())
			out.write((it.next() + "\r\n").getBytes());
		out.close();
		return true;
	}

	/**
	 * 关闭写回文件必须的
	 */
	public void close() {
		
	}

}
