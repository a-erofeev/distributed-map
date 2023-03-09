package test.huawei.dmap.protocol.impl;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import test.huawei.dmap.protocol.api.AsyncCommand;
import test.huawei.dmap.protocol.api.Command;
import test.huawei.dmap.server.api.Counter;

import java.util.Objects;

/**
 * Base implementation of command
 * @author aerofeev
 * @since 1.0
 */
@Component
public abstract class AbstractCommand implements Command {
  protected final String commandStr;
  protected final ChannelHandlerContext ctx;

  public AbstractCommand(String commandStr, ChannelHandlerContext ctx) {
    Objects.requireNonNull(ctx, "[ctx] must be not null");
    Objects.requireNonNull(commandStr, "[commandStr] must be not null");
    this.commandStr = commandStr;
    this.ctx = ctx;
  }

  @Override
  public String getCommandString() {
    return commandStr;
  }

}
