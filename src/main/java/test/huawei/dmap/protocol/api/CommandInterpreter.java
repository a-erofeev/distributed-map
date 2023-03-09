package test.huawei.dmap.protocol.api;

import io.netty.channel.ChannelHandlerContext;

/**
 * Command executor interface
 * @author aerofeev
 * @since 1.0
 */
public interface CommandInterpreter {
  /**
   * Parse and execute string command
   * @param commandStr string presentation of command
   * @param ctx netty channel handler context
   * @return result of execution
   */
  String parseAndExecuteCommand(String commandStr, ChannelHandlerContext ctx);
}
