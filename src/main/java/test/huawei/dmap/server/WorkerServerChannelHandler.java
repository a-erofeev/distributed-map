package test.huawei.dmap.server;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import test.huawei.dmap.server.api.CommandQueue;
import test.huawei.dmap.server.api.WorkerService;

import static test.huawei.dmap.common.CommandParseUtil.*;

/**
 * @author aerofeev
 * @since 9.0.28
 */
@Component
@Scope("prototype")
public class WorkerServerChannelHandler extends ChannelInboundHandlerAdapter {
  private static final Logger LOGGER = LoggerFactory.getLogger(WorkerServerChannelHandler.class);
  @Autowired
  private WorkerService workerService;
  @Autowired
  private CommandQueue commandQueue;

  @Override
  public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
    LOGGER.debug("Worker channel '{}' unregistered", ctx.name());
    workerService.unregisterWorker(ctx.name());
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    String response = ((ByteBuf)msg).toString(StandardCharsets.UTF_8);
    var commandParts = getIdAndCommand(response);
    commandQueue.processResponseAndRemoveCommand(commandParts.getmObj1(), commandParts.getmObj2());
  }

}
