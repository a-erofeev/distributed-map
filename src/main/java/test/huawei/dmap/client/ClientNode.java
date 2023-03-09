package test.huawei.dmap.client;

import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import test.huawei.dmap.common.AbstractClientNode;

/**
 * Client application
 * @author aerofeev
 * @since 9.0.28
 */
@Component
public class ClientNode {
  private AbstractClientNode node;
  @Autowired
  private Function<ChannelInitializer<SocketChannel>, AbstractClientNode> clientNodeFactory;
  @Autowired
  private ClientChannelInitializer clientChannelInitializer;

  @PostConstruct
  public void start() throws Exception {
    node = clientNodeFactory.apply(clientChannelInitializer);
    node.start();
  }

  public ChannelFuture sendCommandToServer(String commandStr) {
    return node.sendCommandToServer(commandStr);
  }

  public void exit() {
    node.exit();
  }
}
