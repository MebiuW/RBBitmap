package mebiuw.rbb.dundation.rowkey.simplerowkey;

import java.io.IOException;

import mebiuw.rbb.dundation.rowkey.IDataItemable;

public class TestRowkey {

	public static void main(String[] args) throws UnexceptedFormart, IOException {
		// TODO Auto-generated method stub
		SimpleListRowkey sl=new SimpleListRowkey("G:\\Datas\\ZDG\\ID2\\Test.txt");
		double data[]={1,2,3,4,5};
		IDataItemable datas=new SimpleDataItem(data);
		sl.insertOrUpdate(datas);
		sl.save();
	}

}
