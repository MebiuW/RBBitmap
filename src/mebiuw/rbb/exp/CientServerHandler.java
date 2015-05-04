package mebiuw.rbb.exp;

import java.net.InetAddress;

import mebiuw.rbb.fundation.protocol.IProtocol;
import mebiuw.rbb.fundation.protocol.chord.ChordMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CientServerHandler extends SimpleChannelInboundHandler<String> {
	Quener suber;
	
	public CientServerHandler(Quener suber) {
		this.suber=suber;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		// 收到消息直接打印输出
		//System.out.println(System.currentTimeMillis()+"  "+ctx.channel().remoteAddress() + " Say : " + msg);
		this.suber.sendBack(Long.parseLong(msg));
		
		// 返回客户端消息 - 我已经接收到了你的消息
		//ctx.writeAndFlush("Received your message !\n");
	}
	
	/*
	 * 
	 * 覆盖 channelActive 方法 在channel被启用的时候触发 (在建立连接的时候)
	 * 
	 * channelActive 和 channelInActive 在后面的内容中讲述，这里先不做详细的描述
	 * */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");
		
		ctx.writeAndFlush( "Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n");
		final ByteBuf time = ctx.alloc().buffer(4); // (2)
		time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
		final ChannelFuture f = ctx.writeAndFlush(time); // (3)
		
	
		super.channelActive(ctx);
	}
}
