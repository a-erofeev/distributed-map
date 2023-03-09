package test.huawei.dmap.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author aerofeev
 * @since 1.0
 */
@Component
public class WorkerChannelInitializer extends ChannelInitializer<SocketChannel> {
  @Autowired
  private WorkerClientChannelHandler workerClientChannelHandler;

  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
    socketChannel.pipeline().addLast(
      new LoggingHandler(LogLevel.DEBUG),
      workerClientChannelHandler
    );
  }
}
