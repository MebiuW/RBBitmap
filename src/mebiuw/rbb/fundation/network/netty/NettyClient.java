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
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import mebiuw.rbb.exp.Logger;
public class NettyClient implements Runnable {
	 private  ReadWriteLock lock = new ReentrantReadWriteLock();  
	public  String host;
	public  int port ;
	StringBuilder tmp=null;
	/**
	 * 初始化一个客户端 发送请求的，一旦受到后，这个请求就开始监听了
	 * @param ip IP地址
	 * @param port 端口
	 */
	public NettyClient(String ip,int port){
		Logger.Log("准备打开通往"+ip+":"+port+"的Netty通道 即将休眠30S后打开");
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
		Thread.sleep(30000);
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
			.channel(NioSocketChannel.class)
			.handler(new NettyClientInitializer());

			// 连接服务端
			Channel ch = b.connect(host, port).sync().channel();

			Logger.Log("已经打开"+host+":"+port+"的Netty通道");
			for (;;) {
				synchronized(this){
				if(tmp!=null )
				ch.writeAndFlush(tmp.toString()+ "\r\n");
				tmp=null;
			
				}
			}
		} 
		catch(Exception e){
			Logger.Log(""+host+":"+port+"的Netty通道异常"+e+"//");
			e.printStackTrace();
		}
		finally {
			// The connection is closed automatically on shutdown.
			group.shutdownGracefully();
		}
	}
	

/**
 * 发送消息的
 * @param msg
 */
	public void sendMsg(String msg){
		synchronized(this){
		if(this.tmp==null){
			this.tmp=new StringBuilder("0~"+msg);
		}
		else{
			this.tmp.append("~"+msg);
		}
		}
		
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
