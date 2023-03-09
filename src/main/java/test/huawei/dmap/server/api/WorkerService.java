package test.huawei.dmap.server.api;

import test.huawei.dmap.protocol.api.AsyncCommand;

/**
 * Service that manages workers
 * @author aerofeev
 * @since 1.0
 */
public interface WorkerService {
  /**
   * Add worker to list
   * @return worker id
   */
  long registerWorker(Worker worker);

  /**
   * Remove worker from list
   * @return worker id
   */
  long unregisterWorker(String ctxName);

  void sendCommandToWorker(AsyncCommand command, String commandStr, int key);
}
