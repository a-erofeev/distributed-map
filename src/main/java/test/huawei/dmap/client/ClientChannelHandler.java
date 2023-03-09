package test.huawei.dmap.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import test.huawei.dmap.server.api.ClientType;

/**
 * Client channel handler on client side
 * @author aerofeev
 * @since 1.0
 */
@ChannelHandler.Sharable
@Component
public class ClientChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {
  private final static Logger LOGGER = LoggerFactory.getLogger(ClientChannelHandler.class);
  @Autowired
  private ClientNode clientNode;

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    LOGGER.debug("Client activated.");
    ctx.writeAndFlush(Unpooled.copiedBuffer(ClientType.CLIENT.getName(), CharsetUtil.UTF_8));
  }

  @Override
  public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
//    LOGGER.debug("Client channel unregistered.");
//    clientNode.exit();
  }

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
    String message = byteBuf.toString(CharsetUtil.UTF_8);
    LOGGER.debug("Client received: " + message);
    System.out.println(message);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    LOGGER.error(cause.getMessage(), cause);
    ctx.close();
  }
}
