package test.huawei.dmap.server.api;

import test.huawei.dmap.protocol.api.AsyncCommand;
import test.huawei.dmap.protocol.api.Command;

/**
 * Queue for asynchronous (remote) command execution
 * @author aerofeev
 * @since 1.0
 */
public interface CommandQueue {
  /**
   * Put command to queue and execute
   * @param command command
   */
  void putCommandAndExecute(AsyncCommand command);

  /**
   * Remove command from queue
   * @param id command id
   * @param reponse response from worker
   */
  void processResponseAndRemoveCommand(long id, String reponse);
}
