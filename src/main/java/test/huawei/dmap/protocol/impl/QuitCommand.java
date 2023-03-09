package test.huawei.dmap.protocol.impl;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import test.huawei.dmap.protocol.api.Command;

/**
 * @author aerofeev
 * @since 1.0
 */
@Component(QuitCommand.NAME)
@Scope("prototype")
public final class QuitCommand extends AbstractCommand implements Command {
  public static final String NAME = "QUIT";
  private static final String RESULT = "BYE";

  public QuitCommand(String commandStr, ChannelHandlerContext ctx) {
    super(commandStr, ctx);
  }

  @Override
  public String execute() {
    ctx.writeAndFlush(Unpooled.copiedBuffer(RESULT, StandardCharsets.UTF_8));
    ctx.close();
    return RESULT;
  }

}
