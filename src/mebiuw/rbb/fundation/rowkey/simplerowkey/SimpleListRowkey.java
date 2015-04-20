package mebiuw.rbb.fundation.rowkey.simplerowkey;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mebiuw.rbb.fundation.rowkey.IDataItemable;
import mebiuw.rbb.fundation.rowkey.IRowkeyable;
import mebiuw.rbb.fundation.sql.Condition;
import mebiuw.rbb.fundation.storage.FileStorage;

public class SimpleListRowkey implements IRowkeyable {
	private FileStorage file;
	private List<IDataItemable> datas;
	private int numberOfParameters;

	public SimpleListRowkey(String filePosition) throws UnexceptedFormart {
		this.file = new FileStorage(filePosition);
		this.datas = new ArrayList<IDataItemable>();
		int numberOfLines;
		// 装载文件
		try {
			List<String> words = file.readAllwords();
			Iterator<String> it = words.iterator();
			try{
			 numberOfParameters = Integer.parseInt(it.next());
			 numberOfLines = Integer.parseInt(it.next());
			}
			catch(Exception e){
				//视为没创建过的文件 所以这样做
				numberOfParameters=0;
				numberOfLines=0;
			}
			for (int i = 0; i < numberOfLines; i++) {
				String nextline = it.next();
				SimpleDataItem item = new SimpleDataItem(nextline);
				this.datas.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnexceptedFormart();
		}

	}

	@Override
	public boolean insertOrUpdate(long id, IDataItemable value) {
		// TODO Auto-generated method stub
		this.datas.add(value);
		if(this.numberOfParameters<value.getData().length)
			this.numberOfParameters=value.getData().length;
		return false;
	}

	@Override
	public boolean insertOrUpdate(IDataItemable value) {
		// TODO Auto-generated method stub
		this.datas.add(value);
		if(this.numberOfParameters<value.getData().length)
			this.numberOfParameters=value.getData().length;
		return true;
	}

	@Override
	public void delete(Condition con) {
		// TODO Auto-generated method stub

	}

	/**
	 * 单点
	 */
	@Override
	public IDataItemable query(Condition con) {
		// TODO Auto-generated method stub
		Iterator<IDataItemable> it = this.datas.iterator();
		// 1 Tuple Result
		while (it.hasNext()) {
			IDataItemable item = it.next();
			if (item.isSatisfy(con))
				return item;
		}
		return null;
	}

	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public IDataItemable query(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRowkeyable init(String fileposition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean save() throws IOException {
		StringBuilder sb = new StringBuilder();
		Iterator<IDataItemable> it = this.datas.iterator();
		sb.append(numberOfParameters+"\r\n");
		sb.append(this.datas.size());
		while (it.hasNext()) {
			IDataItemable entry = it.next();
			sb.append("\r\n"+entry.getStoreRecords());
		}
		this.file.writeToFile(sb.toString());
		return false;
	}

	@Override
	public boolean release() {
		// TODO Auto-generated method stub
		return false;
	}

}
