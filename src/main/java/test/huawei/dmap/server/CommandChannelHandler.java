package test.huawei.dmap.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import test.huawei.dmap.protocol.api.CommandInterpreter;

import java.nio.charset.StandardCharsets;

/**
 * @author aerofeev
 * @since 1.0
 */
@Component
@Scope("prototype")
public class CommandChannelHandler extends ChannelInboundHandlerAdapter {
  private final static Logger LOGGER = LoggerFactory.getLogger(CommandChannelHandler.class);
  @Autowired
  private CommandInterpreter commandInterpreter;
  @Autowired
  private ChannelGroup group;

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    group.add(ctx.channel());
    super.channelActive(ctx);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    var in = (ByteBuf) msg;
    var commandStr = in.toString(CharsetUtil.UTF_8);
    var response = commandInterpreter.parseAndExecuteCommand(commandStr, ctx);
    if (!ctx.isRemoved()) {
      ctx.writeAndFlush(Unpooled.copiedBuffer(response, StandardCharsets.UTF_8));
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    LOGGER.error(cause.getMessage(), cause);
    ctx.close();
  }
}
