package mebiuw.rbb.fundation.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

import mebiuw.rbb.fundation.protocol.AddressItem;



public class MessageClient {
	private Socket socket;
	private BufferedReader in;
	private BufferedWriter out;
	public static int count = 0;
	private String address;
	private int port;
	//private static Logger logger = Logger.getLogger(MessageClient.class); 
	public MessageClient(String address, int port) {
		this.address = address;                                                       
		this.port = port;
	}
	/**
	 * 自动随机端口的
	 */
	public MessageClient(AddressItem ai) {
		this.address = ai.getIp();
		Random ran=new Random(System.currentTimeMillis());
		this.port = ai.getPorts().get(ai.getPorts().size());
	}
	/**
	 * 接受文本的
	 * @param msg
	 */
	public void sendMessage(String msg) {
		try {
			socket = new Socket(address, port);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream(),"UTF-8"));
			out =  new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
			out.write(msg);

		} catch (Exception e) {

		} finally {
			
			try {
				out.close();
				in.close();
				socket.close();
			} catch (Exception e) {
				logger.error("关闭消息发送socket失败",e);
			}
		}

	}
	public void test(int times){
		long time=System.currentTimeMillis();
		for(int i=0;i<times;i++){
			Random ran=new Random(System.nanoTime());
		
		    String msg="LOCATE#INSERT#"+ran.nextInt(1000)+","+ran.nextInt(1000)+","+ran.nextInt(1000);
			this.sendMessage(msg,7878);
	
		}
		time-=System.currentTimeMillis();
		System.out.println(time+"  "+(times/(time+0.0)*1000));
		
	}
}