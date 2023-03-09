package test.huawei.dmap.server.impl;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import test.huawei.dmap.server.api.Worker;

import java.nio.charset.StandardCharsets;

/**
 * @author aerofeev
 * @since 1.0
 */
public class WorkerImpl implements Worker {
  private final ChannelHandlerContext ctx;

  public WorkerImpl(ChannelHandlerContext ctx) {
    this.ctx = ctx;
  }

  @Override
  public ChannelFuture sendCommand(String commandStr) {
    return ctx.writeAndFlush(Unpooled.copiedBuffer(commandStr, StandardCharsets.UTF_8));
  }

  @Override
  public String getName() {
    return ctx.name();
  }
}
