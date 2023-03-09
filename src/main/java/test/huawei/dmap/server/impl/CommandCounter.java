package test.huawei.dmap.server.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import test.huawei.dmap.server.api.Counter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author aerofeev
 * @since 1.0
 */
@Component
@Qualifier("Command")
public class CommandCounter implements Counter {
  private final AtomicLong counter = new AtomicLong(1);

  public long getAndIncrement() {
    return counter.getAndIncrement();
  }
}
