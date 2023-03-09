package test.huawei.dmap.worker;


import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import test.huawei.dmap.common.AbstractClientNode;

/**
 * Main-class to start worker node
 * @author aerofeev
 * @since 1.0
 */
@SpringBootApplication
public class WorkerNodeApp {

  @Bean
  public Function<ChannelInitializer<SocketChannel>, AbstractClientNode> workerNodeFactory() {
    return this::abstractClientNode;
  }

  @Bean
  @Scope("prototype")
  public AbstractClientNode abstractClientNode(ChannelInitializer<SocketChannel> initializer) {
    return new AbstractClientNode(initializer);
  }

  public static void main(String[] args) {
    SpringApplication.run(WorkerNodeApp.class);
  }
}
