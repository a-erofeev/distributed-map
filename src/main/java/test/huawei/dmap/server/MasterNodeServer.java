package test.huawei.dmap.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

/**
 * Main-class to start master server node
 * @author aerofeev
 * @since 1.0
 */
@Component
public class MasterNodeServer {
  private static final Logger LOGGER = LoggerFactory.getLogger(MasterNodeServer.class);
  private static final String BYE_ON_CHANNEL_CLOSE = "BYE";
  @Value("${server.port}")
  private Integer port = 8080;
  @Autowired
  private ServerChannelInitializer channelInitializer;
  @Autowired
  private ChannelGroup channelGroup;
  private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
  private final EventLoopGroup workerGroup = new NioEventLoopGroup(5);

  @PostConstruct
  public void start() {
    Runnable server = () -> {
      try {
        try {
          var serverBootstrap = new ServerBootstrap();
          serverBootstrap.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .handler(new LoggingHandler(LogLevel.DEBUG))
            .childHandler(channelInitializer)
            .bind(port).sync()
            .channel().closeFuture().sync();
        } finally {
//          bossGroup.shutdownGracefully();
//          workerGroup.shutdownGracefully();
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    };

    new Thread(server).start();
  }

  public void shutdown() throws InterruptedException {
    LOGGER.debug("Server is going to stop");
    channelGroup.writeAndFlush(Unpooled.copiedBuffer(BYE_ON_CHANNEL_CLOSE, StandardCharsets.UTF_8))
      .addListener(ChannelFutureListener.CLOSE);
    bossGroup.shutdownGracefully().syncUninterruptibly();
    workerGroup.shutdownGracefully().syncUninterruptibly();
    System.exit(0);
  }
}
