package test.huawei.dmap.worker;

import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import test.huawei.dmap.client.ClientChannelInitializer;
import test.huawei.dmap.common.AbstractClientNode;

/**
 * @author aerofeev
 * @since 9.0.28
 */
@Component
public class WorkerNode {
  @Autowired
  private Function<ChannelInitializer<SocketChannel>, AbstractClientNode> workerNodeFactory;
  @Autowired
  private WorkerChannelInitializer workerChannelInitializer;

  @PostConstruct
  public void start() throws Exception {
    workerNodeFactory.apply(workerChannelInitializer).start();
  }
}
