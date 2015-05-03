package mebiuw.rbb.fundation.network.netty;



import mebiuw.rbb.exp.Logger;
import mebiuw.rbb.fundation.protocol.IProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer implements Runnable {
	private int portNumber;
	private IProtocol suber;
	


	public NettyServer(int portNumber, IProtocol suber) {
		super();
		this.portNumber = portNumber;
		this.suber = suber;
		
	}



	@Override
	public void run() {
		try {
			Logger.Log("本机监听端口"+portNumber);
			EventLoopGroup bossGroup = new NioEventLoopGroup();
			EventLoopGroup workerGroup = new NioEventLoopGroup();
			try {
				ServerBootstrap b = new ServerBootstrap();
				b.group(bossGroup, workerGroup);
				b.channel(NioServerSocketChannel.class);
				b.childHandler(new NettyServerInitializer( suber));

				// 服务器绑定端口监听
				ChannelFuture f = b.bind(portNumber).sync();
				// 监听服务器关闭监听
				f.channel().closeFuture().sync();

				// 可以简写为
				/* b.bind(portNumber).sync().channel().closeFuture().sync(); */
			} finally {
				bossGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Logger.Log("本机监听端口"+portNumber+"异常");
			e.printStackTrace();
		}
		
	}
}
