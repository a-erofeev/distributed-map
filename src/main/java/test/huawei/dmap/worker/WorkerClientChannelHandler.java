package test.huawei.dmap.worker;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import test.huawei.dmap.protocol.impl.GetCommand;
import test.huawei.dmap.protocol.impl.PutCommand;
import test.huawei.dmap.server.api.ClientType;

import static test.huawei.dmap.common.CommandParseUtil.*;

/**
 * @author aerofeev
 * @since 1.0
 */
@Component
public class WorkerClientChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {
  private final static Logger LOGGER = LoggerFactory.getLogger(WorkerClientChannelHandler.class);
  private final Map<Integer, String> dstMapItem = new ConcurrentHashMap<>();
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    ctx.writeAndFlush(Unpooled.copiedBuffer(ClientType.WORKER.getName(), CharsetUtil.UTF_8));
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
    String commandStr = byteBuf.toString(CharsetUtil.UTF_8);
    LOGGER.debug("Worker received: " + commandStr);
    String response = parseAndExecuteCommand(commandStr);
    ctx.writeAndFlush(Unpooled.copiedBuffer(response, StandardCharsets.UTF_8));
  }

  private String parseAndExecuteCommand(String commandStr) {
    var idAndCommand = getIdAndCommand(commandStr);
    var commandParts = idAndCommand.getmObj2().split(COMMAND_PARTS_DELIMITER);
    var commandId = idAndCommand.getmObj1() + COMMAND_PARTS_DELIMITER;
    if (commandParts.length == 0) {
      return commandId + "Invalid command";
    }
    if (commandParts[0].equalsIgnoreCase(GetCommand.NAME)) {
      var key = parseGetCommand(idAndCommand.getmObj2());
      return commandId + dstMapItem.getOrDefault(key, "ERR");
    } else if (commandParts[0].equalsIgnoreCase(PutCommand.NAME)) {
      var entry = parsePutCommand(idAndCommand.getmObj2());
      dstMapItem.put(entry.getmObj1(), entry.getmObj2());
      return commandId + "OK";
    } else {
      return commandId + "Unknown command: " + idAndCommand.getmObj2();
    }
  }
}
