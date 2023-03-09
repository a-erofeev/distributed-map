package test.huawei.dmap.server.impl;

import org.springframework.stereotype.Component;
import test.huawei.dmap.protocol.api.AsyncCommand;
import test.huawei.dmap.server.api.CommandQueue;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author aerofeev
 * @since 1.0
 */
@Component
public class CommandQueueImpl implements CommandQueue {
  private final ConcurrentHashMap<Long, AsyncCommand> commandQueue = new ConcurrentHashMap<>();

  @Override
  public void putCommandAndExecute(AsyncCommand command) {
    Objects.requireNonNull(command, "[command] must not be null");
    commandQueue.put(command.getId(), command);
    command.executeAsync();
  }

  @Override
  public void processResponseAndRemoveCommand(long id, String reponse) {
    Objects.requireNonNull(reponse, "[reponse] must not be null");
    commandQueue.computeIfPresent(id, (int_id, command) -> {
      command.processResponse(reponse);
      commandQueue.remove(int_id);
      return null;
    });
  }
}
