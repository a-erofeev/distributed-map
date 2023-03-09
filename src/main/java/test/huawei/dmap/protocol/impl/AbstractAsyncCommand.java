package test.huawei.dmap.protocol.impl;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import test.huawei.dmap.protocol.api.AsyncCommand;
import test.huawei.dmap.server.api.Counter;

/**
 * Base implementation of AsyncCommand
 * @author aerofeev
 * @since 1.0
 */
public abstract class AbstractAsyncCommand extends AbstractCommand implements AsyncCommand {
  private long id;
  private Counter commandCounter;

  @Autowired
  @Qualifier("Command")
  public void setCommandCounter(Counter commandCounter) {
    this.commandCounter = commandCounter;
    this.id = commandCounter.getAndIncrement();
  }

  public AbstractAsyncCommand(String commandStr, ChannelHandlerContext ctx) {
    super(commandStr, ctx);
  }

  @Override
  public long getId() {
    return id;
  }
}
