package test.huawei.dmap.server;

import java.util.function.Function;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import test.huawei.dmap.server.api.Counter;

/**
 * @author aerofeev
 * @since 1.0
 */
@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
  private final static Logger LOGGER = LoggerFactory.getLogger(ServerChannelInitializer.class);
  @Autowired
  @Qualifier("Channel")
  private Counter channelCounter;
  @Autowired
  private Function<Long, InitChannelHandler> initChannelHandlerFactory;

  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
    socketChannel.pipeline().addLast(
      new LoggingHandler(LogLevel.DEBUG),
      getCommandChannelHandler(channelCounter.getAndIncrement())
    );
  }

  private InitChannelHandler getCommandChannelHandler(long id) {
    return initChannelHandlerFactory.apply(id);
  }
}
