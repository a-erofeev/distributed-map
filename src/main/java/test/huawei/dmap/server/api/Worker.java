package test.huawei.dmap.server.api;

import io.netty.channel.ChannelFuture;

/**
 * Worker node channel
 * @author aerofeev
 * @since 1.0
 */
public interface Worker {
  /**
   * Send command to worker channel
   * @param commandStr command
   * @return channel future
   */
  ChannelFuture sendCommand(String commandStr);

  /**
   * @return worker name
   */
  String getName();
}
