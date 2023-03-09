package test.huawei.dmap.common;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * Node for different kind of client applications (client, worker)
 * @author aerofeev
 * @since 1.0
 */
public class AbstractClientNode {
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractClientNode.class);
  @Value("${server.host}")
  private String host = "localhost";
  @Value("${server.port}")
  private Integer port = 8080;
  private final ChannelInitializer<SocketChannel> initializer;
  private Channel channel;
  private EventLoopGroup eventLoopGroup;

  public AbstractClientNode(ChannelInitializer<SocketChannel> initializer) {
    this.initializer = initializer;
  }

  public void start() {
    Runnable client = () -> {
      try {
        eventLoopGroup = new NioEventLoopGroup();
        try {
          Bootstrap bootstrap = new Bootstrap();
          bootstrap.group(eventLoopGroup)
            .channel(NioSocketChannel.class)
            .remoteAddress(new InetSocketAddress(host, port))
            .handler(initializer);
          ChannelFuture cf = bootstrap.connect().sync();
          channel = cf.channel();
          channel.closeFuture().sync();
        } finally {
          eventLoopGroup.shutdownGracefully().sync();
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    };
    LOGGER.debug("Client node prepared to start. Host = " + host + ", port = " + port);
    new Thread(client).start();
  }

  public ChannelFuture sendCommandToServer(String commandStr) {
    return channel.writeAndFlush(Unpooled.copiedBuffer(commandStr, StandardCharsets.UTF_8));
  }

  public void exit() {
    LOGGER.debug("Node is going to stop.");
    eventLoopGroup.shutdownGracefully().syncUninterruptibly();
    System.exit(0);
  }
}
