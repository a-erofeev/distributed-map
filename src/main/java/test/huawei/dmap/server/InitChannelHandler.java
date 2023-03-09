package test.huawei.dmap.server;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import test.huawei.dmap.server.api.ClientType;
import test.huawei.dmap.server.api.Worker;
import test.huawei.dmap.server.api.WorkerService;

/**
 * @author aerofeev
 * @since 9.0.28
 */
public class InitChannelHandler extends ChannelInboundHandlerAdapter {
  private final static Logger LOGGER = LoggerFactory.getLogger(InitChannelHandler.class);
  private static final String HELLO_CLIENT = "HELLO, CLIENT";
  @Autowired
  private WorkerService workerService;
  @Autowired
  private Function<ChannelHandlerContext, Worker> workerFactory;
  @Autowired
  private ObjectProvider<WorkerServerChannelHandler> workerServerChannelHandlerProvider;
  @Autowired
  private ObjectProvider<CommandChannelHandler> commandChannelHandlerProvider;
  private final long id;

  public InitChannelHandler(long id) {
    this.id = id;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    LOGGER.debug("Read channel: " + id);
    var in = (ByteBuf) msg;
    var commandStr = in.toString(CharsetUtil.UTF_8);
    LOGGER.debug("Server received: " + commandStr);
    if (commandStr.equalsIgnoreCase(ClientType.WORKER.getName())) {
      ctx.pipeline().addLast(workerServerChannelHandlerProvider.getObject());
      workerService.registerWorker(workerFactory.apply(ctx));
    } else if (commandStr.equalsIgnoreCase(ClientType.CLIENT.getName())) {
      ctx.writeAndFlush(Unpooled.copiedBuffer(HELLO_CLIENT, StandardCharsets.UTF_8));
      ctx.pipeline().addLast(commandChannelHandlerProvider.getObject());
    } else {
      super.channelRead(ctx, msg);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    LOGGER.error(cause.getMessage(), cause);
    ctx.close();
  }
}
