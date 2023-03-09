package test.huawei.dmap.server;

import java.util.function.Function;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import test.huawei.dmap.protocol.api.Command;
import test.huawei.dmap.server.api.Worker;
import test.huawei.dmap.server.impl.WorkerImpl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;


/**
 * Main-class to start master server node
 *
 * @author aerofeev
 * @since 1.0
 */
@SpringBootApplication
@ComponentScan({"test.huawei.dmap.protocol.impl", "test.huawei.dmap.server"})
public class MasterNodeApp {
  @Autowired
  private ApplicationContext appCtx;

  @Bean
  public CommandFactory commandFactory() {
    return (String commandName, String commandStr, ChannelHandlerContext ctx) -> {
      //todo - this approach is not type safe, better to use appCtx.getBeansOfType(...)
      var command = appCtx.getBean(commandName, commandStr, ctx);
      if (command instanceof Command) {
        return (Command) command;
      } else {
        throw new IllegalArgumentException("Unknown command: " + commandName);
      }
    };
  }

  @Bean
  public Function<Long, InitChannelHandler> initChannelHandlerFactory() {
    return this::initChannelHandler;
  }

  @Bean
  @Scope("prototype")
  public InitChannelHandler initChannelHandler(long id) {
    return new InitChannelHandler(id);
  }

  @Bean
  public ChannelGroup allChannels() {
    return new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
  }

  @Bean
  public Function<ChannelHandlerContext, Worker> workerFactory() {
    return this::worker;
  }

  @Bean
  @Scope("prototype")
  public Worker worker(ChannelHandlerContext ctx) {
    return new WorkerImpl(ctx);
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(MasterNodeApp.class);
  }

  @FunctionalInterface
  public interface CommandFactory {
    Command getCommand(String commandName, String commandStr, ChannelHandlerContext ctx);
  }
}
