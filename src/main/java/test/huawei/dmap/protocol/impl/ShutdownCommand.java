package test.huawei.dmap.protocol.impl;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import test.huawei.dmap.protocol.api.Command;
import test.huawei.dmap.server.MasterNodeServer;

/**
 * @author aerofeev
 * @since 1.0
 */
@Component(ShutdownCommand.NAME)
@Scope("prototype")
public final class ShutdownCommand extends AbstractCommand implements Command {
  public static final String NAME = "SHUTDOWN";
  @Autowired
  private MasterNodeServer server;

  public ShutdownCommand(String commandStr, ChannelHandlerContext ctx) {
    super(commandStr, ctx);
  }

  @Override
  public String execute() {
    try {
      ctx.writeAndFlush(Unpooled.copiedBuffer("BYE", StandardCharsets.UTF_8));
      ctx.close();
      server.shutdown();
    } catch (InterruptedException e) {
      return "Shutdown interrupted";
    }
    return "Shutdown executed";
  }

}
