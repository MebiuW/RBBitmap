package mebiuw.rbb.fundation.network.netty;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
/**
 * 用来处理客户端受到服务器端的消息的
 * @author MebiuW
 *
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<String> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		
		//System.out.println("Server say : " + msg);
	}
	
	

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Client active ");
		super.channelActive(ctx);
	}



	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Client close ");
		//super.channelInactive(ctx);
	}
}
