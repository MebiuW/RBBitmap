package mebiuw.rbb.dundation.rowkey.simplerowkey;

public class UnexceptedFormart extends Exception {

	@Override
	public void printStackTrace() {
		// TODO Auto-generated method stub
		System.out.println("文件块格式不正确或格式异常");
		super.printStackTrace();
	}
	

}
