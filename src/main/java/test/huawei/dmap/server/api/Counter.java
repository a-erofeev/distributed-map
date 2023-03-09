package test.huawei.dmap.server.api;

/**
 * Counter for different kind of entities (channels, commands...)
 * @author aerofeev
 * @since 1.0
 */
public interface Counter {
  /**
   * Increment counter
   * @return new number
   */
  long getAndIncrement();
}
