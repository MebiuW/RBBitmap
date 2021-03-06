package mebiuw.rbb.fundation.rowkey.simplerowkey;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mebiuw.btree.LinkedBplusTree;

import mebiuw.rbb.fundation.rowkey.IDataItemable;
import mebiuw.rbb.fundation.rowkey.IRowkeyable;
import mebiuw.rbb.fundation.sql.Condition;
import mebiuw.rbb.fundation.storage.FileStorage;

public class SimpleListRowkey implements IRowkeyable {
	private FileStorage file;
	private List<IDataItemable> datas;
	private LinkedBplusTree btree;
	private int numberOfParameters;
	private String filePosition;
	
	
	/**
	 * 全新创建的一个
	 * @param dimension
	 * @param filePosition
	 */
	public SimpleListRowkey(int dimension,String filePosition){
		this.datas=new ArrayList<IDataItemable>();
		this.filePosition=filePosition;
		this.numberOfParameters=dimension;
		this.file = new FileStorage(filePosition);
		this.btree=new LinkedBplusTree(4);
		
		
	}
	/**
	 * 从文件地址加载加载方式加载的
	 * @param filePosition
	 * @throws UnexceptedFormart
	 */
	public SimpleListRowkey(String filePosition) throws UnexceptedFormart {
		this.file = new FileStorage(filePosition);
		this.datas = new ArrayList<IDataItemable>();
		this.btree=new LinkedBplusTree(4);
		this.filePosition=filePosition;
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
				e.printStackTrace();
				numberOfParameters=0;
				numberOfLines=0;
			}
			for (int i = 0; i < numberOfLines*1; i++) {
				String nextline = it.next();
				SimpleDataItem item = new SimpleDataItem(nextline);
				this.datas.add(item);
				this.btree.insertOrUpdate(item.getFirstKey(), item);
			}
			System.out.println("装载完成");
			this.file.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnexceptedFormart();
		}

	}

	@Override
	public boolean insertOrUpdate(long id, IDataItemable value ) {
		// TODO Auto-generated method stub
		this.datas.add(value);
		this.btree.insertOrUpdate(value.getFirstKey(), (SimpleDataItem) value);
		if(this.numberOfParameters<value.getData().length)
			this.numberOfParameters=value.getData().length;
		return false;
	}

	@Override
	public boolean insertOrUpdate(IDataItemable value) {
		// TODO Auto-generated method stub
		this.datas.add(value);
		this.btree.insertOrUpdate(value.getFirstKey(), (SimpleDataItem) value);
		Iterator<IDataItemable> it = this.datas.iterator();
		while(it.hasNext()){
	
			if(value.equals(it.next()))
				break;
		}
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
		this.btree.sleep();
		Iterator<SimpleDataItem> it = this.btree.getLinkedList(con.conditions[0].getValuea()).iterator();
		// 1 Tuple Result
		   try {
		while (it.hasNext()) {
			
				//	Thread.sleep(1);
				
			IDataItemable item = it.next();
			if (item.isSatisfy(con,con.getConditions().length))
			{
				System.out.println("Get it");
				return item;
			}
			else{
				// System.out.println(con.toString()+"  "+item.toStringLine());
			}
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   System.out.println("Get Faild");
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
	@Override
	public void queryAll(Condition con) {
		this.btree.sleep();
		List<IDataItemable> tmp = datas;
		if(tmp!=null){
			
			Iterator<IDataItemable> it = tmp.iterator();
		   try {
			   int count=0;
		while (it.hasNext()) {
			count++;

			IDataItemable item = it.next();
		
			item.isSatisfy(con,con.getConditions().length);
		

		}
		System.out.println(count);
		Thread.sleep(2*count);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	}

}
