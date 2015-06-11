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
	Channel ch=null;
	/**
	 * 初始化一个客户端 发送请求的，一旦受到后，这个请求就开始监听了
	 * @param ip IP地址
	 * @param port 端口
	 */
	public NettyClient(String ip,int port){
		Logger.Log("准备打开通往"+ip+":"+port+"的Netty通道 即将休眠30S后打开");
		this.host=ip;
		this.port=port;
		/**
		 * 打开端口
		 */
		EventLoopGroup group = new NioEventLoopGroup();
		try {
		
			Bootstrap b = new Bootstrap();
			b.group(group)
			.channel(NioSocketChannel.class)
			.handler(new NettyClientInitializer());

			// 连接服务端
			 ch = b.connect(host, port).sync().channel();

			Logger.Log("已经打开"+host+":"+port+"的Netty通道");
			
		} 
		catch(Exception e){
			Logger.Log(""+host+":"+port+"的Netty通道异常"+e+"//");
			e.printStackTrace();
		}
		
	}

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 * 
	 */
	public  void run (String[] args) throws InterruptedException, IOException {
	
		
	}
	

/**
 * 发送消息的
 * @param msg
 */
	public void sendMsg(String msg){
		
		ch.writeAndFlush(msg+ "\r\n");
	
		
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
