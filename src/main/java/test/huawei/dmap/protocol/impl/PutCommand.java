package test.huawei.dmap.protocol.impl;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import test.huawei.dmap.protocol.api.AsyncCommand;
import test.huawei.dmap.protocol.api.Command;
import test.huawei.dmap.server.api.CommandQueue;
import test.huawei.dmap.server.api.WorkerService;

import static test.huawei.dmap.common.CommandParseUtil.checkArgument;
import static test.huawei.dmap.common.CommandParseUtil.parsePutCommand;

/**
 * @author aerofeev
 * @since 1.0
 */
@Component(PutCommand.NAME)
@Scope("prototype")
public final class PutCommand extends AbstractAsyncCommand implements Command, AsyncCommand {
  public static final String NAME = "PUT";
  private final int key;
  @Autowired
  private WorkerService workerService;
  @Autowired
  private CommandQueue commandQueue;

  public PutCommand(String commandStr, ChannelHandlerContext ctx) {
    super(commandStr, ctx);
    key = parsePutCommand(commandStr).getmObj1();
  }

  @Override
  public void executeAsync() {
    workerService.sendCommandToWorker(this, getId() + " " + commandStr, key);
  }

  @Override
  public void processResponse(String response) {
    ctx.writeAndFlush(Unpooled.copiedBuffer(response, StandardCharsets.UTF_8));
  }

  @Override
  public String execute() {
    commandQueue.putCommandAndExecute(this);
    return "";
  }
}
