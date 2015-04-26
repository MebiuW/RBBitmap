package mebiuw.rbb.fundation.network.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
public class NettyClient implements Runnable {
	
	public  String host;
	public  int port = 7878;
	private Queue<String> quene;
	/**
	 * 初始化一个客户端 发送请求的，一旦受到后，这个请求就开始监听了
	 * @param ip IP地址
	 * @param port 端口
	 */
	public NettyClient(String ip,int port){
		this.quene=new LinkedList<String>();
		this.host=ip;
		this.port=port;
		Thread t=new Thread(this);
		t.start();
	}

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 * 
	 */
	public  void run (String[] args) throws InterruptedException, IOException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
			.channel(NioSocketChannel.class)
			.handler(new NettyClientInitializer());

			// 连接服务端
			Channel ch = b.connect(host, port).sync().channel();
			
			// 控制台输入
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			int i=0;
			for (;;) {
				
				/*
				 * 向服务端发送在控制台输入的文本 并用"\r\n"结尾
				 * 之所以用\r\n结尾 是因为我们在handler中添加了 DelimiterBasedFrameDecoder 帧解码。
				 * */
				while(this.quene.isEmpty())
				{
				
					Thread.sleep(1000);
					//System.out.println("No datas  "+this.quene.hashCode());
				}
				ch.writeAndFlush(this.quene.poll()+ "\r\n");
			}
		} finally {
			// The connection is closed automatically on shutdown.
			group.shutdownGracefully();
		}
	}
	

/**
 * 发送消息的
 * @param msg
 */
	public void sendMsg(String msg){
		this.quene.add(msg);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.run(null);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
