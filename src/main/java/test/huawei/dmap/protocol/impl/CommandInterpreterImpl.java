package test.huawei.dmap.protocol.impl;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import test.huawei.dmap.protocol.api.Command;
import test.huawei.dmap.protocol.api.CommandInterpreter;
import test.huawei.dmap.server.MasterNodeApp;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * @author aerofeev
 * @since 1.0
 */
@Service
public class CommandInterpreterImpl implements CommandInterpreter {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommandInterpreterImpl.class);
  private static final String COMMAND_PARAM_DELIMITER = " ";
  @Autowired
  private MasterNodeApp.CommandFactory commandFactory;

  @Override
  public String parseAndExecuteCommand(String commandStr, ChannelHandlerContext ctx) {
    Objects.requireNonNull(commandStr, "[commandStr] must not be null");

    var commandWithParams = Stream.of(commandStr.split(COMMAND_PARAM_DELIMITER)).collect(toList());
    if (commandWithParams.size()== 0) {
      return "Empty command";
    }
    var commandName = commandWithParams.get(0).toUpperCase();
    try {
      Command command = commandFactory.getCommand(commandName, commandStr, ctx);
      return command.execute();
    } catch (BeansException e) {
      Throwable cause1 = e.getCause();
      if (cause1 instanceof BeanInstantiationException && cause1.getCause() instanceof IllegalArgumentException) {
        LOGGER.error(cause1.getCause().getMessage());
        return cause1.getCause().getMessage();
      } else {
        LOGGER.error(e.getMessage());
        return "Unknown command: " + commandName;
      }
    }
  }
}
