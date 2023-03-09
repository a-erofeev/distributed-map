package test.huawei.dmap.server.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import test.huawei.dmap.protocol.api.AsyncCommand;
import test.huawei.dmap.protocol.api.Command;
import test.huawei.dmap.server.api.Worker;
import test.huawei.dmap.server.api.WorkerService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author aerofeev
 * @since 1.0
 */
@Service
public class WorkerServiceImpl implements WorkerService {
  private static final Logger LOGGER = LoggerFactory.getLogger(WorkerServiceImpl.class);
  private final AtomicInteger counter = new AtomicInteger(0);
  private final Map<Integer, Worker> workerMap = new ConcurrentHashMap<>();
  private final Object monitor = new Object();

  @Override
  public long registerWorker(Worker worker) {
    Objects.requireNonNull(worker, "[worker] must not be null");
    synchronized (monitor) {
      //todo - after worker was added, distributed map should be remapped to take into account the new worker
      workerMap.put(counter.getAndIncrement(), worker);
      LOGGER.debug("Registered worker: {}", counter.get());
      return counter.get();
    }
  }

  @Override
  public long unregisterWorker(String ctxName) {
    Objects.requireNonNull(ctxName, "[ctxName] must not be null");
    synchronized (monitor) {
      //todo - before worker is removed, distributed map should be remapped
      for (Map.Entry<Integer, Worker> entry : workerMap.entrySet()) {
        if (entry.getValue().getName().equals(ctxName)) {
          LOGGER.debug("Unregistered worker: {}", entry.getKey());
          workerMap.remove(entry.getKey());
        }
      }
      return counter.decrementAndGet();
    }
  }

  @Override
  public void sendCommandToWorker(AsyncCommand command, String commandStr, int key) {
    Objects.requireNonNull(command, "[command] must not be null");
    Objects.requireNonNull(commandStr, "[commandStr] must not be null");
    synchronized (monitor) {
      if (counter.get() == 0) {
        command.processResponse("There are no active workers to process command.");
        return;
      }
      int workerId = key % counter.get();
      LOGGER.debug("Send command '{}' to worker number {}", commandStr, workerId);
      workerMap.get(workerId).sendCommand(commandStr);
    }
  }
}
